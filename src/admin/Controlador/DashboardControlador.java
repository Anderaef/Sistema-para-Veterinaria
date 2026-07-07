package admin.Controlador;

import modelo.Conexion;
import admin.Vistas.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import vista.JFLoginVista;
import controlador.LoginControlador;

public class DashboardControlador implements ActionListener {

    private JFDashboardVista vista;
    private Conexion conexion = new Conexion();
    private Timer timerActualizacion; 

    public DashboardControlador(JFDashboardVista vista) {
        this.vista = vista;
        initListeners();
        SwingUtilities.invokeLater(this::cargarDatosDashboard);
        iniciarDashboardEnTiempoReal();
    }

    private void initListeners() {
        
        vista.itemUsuarios.addActionListener(this); 
        vista.itemClientes.addActionListener(this);
        vista.itemMascotas.addActionListener(this);
        vista.itemReporte.addActionListener(this);
        vista.itemCitasGlobales.addActionListener(this);
        vista.itemFinanzas.addActionListener(this);
        vista.itemCerrarSesion.addActionListener(this);

        if (vista.btnRapidoUsuario != null) vista.btnRapidoUsuario.addActionListener(this); 
        if (vista.btnRapidoCita != null) vista.btnRapidoCita.addActionListener(this); 
        if (vista.btnRapidoPago != null) vista.btnRapidoPago.addActionListener(this);
        if (vista.btnRapidoCliente != null) vista.btnRapidoCliente.addActionListener(this);
        if (vista.btnRapidoMascota != null) vista.btnRapidoMascota.addActionListener(this);
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
        String sqlCaja = "SELECT COALESCE(SUM(monto), 0) FROM facturacion WHERE fecha_emision::date = CURRENT_DATE AND estado = TRUE";

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
            if (vista.lblTotalCaja != null) {
                try (ResultSet rs = con.createStatement().executeQuery(sqlCaja)) {
                    if (rs.next()) vista.lblTotalCaja.setText("$" + String.format("%.2f", rs.getDouble(1)));
                }
            }
        } catch (SQLException ex) { manejarError("KPIs", ex); }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        // MÓDULO DE USUARIOS 
        if (src == vista.itemUsuarios || src == vista.itemUsuarios || src == vista.btnRapidoUsuario) {
            JDUsuarioVista p = new JDUsuarioVista();
            new UsuarioControlador(p); 
            vista.panelContenedorCentral.add(p, "USUARIOS");
            vista.cardLayout.show(vista.panelContenedorCentral, "USUARIOS");
            
           
        // MÓDULO DE CLIENTES
        } else if (src == vista.itemClientes || src == vista.btnRapidoCliente) {
            JPClienteVista p = new JPClienteVista();
            new ClienteControlador(p); 
            vista.panelContenedorCentral.add(p, "CLIENTES");
            vista.cardLayout.show(vista.panelContenedorCentral, "CLIENTES");
            
        // MÓDULO DE MASCOTAS
        } else if (src == vista.itemMascotas || src == vista.btnRapidoMascota) {
            JPMascotaVista p = new JPMascotaVista();
            new MascotaControlador(p, vista.cardLayout, vista.panelContenedorCentral);
            vista.panelContenedorCentral.add(p, "MASCOTAS");
            vista.cardLayout.show(vista.panelContenedorCentral, "MASCOTAS");
            
        // MÓDULO DE FINANZAS
        } else if (src == vista.itemFinanzas || src == vista.btnRapidoPago) {
            JPFacturacionAdminVista p = new JPFacturacionAdminVista();
            new admin.Controlador.FacturacionAdminControlador(p);
            vista.panelContenedorCentral.add(p, "FINANZAS");
            vista.cardLayout.show(vista.panelContenedorCentral, "FINANZAS");

        // OTROS MÓDULOS DEL MENÚ
        } else if (src == vista.itemReporte) {
            JPReporteVista p = new JPReporteVista();
            new ReporteControlador(p, vista.cardLayout, vista.panelContenedorCentral);
            vista.panelContenedorCentral.add(p, "REPORTE");
            vista.cardLayout.show(vista.panelContenedorCentral, "REPORTE");
            
        } else if (src == vista.itemCitasGlobales || src == vista.btnRapidoCita ) {
            JPCitasAdminVista p = new JPCitasAdminVista();
            new admin.Controlador.CitasAdminControlador(p);
            vista.panelContenedorCentral.add(p, "CITAS_GLOBALES");
            vista.cardLayout.show(vista.panelContenedorCentral, "CITAS_GLOBALES");
            
        // CERRAR SESIÓN
        } else if (src == vista.itemCerrarSesion) {
            cerrarSesion();
        }
    }

    private void cerrarSesion() {
        if(timerActualizacion != null) {
            timerActualizacion.stop(); 
        }
        vista.dispose();
        JFLoginVista login = new JFLoginVista();
        new LoginControlador(login);
        login.setVisible(true);
    }

    private void manejarError(String origen, Exception e) {
        System.err.println("Error en " + origen + ": " + e.getMessage());
    }
}