package Recepcionista.Controlador;

import modelo.Conexion;
import modelo.Sesion;
import Recepcionista.Vista.JPCrearFacturaVista;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Locale;
import javax.swing.*;

public class FacturacionCoordinadorControlador {

    private JPCrearFacturaVista vista;
    private Conexion conexion = new Conexion();
    private boolean isCargando = false; 

    public FacturacionCoordinadorControlador(JPCrearFacturaVista vista) {
        this.vista = vista;
        initListeners();
        cargarCitas();
    }

    private void initListeners() {
        KeyAdapter calculadorTotal = new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { recalcularTotalDinamico(); }
        };
        vista.txtMontoInyeccion.addKeyListener(calculadorTotal);
        vista.txtMontoMedicinas.addKeyListener(calculadorTotal);

        vista.comboCitas.addActionListener(e -> {
            if (isCargando) return; 
            
            int index = vista.comboCitas.getSelectedIndex();
            if (index > 0) {
                vista.txtVetDetalle.setEnabled(true);
                int idCita = Integer.parseInt(((String)vista.comboCitas.getSelectedItem()).split(" ")[2]); 
                cargarReporteVeterinario(idCita);
            } else {
                limpiarFormulario();
            }
        });
        
        vista.btnCancelar.addActionListener(ev -> limpiarFormulario());
        vista.btnGuardar.addActionListener(ev -> guardarFactura());
        
        vista.btnVolver.addActionListener(ev -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            if (topFrame instanceof Recepcionista.Vista.JFDashboardCoordinadorVista) {
                Recepcionista.Vista.JFDashboardCoordinadorVista dash = (Recepcionista.Vista.JFDashboardCoordinadorVista) topFrame;
                dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");
            }
        });
    }

    private void cargarCitas() {
        isCargando = true; 
        vista.comboCitas.removeAllItems();
        vista.comboCitas.addItem("Seleccione una cita");
        
        String sqlCitas = "SELECT c.id_cita, c.motivo FROM citas c " +
                          "WHERE c.estado_cita = 'COMPLETADA' " +
                          "AND c.id_cita NOT IN (SELECT id_cita FROM facturacion WHERE estado = TRUE) " +
                          "ORDER BY c.id_cita ASC";

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlCitas); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vista.comboCitas.addItem("Cita Nº " + rs.getInt("id_cita") + " (" + rs.getString("motivo") + ")");
            }
        } catch (SQLException ex) { 
            System.err.println("Error al cargar citas: " + ex.getMessage()); 
        } finally {
            isCargando = false; 
        }
    }

    private void limpiarFormulario() {
        if (vista.comboCitas.getItemCount() > 0) {
            isCargando = true;
            vista.comboCitas.setSelectedIndex(0);
            isCargando = false;
        }
        vista.txtVetDetalle.setEnabled(false);
        vista.lblInfoCita.setText("Seleccione una cita para ver los datos...");
        vista.chkVetInyeccion.setSelected(false);
        vista.chkVetMedicinas.setSelected(false);
        vista.txtVetDetalle.setText("...");
        vista.txtMontoInyeccion.setEditable(false);
        vista.txtMontoInyeccion.setText("0.00");
        vista.txtMontoMedicinas.setEditable(false);
        vista.txtMontoMedicinas.setText("0.00");
        recalcularTotalDinamico();
    }

    private void recalcularTotalDinamico() {
        try {
            double base = 20.00; 
            String valIny = vista.txtMontoInyeccion.getText().trim().replace(",", ".");
            String valMed = vista.txtMontoMedicinas.getText().trim().replace(",", ".");
            double iny = valIny.isEmpty() ? 0 : Double.parseDouble(valIny);
            double med = valMed.isEmpty() ? 0 : Double.parseDouble(valMed);
            double total = base + iny + med;
            vista.txtMontoTotal.setText(String.format(Locale.US, "%.2f", total));
        } catch (NumberFormatException ex) { 
            vista.txtMontoTotal.setText("Error"); 
        }
    }

        private void cargarReporteVeterinario(int idCita) {
    String sql = 
        "SELECT m.nombre AS mascota, " +
        "       h.aplico_inyeccion, " +
        "       h.receto_medicinas, " +
        "       h.detalle_insumos " +
        "FROM citas c " +
        "JOIN mascotas m ON c.id_mascota = m.id_mascota " +
        "LEFT JOIN historial_clinico h ON h.id_cita = c.id_cita " + 
        "WHERE c.id_cita = ? ";

    try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                vista.lblInfoCita.setText("Mascota a facturar: " + rs.getString("mascota"));
                
                boolean tieneHistorial = rs.getObject("detalle_insumos") != null;
                
                if (tieneHistorial) {
                    boolean aplicoIny = rs.getBoolean("aplico_inyeccion");
                    boolean recetoMed = rs.getBoolean("receto_medicinas");
                    
                    vista.chkVetInyeccion.setSelected(aplicoIny);
                    vista.chkVetMedicinas.setSelected(recetoMed);
                    vista.txtVetDetalle.setText(rs.getString("detalle_insumos"));
                    
                    vista.txtMontoInyeccion.setEditable(aplicoIny);
                    if (!aplicoIny) vista.txtMontoInyeccion.setText("0.00");
                    
                    vista.txtMontoMedicinas.setEditable(recetoMed);
                    if (!recetoMed) vista.txtMontoMedicinas.setText("0.00");
                } else {
                    vista.chkVetInyeccion.setSelected(false);
                    vista.chkVetMedicinas.setSelected(false);
                    vista.txtVetDetalle.setText("Sin registro médico adicional.");
                    vista.txtMontoInyeccion.setEditable(false);
                    vista.txtMontoInyeccion.setText("0.00");
                    vista.txtMontoMedicinas.setEditable(false);
                    vista.txtMontoMedicinas.setText("0.00");
                }
                
                recalcularTotalDinamico();
            }
        } catch (SQLException ex) { 
            System.err.println("Error SQL en cargarReporte: " + ex.getMessage()); 
        }
    }

    private void guardarFactura() {
        if (vista.comboCitas.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione una cita válida.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (vista.txtMontoTotal.getText().equals("Error")) {
            JOptionPane.showMessageDialog(vista, "Montos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String montoStr = vista.txtMontoTotal.getText().trim().replace(",", ".");
        double total = Double.parseDouble(montoStr);
        int idCita = Integer.parseInt(((String)vista.comboCitas.getSelectedItem()).split(" ")[2]); 

        String sqlInsert = "INSERT INTO facturacion (id_cita, monto, monto_consulta, monto_inyecciones, monto_medicinas, creado_por, fecha_emision, estado) VALUES (?, ?, 20.00, ?, ?, ?, CURRENT_TIMESTAMP, TRUE)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlInsert)) {
            ps.setInt(1, idCita); ps.setDouble(2, total);
            ps.setDouble(3, Double.parseDouble(vista.txtMontoInyeccion.getText().replace(",", ".")));
            ps.setDouble(4, Double.parseDouble(vista.txtMontoMedicinas.getText().replace(",", ".")));
            ps.setString(5, Sesion.usuarioActual != null ? Sesion.usuarioActual : "Coordinador");
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(vista, "Cobro registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarCitas();
            limpiarFormulario();
            
        } catch (Exception ex) { JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage()); }
    }
}