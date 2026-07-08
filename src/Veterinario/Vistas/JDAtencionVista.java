package Veterinario.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDAtencionVista extends JDialog {

    public JTextField txtNombreMascota, txtNombreCliente, txtMotivo;
    public JTextArea txtSintomas, txtObservaciones;
    public JButton btnGuardar, btnCancelar;
    public JCheckBox chkInyeccion, chkMedicinas;
    public JTextField txtDetalleInsumos;

    public JDAtencionVista(Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Atención Médica");
        setSize(500, 680);
        setLocationRelativeTo(parent);

        getContentPane().setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout(10, 10));

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        //  PANEL PRINCIPAL
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // TOP PANEL
        JPanel top = new JPanel(new GridLayout(3, 2, 10, 10));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                " Datos de la Cita ", 
                TitledBorder.LEFT, TitledBorder.TOP, fontTitulos, new Color(74, 85, 104)
        ));
        ((TitledBorder)top.getBorder()).setBorder(BorderFactory.createCompoundBorder(
                ((TitledBorder)top.getBorder()).getBorder(), new EmptyBorder(10, 15, 15, 15)));
        
        txtNombreMascota = new JTextField(); 
        estilizarReadOnly(txtNombreMascota, fontTextos);
        
        txtNombreCliente = new JTextField(); 
        estilizarReadOnly(txtNombreCliente, fontTextos);
        
        txtMotivo = new JTextField(); 
        estilizarReadOnly(txtMotivo, fontTextos);

        top.add(crearLabel("Mascota:", fontTitulos));
        top.add(txtNombreMascota);
        top.add(crearLabel("Dueño:", fontTitulos));
        top.add(txtNombreCliente);
        top.add(crearLabel("Motivo:", fontTitulos));
        top.add(txtMotivo);

        //  FORMULARIO CENTRAL  
        JPanel panelTextos = new JPanel(new GridLayout(2, 1, 15, 15));
        panelTextos.setOpaque(false);
        
        // Panel de Síntomas / Diagnóstico
        JPanel panelSintomas = new JPanel(new BorderLayout(0, 5));
        panelSintomas.setOpaque(false);
        panelSintomas.add(crearLabel("Síntomas detallados / Diagnóstico:", fontTitulos), BorderLayout.NORTH);
        
        txtSintomas = new JTextArea(3, 20);
        estilizarTextArea(txtSintomas, fontTextos);
        JScrollPane scrollSintomas = new JScrollPane(txtSintomas);
        scrollSintomas.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 224)));
        panelSintomas.add(scrollSintomas, BorderLayout.CENTER);

        // Panel de Observaciones / Tratamiento
        JPanel panelObservaciones = new JPanel(new BorderLayout(0, 5));
        panelObservaciones.setOpaque(false);
        panelObservaciones.add(crearLabel("Observaciones / Tratamiento:", fontTitulos), BorderLayout.NORTH);
        
        txtObservaciones = new JTextArea(3, 20);
        estilizarTextArea(txtObservaciones, fontTextos);
        JScrollPane scrollObservaciones = new JScrollPane(txtObservaciones);
        scrollObservaciones.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 224)));
        panelObservaciones.add(scrollObservaciones, BorderLayout.CENTER);

        panelTextos.add(panelSintomas);
        panelTextos.add(panelObservaciones);

        // PANEL FACTURACIÓN
        JPanel panelAdicionales = new JPanel(new BorderLayout(10, 10));
        panelAdicionales.setOpaque(false);
        panelAdicionales.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                " Cargos Adicionales / Insumos ", 
                TitledBorder.LEFT, TitledBorder.TOP, fontTitulos, new Color(74, 85, 104)
        ));
        ((TitledBorder)panelAdicionales.getBorder()).setBorder(BorderFactory.createCompoundBorder(
                ((TitledBorder)panelAdicionales.getBorder()).getBorder(), new EmptyBorder(10, 15, 15, 15)));

        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelChecks.setOpaque(false);
        chkInyeccion = crearCheckBox("Se aplicó Inyección/Vacuna", fontTextos);
        chkMedicinas = crearCheckBox("Se recetaron Medicinas", fontTextos);
        panelChecks.add(chkInyeccion);
        panelChecks.add(chkMedicinas);

        JPanel panelDetalleIns = new JPanel(new BorderLayout(10, 0));
        panelDetalleIns.setOpaque(false);
        panelDetalleIns.add(crearLabel("Detalle Insumos:", fontTitulos), BorderLayout.WEST);
        txtDetalleInsumos = new JTextField();
        estilizarTextFieldEditable(txtDetalleInsumos, fontTextos);
        
        txtDetalleInsumos.setEnabled(false); 
        txtDetalleInsumos.setBackground(new Color(237, 242, 247)); 

        ActionListener checkListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (chkInyeccion.isSelected() || chkMedicinas.isSelected()) {
                    txtDetalleInsumos.setEnabled(true);
                    txtDetalleInsumos.setBackground(Color.WHITE);
                } else {
                    txtDetalleInsumos.setEnabled(false);
                    txtDetalleInsumos.setBackground(new Color(237, 242, 247));
                    txtDetalleInsumos.setText("");
                }
            }
        };

        chkInyeccion.addActionListener(checkListener);
        chkMedicinas.addActionListener(checkListener);

        panelDetalleIns.add(txtDetalleInsumos, BorderLayout.CENTER);

        JPanel wrapAdicionales = new JPanel(new GridLayout(2, 1, 5, 10));
        wrapAdicionales.setOpaque(false);
        wrapAdicionales.add(panelChecks);
        wrapAdicionales.add(panelDetalleIns);
        panelAdicionales.add(wrapAdicionales, BorderLayout.CENTER);

        JPanel panelInferiorCentro = new JPanel(new BorderLayout(10, 15));
        panelInferiorCentro.setOpaque(false);
        panelInferiorCentro.add(panelTextos, BorderLayout.CENTER);
        panelInferiorCentro.add(panelAdicionales, BorderLayout.SOUTH);
        panelCentral.add(top, BorderLayout.NORTH);
        panelCentral.add(panelInferiorCentro, BorderLayout.CENTER);

        //  PANEL DE BOTONES 
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setOpaque(false);
        
        btnGuardar = configurarBoton("Guardar Consulta", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCancelar); 
        panelBotones.add(btnGuardar);  

        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(new Color(74, 85, 104)); 
        return label;
    }
    
    private JCheckBox crearCheckBox(String texto, Font fuente) {
        JCheckBox chk = new JCheckBox(texto);
        chk.setFont(fuente);
        chk.setForeground(new Color(74, 85, 104));
        chk.setOpaque(false);
        chk.setFocusPainted(false);
        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return chk;
    }

    private void estilizarReadOnly(JTextField campo, Font fuente) {
        campo.setEditable(false);
        campo.setFont(fuente);
        campo.setBackground(new Color(237, 242, 247)); 
        campo.setForeground(new Color(74, 85, 104));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(5, 10, 5, 10) 
        ));
    }
    
    private void estilizarTextFieldEditable(JTextField campo, Font fuente) {
        campo.setFont(fuente);
        campo.setForeground(new Color(45, 55, 72));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224)),
                new EmptyBorder(5, 10, 5, 10) 
        ));
    }

    private void estilizarTextArea(JTextArea area, Font fuente) {
        area.setFont(fuente);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setMargin(new Insets(10, 10, 10, 10)); 
        area.setForeground(new Color(45, 55, 72));
        area.setBackground(Color.WHITE);
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(165, 40));
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