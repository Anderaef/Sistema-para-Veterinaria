package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JPReporteVista extends JPanel {

    public JTable tablaReporte;
    public DefaultTableModel modeloTabla;
    public JButton btnBuscar, btnRefrescar, btnVolver;
    public JButton btnAnterior, btnSiguiente;
    public JLabel lblPaginaInfo;

    public JPReporteVista() {
        setOpaque(false);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setLayout(new BorderLayout(15, 15));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitulo = new JLabel("Clientes y Mascotas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(45, 55, 72));
        
        btnBuscar = crearBotonEstilo("Buscar", new Color(74, 85, 104));
        btnRefrescar = crearBotonEstilo("Refrescar", new Color(113, 128, 150));
        btnVolver = crearBotonEstilo("Volver", new Color(113, 128, 150));
        
        JPanel pnlBotonesHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotonesHeader.setOpaque(false);
        pnlBotonesHeader.add(btnBuscar);
        pnlBotonesHeader.add(btnRefrescar);
        pnlBotonesHeader.add(btnVolver);
        
        pnlHeader.add(lblTitulo, BorderLayout.WEST);
        pnlHeader.add(pnlBotonesHeader, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID Cliente", "Cliente", "Teléfono", "Email",
                        "ID Mascota", "Mascota", "Especie", "Raza", "Edad"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReporte = new JTable(modeloTabla);

        tablaReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaReporte.setRowHeight(45);
        tablaReporte.setBackground(Color.WHITE);
        tablaReporte.setShowVerticalLines(false);
        tablaReporte.setGridColor(new Color(230, 235, 240));
        
        tablaReporte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReporte.getTableHeader().setReorderingAllowed(false);
        
        tablaReporte.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tablaReporte.setSelectionBackground(new Color(226, 232, 240));
        tablaReporte.setSelectionForeground(new Color(26, 32, 44));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaReporte.getColumnCount(); i++) {
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tablaReporte.getColumnModel().getColumn(0).setPreferredWidth(85);
        tablaReporte.getColumnModel().getColumn(1).setPreferredWidth(180);
        tablaReporte.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaReporte.getColumnModel().getColumn(3).setPreferredWidth(220);
        tablaReporte.getColumnModel().getColumn(4).setPreferredWidth(85);
        tablaReporte.getColumnModel().getColumn(5).setPreferredWidth(150);
        tablaReporte.getColumnModel().getColumn(6).setPreferredWidth(120);
        tablaReporte.getColumnModel().getColumn(7).setPreferredWidth(130);
        tablaReporte.getColumnModel().getColumn(8).setPreferredWidth(75);

        JTableHeader header = tablaReporte.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(237, 242, 247));
        header.setForeground(new Color(74, 85, 104));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 205, 215)));

        JScrollPane scroll = new JScrollPane(tablaReporte);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);
        
        JPanel pnlSur = new JPanel(new BorderLayout());
        pnlSur.setOpaque(false);
        pnlSur.setBorder(new EmptyBorder(10, 0, 5, 0));

        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPaginacion.setOpaque(false);

        btnAnterior = crearBotonEstilo("Ant", new Color(113, 128, 150));
        lblPaginaInfo = new JLabel("Pág. 1");
        lblPaginaInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPaginaInfo.setForeground(new Color(74, 85, 104));
        btnSiguiente = crearBotonEstilo("Sig", new Color(113, 128, 150));

        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaInfo);
        panelPaginacion.add(btnSiguiente);
        pnlSur.add(panelPaginacion, BorderLayout.CENTER);

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
}