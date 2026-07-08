package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class JFLoginVista extends JFrame {
    
    public JTextField txtUsuario = new JTextField(15);
    public JPasswordField txtPassword = new JPasswordField(15);
    public JButton btnIngresar = new JButton("INGRESAR");
    public JToggleButton btnMostrarPass = new JToggleButton();
    
    private JLabel lblImagenLogin = new JLabel();
    
    public char caracterPorDefecto; 

    public JFLoginVista() {
        setTitle("Acceso - VetClinic");
        setSize(360, 520); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(35, 45, 35, 45));

        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 22);
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 13);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBoton = new Font("Segoe UI", Font.BOLD, 14);

        // IMAGEN / LOGO
        lblImagenLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        cargarImagen(lblImagenLogin, "/Imagenes/logo.png"); 
        
        // TÍTULO PRINCIPAL
        JLabel lblTitulo = new JLabel("INICIAR SESIÓN");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(new Color(45, 55, 72)); 
        
        // ETIQUETA USUARIO
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(fontLabels);
        lblUsuario.setForeground(new Color(74, 85, 104));
        lblUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // ETIQUETA CONTRASEÑA
        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(fontLabels);
        lblPassword.setForeground(new Color(74, 85, 104));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // CAJA DE TEXTO USUARIO
        txtUsuario.setFont(fontTextos);
        txtUsuario.setMaximumSize(new Dimension(300, 38));
        txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsuario.setForeground(new Color(45, 55, 72));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10) 
        ));
        
        //  CONTENEDOR DE CONTRASEÑA Y OJITO 
        JPanel panelPassContainer = new JPanel(new BorderLayout(5, 0));
        panelPassContainer.setOpaque(false);
        panelPassContainer.setMaximumSize(new Dimension(300, 38));
        panelPassContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPassContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(2, 10, 2, 5)
        ));

        // CAJA DE TEXTO CONTRASEÑA
        caracterPorDefecto = txtPassword.getEchoChar(); 
        txtPassword.setFont(fontTextos);
        txtPassword.setForeground(new Color(45, 55, 72));
        txtPassword.setBorder(null); 
        
        // CONFIGURACIÓN DEL BOTÓN OJITO
        btnMostrarPass.setIcon(new IconoOjo(false));
        btnMostrarPass.setSelectedIcon(new IconoOjo(true)); 
        btnMostrarPass.setContentAreaFilled(false);
        btnMostrarPass.setBorderPainted(false);
        btnMostrarPass.setFocusPainted(false);
        btnMostrarPass.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelPassContainer.add(txtPassword, BorderLayout.CENTER);
        panelPassContainer.add(btnMostrarPass, BorderLayout.EAST);
        Color colorNormal = new Color(42, 157, 143);
        Color colorHover = new Color(34, 128, 116);
        
        btnIngresar.setBackground(colorNormal);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setMaximumSize(new Dimension(300, 42));
        btnIngresar.setFont(fontBoton);
        
        btnIngresar.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnIngresar.setBackground(colorHover); }
            @Override public void mouseExited(MouseEvent e) { btnIngresar.setBackground(colorNormal); }
        });
        
        panelPrincipal.add(lblImagenLogin);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JPanel panelWrapUser = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelWrapUser.setOpaque(false);
        panelWrapUser.setMaximumSize(new Dimension(300, 20));
        panelWrapUser.add(lblUsuario);
        panelPrincipal.add(panelWrapUser);
        
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPrincipal.add(txtUsuario);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel panelWrapPass = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelWrapPass.setOpaque(false);
        panelWrapPass.setMaximumSize(new Dimension(300, 20));
        panelWrapPass.add(lblPassword);
        panelPrincipal.add(panelWrapPass);
        
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPrincipal.add(panelPassContainer); 
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPrincipal.add(btnIngresar);

        add(panelPrincipal);
    }

    private void cargarImagen(JLabel label, String rutaImagen) {
        try {
            java.net.URL imgURL = getClass().getResource(rutaImagen);
            if (imgURL != null) {
                BufferedImage imgOriginal = ImageIO.read(imgURL);
                Image imgEscalada = imgOriginal.getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(imgEscalada));
            } else {
                label.setText("LOGO NO ENCONTRADO");
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setForeground(new Color(224, 122, 95));
            }
        } catch (Exception e) {
            label.setText("ERROR CARGA");
            label.setForeground(new Color(224, 122, 95));
        }
    }

    public void setLoginListener(ActionListener listener) {
        btnIngresar.addActionListener(listener); 
        txtPassword.addActionListener(listener);    
    }
    
    class IconoOjo implements Icon {
        private boolean tachado;
        public IconoOjo(boolean tachado) { this.tachado = tachado; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(107, 114, 128)); 
            g2.setStroke(new BasicStroke(1.5f)); 
            
            g2.drawArc(x + 2, y + 5, 16, 10, 0, 180);
            g2.drawArc(x + 2, y + 5, 16, 10, 180, 180);
            g2.fillOval(x + 7, y + 7, 6, 6);
            
            if (tachado) {
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawLine(x + 2, y + 3, x + 18, y + 17);
            }
            g2.dispose();
        }

        @Override public int getIconWidth() { return 20; }
        @Override public int getIconHeight() { return 20; }
    }
}