package admin.Controlador;

import modelo.Conexion;
import modelo.Sesion;
import admin.Vistas.JPMascotaVista;
import admin.Vistas.JDRegistrarMascotaVista;
import admin.Vistas.JDEditarMascotaVista;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MascotaControlador implements ActionListener {

    private JPMascotaVista vista;
    private Conexion conexion = new Conexion();
    private CardLayout cardLayout;
    private JPanel contenedor;
    private int paginaActual = 1;
    private final int filasPorPagina = 10; 

    public MascotaControlador(JPMascotaVista vista, CardLayout cardLayout, JPanel contenedor) {
        this.vista = vista;
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.vista.btnRegistrar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnRefrescar.addActionListener(this);
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
        int fila = vista.tablaMascotas.getSelectedRow();
        if (fila == -1) fila = vista.tablaMascotas.getEditingRow();
        
        if (fila != -1) {
            if (vista.tablaMascotas.isEditing()) {
                vista.tablaMascotas.getCellEditor().stopCellEditing();
            }
            if (tipoAccion == 1) abrirEditarMascota(fila);
            else if (tipoAccion == 2) eliminarMascota(fila);
        } else {
            JOptionPane.showMessageDialog(vista, "Seleccione un paciente de la tabla primero.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrar) abrirRegistroMascota();
        else if (e.getSource() == vista.btnRefrescar) {
            paginaActual = 1;
            actualizarPantalla();
        }
        else if (e.getSource() == vista.btnBuscar) buscarMascota();
        else if (e.getSource() == vista.btnVolver) volver();
    }

    private void volver() {
        cardLayout.show(contenedor, "DASHBOARD");
    }

    private void cambiarPagina(int direccion) {
        int nuevaPagina = paginaActual + direccion;
        if (nuevaPagina >= 1) {
            paginaActual = nuevaPagina;
            actualizarPantalla();
        }
    }

    private void actualizarPantalla() {
        listarMascotas();
        if (vista.lblPaginaInfo != null) vista.lblPaginaInfo.setText("Pág. " + paginaActual);
        if (vista.btnAnterior != null) vista.btnAnterior.setEnabled(paginaActual > 1);
    }

    private void listarMascotas() {
        vista.modeloTabla.setRowCount(0);
        int offset = (paginaActual - 1) * filasPorPagina;
        String sql = "SELECT id_mascota, nombre, especie, raza, edad, id_cliente FROM mascotas WHERE estado = TRUE ORDER BY id_mascota ASC LIMIT ? OFFSET ?";
        
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, filasPorPagina);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                int contadorFilas = 0;
                while (rs.next()) {
                    contadorFilas++;
                    vista.modeloTabla.addRow(new Object[]{
                            rs.getInt("id_mascota"),
                            rs.getString("nombre"),
                            rs.getString("especie"),
                            rs.getString("raza"),
                            rs.getInt("edad"),
                            rs.getInt("id_cliente"),
                            "" 
                    });
                }
                if (vista.btnSiguiente != null) {
                    vista.btnSiguiente.setEnabled(contadorFilas == filasPorPagina);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al listar mascotas: " + ex.getMessage());
        }
    }

    // BUSCADOR 
    private void buscarMascota() {
        String nombreBuscar = JOptionPane.showInputDialog(vista, "Nombre del paciente (Mascota) a buscar:");
        if (nombreBuscar == null) return; 
                
        if (nombreBuscar.trim().isEmpty()) {
            paginaActual = 1;
            actualizarPantalla();
            return;
        }

        String sql = "SELECT id_mascota, nombre, especie, raza, edad, id_cliente FROM mascotas WHERE nombre ILIKE ? AND estado = TRUE";
        int contador = 0;

        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombreBuscar.trim() + "%");
                        
            DefaultTableModel modeloTemp = new DefaultTableModel(new Object[]{"ID", "Nombre", "Especie", "Raza", "Edad", "ID Dueño", "Acciones"}, 0);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contador++;
                    modeloTemp.addRow(new Object[]{
                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getInt(6), ""
                    });
                }
            }

            if (contador == 0) {
                JOptionPane.showMessageDialog(vista, "No se encontró ningún paciente llamado '" + nombreBuscar.trim() + "'.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                paginaActual = 1;
                actualizarPantalla();
            } else {
                vista.modeloTabla.setRowCount(0);
                for (int i = 0; i < contador; i++) {
                    Object[] filaDatos = new Object[7];
                    for (int j = 0; j < 7; j++) filaDatos[j] = modeloTemp.getValueAt(i, j);
                    vista.modeloTabla.addRow(filaDatos);
                }
                if (vista.btnSiguiente != null) vista.btnSiguiente.setEnabled(false);
                if (vista.btnAnterior != null) vista.btnAnterior.setEnabled(false);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error en la búsqueda: " + ex.getMessage());
        }
    }

    private void abrirRegistroMascota() {
        Frame fr = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDRegistrarMascotaVista jd = new JDRegistrarMascotaVista(fr, true);
        
        jd.btnGuardar.addActionListener(ev -> {
            
            if (jd.txtNombre.getText().trim().isEmpty() || jd.txtEspecie.getText().trim().isEmpty() || jd.txtIdCliente.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(jd, "Rellene los campos obligatorios (Nombre, Especie, ID Dueño).", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO mascotas (nombre, especie, raza, edad, id_cliente, creado_por) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, jd.txtNombre.getText().trim());
                ps.setString(2, jd.txtEspecie.getText().trim());
                ps.setString(3, jd.txtRaza.getText().trim());
                
                int edad = jd.txtEdad.getText().trim().isEmpty() ? 0 : Integer.parseInt(jd.txtEdad.getText().trim());
                ps.setInt(4, edad);
                ps.setInt(5, Integer.parseInt(jd.txtIdCliente.getText().trim()));
                ps.setString(6, Sesion.usuarioActual != null ? Sesion.usuarioActual : "admin");

                ps.executeUpdate();
                JOptionPane.showMessageDialog(jd, "Paciente registrado con éxito.");
                jd.dispose();
                paginaActual = 1;
                actualizarPantalla();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(jd, "La edad y el ID del Dueño deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(jd, "Error al registrar: " + ex.getMessage());
            }
        });
        
        jd.setVisible(true);
    }

    private void abrirEditarMascota(int fila) {
        int id = (int) vista.modeloTabla.getValueAt(fila, 0);
        Frame fr = (Frame) SwingUtilities.getWindowAncestor(vista);
        JDEditarMascotaVista jd = new JDEditarMascotaVista(fr, true);

        jd.txtNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        jd.txtEspecie.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
        jd.txtRaza.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
        jd.txtEdad.setText(vista.modeloTabla.getValueAt(fila, 4).toString());
        jd.txtIdCliente.setText(vista.modeloTabla.getValueAt(fila, 5).toString());

        jd.btnGuardar.addActionListener(ev -> {
            String sql = "UPDATE mascotas SET nombre=?, especie=?, raza=?, edad=?, id_cliente=? WHERE id_mascota=?";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, jd.txtNombre.getText().trim());
                ps.setString(2, jd.txtEspecie.getText().trim());
                ps.setString(3, jd.txtRaza.getText().trim());
                ps.setInt(4, Integer.parseInt(jd.txtEdad.getText().trim()));
                ps.setInt(5, Integer.parseInt(jd.txtIdCliente.getText().trim()));
                ps.setInt(6, id);

                ps.executeUpdate();
                JOptionPane.showMessageDialog(jd, "Datos actualizados correctamente.");
                jd.dispose();
                actualizarPantalla();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(jd, "La edad y el ID del Dueño deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(jd, "Error al actualizar: " + ex.getMessage());
            }
        });

        jd.setVisible(true);
    }

    private void eliminarMascota(int fila) {
        int id = (int) vista.modeloTabla.getValueAt(fila, 0);
        String nombre = (String) vista.modeloTabla.getValueAt(fila, 1);
        
        if (JOptionPane.showConfirmDialog(vista, "¿Desea dar de baja al paciente " + nombre + "?", "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            String sql = "UPDATE mascotas SET estado = FALSE WHERE id_mascota = ?";
            try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(vista, "Paciente dado de baja del sistema.");
                actualizarPantalla();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vista, "Error al eliminar: " + ex.getMessage());
            }
        }
    }
}