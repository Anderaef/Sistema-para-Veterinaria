package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JDUsuarioVista extends JPanel {

    public JTable tablaUsuarios;
    public DefaultTableModel modeloTabla;
    public JButton btnRegistrar, btnBuscar, btnRefrescar, btnVolver;
    public JButton btnAccionEditar, btnAccionEliminar;
    public JButton btnAnterior, btnSiguiente;
    public JLabel lblPaginaInfo;

    public JDUsuarioVista() {
        setOpaque(false);
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(15, 15));
        
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);
        Font fontHeader = new Font("Segoe UI", Font.BOLD, 22);

        JPanel panelHeaderMaster = new JPanel(new BorderLayout());
        panelHeaderMaster.setOpaque(false);

        JLabel lblHeader = new JLabel("Panel de Control del Personal");
        lblHeader.setFont(fontHeader);
        lblHeader.setForeground(new Color(45, 55, 72)); 
        
        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotonesTop.setOpaque(false);

        btnBuscar = configurarBoton("Buscar Personal", new Color(74, 85, 104), new Color(45, 55, 72), fontBotones);
        btnBuscar.setPreferredSize(new Dimension(160, 38));
        
        btnRefrescar = configurarBoton("Actualizar Lista", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnRefrescar.setPreferredSize(new Dimension(160, 38));

        btnVolver = configurarBoton("Volver al Inicio", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnVolver.setPreferredSize(new Dimension(180, 38));

        panelBotonesTop.add(btnBuscar);
        panelBotonesTop.add(btnRefrescar);
        panelBotonesTop.add(btnVolver);
        
        panelHeaderMaster.add(lblHeader, BorderLayout.WEST);
        panelHeaderMaster.add(panelBotonesTop, BorderLayout.EAST);
        add(panelHeaderMaster, BorderLayout.NORTH);
        
        modeloTabla = new DefaultTableModel(
                new Object[]{"ID Personal", "Nombre", "Apellido", "Username", "Rol de Sistema", "Acciones"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; 
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(fontNormal);
        tablaUsuarios.setRowHeight(45);
        tablaUsuarios.setBackground(Color.WHITE);
        tablaUsuarios.setShowVerticalLines(false);
        tablaUsuarios.setGridColor(new Color(230, 235, 240));
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        tablaUsuarios.setSelectionBackground(new Color(226, 232, 240));
        tablaUsuarios.setSelectionForeground(new Color(26, 32, 44));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 5; i++) {
            tablaUsuarios.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = tablaUsuarios.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247));
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));
        
        // BOTONES TABLA
        btnAccionEditar = crearBotonTabla("Editar", new Color(113, 128, 150)); 
        btnAccionEliminar = crearBotonTabla("Eliminar",  new Color(224, 122, 95));

        AccionesCeldaEditorRender panelAccionesCelda = new AccionesCeldaEditorRender(btnAccionEditar, btnAccionEliminar);
        tablaUsuarios.getColumnModel().getColumn(5).setCellRenderer(panelAccionesCelda);
        tablaUsuarios.getColumnModel().getColumn(5).setCellEditor(panelAccionesCelda);
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(210); 
        tablaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(80);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        
        //PARTE INFERIOR 
        JPanel panelBotonesBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        panelBotonesBottom.setOpaque(false);

        btnRegistrar = configurarBoton("Registrar Personal", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);
        btnRegistrar.setPreferredSize(new Dimension(185, 42));
        panelBotonesBottom.add(btnRegistrar);

        JPanel panelCentralMaster = new JPanel(new BorderLayout(0, 15));
        panelCentralMaster.setOpaque(false);
        panelCentralMaster.add(scroll, BorderLayout.CENTER);
        panelCentralMaster.add(panelBotonesBottom, BorderLayout.SOUTH);
        add(panelCentralMaster, BorderLayout.CENTER);

        // PAGINACIÓN
        JPanel bottomMaster = new JPanel(new BorderLayout());
        bottomMaster.setOpaque(false);
        bottomMaster.setBorder(new EmptyBorder(10, 0, 5, 0));

        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPaginacion.setOpaque(false);

        btnAnterior = configurarBoton("Ant", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnAnterior.setPreferredSize(new Dimension(85, 36));
        
        lblPaginaInfo = new JLabel("Pág. 1");
        lblPaginaInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPaginaInfo.setForeground(new Color(74, 85, 104));
        
        btnSiguiente = configurarBoton("Sig", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnSiguiente.setPreferredSize(new Dimension(85, 36));
        
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaInfo);
        panelPaginacion.add(btnSiguiente);
        
        bottomMaster.add(panelPaginacion, BorderLayout.CENTER);
        add(bottomMaster, BorderLayout.SOUTH);
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setBackground(normal);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.setBorder(new CompoundBorder(new LineBorder(normal, 1, true), new EmptyBorder(5, 10, 5, 10)));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { 
                btn.setBackground(hover); 
                btn.setBorder(new CompoundBorder(new LineBorder(hover, 1, true), new EmptyBorder(5, 10, 5, 10)));
            }
            @Override public void mouseExited(MouseEvent e) { 
                btn.setBackground(normal); 
                btn.setBorder(new CompoundBorder(new LineBorder(normal, 1, true), new EmptyBorder(5, 10, 5, 10)));
            }
        });
        return btn;
    }

    private JButton crearBotonTabla(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(95, 32)); 
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(bg.darker(), 1, true)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
}

class AccionesCeldaEditorRender extends AbstractCellEditor implements javax.swing.table.TableCellRenderer, javax.swing.table.TableCellEditor {
    private final JPanel panel;

    public AccionesCeldaEditorRender(JButton btnEditar, JButton btnEliminar) {
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.add(btnEditar);
        panel.add(btnEliminar);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}