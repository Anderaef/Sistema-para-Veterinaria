package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDRegistrarUsuarioVista extends JDialog {

    public JTextField txtNombre, txtApellido, txtUsername;
    public JPasswordField txtPassword;
    public JComboBox<String> cbRol;
    public JButton btnGuardar, btnCancelar;

    public JDRegistrarUsuarioVista(Frame parent, boolean modal) {
        super(parent, modal);
        
        setTitle("Registrar Nuevo Personal");
        setSize(390, 440); 
        setLocationRelativeTo(parent);
        setResizable(false); 
        Color colorFondo = new Color(248, 249, 250);

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);
        Font fontAyuda = new Font("Segoe UI", Font.ITALIC, 11);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 12));
        mainPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        mainPanel.setBackground(colorFondo);
        setContentPane(mainPanel);

        JPanel panelForm = new JPanel(new GridLayout(5, 2, 12, 14));
        panelForm.setOpaque(false);
        
        TitledBorder bordeTitulo = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1), "Credenciales de Acceso ");
        bordeTitulo.setTitleFont(fontTitulos);
        bordeTitulo.setTitleColor(new Color(74, 85, 104));
        panelForm.setBorder(BorderFactory.createCompoundBorder(bordeTitulo, new EmptyBorder(12, 15, 12, 15)));
        
        KeyAdapter soloLetras = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        };

        txtNombre = new JTextField();
        estilizarCampoTexto(txtNombre, fontTextos);
        txtNombre.addKeyListener(soloLetras);
        
        txtApellido = new JTextField();
        estilizarCampoTexto(txtApellido, fontTextos);
        txtApellido.addKeyListener(soloLetras);
        
        txtUsername = new JTextField();
        estilizarCampoTexto(txtUsername, fontTextos);
        txtUsername.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (Character.isWhitespace(e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(fontTextos);
        txtPassword.setForeground(new Color(45, 55, 72));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String[] roles = {"admin", "veterinario", "recepcionista"};
        cbRol = new JComboBox<>(roles);
        cbRol.setFont(fontTextos);
        cbRol.setBackground(Color.WHITE);
        cbRol.setForeground(new Color(45, 55, 72));
        
        panelForm.add(crearLabel("Nombre:", fontTitulos));
        panelForm.add(txtNombre);
        panelForm.add(crearLabel("Apellido:", fontTitulos));
        panelForm.add(txtApellido);
        panelForm.add(crearLabel("Username:", fontTitulos));
        panelForm.add(txtUsername);
        panelForm.add(crearLabel("Contraseña:", fontTitulos));
        panelForm.add(txtPassword);
        panelForm.add(crearLabel("Rol de Sistema:", fontTitulos));
        panelForm.add(cbRol);

        JPanel panelInfoPassword = new JPanel();
        panelInfoPassword.setLayout(new BoxLayout(panelInfoPassword, BoxLayout.Y_AXIS));
        panelInfoPassword.setOpaque(false);
        panelInfoPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(237, 242, 247), 1), 
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JLabel lblInfoTitle = new JLabel("? Requisitos de seguridad:");
        lblInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblInfoTitle.setForeground(new Color(74, 85, 104));
        
        JLabel lblInfoDetalle = new JLabel("<html>Mínimo 8 caracteres, incluir una <b>MAYÚSCULA</b>, un <b>número</b> y un símbolo especial.</html>");
        lblInfoDetalle.setFont(fontAyuda);
        lblInfoDetalle.setForeground(new Color(113, 128, 150));

        panelInfoPassword.add(lblInfoTitle);
        panelInfoPassword.add(Box.createRigidArea(new Dimension(0, 3)));
        panelInfoPassword.add(lblInfoDetalle);

        JPanel panelCuerpoAgrupado = new JPanel(new BorderLayout(0, 10));
        panelCuerpoAgrupado.setOpaque(false);
        panelCuerpoAgrupado.add(panelForm, BorderLayout.CENTER);
        panelCuerpoAgrupado.add(panelInfoPassword, BorderLayout.SOUTH);
        mainPanel.add(panelCuerpoAgrupado, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        panelBotones.setOpaque(false);

        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Guardar", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
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
                BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(130, 38)); 
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