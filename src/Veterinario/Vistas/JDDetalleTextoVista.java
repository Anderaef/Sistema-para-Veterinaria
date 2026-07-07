package Veterinario.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class JDDetalleTextoVista extends JDialog {
    
    public JTextArea txtDiagnostico;
    public JTextArea txtTratamiento;
    public JTextArea txtDetalleInsumos;
    public JCheckBox chkInyeccion;
    public JCheckBox chkMedicinas;
    public JButton btnCerrar;

    public JDDetalleTextoVista(Dialog parent, boolean modal) {
        super(parent, "Detalle Completo de la Consulta", modal);
        setSize(550, 650);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        mainPanel.setBackground(new Color(248, 249, 250));
        setContentPane(mainPanel);

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        // 1. Diagnóstico
        txtDiagnostico = crearTextArea(fontTextos);
        panelCentro.add(crearPanelConTitulo("Diagnóstico", txtDiagnostico, fontTitulos));
        panelCentro.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2. Tratamiento
        txtTratamiento = crearTextArea(fontTextos);
        panelCentro.add(crearPanelConTitulo("Tratamiento", txtTratamiento, fontTitulos));
        panelCentro.add(Box.createRigidArea(new Dimension(0, 10)));

        // 3. Insumos y Medicinas 
        JPanel pnlInsumos = new JPanel(new BorderLayout(5, 5));
        pnlInsumos.setOpaque(false);
        pnlInsumos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224)),
                " Insumos Aplicados / Recetados ",
                TitledBorder.LEFT, TitledBorder.TOP, fontTitulos, new Color(74, 85, 104)
        ));

        JPanel pnlChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlChecks.setOpaque(false);

        chkInyeccion = new JCheckBox("Aplicó Inyección/Vacuna");
        chkMedicinas = new JCheckBox("Recetó Medicinas");
        estilizarCheck(chkInyeccion, fontTextos);
        estilizarCheck(chkMedicinas, fontTextos);
        pnlChecks.add(chkInyeccion);
        pnlChecks.add(chkMedicinas);

        txtDetalleInsumos = crearTextArea(fontTextos);
        txtDetalleInsumos.setRows(3);
        JScrollPane scrollInsumos = new JScrollPane(txtDetalleInsumos);
        scrollInsumos.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        pnlInsumos.add(pnlChecks, BorderLayout.NORTH);
        pnlInsumos.add(scrollInsumos, BorderLayout.CENTER);

        panelCentro.add(pnlInsumos);

        mainPanel.add(panelCentro, BorderLayout.CENTER);

        // Botón Cerrar
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtn.setOpaque(false);
        btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrar.setBackground(new Color(113, 128, 150));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setPreferredSize(new Dimension(120, 35));
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        panelBtn.add(btnCerrar);

        mainPanel.add(panelBtn, BorderLayout.SOUTH);
    }

    private JTextArea crearTextArea(Font fuente) {
        JTextArea area = new JTextArea(4, 20);
        area.setFont(fuente);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBackground(new Color(237, 242, 247));
        area.setForeground(new Color(45, 55, 72));
        area.setBorder(new EmptyBorder(5, 5, 5, 5));
        return area;
    }

    private JPanel crearPanelConTitulo(String titulo, JTextArea area, Font fuenteTitulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224)),
                " " + titulo + " ",
                TitledBorder.LEFT, TitledBorder.TOP, fuenteTitulo, new Color(74, 85, 104)
        ));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void estilizarCheck(JCheckBox chk, Font fuente) {
        chk.setFont(fuente);
        chk.setForeground(new Color(74, 85, 104));
        chk.setOpaque(false);
        chk.setEnabled(false); 
    }
}