package admin.Controlador;

import modelo.Conexion;
import modelo.Sesion;
import admin.Vistas.JDUsuarioVista;
import admin.Vistas.JFDashboardVista;
import admin.Vistas.JDRegistrarUsuarioVista;  
import admin.Vistas.JDEditarUsuarioVista;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class UsuarioControlador implements ActionListener {

    private JDUsuarioVista vista;
    private Conexion conexion = new Conexion();
    private int paginaActual = 1;
    private final int filasPorPagina = 10; 
    public UsuarioControlador(JDUsuarioVista vista) {
        
        this.vista = vista;     
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnRefrescar.addActionListener(this); 
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnVolver.addActionListener(this);

        if (this.vista.btnAnterior != null) {
            this.vista.btnAnterior.addActionListener(e -> cambiarPagina(-1));
        }
        if (this.vista.btnSiguiente != null) {
            this.vista.btnSiguiente.addActionListener(e -> cambiarPagina(1));
        }
        
        if (this.vista.btnAccionEditar != null) {
            this.vista.btnAccionEditar.addActionListener(e -> {
                if (vista.tablaUsuarios.isEditing()) {
                    vista.tablaUsuarios.getCellEditor().stopCellEditing();
                }
                abrirFormularioEdicion();
            });
        }

        if (this.vista.btnAccionEliminar != null) {
            this.vista.btnAccionEliminar.addActionListener(e -> {
                if (vista.tablaUsuarios.isEditing()) {
                    vista.tablaUsuarios.getCellEditor().stopCellEditing();
                }
                eliminarUsuario();
            });
        }
        actualizarPantalla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) {
            abrirFormularioRegistro();
        } else if (e.getSource() == vista.btnRefrescar) {
            paginaActual = 1;
            actualizarPantalla();
        } else if (e.getSource() == vista.btnBuscar) {
            buscarUsuario();
        } else if (e.getSource() == vista.btnVolver) {
            volver();
        }
    }

    private void volver() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);
        if (topFrame instanceof JFDashboardVista) {
            JFDashboardVista dash = (JFDashboardVista) topFrame;
            dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");
        }
    }

    private void cambiarPagina(int direccion) {
        int nuevaPagina = paginaActual + direccion;
        if (nuevaPagina >= 1) {
            paginaActual = nuevaPagina;
            actualizarPantalla();
        }
    }

    private void actualizarPantalla() {
        listarUsuarios();
        if (vista.lblPaginaInfo != null) {
            vista.lblPaginaInfo.setText("Pág. " + paginaActual);
        }
        if (vista.btnAnterior != null) {
            vista.btnAnterior.setEnabled(paginaActual > 1);
        }
    }

    private void listarUsuarios() {
        vista.modeloTabla.setRowCount(0);
        int offset = (paginaActual - 1) * filasPorPagina;
        String sql = "SELECT id_usuario, nombre, apellido, username, rol FROM usuarios WHERE estado = TRUE ORDER BY id_usuario ASC LIMIT ? OFFSET ?";
        int contadorFilas = 0;

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, filasPorPagina);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contadorFilas++;
                    vista.modeloTabla.addRow(new Object[]{
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("username"),
                            rs.getString("rol"),
                            "" 
                    });
                }
            }

            if (vista.btnSiguiente != null) {
                vista.btnSiguiente.setEnabled(contadorFilas == filasPorPagina);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al listar personal: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuario() {
        String nombreBuscar = JOptionPane.showInputDialog(vista, "Nombre del empleado a auditar:");
        if (nombreBuscar == null) return; 
                
        if (nombreBuscar.trim().isEmpty()) {
            paginaActual = 1;
            actualizarPantalla();
            return;
        }

        String queryBusqueda = "%" + nombreBuscar.trim() + "%";
        String sql = "SELECT id_usuario, nombre, apellido, username, rol FROM usuarios WHERE nombre ILIKE ? AND estado = TRUE";
        int contadorBuscados = 0;

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, queryBusqueda);
                        
            DefaultTableModel modeloTemporal = new DefaultTableModel(
                    new Object[]{"ID Personal", "Nombre", "Apellido", "Username", "Rol de Sistema", "Acciones"}, 0);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contadorBuscados++;
                    modeloTemporal.addRow(new Object[]{
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("username"),
                            rs.getString("rol"),
                            ""
                    });
                }
            }

            if (contadorBuscados == 0) {
                JOptionPane.showMessageDialog(vista, "El usuario '" + nombreBuscar.trim() + "' no se encuentra registrado.", "Sin Coincidencias", JOptionPane.INFORMATION_MESSAGE);
                paginaActual = 1;
                actualizarPantalla();
            } else {
                vista.modeloTabla.setRowCount(0);
                for (int i = 0; i < contadorBuscados; i++) {
                    Object[] filaDatos = new Object[6];
                    for (int j = 0; j < 6; j++) {
                        filaDatos[j] = modeloTemporal.getValueAt(i, j);
                    }
                    vista.modeloTabla.addRow(filaDatos);
                }
                                
                if (vista.btnSiguiente != null) vista.btnSiguiente.setEnabled(false);
                if (vista.btnAnterior != null) vista.btnAnterior.setEnabled(false);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error en la búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        int fila = vista.tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione una fila en la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = (int) vista.modeloTabla.getValueAt(fila, 0);
        String username = (String) vista.modeloTabla.getValueAt(fila, 3);

        if (username.equalsIgnoreCase(Sesion.usuarioActual)) {
            JOptionPane.showMessageDialog(vista, "No puede dar de baja su propio usuario activo.", "Acción Denegada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(vista, "¿Desea dar de baja a " + username + "?", "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE usuarios SET estado = FALSE WHERE id_usuario = ?";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "El empleado ha sido retirado.");
            actualizarPantalla();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al eliminar: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormularioRegistro() {
        Frame f = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDRegistrarUsuarioVista dialog = new JDRegistrarUsuarioVista(f, true);
                
        dialog.btnGuardar.addActionListener(ev -> {
            String pass = new String(dialog.txtPassword.getPassword()).trim();
            String patron = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$";
                        
            if(!pass.matches(patron)) {
                JOptionPane.showMessageDialog(dialog, "Contraseña débil. Debe cumplir con las políticas.", "Seguridad", JOptionPane.ERROR_MESSAGE);
                return;
            }
                        
            String sql = "INSERT INTO usuarios (username, password, rol, nombre, apellido, creado_por) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dialog.txtUsername.getText().trim());
                ps.setString(2, pass);
                ps.setString(3, dialog.cbRol.getSelectedItem().toString());
                ps.setString(4, dialog.txtNombre.getText().trim());
                ps.setString(5, dialog.txtApellido.getText().trim());
                ps.setString(6, Sesion.usuarioActual);
                ps.executeUpdate();
                                
                JOptionPane.showMessageDialog(dialog, "Personal registrado con éxito.");
                dialog.dispose();
                paginaActual = 1;
                actualizarPantalla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        dialog.setVisible(true);
    }

    private void abrirFormularioEdicion() {
        int fila = vista.tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione el registro que desea editar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
                
        int idUsuario = (int) vista.modeloTabla.getValueAt(fila, 0);
        Frame f = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDEditarUsuarioVista jd = new JDEditarUsuarioVista(f, true);
        
        jd.txtNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        jd.txtApellido.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
        jd.txtUsername.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
        jd.cbRol.setSelectedItem(vista.modeloTabla.getValueAt(fila, 4).toString());
        jd.btnGuardar.addActionListener(ev -> {
            String pass = new String(jd.txtPassword.getPassword()).trim();
            if (!pass.isEmpty()) {
                String patron = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$";
                if (!pass.matches(patron)) {
                    JOptionPane.showMessageDialog(jd, "Contraseña débil. Debe cumplir con las políticas de seguridad.", "Seguridad", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String sql = pass.isEmpty() 
                ? "UPDATE usuarios SET nombre=?, apellido=?, username=?, rol=? WHERE id_usuario=?"
                : "UPDATE usuarios SET nombre=?, apellido=?, username=?, rol=?, password=? WHERE id_usuario=?";

            try (Connection con = conexion.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, jd.txtNombre.getText().trim());
                ps.setString(2, jd.txtApellido.getText().trim());
                ps.setString(3, jd.txtUsername.getText().trim());
                ps.setString(4, jd.cbRol.getSelectedItem().toString());

                if (pass.isEmpty()) {
                    ps.setInt(5, idUsuario);
                } else {
                    ps.setString(5, pass);
                    ps.setInt(6, idUsuario);
                }

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(jd, "Registro de personal actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    jd.dispose();
                    actualizarPantalla(); 
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(jd, "Error SQL al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        jd.setVisible(true);
    }
}