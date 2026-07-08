package Recepcionista.Controlador;

import modelo.Conexion;
import Recepcionista.Vista.JFDashboardCoordinadorVista;
import Recepcionista.Vista.JPCrearFacturaVista; 
import admin.Vistas.*; 
import admin.Controlador.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import vista.JFLoginVista;
import controlador.LoginControlador;

public class DashboardCoordinadorControlador implements ActionListener {

    private JFDashboardCoordinadorVista vista;
    private Conexion conexion = new Conexion();
    private Timer timerActualizacion; 

    public DashboardCoordinadorControlador(JFDashboardCoordinadorVista vista) {
        this.vista = vista;
        initListeners();
        SwingUtilities.invokeLater(this::cargarDatosDashboard);
        iniciarDashboardEnTiempoReal(); 
    }

    private void initListeners() {

        vista.itemAgendarCita.addActionListener(this);
        vista.itemClientes.addActionListener(this);
        vista.itemMascotas.addActionListener(this);
        vista.itemCrearFactura.addActionListener(this);
        vista.itemCerrarSesion.addActionListener(this);
        vista.btnRapidoCita.addActionListener(this); 
        vista.btnRapidoPago.addActionListener(this);
        vista.btnRapidoCliente.addActionListener(this);
        vista.btnRapidoMascota.addActionListener(this);
    }

    private void cargarDatosDashboard() {
        cargarKPIs();
    }

    private void iniciarDashboardEnTiempoReal() {
        timerActualizacion = new Timer(3000, e -> cargarKPIs());
        timerActualizacion.start();
    }

    private void cargarKPIs() {
        String sqlTotal = "SELECT COUNT(*) FROM mascotas WHERE estado = TRUE";
        String sqlCitasHoy = "SELECT COUNT(*) FROM citas WHERE fecha_hora::date = CURRENT_DATE AND estado_cita = 'COMPLETADA'";

        try (Connection con = conexion.getConexion()) {
            if (vista.lblTotalPacientes != null) {
                try (ResultSet rs = con.createStatement().executeQuery(sqlTotal)) {
                    if (rs.next()) vista.lblTotalPacientes.setText(rs.getString(1));
                }
            }
            if (vista.lblCitasHoy != null) {
                try (ResultSet rs = con.createStatement().executeQuery(sqlCitasHoy)) {
                    if (rs.next()) vista.lblCitasHoy.setText(rs.getString(1));
                }
            }
        } catch (SQLException ex) { System.err.println("Error KPIs: " + ex.getMessage()); }
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        // REUTILIZA EL MÓDULO DE CITAS DEL ADMIN
    if (src == vista.itemAgendarCita || src == vista.btnRapidoCita) {

        JPCitasAdminVista p = new JPCitasAdminVista();
        new CitasAdminControlador(p);

        vista.panelContenedorCentral.add(p, "CITAS_ADMIN");
        vista.cardLayout.show(vista.panelContenedorCentral, "CITAS_ADMIN");

        // REUTILIZA EL MÓDULO DE CLIENTES DEL ADMIN
        } else if (src == vista.itemClientes || src == vista.btnRapidoCliente) {
            JPClienteVista p = new JPClienteVista();
            new ClienteControlador(p); 
            vista.panelContenedorCentral.add(p, "CLIENTES");
            vista.cardLayout.show(vista.panelContenedorCentral, "CLIENTES");
            
        // REUTILIZA EL MÓDULO DE MASCOTAS DEL ADMIN
        } else if (src == vista.itemMascotas || src == vista.btnRapidoMascota) {
            JPMascotaVista p = new JPMascotaVista();
            new MascotaControlador(p, vista.cardLayout, vista.panelContenedorCentral);
            vista.panelContenedorCentral.add(p, "MASCOTAS");
            vista.cardLayout.show(vista.panelContenedorCentral, "MASCOTAS");
            
        // FACTURA
        } else if (src == vista.itemCrearFactura || src == vista.btnRapidoPago) {
            JPCrearFacturaVista p = new JPCrearFacturaVista();
            new FacturacionCoordinadorControlador(p); 
            vista.panelContenedorCentral.add(p, "CREAR_FACTURA");
            vista.cardLayout.show(vista.panelContenedorCentral, "CREAR_FACTURA");

        // CERRAR SESIÓN
        } else if (src == vista.itemCerrarSesion) {
            cerrarSesion();
        }
    }
    
    private void cerrarSesion() {
        if(timerActualizacion != null) timerActualizacion.stop();
        vista.dispose();
        JFLoginVista login = new JFLoginVista();
        new LoginControlador(login);
        login.setVisible(true);
    }
}