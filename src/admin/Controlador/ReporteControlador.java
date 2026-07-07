package admin.Controlador;

import modelo.Conexion;
import admin.Vistas.JPReporteVista;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ReporteControlador implements ActionListener {

    private JPReporteVista vista;
    private Conexion conexion;
    private CardLayout cardLayout;
    private JPanel contenedor;

    public ReporteControlador(JPReporteVista vista, CardLayout cardLayout, JPanel contenedor) {
        
        this.vista = vista;
        this.conexion = new Conexion();
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        
        this.vista.btnVolver.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnRefrescar.addActionListener(this);

        cargarDatos(""); 
    }

    private void cargarDatos(String busqueda) {
        
        String sql = 
                "SELECT " +
                "c.id_cliente, " +
                "c.nombre AS cliente, " +
                "c.telefono, " +
                "c.email, " +
                "m.id_mascota, " +
                "m.nombre AS mascota, " +
                "m.especie, " +
                "m.raza, " +
                "m.edad " +
                "FROM clientes c " +
                "LEFT JOIN mascotas m ON c.id_cliente = m.id_cliente " +
                "WHERE c.estado = TRUE ";

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql += "AND c.nombre LIKE ? ";
        }
        
        sql += "ORDER BY c.nombre";

        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                ps.setString(1, "%" + busqueda.trim() + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                
                int contador = 0; 
                vista.modeloTabla.setRowCount(0);

                while (rs.next()) {
                    contador++;
                    vista.modeloTabla.addRow(new Object[]{
                            rs.getInt("id_cliente"),
                            rs.getString("cliente"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            rs.getObject("id_mascota") == null ? "N/A" : rs.getInt("id_mascota"),
                            rs.getString("mascota") == null ? "Sin mascota" : rs.getString("mascota"),
                            rs.getString("especie") == null ? "-" : rs.getString("especie"),
                            rs.getString("raza") == null ? "-" : rs.getString("raza"),
                            rs.getObject("edad") == null ? "-" : rs.getInt("edad")
                    });
                }
                if (contador == 0 && busqueda != null && !busqueda.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "No se encontró ningún cliente registrado con el nombre: '" + busqueda + "'.", "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatos("");
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar reporte: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnVolver) {
            cardLayout.show(contenedor, "DASHBOARD");
        } 
        else if (e.getSource() == vista.btnBuscar) {
            String nombreBusqueda = JOptionPane.showInputDialog(vista, "Ingrese el nombre del cliente a buscar:", "Buscar Cliente", JOptionPane.QUESTION_MESSAGE);
            
            if (nombreBusqueda != null && !nombreBusqueda.trim().isEmpty()) {
                cargarDatos(nombreBusqueda);
            } 
            else if (nombreBusqueda != null && nombreBusqueda.trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Debe ingresar un nombre para realizar la búsqueda.", "Atención", JOptionPane.WARNING_MESSAGE);
            }
        } 
        else if (e.getSource() == vista.btnRefrescar) {
            cargarDatos(""); 
        }
    }
}