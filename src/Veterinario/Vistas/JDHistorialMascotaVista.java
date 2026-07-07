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

public class JDHistorialMascotaVista extends JDialog {

    public JTable tablaHistorial;
    public DefaultTableModel modeloHistorial;
    public JLabel lblTitulo;
    public JButton btnCerrar;
    public ButtonEditorDetalle btnEditorDetalle; 

    public JDHistorialMascotaVista(Frame parent, boolean modal) {
        super(parent, modal);
        setSize(1000, 450);
        setLocationRelativeTo(parent);
        
        getContentPane().setBackground(new Color(248, 249, 250));
        setLayout(new BorderLayout(10, 10));

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 16);
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);

        //  TOP PANEL 
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(15, 25, 5, 25));
        
        lblTitulo = new JLabel("Historial Clínico de la Mascota");
        lblTitulo.setFont(fontTitulos);
        lblTitulo.setForeground(new Color(45, 55, 72)); 
        topPanel.add(lblTitulo);
        add(topPanel, BorderLayout.NORTH);

        modeloHistorial = new DefaultTableModel(new Object[]{
                "Fecha Consulta", "Diagnóstico", "Tratamiento", "Inyección", "Medicinas", "Atendido por", "Detalles"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };

        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setFont(fontNormal);
        tablaHistorial.setRowHeight(38); 
        tablaHistorial.setBackground(Color.WHITE);
        tablaHistorial.setShowVerticalLines(false);
        tablaHistorial.setGridColor(new Color(230, 235, 240));

        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.getTableHeader().setReorderingAllowed(false);

        tablaHistorial.setSelectionBackground(new Color(226, 232, 240));
        tablaHistorial.setSelectionForeground(new Color(26, 32, 44));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaHistorial.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        tablaHistorial.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); 
        tablaHistorial.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); 
        tablaHistorial.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

        btnEditorDetalle = new ButtonEditorDetalle(new JCheckBox());
        tablaHistorial.getColumnModel().getColumn(6).setCellRenderer(new ButtonRendererDetalle());
        tablaHistorial.getColumnModel().getColumn(6).setCellEditor(btnEditorDetalle);
        tablaHistorial.getColumnModel().getColumn(6).setPreferredWidth(80); 
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(80);

        JTableHeader header = tablaHistorial.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247)); 
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(0, 25, 0, 25));
        panelCentral.add(scroll, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        //  BOTÓN CERRAR 
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 25, 20, 25));

        btnCerrar = configurarBoton("Cerrar", new Color(74, 85, 104), new Color(45, 55, 72), fontBotones);
        btnCerrar.setPreferredSize(new Dimension(120, 35));
        btnCerrar.addActionListener(e -> dispose());
        
        bottomPanel.add(btnCerrar);
        add(bottomPanel, BorderLayout.SOUTH);
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
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
        return btn;
    }
}

class ButtonRendererDetalle extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRendererDetalle() {
        setOpaque(true);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBackground(new Color(237, 242, 247));
        setForeground(new Color(74, 85, 104));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Ver Insumos");
        return this;
    }
}

class ButtonEditorDetalle extends DefaultCellEditor {
    protected JButton button;

    public ButtonEditorDetalle(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Ver Insumos");
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(226, 232, 240)); 
        button.setForeground(new Color(45, 55, 72));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return button;
    }

    public void setAccion(ActionListener listener) {
        button.addActionListener(listener);
    }
}