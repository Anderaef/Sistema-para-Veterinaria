package admin.Controlador;

import modelo.Conexion;
import java.util.ArrayList;
import java.util.List;
import admin.Vistas.JPCitasAdminVista;
import admin.Vistas.JDAgendarCitaVista;
import admin.Vistas.JDEditarCitaVista;
import Recepcionista.Vista.JFDashboardCoordinadorVista;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CitasAdminControlador implements ActionListener {

    private JPCitasAdminVista vista;
    private Conexion conexion = new Conexion();
    private List<Integer> listaIdMascotas = new ArrayList<>();
    private List<Integer> listaIdVeterinarios = new ArrayList<>(); 
    
    private int paginaActual = 1;
    private final int filasPorPagina = 10;

    public CitasAdminControlador(JPCitasAdminVista vista) {
        this.vista = vista;
        
        this.vista.btnAgendarCita.addActionListener(this);
        this.vista.btnRefrescar.addActionListener(this);
        this.vista.btnVolver.addActionListener(this);
        
        this.vista.btnEditarInt.addActionListener(e -> editarCitaFila());
        this.vista.btnEliminarInt.addActionListener(e -> cancelarCitaFila());
        
        this.vista.btnAnterior.addActionListener(e -> cambiarPagina(-1));
        this.vista.btnSiguiente.addActionListener(e -> cambiarPagina(1));

        cargarFiltroVeterinarios();
        
        this.vista.cbFiltroVeterinario.addActionListener(e -> {
            paginaActual = 1;
            cargarDatosTabla();
        });

        cargarDatosTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgendarCita) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            JDAgendarCitaVista modalCrear = new JDAgendarCitaVista(topFrame, true);
            cargarMascotasYDueños(modalCrear);
            cargarVeterinariosDisponiblesParaAgendar(modalCrear); 
            modalCrear.btnGuardarCita.addActionListener(e2 -> insertarNuevaCita(modalCrear));
            modalCrear.btnCancelar.addActionListener(e2 -> limpiarCampos(modalCrear));
            modalCrear.setVisible(true);

            cargarDatosTabla();
        } else if (e.getSource() == vista.btnRefrescar) {
            vista.cbFiltroVeterinario.setSelectedIndex(0);
            paginaActual = 1;
            cargarDatosTabla();
            
        } else if (e.getSource() == vista.btnVolver) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            if (topFrame instanceof admin.Vistas.JFDashboardVista) {
                admin.Vistas.JFDashboardVista dash = (admin.Vistas.JFDashboardVista) topFrame;
                dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");

            } else if (topFrame instanceof JFDashboardCoordinadorVista) {
                JFDashboardCoordinadorVista dash = (JFDashboardCoordinadorVista) topFrame;
                dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");
            }
        }
    }

    private void actualizarEstadosVencidos() {
        String sqlUpdateEstados = 
            "UPDATE citas " +
            "SET estado_cita = 'NO COMPLETADA' " +
            "WHERE estado_cita = 'PROGRAMADA' " +
            "AND fecha_hora <= (CURRENT_TIMESTAMP - INTERVAL '20 minutes')";
            
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sqlUpdateEstados)) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error actualizando estados vencidos: " + ex.getMessage());
        }
    }

    private void cargarDatosTabla() {
        actualizarEstadosVencidos();
        
        vista.modeloCitas.setRowCount(0);
        int offset = (paginaActual - 1) * filasPorPagina;
        
        String filtroVet = (String) vista.cbFiltroVeterinario.getSelectedItem();
        boolean filtrar = filtroVet != null && !filtroVet.equals("Todos los Médicos");

        String sql = "SELECT c.id_cita, c.fecha_hora, m.nombre AS mascota, cl.nombre AS dueno, " +
                     "u.nombre AS veterinario, c.motivo, c.estado_cita " +
                     "FROM citas c " +
                     "JOIN mascotas m ON c.id_mascota = m.id_mascota " +
                     "JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
                     "JOIN usuarios u ON c.id_veterinario = u.id_usuario ";
                     
        if (filtrar) {
            sql += "WHERE CONCAT(u.nombre, ' ', u.apellido) = ? ";
        }
        sql += "ORDER BY c.id_cita DESC LIMIT ? OFFSET ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            if (filtrar) {
                ps.setString(index++, filtroVet);
            }
            ps.setInt(index++, filasPorPagina);
            ps.setInt(index, offset);

            int contadorFilas = 0;
            try (ResultSet rs = ps.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                while (rs.next()) {
                    contadorFilas++;
                    vista.modeloCitas.addRow(new Object[]{
                        rs.getInt("id_cita"),
                        sdf.format(rs.getTimestamp("fecha_hora")),
                        rs.getString("mascota"),
                        rs.getString("dueno"),
                        rs.getString("veterinario"),
                        rs.getString("motivo"),
                        rs.getString("estado_cita")
                    });
                }
            }
            
            vista.btnSiguiente.setEnabled(contadorFilas == filasPorPagina);
            vista.btnAnterior.setEnabled(paginaActual > 1);
            vista.lblPaginaInfo.setText("Pág. " + paginaActual);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar citas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarPagina(int direccion) {
        int nuevaPagina = paginaActual + direccion;
        if (nuevaPagina >= 1) {
            paginaActual = nuevaPagina;
            cargarDatosTabla();
        }
    }

    private void cargarFiltroVeterinarios() {
        vista.cbFiltroVeterinario.removeAllItems();
        vista.cbFiltroVeterinario.addItem("Todos los Médicos");

        String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo " +
                     "FROM usuarios WHERE LOWER(rol) = 'veterinario' AND estado = TRUE ORDER BY nombre";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                vista.cbFiltroVeterinario.addItem(rs.getString("nombre_completo"));
            }
        } catch (SQLException ex) {
            System.err.println("Error cargando filtro veterinarios: " + ex.getMessage());
        }
    }

    private void editarCitaFila() {
        int fila = vista.tablaCitasGlobales.getSelectedRow();
        if (fila != -1) {
            String estado = (String) vista.modeloCitas.getValueAt(fila, 6);
            if (!"PROGRAMADA".equalsIgnoreCase(estado)) {
                JOptionPane.showMessageDialog(vista, "Solo se pueden reprogramar citas en estado 'PROGRAMADA'.\nEsta cita se encuentra '" + estado + "'.", "Edición no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idCita = (int) vista.modeloCitas.getValueAt(fila, 0);
            String fechaActual = (String) vista.modeloCitas.getValueAt(fila, 1);
            String veterinarioActual = (String) vista.modeloCitas.getValueAt(fila, 4); 
            
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
            JDEditarCitaVista modalEditar = new JDEditarCitaVista(topFrame, true);
            
            modalEditar.txtPaciente.setText((String) vista.modeloCitas.getValueAt(fila, 2));
            modalEditar.txtMotivo.setText((String) vista.modeloCitas.getValueAt(fila, 5));
            modalEditar.txtFecha.setText(fechaActual.substring(0, 10));

            modalEditar.cbVeterinarios.removeAllItems();
            modalEditar.cbVeterinarios.addItem("Seleccione un Médico"); 
            listaIdVeterinarios.clear();

            String sqlVets = "SELECT id_usuario, CONCAT(nombre,' ',apellido) nombre_completo " +
                             "FROM usuarios WHERE LOWER(rol)='veterinario' AND estado=TRUE ORDER BY nombre";
            
            int indexSeleccion = 0; 
            try(Connection con = conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sqlVets);
                ResultSet rs = ps.executeQuery()){
                
                int counter = 1;
                while(rs.next()){
                    listaIdVeterinarios.add(rs.getInt("id_usuario"));
                    String nombreVetBD = rs.getString("nombre_completo");
                    modalEditar.cbVeterinarios.addItem(nombreVetBD);
                    
                    if (nombreVetBD.contains(veterinarioActual)) { 
                        indexSeleccion = counter;
                    }
                    counter++;
                }
            } catch(SQLException ex){
                System.err.println("Error al cargar veterinarios para edición: " + ex.getMessage());
            }

            if (indexSeleccion > 0) {
                modalEditar.cbVeterinarios.setSelectedIndex(indexSeleccion);
            }

            modalEditar.cbVeterinarios.setEnabled(true);
            
            modalEditar.btnGuardarCambios.addActionListener(ev -> {
                int indexVet = modalEditar.cbVeterinarios.getSelectedIndex();
                if (indexVet <= 0) {
                    JOptionPane.showMessageDialog(modalEditar, "Debe seleccionar un médico válido.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nuevaFecha = modalEditar.txtFecha.getText();
                String nuevaHora = new SimpleDateFormat("HH:mm:00").format((Date) modalEditar.spnHora.getValue());
                String fechaHoraFinal = nuevaFecha + " " + nuevaHora;
                
                int nuevoIdVeterinario = listaIdVeterinarios.get(indexVet - 1);
                
                String sqlUpdate = "UPDATE citas SET fecha_hora = ?, id_veterinario = ? WHERE id_cita = ?";
                try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                    ps.setTimestamp(1, Timestamp.valueOf(fechaHoraFinal));
                    ps.setInt(2, nuevoIdVeterinario);
                    ps.setInt(3, idCita);
                    ps.executeUpdate();
                    modalEditar.dispose();
                    cargarDatosTabla();
                    JOptionPane.showMessageDialog(vista, "Cita reprogramada correctamente.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(modalEditar, "Error al actualizar: " + ex.getMessage());
                }
            });
            
            modalEditar.setVisible(true);
        }
    }

    private void cancelarCitaFila() {
        int fila = vista.tablaCitasGlobales.getSelectedRow();
        if (fila != -1) {
            String estado = (String) vista.modeloCitas.getValueAt(fila, 6);
            if (!"PROGRAMADA".equalsIgnoreCase(estado)) {
                JOptionPane.showMessageDialog(vista, "Solo se pueden cancelar citas en estado 'PROGRAMADA'.\nEsta cita se encuentra '" + estado + "'.", "Acción no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int idCita = (int) vista.modeloCitas.getValueAt(fila, 0);
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                    "¿Estás seguro de cancelar la cita Nº " + idCita + "?\n(El registro cambiará a estado CANCELADA)", 
                    "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                String sql = "UPDATE citas SET estado_cita = 'CANCELADA' WHERE id_cita = ?";
                try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idCita);
                    ps.executeUpdate();
                    cargarDatosTabla();
                    JOptionPane.showMessageDialog(vista, "Cita cancelada correctamente.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vista, "Error al cancelar: " + ex.getMessage());
                }
            }
        }
    }

    private void cargarMascotasYDueños(JDAgendarCitaVista vistaCrear) {
        vistaCrear.cbMascotasClientes.removeAllItems();
        listaIdMascotas.clear();
        vistaCrear.cbMascotasClientes.addItem("Seleccione Mascota (Dueño)");

        String sql = "SELECT m.id_mascota, m.nombre perro, cl.nombre dueno " +
                     "FROM mascotas m " +
                     "INNER JOIN clientes cl ON m.id_cliente = cl.id_cliente " +
                     "WHERE m.estado = TRUE AND cl.estado = TRUE ORDER BY m.nombre";

        try(Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                listaIdMascotas.add(rs.getInt("id_mascota"));
                vistaCrear.cbMascotasClientes.addItem(rs.getString("perro") + " (Dueño: " + rs.getString("dueno") + ")");
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        }
    }

    private void cargarVeterinariosDisponiblesParaAgendar(JDAgendarCitaVista vistaCrear){
        vistaCrear.cbVeterinarios.removeAllItems();
        listaIdVeterinarios.clear();
        vistaCrear.cbVeterinarios.addItem("Seleccione un Médico");

        String sql = "SELECT id_usuario, CONCAT(nombre,' ',apellido) nombre_completo " +
                     "FROM usuarios WHERE LOWER(rol)='veterinario' AND estado=TRUE ORDER BY nombre";

        try(Connection con = conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                listaIdVeterinarios.add(rs.getInt("id_usuario"));
                vistaCrear.cbVeterinarios.addItem(rs.getString("nombre_completo"));
            }
        } catch(SQLException ex){
            JOptionPane.showMessageDialog(vista, ex.getMessage());
        }
    }
    
   private void insertarNuevaCita(JDAgendarCitaVista vistaCrear) {
    String motivo = vistaCrear.txtMotivo.getText().trim();
    int indexMascota = vistaCrear.cbMascotasClientes.getSelectedIndex(); 
    int indexVet = vistaCrear.cbVeterinarios.getSelectedIndex();        
    
    if (motivo.isEmpty() || indexMascota <= 0 || indexVet <= 0) {
        JOptionPane.showMessageDialog(vistaCrear, "Por favor, selecciona una mascota y un veterinario válido.");
        return;
    }

    try {
        String fecha = vistaCrear.txtFecha.getText();
        String hora = new SimpleDateFormat("HH:mm:00").format((Date) vistaCrear.spnHora.getValue());
        Timestamp fechaHora = Timestamp.valueOf(fecha + " " + hora);

        int idMascota = listaIdMascotas.get(indexMascota - 1);
        int idVeterinario = listaIdVeterinarios.get(indexVet - 1);

        String sql = "INSERT INTO citas(id_mascota, id_veterinario, fecha_hora, motivo, estado_cita, creado_por) "
                   + "VALUES(?, ?, ?, ?, 'PROGRAMADA', ?)";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMascota);
            ps.setInt(2, idVeterinario);
            ps.setTimestamp(3, fechaHora);
            ps.setString(4, motivo);
            ps.setString(5, "admin"); 
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(vistaCrear, "¡Cita agendada correctamente!");
            vistaCrear.dispose();
            cargarDatosTabla();
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(vistaCrear, "Error al guardar: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    private void limpiarCampos(JDAgendarCitaVista vistaCrear){
        vistaCrear.txtMotivo.setText("");
        vistaCrear.cbMascotasClientes.setSelectedIndex(0);
        vistaCrear.cbVeterinarios.setSelectedIndex(0);
        vistaCrear.txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        vistaCrear.spnHora.setValue(new Date());
    }
}