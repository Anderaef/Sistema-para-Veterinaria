package admin.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class JDEditarMascotaVista extends JDialog {
    public JTextField txtNombre, txtEspecie, txtRaza, txtEdad, txtIdCliente;
    public JButton btnGuardar, btnCancelar;

    public JDEditarMascotaVista(Frame parent, boolean modal) {
        super(parent, "Editar Registro de Mascota", modal);
        setSize(400, 430); 
        setLocationRelativeTo(parent);
        Color colorFondo = new Color(248, 249, 250);
        
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 25, 15, 25));
        mainPanel.setBackground(colorFondo);
        setContentPane(mainPanel);

        // PANEL DEL FORMULARIO 
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 12, 18));
        panelForm.setOpaque(false);
        
        panelForm.add(crearLabel("Nombre:", fontLabels)); 
        txtNombre = new JTextField(); 
        estilizarCampoTexto(txtNombre, fontTextos); 
        panelForm.add(txtNombre);
        
        panelForm.add(crearLabel("Especie:", fontLabels)); 
        txtEspecie = new JTextField(); 
        estilizarCampoTexto(txtEspecie, fontTextos); 
        panelForm.add(txtEspecie);
        
        panelForm.add(crearLabel("Raza:", fontLabels)); 
        txtRaza = new JTextField(); 
        estilizarCampoTexto(txtRaza, fontTextos); 
        panelForm.add(txtRaza);
        
        panelForm.add(crearLabel("Edad (Años):", fontLabels)); 
        txtEdad = new JTextField(); 
        estilizarCampoTexto(txtEdad, fontTextos); 
        txtEdad.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); 
                }
            }
        });
        panelForm.add(txtEdad);
        
        panelForm.add(crearLabel("ID Dueño:", fontLabels)); 
        txtIdCliente = new JTextField(); 
        estilizarCampoTexto(txtIdCliente, fontTextos); 
        txtIdCliente.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume(); 
                }
            }
        });
        panelForm.add(txtIdCliente);

        // PANEL DE BOTÓN INFERIOR
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBtn.setOpaque(false);
        
        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Guardar Cambios", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        
        btnCancelar.addActionListener(e -> dispose());
        
        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);

        mainPanel.add(panelForm, BorderLayout.CENTER);
        mainPanel.add(panelBtn, BorderLayout.SOUTH);
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
        btn.setPreferredSize(new Dimension(150, 40)); 
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