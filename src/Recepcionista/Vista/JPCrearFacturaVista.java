package Recepcionista.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class JPCrearFacturaVista extends JPanel {
    
    public JComboBox<String> comboCitas; 
    public JLabel lblInfoCita; 
    public JCheckBox chkVetInyeccion;
    public JCheckBox chkVetMedicinas;
    public JTextField txtVetDetalle;
    public JTextField txtMontoConsulta;  
    public JTextField txtMontoInyeccion;
    public JTextField txtMontoMedicinas;
    public JTextField txtMontoTotal;     

    public JButton btnGuardar, btnCancelar, btnVolver;

    public JPCrearFacturaVista() {
        setOpaque(true);
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(10, 25, 10, 25));
        setLayout(new BorderLayout(15, 10));

        // ENCABEZADO
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitulo = new JLabel("Emitir Factura de Cita");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(45, 55, 72));
        pnlHeader.add(lblTitulo, BorderLayout.WEST);

        btnVolver = configurarBoton("Volver al Inicio",new Color(113, 128, 150), new Font("Segoe UI", Font.BOLD, 13));
        pnlHeader.add(btnVolver, BorderLayout.EAST);

        add(pnlHeader, BorderLayout.NORTH);

        //  CONTENEDOR CENTRAL CON BOXLAYOUT 
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        // 1. PANEL DE SELECCIÓN DE CITA
        JPanel pnlCita = new JPanel(new GridLayout(2, 1, 0, 10));
        pnlCita.setOpaque(false);
        pnlCita.setBorder(crearBordeTitulo(" 1. Selección de Cita ", new Font("Segoe UI", Font.BOLD, 13)));
        
        JPanel filaCombo = new JPanel(new BorderLayout(10, 0));
        filaCombo.setOpaque(false);
        filaCombo.add(crearLabel("Seleccionar Cita Completada:", new Font("Segoe UI", Font.BOLD, 13)), BorderLayout.WEST);
        comboCitas = new JComboBox<>();
        comboCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        pnlReporte.setBorder(crearBordeTitulo(" 2. Insumos Reportados por el Veterinario ", new Font("Segoe UI", Font.BOLD, 13)));
        
        JPanel pnlChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlChecks.setOpaque(false);
        chkVetInyeccion = new JCheckBox("Aplicó Inyección/Vacuna");
        chkVetMedicinas = new JCheckBox("Recetó Medicinas");
        estilizarCheckReadOnly(chkVetInyeccion, new Font("Segoe UI", Font.PLAIN, 14));
        estilizarCheckReadOnly(chkVetMedicinas, new Font("Segoe UI", Font.PLAIN, 14));
        pnlChecks.add(chkVetInyeccion);
        pnlChecks.add(chkVetMedicinas);

        JPanel pnlDetalle = new JPanel(new BorderLayout(10, 0));
        pnlDetalle.setOpaque(false);
        pnlDetalle.add(crearLabel("Detalle:", new Font("Segoe UI", Font.BOLD, 13)), BorderLayout.WEST);
        txtVetDetalle = new JTextField("...");
        estilizarReadOnly(txtVetDetalle, new Font("Segoe UI", Font.PLAIN, 14));
        pnlDetalle.add(txtVetDetalle, BorderLayout.CENTER);

        pnlReporte.add(pnlChecks, BorderLayout.NORTH);
        pnlReporte.add(pnlDetalle, BorderLayout.CENTER);

        // 3. PANEL DESGLOSE DE COBROS
        JPanel pnlDesglose = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlDesglose.setOpaque(false);
        pnlDesglose.setBorder(crearBordeTitulo(" 3. Desglose de Facturación ", new Font("Segoe UI", Font.BOLD, 13)));

        txtMontoConsulta = new JTextField("20.00");
        estilizarReadOnly(txtMontoConsulta, new Font("Segoe UI", Font.PLAIN, 14)); 
        
        txtMontoInyeccion = new JTextField("0.00");
        estilizarCampoTexto(txtMontoInyeccion, new Font("Segoe UI", Font.PLAIN, 14));
        txtMontoInyeccion.setEditable(false); 
        
        txtMontoMedicinas = new JTextField("0.00");
        estilizarCampoTexto(txtMontoMedicinas, new Font("Segoe UI", Font.PLAIN, 14));
        txtMontoMedicinas.setEditable(false); 
        
        txtMontoTotal = new JTextField("20.00");
        estilizarTotal(txtMontoTotal, new Font("Segoe UI", Font.BOLD, 18));

        pnlDesglose.add(crearLabel("Consulta Base ($):", new Font("Segoe UI", Font.BOLD, 13)));
        pnlDesglose.add(txtMontoConsulta);
        pnlDesglose.add(crearLabel("Costo Inyecciones/Vacunas ($):", new Font("Segoe UI", Font.BOLD, 13)));
        pnlDesglose.add(txtMontoInyeccion);
        pnlDesglose.add(crearLabel("Costo Medicinas/Insumos ($):", new Font("Segoe UI", Font.BOLD, 13)));
        pnlDesglose.add(txtMontoMedicinas);
        pnlDesglose.add(crearLabel("MONTO TOTAL A PAGAR ($):", new Font("Segoe UI", Font.BOLD, 14)));
        pnlDesglose.add(txtMontoTotal);

        panelCentro.add(pnlCita);
        panelCentro.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentro.add(pnlReporte);
        panelCentro.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentro.add(pnlDesglose);

        add(panelCentro, BorderLayout.CENTER);

        // PANEL DE BOTONES INFERIOR
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelBtn.setOpaque(false);
        btnCancelar = configurarBoton("Limpiar Formulario", new Color(224, 122, 95), new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar = configurarBoton("Registrar Pago", new Color(42, 157, 143), new Font("Segoe UI", Font.BOLD, 13));

        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);
        add(panelBtn, BorderLayout.SOUTH);
    }

    // MÉTODOS DE ESTILOS
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

    private JButton configurarBoton(String texto, Color normal, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(160, 40)); 
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
        return btn;
    }
}