package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDCrearFacturaVista extends JDialog {
    
    public JComboBox<String> comboCitas; 
    public JLabel lblInfoCita; 
    public JCheckBox chkVetInyeccion;
    public JCheckBox chkVetMedicinas;
    public JTextField txtVetDetalle;
    public JTextField txtMontoConsulta;  
    public JTextField txtMontoInyeccion;
    public JTextField txtMontoMedicinas;
    public JTextField txtMontoTotal;     

    public JButton btnGuardar, btnCancelar;

    public JDCrearFacturaVista(Frame parent, boolean modal) {
        super(parent, "Registrar Nueva Factura / Cobro", modal);
        
        setSize(600, 700); 
        setLocationRelativeTo(parent);
        setResizable(false);
        
        Color colorFondo = new Color(248, 249, 250);
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 20);
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 13);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 10));
        mainPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        mainPanel.setBackground(colorFondo);
        setContentPane(mainPanel);

        // ENCABEZADO 
        JLabel lblTitulo = new JLabel("Emitir Factura de Cita");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(new Color(45, 55, 72));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);

        // 1. PANEL DE SELECCIÓN DE CITA
        JPanel pnlCita = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlCita.setOpaque(false);
        pnlCita.setBorder(crearBordeTitulo(" 1. Selección de Cita ", fontLabels));
        
        JPanel filaCombo = new JPanel(new BorderLayout(10, 0));
        filaCombo.setOpaque(false);
        filaCombo.add(crearLabel("Seleccionar Cita Completada:", fontLabels), BorderLayout.WEST);
        comboCitas = new JComboBox<>();
        comboCitas.setFont(fontTextos);
        comboCitas.setBackground(Color.WHITE);
        filaCombo.add(comboCitas, BorderLayout.CENTER);
        
        JPanel filaInfo = new JPanel(new BorderLayout(10, 0));
        filaInfo.setOpaque(false);
        lblInfoCita = new JLabel("Seleccione una cita para ver los datos...");
        lblInfoCita.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblInfoCita.setForeground(new Color(42, 157, 143));
        filaInfo.add(lblInfoCita, BorderLayout.CENTER);
        
        pnlCita.add(filaCombo);
        pnlCita.add(filaInfo);

        // 2. PANEL REPORTE MÉDICO
        JPanel pnlReporte = new JPanel(new BorderLayout(10, 10));
        pnlReporte.setOpaque(false);
        pnlReporte.setBorder(crearBordeTitulo(" 2. Insumos Reportados por el Veterinario ", fontLabels));
        
        JPanel pnlChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlChecks.setOpaque(false);
        chkVetInyeccion = new JCheckBox("Aplicó Inyección/Vacuna");
        chkVetMedicinas = new JCheckBox("Recetó Medicinas");
        estilizarCheckReadOnly(chkVetInyeccion, fontTextos);
        estilizarCheckReadOnly(chkVetMedicinas, fontTextos);
        pnlChecks.add(chkVetInyeccion);
        pnlChecks.add(chkVetMedicinas);

        JPanel pnlDetalle = new JPanel(new BorderLayout(10, 0));
        pnlDetalle.setOpaque(false);
        pnlDetalle.add(crearLabel("Detalle:", fontLabels), BorderLayout.WEST);
        txtVetDetalle = new JTextField("...");
        estilizarReadOnly(txtVetDetalle, fontTextos);
        pnlDetalle.add(txtVetDetalle, BorderLayout.CENTER);

        pnlReporte.add(pnlChecks, BorderLayout.NORTH);
        pnlReporte.add(pnlDetalle, BorderLayout.CENTER);

        // 3. PANEL DESGLOSE DE COBROS
        JPanel pnlDesglose = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlDesglose.setOpaque(false);
        pnlDesglose.setBorder(crearBordeTitulo(" 3. Desglose de Facturación ", fontLabels));

        txtMontoConsulta = new JTextField("20.00");
        estilizarReadOnly(txtMontoConsulta, fontTextos); 
        
        txtMontoInyeccion = new JTextField("0.00");
        estilizarCampoTexto(txtMontoInyeccion, fontTextos);
        
        txtMontoMedicinas = new JTextField("0.00");
        estilizarCampoTexto(txtMontoMedicinas, fontTextos);
        
        txtMontoTotal = new JTextField("20.00");
        estilizarTotal(txtMontoTotal, new Font("Segoe UI", Font.BOLD, 18));

        pnlDesglose.add(crearLabel("Consulta Base ($):", fontLabels));
        pnlDesglose.add(txtMontoConsulta);
        pnlDesglose.add(crearLabel("Costo Inyecciones/Vacunas ($):", fontLabels));
        pnlDesglose.add(txtMontoInyeccion);
        pnlDesglose.add(crearLabel("Costo Medicinas/Insumos ($):", fontLabels));
        pnlDesglose.add(txtMontoMedicinas);
        pnlDesglose.add(crearLabel("MONTO TOTAL A PAGAR ($):", new Font("Segoe UI", Font.BOLD, 14)));
        pnlDesglose.add(txtMontoTotal);

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 15));
        panelSuperior.setOpaque(false);
        panelSuperior.add(pnlCita, BorderLayout.NORTH);
        panelSuperior.add(pnlReporte, BorderLayout.CENTER);

        panelCentro.add(panelSuperior, BorderLayout.NORTH);
        panelCentro.add(pnlDesglose, BorderLayout.CENTER);
        mainPanel.add(panelCentro, BorderLayout.CENTER);

        // PANEL DE BOTONES INFERIOR
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelBtn.setOpaque(false);

        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Registrar Pago", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        
        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);
        mainPanel.add(panelBtn, BorderLayout.SOUTH);
    }

    private TitledBorder crearBordeTitulo(String titulo, Font fuente) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                titulo, TitledBorder.LEFT, TitledBorder.TOP, fuente, new Color(74, 85, 104));
        border.setBorder(BorderFactory.createCompoundBorder(border.getBorder(), new EmptyBorder(10, 15, 15, 15)));
        return border;
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(new Color(74, 85, 104)); 
        return label;
    }
    
    private void estilizarCheckReadOnly(JCheckBox chk, Font fuente) {
        chk.setFont(fuente);
        chk.setForeground(new Color(100, 116, 139));
        chk.setOpaque(false);
        chk.setEnabled(false); 
    }

    private void estilizarCampoTexto(JTextField campo, Font fuente) {
        campo.setFont(fuente);
        campo.setForeground(new Color(45, 55, 72));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void estilizarReadOnly(JTextField campo, Font fuente) {
        campo.setEditable(false);
        campo.setFont(fuente);
        campo.setBackground(new Color(237, 242, 247)); 
        campo.setForeground(new Color(74, 85, 104));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
    }
    
    private void estilizarTotal(JTextField campo, Font fuente) {
        campo.setEditable(false);
        campo.setFont(fuente);
        campo.setBackground(new Color(229, 248, 237)); 
        campo.setForeground(new Color(5, 150, 105)); 
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129), 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(160, 40)); 
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
        
        return btn;
    }
}