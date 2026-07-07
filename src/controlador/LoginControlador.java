package controlador;
import modelo.Conexion;
import modelo.Sesion;
import vista.JFLoginVista;

import admin.Vistas.JFDashboardVista;
import admin.Controlador.DashboardControlador;
import Veterinario.Vistas.JFDashboardVet;
import veterinario.controlador.DashboardVetControlador;
import Recepcionista.Vista.JFDashboardCoordinadorVista;
import Recepcionista.Controlador.DashboardCoordinadorControlador;

import javax.swing.*;
import java.sql.*;

public class LoginControlador {

    private JFLoginVista vista;

    public LoginControlador(JFLoginVista vista) {
        this.vista = vista;
        this.vista.setLoginListener(e -> validarLogin());
        
        this.vista.btnMostrarPass.addActionListener(e -> {
            if (this.vista.btnMostrarPass.isSelected()) {
                this.vista.txtPassword.setEchoChar((char) 0);
            } else {
                this.vista.txtPassword.setEchoChar(this.vista.caracterPorDefecto);
            }
        });
    }

    private void validarLogin() {

        String usuario = vista.txtUsuario.getText();
        String password = new String(vista.txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos.");
            return;
        }

        String sql = "SELECT id_usuario, username, rol, nombre, apellido FROM usuarios WHERE username = ? AND password = ?";

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Sesion.idUsuario = rs.getInt("id_usuario");
                    Sesion.usuarioActual = rs.getString("username");
                    Sesion.rol = rs.getString("rol");
                    Sesion.nombre = rs.getString("nombre");
                    Sesion.apellido = rs.getString("apellido");

                    switch (Sesion.rol.toLowerCase()) {

                        case "admin":
                            JFDashboardVista dashAdmin = new JFDashboardVista();
                            new DashboardControlador(dashAdmin);
                            dashAdmin.setVisible(true);
                            vista.dispose();
                            break;

                        case "veterinario":
                            JFDashboardVet dashVet = new JFDashboardVet();
                            new DashboardVetControlador(dashVet);
                            dashVet.setVisible(true);
                            vista.dispose();
                            break;

                        case "recepcionista":
                        case "coordinador": 
                            JFDashboardCoordinadorVista dashCoord = new JFDashboardCoordinadorVista();
                            new DashboardCoordinadorControlador(dashCoord);
                            dashCoord.setVisible(true);
                            vista.dispose();
                            break;

                        default:
                            JOptionPane.showMessageDialog(vista, "Rol no reconocido.");
                            break;
                    }

                } else {
                    JOptionPane.showMessageDialog(vista, "Acceso denegado. Usuario o contraseña incorrectos.");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}