package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDRegistrarClienteVista extends JDialog {
    public JTextField txtNombre, txtTelefono, txtEmail;
    public JButton btnGuardar, btnCancelar;

    public JDRegistrarClienteVista(Frame parent, boolean modal) {
        super(parent, "Registrar Nuevo Cliente", modal);
        
        setSize(480, 360);
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
        JLabel lblTitulo = new JLabel("Registrar Nuevo Cliente");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(new Color(45, 55, 72));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // PANEL DEL FORMULARIO
        JPanel panelForm = new JPanel(new GridLayout(3, 1, 0, 15));
        panelForm.setOpaque(false);
        
        // Fila 1: Nombre
        JPanel pnlNombre = new JPanel(new BorderLayout(10, 0));
        pnlNombre.setOpaque(false);
        JLabel lblNombre = crearLabel("Nombre:", fontLabels);
        lblNombre.setPreferredSize(new Dimension(140, 35));
        txtNombre = new JTextField(); 
        estilizarCampoTexto(txtNombre, fontTextos);
        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        pnlNombre.add(lblNombre, BorderLayout.WEST);
        pnlNombre.add(txtNombre, BorderLayout.CENTER);
        panelForm.add(pnlNombre);
        
        // Fila 2: Teléfono
        JPanel pnlTelefono = new JPanel(new BorderLayout(10, 0));
        pnlTelefono.setOpaque(false);
        JLabel lblTelefono = crearLabel("Teléfono:", fontLabels);
        lblTelefono.setPreferredSize(new Dimension(140, 35));
        txtTelefono = new JTextField(); 
        estilizarCampoTexto(txtTelefono, fontTextos);
        txtTelefono.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        pnlTelefono.add(lblTelefono, BorderLayout.WEST);
        pnlTelefono.add(txtTelefono, BorderLayout.CENTER);
        panelForm.add(pnlTelefono);
        
        // Fila 3: Email
        JPanel pnlEmail = new JPanel(new BorderLayout(10, 0));
        pnlEmail.setOpaque(false);
        JLabel lblEmail = crearLabel("Correo Electrónico:", fontLabels);
        lblEmail.setPreferredSize(new Dimension(140, 35));
        txtEmail = new JTextField(); 
        estilizarCampoTexto(txtEmail, fontTextos);
        txtEmail.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (Character.isWhitespace(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        pnlEmail.add(lblEmail, BorderLayout.WEST);
        pnlEmail.add(txtEmail, BorderLayout.CENTER);
        panelForm.add(pnlEmail);

        JPanel wrapperForm = new JPanel(new BorderLayout());
        wrapperForm.setOpaque(false);
        wrapperForm.add(panelForm, BorderLayout.NORTH);
        mainPanel.add(wrapperForm, BorderLayout.CENTER);

        // PANEL DE BOTONES INFERIOR
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelBtn.setOpaque(false);
        
        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Guardar Cliente", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        
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
        btn.setPreferredSize(new Dimension(160, 38)); 
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