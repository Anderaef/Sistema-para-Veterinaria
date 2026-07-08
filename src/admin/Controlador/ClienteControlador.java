package admin.Controlador;

import admin.Vistas.*;
import Recepcionista.Vista.JFDashboardCoordinadorVista;
import modelo.Conexion;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClienteControlador implements ActionListener {
    private JPClienteVista vista;
    private Conexion conexion = new Conexion();
    private int paginaActual = 1;
    private final int filasPorPagina = 10;

    public ClienteControlador(JPClienteVista vista) {
        
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

        if (this.vista.btnEditarInt != null) {
            this.vista.btnEditarInt.addActionListener(e -> procesarAccionTabla(1));
        }

        if (this.vista.btnEliminarInt != null) {
            this.vista.btnEliminarInt.addActionListener(e -> procesarAccionTabla(2));
        }
         actualizarPantalla();
    }

    private void procesarAccionTabla(int tipoAccion) {
        int fila = vista.tablaClientes.getSelectedRow();
        if (fila == -1) fila = vista.tablaClientes.getEditingRow();
        
        if (fila != -1) {
            if (vista.tablaClientes.isEditing()) {
                vista.tablaClientes.getCellEditor().stopCellEditing();
            }
            if (tipoAccion == 1) abrirEdicion(fila);
            else if (tipoAccion == 2) eliminar(fila);
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un cliente de la tabla primero.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) abrirRegistro();
        else if (e.getSource() == vista.btnRefrescar) {
            paginaActual = 1;
            actualizarPantalla();
        }
        else if (e.getSource() == vista.btnBuscar) buscarCliente();
        else if (e.getSource() == vista.btnVolver) volver();
    }

private void volver() {
    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(vista);

    if (topFrame instanceof JFDashboardVista) {
        JFDashboardVista dash = (JFDashboardVista) topFrame;
        dash.cardLayout.show(dash.panelContenedorCentral, "DASHBOARD");
    } 
    else if (topFrame instanceof JFDashboardCoordinadorVista) {
        JFDashboardCoordinadorVista dash = (JFDashboardCoordinadorVista) topFrame;
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
        listarClientes();
        if (vista.lblPaginaInfo != null) {
            vista.lblPaginaInfo.setText("Pág. " + paginaActual);
        }
        if (vista.btnAnterior != null) {
            vista.btnAnterior.setEnabled(paginaActual > 1);
        }
    }

    private void listarClientes() {
        vista.modeloTabla.setRowCount(0);
        int offset = (paginaActual - 1) * filasPorPagina;
        String sql = "SELECT id_cliente, nombre, telefono, email FROM clientes WHERE estado = TRUE ORDER BY id_cliente ASC LIMIT ? OFFSET ?";
        int contadorFilas = 0;

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, filasPorPagina);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contadorFilas++;
                    vista.modeloTabla.addRow(new Object[]{
                        rs.getInt(1), 
                        rs.getString(2), 
                        rs.getString(3), 
                        rs.getString(4), 
                        "" 
                    });
                }
            }
            
            if (vista.btnSiguiente != null) {
                vista.btnSiguiente.setEnabled(contadorFilas == filasPorPagina);
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage()); }
    }

    private void buscarCliente() {
        String nombreBuscar = JOptionPane.showInputDialog(vista, "Nombre del cliente a buscar:");
        if (nombreBuscar == null) return; 
                
        if (nombreBuscar.trim().isEmpty()) {
            paginaActual = 1;
            actualizarPantalla();
            return;
        }

        String queryBusqueda = "%" + nombreBuscar.trim() + "%";
        String sql = "SELECT id_cliente, nombre, telefono, email FROM clientes WHERE nombre ILIKE ? AND estado = TRUE";
        int contadorBuscados = 0;

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, queryBusqueda);
                        
            DefaultTableModel modeloTemporal = new DefaultTableModel(
                    new Object[]{"ID", "Nombre", "Teléfono", "Email", "Acciones"}, 0);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contadorBuscados++;
                    modeloTemporal.addRow(new Object[]{
                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), ""
                    });
                }
            }

            if (contadorBuscados == 0) {
                JOptionPane.showMessageDialog(vista, "El cliente '" + nombreBuscar.trim() + "' no se encuentra registrado.", "Sin Coincidencias", JOptionPane.INFORMATION_MESSAGE);
                paginaActual = 1;
                actualizarPantalla();
            } else {
                vista.modeloTabla.setRowCount(0);
                for (int i = 0; i < contadorBuscados; i++) {
                    Object[] filaDatos = new Object[5];
                    for (int j = 0; j < 5; j++) filaDatos[j] = modeloTemporal.getValueAt(i, j);
                    vista.modeloTabla.addRow(filaDatos);
                }
                                
                if (vista.btnSiguiente != null) vista.btnSiguiente.setEnabled(false);
                if (vista.btnAnterior != null) vista.btnAnterior.setEnabled(false);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error en la búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirEdicion(int fila) {
        int id = (int) vista.modeloTabla.getValueAt(fila, 0);
        JDEditarClienteVista jd = new JDEditarClienteVista((java.awt.Frame) SwingUtilities.getWindowAncestor(vista), true);
        
        jd.txtNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        jd.txtTelefono.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
        jd.txtEmail.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
        
        jd.btnGuardar.addActionListener(ev -> {
            String sql = "UPDATE clientes SET nombre=?, telefono=?, email=? WHERE id_cliente=?";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, jd.txtNombre.getText().trim());
                ps.setString(2, jd.txtTelefono.getText().trim());
                ps.setString(3, jd.txtEmail.getText().trim());
                ps.setInt(4, id);
                ps.executeUpdate();
                jd.dispose();
                actualizarPantalla();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(jd, "Error al actualizar: " + ex.getMessage()); }
        });
        
        jd.setVisible(true);
    }

    private void eliminar(int fila) {
        int id = (int) vista.modeloTabla.getValueAt(fila, 0);
        String nombre = (String) vista.modeloTabla.getValueAt(fila, 1);
        
        if(JOptionPane.showConfirmDialog(vista, "¿Desea dar de baja al cliente " + nombre + "?", "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement("UPDATE clientes SET estado = FALSE WHERE id_cliente = ?")) {
                ps.setInt(1, id); 
                ps.executeUpdate();
                JOptionPane.showMessageDialog(vista, "El cliente ha sido retirado.");
                actualizarPantalla();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(vista, "Error al eliminar: " + ex.getMessage()); }
        }
    }
    
    private void abrirRegistro() { 
        JDRegistrarClienteVista jd = new JDRegistrarClienteVista((java.awt.Frame) SwingUtilities.getWindowAncestor(vista), true);
        
        jd.btnGuardar.addActionListener(ev -> {
            String sql = "INSERT INTO clientes (nombre, telefono, email, creado_por) VALUES (?, ?, ?, ?)";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, jd.txtNombre.getText().trim());
                ps.setString(2, jd.txtTelefono.getText().trim());
                ps.setString(3, jd.txtEmail.getText().trim());
                ps.setString(4, modelo.Sesion.usuarioActual != null ? modelo.Sesion.usuarioActual : "admin"); 
                ps.executeUpdate();
                
                JOptionPane.showMessageDialog(jd, "Cliente registrado con éxito.");
                jd.dispose();
                paginaActual = 1;
                actualizarPantalla();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(jd, "Error al guardar: " + ex.getMessage()); }
        });
        
        jd.setVisible(true); 
    }
}