package admin.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class JDRegistrarMascotaClienteVista extends JDialog {
    public JTextField txtNombre, txtEspecie, txtRaza, txtEdad;
    public JButton btnGuardar, btnCancelar;
    private int idCliente; 

    public JDRegistrarMascotaClienteVista(Frame parent, boolean modal, int idCliente) {
        super(parent, modal);
        this.idCliente = idCliente;
        
        setTitle("Registrar Nueva Mascota");
        setSize(390, 360); 
        setLocationRelativeTo(parent);
        Color colorFondo = new Color(248, 249, 250);
        
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 14);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(25, 25, 15, 25));
        mainPanel.setBackground(colorFondo);
        setContentPane(mainPanel);

        //  PANEL DEL FORMULARIO 
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 12, 18));
        panelForm.setOpaque(false);
        
        KeyAdapter soloLetras = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        };

        panelForm.add(crearLabel("Nombre:", fontLabels));
        txtNombre = new JTextField();
        estilizarCampoTexto(txtNombre, fontTextos);
        panelForm.add(txtNombre);
        
        panelForm.add(crearLabel("Especie:", fontLabels));
        txtEspecie = new JTextField();
        estilizarCampoTexto(txtEspecie, fontTextos);
        txtEspecie.addKeyListener(soloLetras);
        panelForm.add(txtEspecie);
        
        panelForm.add(crearLabel("Raza:", fontLabels));
        txtRaza = new JTextField();
        estilizarCampoTexto(txtRaza, fontTextos);
        txtRaza.addKeyListener(soloLetras);
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

        // BARRA DE BOTONES INFERIOR 
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelBtn.setOpaque(false);
        
        btnCancelar = configurarBoton("Cancelar", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardar = configurarBoton("Guardar", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        
        panelBtn.add(btnCancelar);
        panelBtn.add(btnGuardar);
        
        mainPanel.add(panelForm, BorderLayout.CENTER);
        mainPanel.add(panelBtn, BorderLayout.SOUTH);
        btnCancelar.addActionListener(e -> dispose());
    }

    public int getIdCliente() {
        return idCliente;
    }

    // --- MÉTODOS DE DISEÑO
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