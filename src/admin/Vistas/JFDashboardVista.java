package admin.Vistas;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class JFDashboardVista extends JFrame {

    public JMenuBar menuBar = new JMenuBar();
    public JMenu menuGestion = new JMenu("Gestión");
    public JMenuItem itemUsuarios = new JMenuItem("Gestionar Personal");
    public JMenuItem itemClientes = new JMenuItem("Gestión de Clientes");
    public JMenuItem itemMascotas = new JMenuItem("Gestión de Mascotas");
    public JMenuItem itemReporte = new JMenuItem("Ver Clientes y Mascotas");
    public JMenuItem itemCitasGlobales = new JMenuItem("Auditoría General de Citas");  
    public JMenuItem itemFinanzas = new JMenuItem("Control de Caja y Finanzas");
    public JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");
    public JButton btnRapidoUsuario, btnRapidoCita, btnRapidoPago, btnRapidoCliente, btnRapidoMascota;
    public JPanel panelContenedorCentral;
    public CardLayout cardLayout;
    public JLabel lblTotalPacientes, lblCitasHoy, lblTotalCaja;

    private final Color FONDO = new Color(238, 242, 246); 
    private final Color TEXTO_TITULO = new Color(15, 23, 42); 

    public JFDashboardVista() {
        setTitle("VetClinic - Panel de Administración");
        setSize(1100, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(FONDO);
        setJMenuBar(menuBar);

        // Menú
        menuBar.add(menuGestion);
        menuGestion.add(itemUsuarios);  
        menuGestion.add(itemCitasGlobales);
        menuGestion.addSeparator();
        menuGestion.add(itemClientes); 
        menuGestion.add(itemMascotas);
        menuGestion.addSeparator(); 
        menuGestion.add(itemReporte); 
        menuGestion.add(itemFinanzas);
        menuGestion.addSeparator();
        menuGestion.add(itemCerrarSesion);

        cardLayout = new CardLayout();
        panelContenedorCentral = new JPanel(cardLayout);
        panelContenedorCentral.setOpaque(false);

        // DASHBOARD HOME 
        JPanel dashboardHome = new JPanel(new BorderLayout(0, 40));
        dashboardHome.setOpaque(false);
        dashboardHome.setBorder(new EmptyBorder(40, 40, 40, 40)); // Márgenes ligeramente reducidos para que respiren los 5 botones

        // 1. Cabecera y Título
        JLabel lblTitulo = new JLabel("Resumen de Administración", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(TEXTO_TITULO);

        // 2. Panel de Acciones Rápidas 
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlAcciones.setOpaque(false);
             
        btnRapidoUsuario = crearBtnRapido("Crear Usuario", "👥", new Color(255, 237, 213), new Color(254, 215, 170), "#C2410C"); 
        btnRapidoCita = crearBtnRapido("Agendar Cita", "📅", new Color(237, 233, 254), new Color(221, 214, 254), "#6D28D9"); 
        btnRapidoPago = crearBtnRapido("Registrar Pago", "💵", new Color(209, 250, 229), new Color(167, 243, 208), "#047857"); 
        btnRapidoCliente = crearBtnRapido("Nuevo Cliente", "👤", new Color(219, 234, 254), new Color(191, 219, 254), "#1D4ED8"); 
        btnRapidoMascota = crearBtnRapido("Nueva Mascota", "🐾", new Color(255, 228, 230), new Color(254, 205, 211), "#BE123C"); 

        pnlAcciones.add(btnRapidoUsuario);
        pnlAcciones.add(btnRapidoCita);
        pnlAcciones.add(btnRapidoPago);
        pnlAcciones.add(btnRapidoCliente);
        pnlAcciones.add(btnRapidoMascota);

        // 3. KPIs 
        JPanel pnlKpis = new JPanel(new GridLayout(1, 3, 35, 0));
        pnlKpis.setOpaque(false);
        lblTotalPacientes = new JLabel("0", SwingConstants.CENTER);
        lblCitasHoy = new JLabel("0", SwingConstants.CENTER);
        lblTotalCaja = new JLabel("$0.00", SwingConstants.CENTER);
        
        pnlKpis.add(crearTarjeta("PACIENTES ACTIVOS", lblTotalPacientes, new Color(16, 185, 129))); // Verde Esmeralda
        pnlKpis.add(crearTarjeta("CITAS DEL DÍA COMPLETADAS", lblCitasHoy, new Color(59, 130, 246))); // Azul Rey
        pnlKpis.add(crearTarjeta("RECAUDACIÓN HOY", lblTotalCaja, new Color(245, 158, 11))); // Naranja/Ambar
        
        JPanel pnlNorte = new JPanel(new BorderLayout(0, 30));
        pnlNorte.setOpaque(false);
        pnlNorte.add(lblTitulo, BorderLayout.NORTH);
        pnlNorte.add(pnlAcciones, BorderLayout.CENTER);

        dashboardHome.add(pnlNorte, BorderLayout.NORTH);
        dashboardHome.add(pnlKpis, BorderLayout.CENTER);

        panelContenedorCentral.add(dashboardHome, "DASHBOARD");
        add(panelContenedorCentral, BorderLayout.CENTER);
    }

    private JButton crearBtnRapido(String texto, String icono, Color bgNormal, Color bgHover, String hexColorText) {
        String htmlText = "<html><center><span style='font-size:26px'>" + icono + "</span><br><br><span style='font-size:13px; font-weight:bold; color:" + hexColorText + "'>" + texto + "</span></center></html>";
        JButton btn = new JButton(htmlText);
        btn.setPreferredSize(new Dimension(160, 115)); 
        btn.setBackground(bgNormal);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 5, 5, new Color(0, 0, 0, 15)),
            new LineBorder(bgHover, 2, true)
        ));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgHover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgNormal); }
        });
        return btn;
    }

    private JPanel crearTarjeta(String t, JLabel v, Color bgColor) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(bgColor);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 6, 6, new Color(0, 0, 0, 30)), 
            new EmptyBorder(30, 20, 30, 20)
        ));

        v.setFont(new Font("Segoe UI", Font.BOLD, 50)); 
        v.setForeground(Color.WHITE);
        
        JLabel titulo = new JLabel(t, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(new Color(255, 255, 255, 220)); 
        p.add(titulo, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        return p;
    }
}