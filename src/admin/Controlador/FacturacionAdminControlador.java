package admin.Controlador;

import modelo.Conexion;
import modelo.Sesion;
import admin.Vistas.JPFacturacionAdminVista;
import admin.Vistas.JDCrearFacturaVista; 
import java.awt.Font;
import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; 
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.border.EmptyBorder;

public class FacturacionAdminControlador {

    private JPFacturacionAdminVista vista;
    private Conexion conexion = new Conexion();
    private int paginaActual = 1;
    private final int filasPorPagina = 10;
    private DecimalFormat df = new DecimalFormat("$#,##0.00");
    private boolean filtroAplicado = false;

    public FacturacionAdminControlador(JPFacturacionAdminVista vista) {
        this.vista = vista;

        this.vista.btnFiltrarFechas.addActionListener(e -> {
            String inicio = vista.txtFechaInicio.getText().trim();
            String fin = vista.txtFechaFin.getText().trim();
            
            if (inicio.isEmpty() || fin.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Seleccione un rango de fechas válido.", "Filtro Incompleto", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaInicio = sdf.parse(inicio);
                Date fechaFin = sdf.parse(fin);
                
                Calendar calHoy = Calendar.getInstance();
                calHoy.set(Calendar.HOUR_OF_DAY, 23);
                calHoy.set(Calendar.MINUTE, 59);
                calHoy.set(Calendar.SECOND, 59);
                Date hoy = calHoy.getTime();
                
                if (fechaInicio.after(hoy) || fechaFin.after(hoy)) {
                    JOptionPane.showMessageDialog(vista, "No puedes auditar fechas futuras.\nPor favor, selecciona una fecha igual o menor al día de hoy.", "Fecha Futura Inválida", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
                
                if (fechaInicio.after(fechaFin)) {
                    JOptionPane.showMessageDialog(vista, "Rango inconsistente.\nLa fecha 'Desde' no puede ser mayor que la fecha 'Hasta'.", "Rango Inválido", JOptionPane.WARNING_MESSAGE);
                    return; 
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error interno al validar las fechas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            filtroAplicado = true;
            paginaActual = 1;
            actualizarPanel();
        });
        
        this.vista.btnRefrescar.addActionListener(e -> {
            filtroAplicado = false;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vista.txtFechaFin.setText(sdf.format(cal.getTime()));
            vista.txtFechaInicio.setText(sdf.format(cal.getTime())); 
            
            paginaActual = 1;
            actualizarPanel();
        });

        this.vista.btnAnterior.addActionListener(e -> cambiarPagina(-1));
        this.vista.btnSiguiente.addActionListener(e -> cambiarPagina(1));
        this.vista.btnNuevaFactura.addActionListener(e -> desplegarModalCrearFactura());

        this.vista.tablaFacturas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = vista.tablaFacturas.columnAtPoint(e.getPoint());
                int row = vista.tablaFacturas.rowAtPoint(e.getPoint());
                if (col == 6 && row != -1) { 
                    int idCita = (int) vista.modeloFacturas.getValueAt(row, 1);
                    verInformacionCita(idCita, vista);
                }
            }
        });

        actualizarPanel();
        
        if (this.vista.btnVolver != null) {
            this.vista.btnVolver.addActionListener(e -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
                if (topFrame instanceof admin.Vistas.JFDashboardVista) {
                    admin.Vistas.JFDashboardVista dash = (admin.Vistas.JFDashboardVista) topFrame;
                    dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");
                }
            });
        }
    }

    private void cambiarPagina(int direccion) {
        int nuevaPagina = paginaActual + direccion;
        if (nuevaPagina >= 1) {
            paginaActual = nuevaPagina;
            actualizarPanel();
        }
    }

    private void actualizarPanel() {
        calcularCierreCajaDinamico();
        cargarHistorialFacturasDinamico();
        vista.lblPaginaInfo.setText("Pág. " + paginaActual);
        vista.btnAnterior.setEnabled(paginaActual > 1);
    }

