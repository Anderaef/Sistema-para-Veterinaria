package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JPMascotaVista extends JPanel {

    public JTable tablaMascotas;
    public DefaultTableModel modeloTabla;
    public JButton btnRegistrar, btnBuscar, btnRefrescar, btnVolver;
    public JButton btnEditarInt, btnEliminarInt;
    public JButton btnAnterior, btnSiguiente;
    public JLabel lblPaginaInfo;
    
    public JTextField txtNombre = new JTextField(), txtEspecie = new JTextField(), 
                     txtRaza = new JTextField(), txtEdad = new JTextField(), txtIdCliente = new JTextField();
    public JButton btnEditar = new JButton(), btnEliminar = new JButton();

    public JPMascotaVista() {
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setLayout(new BorderLayout(15, 15));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitulo = new JLabel("Gestión de Mascotas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(45, 55, 72));
        
        btnBuscar = crearBotonEstilo("Buscar", new Color(74, 85, 104));
        btnRefrescar = crearBotonEstilo("Refrescar", new Color(113, 128, 150));
        btnVolver = crearBotonEstilo("Volver al inicio", new Color(113, 128, 150));
        
        JPanel pnlBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotonesHeader.setOpaque(false);
        pnlBotonesHeader.add(btnBuscar); 
        pnlBotonesHeader.add(btnRefrescar); 
        pnlBotonesHeader.add(btnVolver);
 
        pnlHeader.add(lblTitulo, BorderLayout.WEST);
        pnlHeader.add(pnlBotonesHeader, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Especie", "Raza", "Edad", "ID Dueño", "Acciones"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 6; }
        };
        tablaMascotas = new JTable(modeloTabla);
        
        tablaMascotas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaMascotas.setRowHeight(45);
        tablaMascotas.setBackground(Color.WHITE);
        tablaMascotas.setShowVerticalLines(false);
        tablaMascotas.setGridColor(new Color(230, 235, 240));
        
        tablaMascotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMascotas.getTableHeader().setReorderingAllowed(false);
        
        tablaMascotas.setSelectionBackground(new Color(226, 232, 240));
        tablaMascotas.setSelectionForeground(new Color(26, 32, 44));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 6; i++) {
            tablaMascotas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = tablaMascotas.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247));
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));
        
        btnEditarInt = crearBotonInterno("Editar", new Color(113, 128, 150));
        btnEliminarInt = crearBotonInterno("Eliminar", new Color(224, 122, 95));
        
        tablaMascotas.getColumnModel().getColumn(6).setCellRenderer(new AccionesMascotaRenderer());
        tablaMascotas.getColumnModel().getColumn(6).setCellEditor(new AccionesMascotaEditor(btnEditarInt, btnEliminarInt));
        tablaMascotas.getColumnModel().getColumn(6).setPreferredWidth(220);
        
        JScrollPane scroll = new JScrollPane(tablaMascotas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setOpaque(false);
        pnlSur.setBorder(new EmptyBorder(10, 0, 5, 0));
        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPaginacion.setOpaque(false);

        btnAnterior = crearBotonEstilo("Ant", new Color(113, 128, 150));
        btnAnterior.setForeground(Color.WHITE); 
        
        lblPaginaInfo = new JLabel("Pág. 1");
        lblPaginaInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPaginaInfo.setForeground(new Color(74, 85, 104));
        
        btnSiguiente = crearBotonEstilo("Sig", new Color(113, 128, 150));
        btnSiguiente.setForeground(Color.WHITE); 
        
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaInfo);
        panelPaginacion.add(btnSiguiente);
        pnlSur.add(panelPaginacion, BorderLayout.CENTER);
        btnRegistrar = crearBotonEstilo("Registrar Nuevo Paciente", new Color(42, 157, 143));
        JPanel panelBotonesBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonesBottom.setOpaque(false);
        panelBotonesBottom.add(btnRegistrar);
        pnlSur.add(panelBotonesBottom, BorderLayout.EAST);
        add(pnlSur, BorderLayout.SOUTH);
    }

    private JButton crearBotonEstilo(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }

    public static JButton crearBotonInterno(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
        return btn;
    }
}

class AccionesMascotaRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    public AccionesMascotaRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JButton btnDibujoEditar = JPMascotaVista.crearBotonInterno("Editar", new Color(113, 128, 150));
        JButton btnDibujoEliminar = JPMascotaVista.crearBotonInterno("Eliminar", new Color(224, 122, 95));
        
        add(btnDibujoEditar);
        add(btnDibujoEliminar);
    }
    
    @Override 
    public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
        setBackground(s ? t.getSelectionBackground() : t.getBackground());
        return this;
    }
}

class AccionesMascotaEditor extends DefaultCellEditor {
    private JPanel panel;
    public AccionesMascotaEditor(JButton btnRealEditar, JButton btnRealEliminar) {
        super(new JCheckBox());
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.add(btnRealEditar); 
        panel.add(btnRealEliminar);
        
        btnRealEditar.addActionListener(ev -> fireEditingStopped());
        btnRealEliminar.addActionListener(ev -> fireEditingStopped());
    }
    
    @Override 
    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { 
        panel.setBackground(t.getSelectionBackground());
        return panel; 
    }
}