package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JPCitasAdminVista extends JPanel {

    public JTable tablaCitasGlobales;
    public DefaultTableModel modeloCitas;
    public JButton btnRefrescar, btnVolver; 
    public JButton btnAgendarCita;
    public JButton btnEditarInt, btnEliminarInt;
    public JComboBox<String> cbFiltroVeterinario;
    public JLabel lblTitulo;
    public JButton btnAnterior, btnSiguiente;
    public JLabel lblPaginaInfo;

    public JPCitasAdminVista() {
        setOpaque(false);
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 20, 15, 20)); 

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 18);
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);
        
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel panelIzquierdoTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdoTop.setOpaque(false);

        lblTitulo = new JLabel("Control de Citas");
        lblTitulo.setFont(fontTitulos);
        lblTitulo.setForeground(new Color(45, 55, 72));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelFiltros.setOpaque(false);

        JLabel lblFiltrar = new JLabel("Filtrar por Médico:");
        lblFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFiltrar.setForeground(new Color(74, 85, 104));
        cbFiltroVeterinario = new JComboBox<>();
        cbFiltroVeterinario.setFont(fontNormal);
        cbFiltroVeterinario.setPreferredSize(new Dimension(180, 36));
        cbFiltroVeterinario.setBackground(Color.WHITE);
        btnRefrescar = configurarBoton("Actualizar", new Color(74, 85, 104), new Color(45, 55, 72), fontBotones);
        btnRefrescar.setPreferredSize(new Dimension(120, 36));
        panelFiltros.add(lblFiltrar);
        panelFiltros.add(cbFiltroVeterinario);
        panelFiltros.add(btnRefrescar);
        panelIzquierdoTop.add(lblTitulo);
        panelIzquierdoTop.add(panelFiltros);

        JPanel pnlBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotonesHeader.setOpaque(false);
        btnAgendarCita = configurarBoton("Añadir Cita", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        btnAgendarCita.setPreferredSize(new Dimension(130, 36));
        btnVolver = configurarBoton("Volver al Inicio", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnVolver.setPreferredSize(new Dimension(160, 36));
        
        pnlBotonesHeader.add(btnAgendarCita);
        pnlBotonesHeader.add(btnVolver);

        top.add(panelIzquierdoTop, BorderLayout.WEST);
        top.add(pnlBotonesHeader, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        modeloCitas = new DefaultTableModel(new Object[]{
                "ID Cita", "Fecha y Hora", "Mascota", "Dueño", "Veterinario Asignado", "Motivo", "Estado", "Acciones"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tablaCitasGlobales = new JTable(modeloCitas);
        tablaCitasGlobales.setFont(fontNormal);
        tablaCitasGlobales.setRowHeight(42);
        tablaCitasGlobales.setBackground(Color.WHITE);
        tablaCitasGlobales.setShowVerticalLines(false);
        tablaCitasGlobales.setGridColor(new Color(230, 235, 240));
        
        tablaCitasGlobales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCitasGlobales.getTableHeader().setReorderingAllowed(false);
        
        tablaCitasGlobales.setSelectionBackground(new Color(226, 232, 240));
        tablaCitasGlobales.setSelectionForeground(new Color(26, 32, 44));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tablaCitasGlobales.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaCitasGlobales.getColumnModel().getColumn(0).setPreferredWidth(60); 
        
        tablaCitasGlobales.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaCitasGlobales.getColumnModel().getColumn(1).setPreferredWidth(150); 
        
        tablaCitasGlobales.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        JTableHeader header = tablaCitasGlobales.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247));
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));

        btnEditarInt = crearBotonInterno("Editar", new Color(113, 128, 150));
        btnEliminarInt = crearBotonInterno("Eliminar", new Color(224, 122, 95));

        tablaCitasGlobales.getColumnModel().getColumn(7).setCellRenderer(new AccionesCitaRenderer());
        tablaCitasGlobales.getColumnModel().getColumn(7).setCellEditor(new AccionesCitaEditor(btnEditarInt, btnEliminarInt));
        tablaCitasGlobales.getColumnModel().getColumn(7).setPreferredWidth(190);

        JScrollPane scroll = new JScrollPane(tablaCitasGlobales);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(10, 0, 5, 0));

        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPaginacion.setOpaque(false);
        btnAnterior = configurarBoton("Ant", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnAnterior.setPreferredSize(new Dimension(85, 36));
        lblPaginaInfo = new JLabel("Pág. 1");
        lblPaginaInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPaginaInfo.setForeground(new Color(74, 85, 104));
        btnSiguiente = configurarBoton("Sig", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnSiguiente.setPreferredSize(new Dimension(85, 36));
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaInfo);
        panelPaginacion.add(btnSiguiente);
        
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);
        
        bottom.add(panelPaginacion, BorderLayout.CENTER);
        bottom.add(panelAcciones, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);
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

    public static JButton crearBotonInterno(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE); 
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { 
                if (btn.isEnabled()) btn.setBackground(color.darker()); 
            }
            @Override public void mouseExited(MouseEvent e) { 
                if (btn.isEnabled()) btn.setBackground(color); 
            }
        });
        return btn;
    }
}

class AccionesCitaRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
    private JButton btnDibujoEditar;
    private JButton btnDibujoEliminar;

    public AccionesCitaRenderer() {
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        btnDibujoEditar = JPCitasAdminVista.crearBotonInterno("Editar", new Color(113, 128, 150));
        btnDibujoEliminar = JPCitasAdminVista.crearBotonInterno("Eliminar", new Color(224, 122, 95));
        
        add(btnDibujoEditar);
        add(btnDibujoEliminar);
    }
    
    @Override 
    public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
        setBackground(s ? t.getSelectionBackground() : t.getBackground());
        String estado = (String) t.getValueAt(r, 6);
        boolean activo = "PROGRAMADA".equalsIgnoreCase(estado);
        
        btnDibujoEditar.setEnabled(activo);
        btnDibujoEliminar.setEnabled(activo);
        btnDibujoEditar.setBackground(activo ? new Color(113, 128, 150) : Color.GRAY);
        btnDibujoEliminar.setBackground(activo ? new Color(224, 122, 95) : Color.GRAY);
        
        return this;
    }
}

class AccionesCitaEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnRealEditar;
    private JButton btnRealEliminar;

    public AccionesCitaEditor(JButton btnRealEditar, JButton btnRealEliminar) {
        super(new JCheckBox());
        this.btnRealEditar = btnRealEditar;
        this.btnRealEliminar = btnRealEliminar;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.add(btnRealEditar); 
        panel.add(btnRealEliminar);
        
        btnRealEditar.addActionListener(ev -> fireEditingStopped());
        btnRealEliminar.addActionListener(ev -> fireEditingStopped());
    }
    
    @Override 
    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { 
        panel.setBackground(t.getSelectionBackground());
        String estado = (String) t.getValueAt(r, 6);
        boolean activo = "PROGRAMADA".equalsIgnoreCase(estado);
        
        btnRealEditar.setEnabled(activo);
        btnRealEliminar.setEnabled(activo);
        btnRealEditar.setBackground(activo ? new Color(113, 128, 150) : Color.GRAY);
        btnRealEliminar.setBackground(activo ? new Color(224, 122, 95) : Color.GRAY);
        
        return panel; 
    }
}