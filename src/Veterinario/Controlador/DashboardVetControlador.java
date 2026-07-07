package veterinario.controlador;

import modelo.Sesion;
import modelo.Conexion;
import Veterinario.Vistas.JFDashboardVet;
import Veterinario.Vistas.JDAtencionVista;
import Veterinario.Vistas.JDHistorialMascotaVista;
import Veterinario.Vistas.JDDetalleTextoVista;
import vista.JFLoginVista;
import controlador.LoginControlador;
import javax.swing.*;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class DashboardVetControlador {

    private JFDashboardVet vista;
    private Conexion conexion = new Conexion();

    public DashboardVetControlador(JFDashboardVet vista) {

        this.vista = vista;
        
        vista.actualizarTextoCabecera(Sesion.getNombreCompleto(), 0); 
        vista.setVisibilidadHistorial(false);

        vista.btnRefrescar.addActionListener(e -> cargarCitas());
        vista.btnCerrarSesion.addActionListener(e -> cerrarSesion());
        vista.btnHistorial.addActionListener(e -> abrirHistorialCompleto());

        vista.tablaCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnaClickeada = vista.tablaCitas.getColumnModel().getColumnIndexAtX(e.getX());
                int filaClickeada = e.getY() / vista.tablaCitas.getRowHeight();

                if (filaClickeada < vista.tablaCitas.getRowCount() && filaClickeada >= 0 &&
                    columnaClickeada < vista.tablaCitas.getColumnCount() && columnaClickeada >= 0) {
                    vista.setVisibilidadHistorial(true);
                    
                    if (columnaClickeada == 6) { 
                        if (vista.tablaCitas.isEditing()) {
                            vista.tablaCitas.getCellEditor().stopCellEditing();
                        }
                        
                        int idCita = (int) vista.modelo.getValueAt(filaClickeada, 0);
                        String estado = (String) vista.modelo.getValueAt(filaClickeada, 5);
                        
                        if ("COMPLETADA".equalsIgnoreCase(estado)) {
                            verInformacionCita(idCita);
                        } else {
                            abrirVentanaAtencion(idCita);
                        }
                    }
                }
            }
        });

        cargarCitas();
    }

    private void actualizarCabecera(int citasPendientes) {
        vista.actualizarTextoCabecera(Sesion.getNombreCompleto(), citasPendientes);
    }

    private void actualizarEstadosVencidos() {
        String sqlUpdateEstados = 
            "UPDATE citas " +
            "SET estado_cita = 'NO COMPLETADA' " +
            "WHERE estado_cita = 'PROGRAMADA' " +
            "AND fecha_hora <= (CURRENT_TIMESTAMP - INTERVAL '20 minutes')";
            
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlUpdateEstados)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error actualizando estados vencidos: " + ex.getMessage());
        }
    }

    private void cargarCitas() {
        actualizarEstadosVencidos();
        
        vista.modelo.setRowCount(0);
        vista.setVisibilidadHistorial(false);
        
        String sql =
                "SELECT ci.id_cita, ci.fecha_hora, m.nombre AS mascota, " +
                "cl.nombre AS cliente, ci.motivo, ci.estado_cita " +
                "FROM citas ci " +
                "INNER JOIN mascotas m ON ci.id_mascota = m.id_mascota " + 
                "INNER JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
                "WHERE ci.id_veterinario = ? " +
                "AND UPPER(ci.estado_cita) = 'PROGRAMADA' " + 
                "AND CAST(ci.fecha_hora AS DATE) = CURRENT_DATE " + 
                "ORDER BY ci.fecha_hora";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Sesion.idUsuario);
            ResultSet rs = ps.executeQuery();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            int contador = 0;

            while (rs.next()) {
                contador++;
                Timestamp ts = rs.getTimestamp("fecha_hora");
                String fechaFormateada = (ts != null) ? sdf.format(ts) : "";

                vista.modelo.addRow(new Object[]{
                        rs.getInt("id_cita"), 
                        fechaFormateada, 
                        rs.getString("mascota"), 
                        rs.getString("cliente"),
                        rs.getString("motivo"), 
                        rs.getString("estado_cita"), 
                        "Accion" 
                });
            }
            
            actualizarCabecera(contador);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar citas de hoy: " + ex.getMessage());
        }
    }

