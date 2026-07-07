package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDRegistrarMascotaVista extends JDialog {
    public JTextField txtNombre, txtEspecie, txtRaza, txtEdad, txtIdCliente;
    public JButton btnGuardar, btnCancelar;
    
    public JDRegistrarMascotaVista(Frame parent, boolean modal) {
        super(parent, "Registrar Nuevo Paciente", modal);
        inicializarComponentes(parent);
    }

    public JDRegistrarMascotaVista(Frame parent, boolean modal, int idCliente) {
        super(parent, "Registrar Nuevo Paciente", modal);
        inicializarComponentes(parent);
        
        txtIdCliente.setText(String.valueOf(idCliente));
        txtIdCliente.setEditable(false);
        txtIdCliente.setBackground(new Color(237, 242, 247)); 
    }

    private void inicializarComponentes(Frame parent) {
        setSize(480, 480); 
        setLocationRelativeTo(parent);
        setResizable(false);
        
        Color colorFondo = new Color(248, 249, 250);
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 20);
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        mainPanel.setBackground(colorFondo);
        setContentPane(mainPanel);

        // ENCABEZADO 
        JLabel lblTitulo = new JLabel("Registrar Nuevo Paciente");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(new Color(45, 55, 72));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // PANEL DEL FORMULARIO 
        JPanel panelForm = new JPanel(new GridLayout(5, 1, 0, 12));
        panelForm.setOpaque(false);
        
        // Fila 1: Nombre
        JPanel pnlNombre = new JPanel(new BorderLayout(10, 0));
        pnlNombre.setOpaque(false);
        JLabel lblNombre = crearLabel("Nombre Mascota:", fontLabels);
        lblNombre.setPreferredSize(new Dimension(140, 35));
        txtNombre = new JTextField(); 
        estilizarCampoTexto(txtNombre, fontTextos);
        pnlNombre.add(lblNombre, BorderLayout.WEST);
        pnlNombre.add(txtNombre, BorderLayout.CENTER);
        panelForm.add(pnlNombre);
        
        // Fila 2: Especie
        JPanel pnlEspecie = new JPanel(new BorderLayout(10, 0));
        pnlEspecie.setOpaque(false);
        JLabel lblEspecie = crearLabel("Especie:", fontLabels);
        lblEspecie.setPreferredSize(new Dimension(140, 35));
        txtEspecie = new JTextField(); 
        estilizarCampoTexto(txtEspecie, fontTextos);
        pnlEspecie.add(lblEspecie, BorderLayout.WEST);
        pnlEspecie.add(txtEspecie, BorderLayout.CENTER);
        panelForm.add(pnlEspecie);
        
        // Fila 3: Raza
        JPanel pnlRaza = new JPanel(new BorderLayout(10, 0));
        pnlRaza.setOpaque(false);
        JLabel lblRaza = crearLabel("Raza:", fontLabels);
        lblRaza.setPreferredSize(new Dimension(140, 35));
        txtRaza = new JTextField(); 
        estilizarCampoTexto(txtRaza, fontTextos);
        pnlRaza.add(lblRaza, BorderLayout.WEST);
        pnlRaza.add(txtRaza, BorderLayout.CENTER);
        panelForm.add(pnlRaza);

        // Fila 4: Edad
        JPanel pnlEdad = new JPanel(new BorderLayout(10, 0));
        pnlEdad.setOpaque(false);
        JLabel lblEdad = crearLabel("Edad (Años):", fontLabels);
        lblEdad.setPreferredSize(new Dimension(140, 35));
        txtEdad = new JTextField(); 
        estilizarCampoTexto(txtEdad, fontTextos);
        pnlEdad.add(lblEdad, BorderLayout.WEST);
        pnlEdad.add(txtEdad, BorderLayout.CENTER);
        panelForm.add(pnlEdad);

        // Fila 5: ID Dueño
        JPanel pnlDueño = new JPanel(new BorderLayout(10, 0));
        pnlDueño.setOpaque(false);
        JLabel lblDueño = crearLabel("ID Dueño:", fontLabels);
        lblDueño.setPreferredSize(new Dimension(140, 35));
        txtIdCliente = new JTextField(); 
        estilizarCampoTexto(txtIdCliente, fontTextos);
        pnlDueño.add(lblDueño, BorderLayout.WEST);
        pnlDueño.add(txtIdCliente, BorderLayout.CENTER);
        panelForm.add(pnlDueño);

        JPanel wrapperForm = new JPanel(new BorderLayout());
        wrapperForm.setOpaque(false);
        wrapperForm.add(panelForm, BorderLayout.NORTH);
        mainPanel.add(wrapperForm, BorderLayout.CENTER);

        // PANEL DE BOTONES INFERIOR
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelBtn.setOpaque(false);
        
        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Guardar Paciente", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        
        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);
        mainPanel.add(panelBtn, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(new Color(74, 85, 104));
        return label;
    }

    private void estilizarCampoTexto(JTextField campo, Font fuente) {
        campo.setFont(fuente);
        campo.setForeground(new Color(45, 55, 72));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(165, 38)); 
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
        return btn;
    }
}