    private void calcularCierreCajaDinamico() {
        String sql = "SELECT SUM(monto) AS total_monto FROM facturacion WHERE estado = TRUE ";
        if (filtroAplicado) {
            sql += "AND CAST(fecha_emision AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) ";
        } else {
            sql += "AND CAST(fecha_emision AS DATE) = CURRENT_DATE ";
        }
        
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (filtroAplicado) {
                ps.setString(1, vista.txtFechaInicio.getText().trim());
                ps.setString(2, vista.txtFechaFin.getText().trim());
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vista.lblTotalCaja.setText(df.format(rs.getDouble("total_monto")));
                }
            }
        } catch (SQLException ex) { System.err.println("Error al calcular balance: " + ex.getMessage()); }
    }

    private void cargarHistorialFacturasDinamico() {
        vista.modeloFacturas.setRowCount(0);
        int offset = (paginaActual - 1) * filasPorPagina;

        String sql = "SELECT f.id_factura, f.id_cita, m.nombre AS mascota, f.monto, f.fecha_emision, f.creado_por " +
                     "FROM facturacion f " +
                     "JOIN citas c ON f.id_cita = c.id_cita " +
                     "JOIN mascotas m ON c.id_mascota = m.id_mascota " +
                     "WHERE f.estado = TRUE ";

        if (filtroAplicado) {
            sql += "AND CAST(f.fecha_emision AS DATE) BETWEEN CAST(? AS DATE) AND CAST(? AS DATE) ";
        } else {
            sql += "AND CAST(f.fecha_emision AS DATE) = CURRENT_DATE ";
        }
        
        sql += "ORDER BY f.fecha_emision ASC LIMIT ? OFFSET ?";

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            if (filtroAplicado) {
                ps.setString(index++, vista.txtFechaInicio.getText().trim());
                ps.setString(index++, vista.txtFechaFin.getText().trim());
            }
            ps.setInt(index++, filasPorPagina);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int contadorFilas = 0;

            while (rs.next()) {
                contadorFilas++;
                Timestamp ts = rs.getTimestamp("fecha_emision");
                String fechaFormateada = (ts != null) ? sdf.format(ts) : "";

                vista.modeloFacturas.addRow(new Object[]{
                        rs.getInt("id_factura"),
                        rs.getInt("id_cita"),
                        rs.getString("mascota"),
                        df.format(rs.getDouble("monto")),
                        fechaFormateada,
                        rs.getString("creado_por"),
                        "Ver Detalle"
                });
            }
            vista.btnSiguiente.setEnabled(contadorFilas == filasPorPagina);
        } catch (SQLException ex) { 
            JOptionPane.showMessageDialog(vista, "Error al cargar historial: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private void recalcularTotalDinamico(JDCrearFacturaVista jd) {
        try {
            double base = 20.00; 
            String valIny = jd.txtMontoInyeccion.getText().trim().replace(",", ".");
            String valMed = jd.txtMontoMedicinas.getText().trim().replace(",", ".");
            double iny = valIny.isEmpty() ? 0 : Double.parseDouble(valIny);
            double med = valMed.isEmpty() ? 0 : Double.parseDouble(valMed);
            double total = base + iny + med;
            jd.txtMontoTotal.setText(String.format(Locale.US, "%.2f", total));
        } catch (NumberFormatException ex) { 
            jd.txtMontoTotal.setText("Error"); 
        }
    }

    private void desplegarModalCrearFactura() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
        JDCrearFacturaVista jd = new JDCrearFacturaVista(topFrame, true);
        ArrayList<String> detallesMascotas = new ArrayList<>();
        
        String sqlCitas = "SELECT c.id_cita, m.nombre AS mascota, c.motivo " +
                          "FROM citas c " +
                          "JOIN mascotas m ON c.id_mascota = m.id_mascota " +
                          "WHERE c.estado_cita = 'COMPLETADA' " +
                          "AND c.id_cita NOT IN (SELECT id_cita FROM facturacion WHERE estado = TRUE) " +
                          "ORDER BY c.id_cita DESC";

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlCitas); ResultSet rs = ps.executeQuery()) {
            jd.comboCitas.addItem("Seleccione una cita");
            detallesMascotas.add(""); 
            while (rs.next()) {
                int idCita = rs.getInt("id_cita");
                jd.comboCitas.addItem("Cita Nº " + idCita + " (" + rs.getString("motivo") + ")");
                detallesMascotas.add("Mascota: " + rs.getString("mascota"));
            }
        } catch (SQLException ex) { System.err.println("Error al cargar combo: " + ex.getMessage()); }

        KeyAdapter calculadorTotal = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                recalcularTotalDinamico(jd);
            }
        };
        jd.txtMontoInyeccion.addKeyListener(calculadorTotal);
        jd.txtMontoMedicinas.addKeyListener(calculadorTotal);

        jd.comboCitas.addActionListener(e -> {
            int index = jd.comboCitas.getSelectedIndex();
            if (index > 0) {
                jd.txtVetDetalle.setEnabled(true);
                jd.lblInfoCita.setText(detallesMascotas.get(index));
                int idCita = Integer.parseInt(((String)jd.comboCitas.getSelectedItem()).split(" ")[2]); 
                cargarReporteVeterinario(idCita, jd);
            } else {
                jd.txtVetDetalle.setEnabled(false);
                jd.lblInfoCita.setText("Seleccione una cita...");
                jd.chkVetInyeccion.setSelected(false);
                jd.chkVetMedicinas.setSelected(false);
                jd.txtVetDetalle.setText("...");
                jd.txtMontoInyeccion.setEditable(false);
                jd.txtMontoInyeccion.setText("0.00");
                jd.txtMontoMedicinas.setEditable(false);
                jd.txtMontoMedicinas.setText("0.00");
                recalcularTotalDinamico(jd);
            }
        });
        
        jd.txtVetDetalle.addActionListener(ev -> {
            int idCita = Integer.parseInt(((String)jd.comboCitas.getSelectedItem()).split(" ")[2]); 
            verInformacionCita(idCita, jd);
        });
        
        jd.btnCancelar.addActionListener(ev -> jd.dispose());
        jd.btnGuardar.addActionListener(ev -> {
            if (jd.comboCitas.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(jd, "Por favor seleccione una cita válida a cobrar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (jd.txtMontoTotal.getText().equals("Error")) {
                JOptionPane.showMessageDialog(jd, "Los montos ingresados son inválidos. Solo use números (ej. 15.50)", "Error de formato", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String montoStr = jd.txtMontoTotal.getText().trim().replace(",", ".");
            double total = Double.parseDouble(montoStr);
            int idCita = Integer.parseInt(((String)jd.comboCitas.getSelectedItem()).split(" ")[2]); 

            String sqlInsert = "INSERT INTO facturacion (id_cita, monto, monto_consulta, monto_inyecciones, monto_medicinas, creado_por, fecha_emision, estado) VALUES (?, ?, 20.00, ?, ?, ?, CURRENT_TIMESTAMP, TRUE)";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setInt(1, idCita); ps.setDouble(2, total);
                ps.setDouble(3, Double.parseDouble(jd.txtMontoInyeccion.getText().replace(",", ".")));
                ps.setDouble(4, Double.parseDouble(jd.txtMontoMedicinas.getText().replace(",", ".")));
                ps.setString(5, Sesion.usuarioActual != null ? Sesion.usuarioActual : "admin");
                ps.executeUpdate();
                jd.dispose();
                actualizarPanel(); 
                JOptionPane.showMessageDialog(vista, "Cobro registrado correctamente.", "Factura Emitida", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(jd, "Error: " + ex.getMessage()); }
        });
        
        jd.setVisible(true);
    }

    private void cargarReporteVeterinario(int idCita, JDCrearFacturaVista jd) {
        String sql = 
            "SELECT m.nombre AS mascota, h.aplico_inyeccion, h.receto_medicinas, h.detalle_insumos " +
            "FROM citas c " +
            "JOIN mascotas m ON c.id_mascota = m.id_mascota " +
            "LEFT JOIN historial_clinico h ON h.id_cita = c.id_cita " +
            "WHERE c.id_cita = ?";

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean tieneHistorial = rs.getObject("detalle_insumos") != null;
                
                if (tieneHistorial) {
                    boolean aplicoIny = rs.getBoolean("aplico_inyeccion");
                    boolean recetoMed = rs.getBoolean("receto_medicinas");
                    
                    jd.chkVetInyeccion.setSelected(aplicoIny);
                    jd.chkVetMedicinas.setSelected(recetoMed);
                    jd.txtVetDetalle.setText(rs.getString("detalle_insumos"));
                    
                    jd.txtMontoInyeccion.setEditable(aplicoIny);
                    if (!aplicoIny) jd.txtMontoInyeccion.setText("0.00");
                    
                    jd.txtMontoMedicinas.setEditable(recetoMed);
                    if (!recetoMed) jd.txtMontoMedicinas.setText("0.00");
                } else {
                    jd.chkVetInyeccion.setSelected(false);
                    jd.chkVetMedicinas.setSelected(false);
                    jd.txtVetDetalle.setText("Sin registro médico.");
                    jd.txtMontoInyeccion.setEditable(false);
                    jd.txtMontoInyeccion.setText("0.00");
                    jd.txtMontoMedicinas.setEditable(false);
                    jd.txtMontoMedicinas.setText("0.00");
                }
                recalcularTotalDinamico(jd);
            }
        } catch (SQLException ex) { System.err.println("Error SQL en cargarReporte: " + ex.getMessage()); }
    }

    private void verInformacionCita(int idCita, java.awt.Component parent) {
        String sql = 
            "SELECT m.nombre AS mascota, cl.nombre AS cliente, ci.motivo, " +
            "h.diagnostico, h.tratamiento, h.aplico_inyeccion, h.receto_medicinas, h.detalle_insumos " +
            "FROM citas ci " +
            "JOIN mascotas m ON ci.id_mascota = m.id_mascota " +
            "JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
            "LEFT JOIN historial_clinico h ON h.id_cita = ci.id_cita " +
            "WHERE ci.id_cita = ?";

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita); 
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String diagnostico = rs.getString("diagnostico");
                String tratamiento = rs.getString("tratamiento");
                String detalle = rs.getString("detalle_insumos");
                
                String resumen = "MASCOTA: " + rs.getString("mascota") + 
                                 "\nCLIENTE: " + rs.getString("cliente") + 
                                 "\nMOTIVO: " + rs.getString("motivo") + 
                                 "\n\nDIAGNÓSTICO\n" + (diagnostico != null ? diagnostico : "No registrado") + 
                                 "\n\nTRATAMIENTO\n" + (tratamiento != null ? tratamiento : "No registrado") +
                                 "\n\nDETALLE DE INSUMOS\n" + 
                                 (rs.getBoolean("aplico_inyeccion") ? "Sí - Inyección/Vacuna\n" : "NO Inyección/Vacuna\n") +
                                 (rs.getBoolean("receto_medicinas") ? "Sí - Medicinas\n" : "NO Medicinas\n") +
                                 "Detalle: " + (detalle != null ? detalle : "Ninguno");

                JTextArea areaTexto = new JTextArea(resumen);
                areaTexto.setEditable(false);
                areaTexto.setRows(15);
                areaTexto.setColumns(40);
                areaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                areaTexto.setBorder(new EmptyBorder(10, 10, 10, 10));
                
                JOptionPane.showMessageDialog(parent, new JScrollPane(areaTexto), 
                        "Resumen de Atención Médica", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) { 
            JOptionPane.showMessageDialog(parent, "Error al cargar resumen: " + ex.getMessage()); 
        }
    }
}