private void abrirHistorialCompleto() {
        int fila = vista.tablaCitas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Selecciona una cita para ver el historial.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCita = (int) vista.modelo.getValueAt(fila, 0);
        String nombreMascota = (String) vista.modelo.getValueAt(fila, 2);

        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDHistorialMascotaVista ventanaHistorial = new JDHistorialMascotaVista(parentFrame, true);
        ventanaHistorial.lblTitulo.setText("Historial Clínico de: " + nombreMascota);
        java.util.List<String> listaDetalleInsumos = new java.util.ArrayList<>();

        ventanaHistorial.tablaHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = ventanaHistorial.tablaHistorial.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / ventanaHistorial.tablaHistorial.getRowHeight();

                if (row < ventanaHistorial.tablaHistorial.getRowCount() && row >= 0 &&
                    col < ventanaHistorial.tablaHistorial.getColumnCount() && col >= 0) {
                    
                    if (col == 6) { 
                        if (ventanaHistorial.tablaHistorial.isEditing()) {
                            ventanaHistorial.tablaHistorial.getCellEditor().stopCellEditing();
                        }
                        
                        String diag = (String) ventanaHistorial.modeloHistorial.getValueAt(row, 1);
                        String trat = (String) ventanaHistorial.modeloHistorial.getValueAt(row, 2);
                        boolean aplicoIny = ventanaHistorial.modeloHistorial.getValueAt(row, 3).equals("Sí");
                        boolean recetoMed = ventanaHistorial.modeloHistorial.getValueAt(row, 4).equals("Sí");
                        String detalle = listaDetalleInsumos.get(row);
                        
                        JDDetalleTextoVista ventanaDetalle = new JDDetalleTextoVista(ventanaHistorial, true);
                        ventanaDetalle.txtDiagnostico.setText(diag);
                        ventanaDetalle.txtTratamiento.setText(trat);
                        ventanaDetalle.chkInyeccion.setSelected(aplicoIny);
                        ventanaDetalle.chkMedicinas.setSelected(recetoMed);
                        ventanaDetalle.txtDetalleInsumos.setText(detalle != null && !detalle.isEmpty() ? detalle : "Ninguno");
                        
                        ventanaDetalle.setVisible(true);
                    }
                }
            }
        });

        String sql = "SELECT h.fecha_consulta, h.diagnostico, h.tratamiento, " +
                     "h.aplico_inyeccion, h.receto_medicinas, h.detalle_insumos, " +
                     "COALESCE(CONCAT(u.nombre, ' ', u.apellido), h.creado_por) AS medico_real " +
                     "FROM historial_clinico h " +
                     "LEFT JOIN usuarios u ON LOWER(h.creado_por) = LOWER(u.username) OR LOWER(h.creado_por) = LOWER(CONCAT(u.nombre, ' ', u.apellido)) " +
                     "WHERE h.id_mascota = (SELECT id_mascota FROM citas WHERE id_cita = ?) " +
                     "ORDER BY h.fecha_consulta ASC";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha_consulta");
                String fechaHistorial = (ts != null) ? sdf.format(ts) : "";

                boolean inyeccion = rs.getBoolean("aplico_inyeccion");
                boolean medicina = rs.getBoolean("receto_medicinas");

                ventanaHistorial.modeloHistorial.addRow(new Object[]{
                    fechaHistorial,
                    rs.getString("diagnostico"),
                    rs.getString("tratamiento"), 
                    inyeccion ? "Sí" : "No",
                    medicina ? "Sí" : "No",
                    rs.getString("medico_real"), 
                    "Ver Insumos" 
                });
                
                listaDetalleInsumos.add(rs.getString("detalle_insumos"));
            }
            
            if (ventanaHistorial.modeloHistorial.getRowCount() == 0) {
                JOptionPane.showMessageDialog(vista, "Esta mascota no tiene historial clínico previo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar historial: " + ex.getMessage());
        }

        ventanaHistorial.setTitle("Historial de " + nombreMascota);
        ventanaHistorial.setVisible(true);
    }

    private void abrirVentanaAtencion(int idCita) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDAtencionVista ventanaAtencion = new JDAtencionVista(parentFrame, true);

        String sql = "SELECT m.nombre AS mascota, cl.nombre AS cliente, ci.motivo " +
                     "FROM citas ci " +
                     "INNER JOIN mascotas m ON ci.id_mascota = m.id_mascota " +
                     "INNER JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
                     "WHERE ci.id_cita = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ventanaAtencion.txtNombreMascota.setText(rs.getString("mascota"));
                ventanaAtencion.txtNombreCliente.setText(rs.getString("cliente")); 
                ventanaAtencion.txtMotivo.setText(rs.getString("motivo"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar datos de la cita: " + ex.getMessage());
        }

        ventanaAtencion.btnGuardar.addActionListener(e -> guardarConsulta(idCita, ventanaAtencion));
        ventanaAtencion.setVisible(true);
    }

    private void verInformacionCita(int idCita) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDAtencionVista ventanaAtencion = new JDAtencionVista(parentFrame, true);
        
        ventanaAtencion.setTitle("Información de la Consulta");
        ventanaAtencion.txtSintomas.setEditable(false);
        ventanaAtencion.txtObservaciones.setEditable(false);
        ventanaAtencion.chkInyeccion.setEnabled(false);
        ventanaAtencion.chkMedicinas.setEnabled(false);
        ventanaAtencion.txtDetalleInsumos.setEditable(false);
        
        ventanaAtencion.btnGuardar.setVisible(false); 

        String sql = 
            "SELECT m.nombre AS mascota, cl.nombre AS cliente, ci.motivo, " +
            "h.diagnostico, h.tratamiento, h.aplico_inyeccion, h.receto_medicinas, h.detalle_insumos " +
            "FROM citas ci " +
            "JOIN mascotas m ON ci.id_mascota = m.id_mascota " +
            "JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
            "LEFT JOIN historial_clinico h ON h.id_cita = ci.id_cita " +
            "WHERE ci.id_cita = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ventanaAtencion.txtNombreMascota.setText(rs.getString("mascota"));
                ventanaAtencion.txtNombreCliente.setText(rs.getString("cliente"));
                ventanaAtencion.txtMotivo.setText(rs.getString("motivo"));
                ventanaAtencion.txtSintomas.setText(rs.getString("diagnostico") != null ? rs.getString("diagnostico") : "");
                ventanaAtencion.txtObservaciones.setText(rs.getString("tratamiento") != null ? rs.getString("tratamiento") : "");
                
                ventanaAtencion.chkInyeccion.setSelected(rs.getBoolean("aplico_inyeccion"));
                ventanaAtencion.chkMedicinas.setSelected(rs.getBoolean("receto_medicinas"));
                ventanaAtencion.txtDetalleInsumos.setText(rs.getString("detalle_insumos") != null ? rs.getString("detalle_insumos") : "");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al leer el historial médico: " + ex.getMessage());
        }

        ventanaAtencion.setVisible(true);
    }

    private void guardarConsulta(int idCita, JDAtencionVista dialog) {
        String diagnostico = dialog.txtSintomas.getText().trim();
        String tratamiento = dialog.txtObservaciones.getText().trim();
        
        boolean aplicoInyeccion = dialog.chkInyeccion.isSelected();
        boolean recetoMedicinas = dialog.chkMedicinas.isSelected();
        String detalleInsumos = dialog.txtDetalleInsumos.getText().trim();

        if (diagnostico.isEmpty() || tratamiento.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Por favor, completa el diagnóstico y el tratamiento.");
            return;
        }

        String sqlInsertHistorial = 
            "INSERT INTO historial_clinico (id_mascota, diagnostico, tratamiento, aplico_inyeccion, receto_medicinas, detalle_insumos, creado_por, id_cita) " +
            "VALUES ((SELECT id_mascota FROM citas WHERE id_cita = ?), ?, ?, ?, ?, ?, ?, ?)";
            
        String sqlUpdateCita = "UPDATE citas SET estado_cita = 'COMPLETADA' WHERE id_cita = ?";

        try (Connection con = conexion.getConexion()) {
            con.setAutoCommit(false); 
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsertHistorial);
                 PreparedStatement psUpdate = con.prepareStatement(sqlUpdateCita)) {

                psInsert.setInt(1, idCita); 
                psInsert.setString(2, diagnostico);
                psInsert.setString(3, tratamiento);
                psInsert.setBoolean(4, aplicoInyeccion); 
                psInsert.setBoolean(5, recetoMedicinas);
                psInsert.setString(6, detalleInsumos);  
                psInsert.setString(7, Sesion.getNombreCompleto()); 
                psInsert.setInt(8, idCita);
                
                psInsert.executeUpdate();

                psUpdate.setInt(1, idCita);
                psUpdate.executeUpdate();

                con.commit(); 
                JOptionPane.showMessageDialog(dialog, "Guardado exitosamente.");
                dialog.dispose();
                cargarCitas(); 
            } catch (SQLException ex) {
                con.rollback(); 
                throw ex;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
            vista, 
            "¿Está seguro de que desea cerrar la sesión actual?", 
            "Confirmar salida", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            vista.dispose();
            JFLoginVista login = new JFLoginVista();
            new LoginControlador(login);
            login.setVisible(true);
        }
    }
}