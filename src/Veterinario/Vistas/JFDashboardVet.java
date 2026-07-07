package Veterinario.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JFDashboardVet extends JFrame {

    public JLabel lblVeterinario;
    public JButton btnCerrarSesion;
    public JButton btnRefrescar;
    public JButton btnHistorial; 
    public JTable tablaCitas;
    public DefaultTableModel modelo;
    public ButtonEditorAtender btnEditorAccion; 

    public JFDashboardVet() {

        setTitle("VetClinic - Dashboard Veterinario");
        setSize(950, 550); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        getContentPane().setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout(10, 10));

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 16);
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        // TOP PANEL
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(20, 25, 10, 25)); 

        lblVeterinario = new JLabel("Veterinario: ");
        lblVeterinario.setFont(fontTitulos);
        lblVeterinario.setForeground(new Color(45, 55, 72));

        btnCerrarSesion = configurarBoton("Cerrar sesión", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnCerrarSesion.setPreferredSize(new Dimension(130, 35));

        top.add(lblVeterinario, BorderLayout.WEST);
        top.add(btnCerrarSesion, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // TABLA
        modelo = new DefaultTableModel(new Object[]{
                "ID", "Hora", "Mascota", "Cliente", "Motivo", "Estado", "Acción" 
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };

        tablaCitas = new JTable(modelo);
        tablaCitas.setFont(fontNormal);
        tablaCitas.setRowHeight(40);
        tablaCitas.setOpaque(true);
        tablaCitas.setBackground(Color.WHITE);
        tablaCitas.setShowVerticalLines(false); 
        tablaCitas.setGridColor(new Color(230, 235, 240));

        tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCitas.getTableHeader().setReorderingAllowed(false);
        
        tablaCitas.setSelectionBackground(new Color(226, 232, 240));
        tablaCitas.setSelectionForeground(new Color(26, 32, 44));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 6; i++) {
            tablaCitas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = tablaCitas.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247)); 
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));

        btnEditorAccion = new ButtonEditorAtender(new JCheckBox());
        tablaCitas.getColumnModel().getColumn(6).setCellRenderer(new ButtonRendererAtender());
        tablaCitas.getColumnModel().getColumn(6).setCellEditor(btnEditorAccion);

        JScrollPane scroll = new JScrollPane(tablaCitas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(0, 25, 0, 25));
        panelCentral.add(scroll, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // BOTONES INFERIORES
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(10, 25, 20, 25)); 

        btnHistorial = configurarBoton("Ver Historial Completo", new Color(29, 53, 87), new Color(21, 39, 66), fontBotones);
        btnHistorial.setPreferredSize(new Dimension(195, 35));
        btnHistorial.setEnabled(false);
        btnHistorial.setBackground(Color.GRAY);

        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                if (tablaCitas.getSelectedRow() != -1) {
                    btnHistorial.setEnabled(true);
                    btnHistorial.setBackground(new Color(29, 53, 87));
                } else {
                    btnHistorial.setEnabled(false);
                    btnHistorial.setBackground(Color.GRAY);
                }
            }
        });

        btnRefrescar = configurarBoton("Refrescar", new Color(74, 85, 104), new Color(45, 55, 72), fontBotones);
        btnRefrescar.setPreferredSize(new Dimension(130, 35));
        
        bottom.add(btnHistorial);
        bottom.add(btnRefrescar);

        add(bottom, BorderLayout.SOUTH);
    }

    public void actualizarTextoCabecera(String nombreMedico, int citasPendientes) {
        String texto = "Veterinario: " + nombreMedico;
        if (citasPendientes > 0) {
            texto += "  | Citas para hoy: " + citasPendientes;
        } else {
            texto += "  | ¡Al día por hoy!";
        }
        lblVeterinario.setText(texto);
    }

    public void setVisibilidadHistorial(boolean visible) {
        btnHistorial.setVisible(visible);
        if (btnHistorial.getParent() != null) {
            btnHistorial.getParent().revalidate();
            btnHistorial.getParent().repaint();
        }
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                if (btn.isEnabled()) btn.setBackground(hover); 
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                if (btn.isEnabled()) btn.setBackground(normal); 
            }
        });
        return btn;
    }
}

class ButtonRendererAtender extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRendererAtender() {
        setOpaque(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        String estado = (String) table.getValueAt(row, 5);
        
        if ("COMPLETADA".equalsIgnoreCase(estado)) {
            setText("Ver info");
            setBackground(new Color(66, 153, 225)); 
            setForeground(Color.WHITE);
        } else {
            setText("Atender");
            setBackground(new Color(42, 157, 143)); 
            setForeground(Color.WHITE);
        }
        return this;
    }
}

class ButtonEditorAtender extends DefaultCellEditor {
    protected JButton button;
    private int idCita;
    private String estadoCita;

    public ButtonEditorAtender(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        
        this.idCita = (int) table.getValueAt(row, 0); 
        this.estadoCita = (String) table.getValueAt(row, 5); 
        
        if ("COMPLETADA".equalsIgnoreCase(estadoCita)) {
            button.setText("Ver info");
            button.setBackground(new Color(66, 153, 225));
            button.setForeground(Color.WHITE);
        } else {
            button.setText("Atender");
            button.setBackground(new Color(42, 157, 143));
            button.setForeground(Color.WHITE);
        }
        return button;
    }

    public int getIdCita() { return idCita; }
    public String getEstadoCita() { return estadoCita; }
    public void setAccion(ActionListener listener) { button.addActionListener(listener); }
}