package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JPFacturacionAdminVista extends JPanel {

    public JTable tablaFacturas;
    public DefaultTableModel modeloFacturas;
    public JButton btnRefrescar, btnAnterior, btnSiguiente, btnVolver, btnNuevaFactura; 
    public JButton btnFiltrarFechas; 
    public JTextField txtFechaInicio, txtFechaFin;
    public JButton btnCalInicio, btnCalFin; 
    public JLabel lblTitulo, lblTotalCaja, lblPaginaInfo;

    public JPFacturacionAdminVista() {
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontLabels = new Font("Segoe UI", Font.BOLD, 13);
        Font fontCajaGrande = new Font("Segoe UI", Font.BOLD, 26);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaHasta = sdf.format(cal.getTime());
        String fechaDesde = sdf.format(cal.getTime());

        Color colorTextoOscuro = new Color(31, 41, 55);
        Color colorTextoGris = new Color(107, 114, 128);
        Color colorBorde = new Color(229, 231, 235);

        setOpaque(false);
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(0, 20));

        // ENCABEZADO 
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        
        lblTitulo = new JLabel("Control de Caja y Finanzas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorTextoOscuro);
        
        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotonesTop.setOpaque(false);
        
        btnNuevaFactura = configurarBoton("Registrar Factura", new Color(113, 128, 150), new Color(75, 85, 99)); 
        btnNuevaFactura.setPreferredSize(new Dimension(170, 38));
        
        btnVolver = configurarBoton("Volver al Inicio", new Color(107, 114, 128), new Color(75, 85, 99)); 
        btnVolver.setPreferredSize(new Dimension(150, 38));
        
        panelBotonesTop.add(btnNuevaFactura);
        panelBotonesTop.add(btnVolver);
        
        pnlHeader.add(lblTitulo, BorderLayout.WEST);
        pnlHeader.add(panelBotonesTop, BorderLayout.EAST);

        // FILTROS 
        JPanel pnlFiltrosCard = new JPanel(new BorderLayout(15, 0));
        pnlFiltrosCard.setBackground(Color.WHITE);
        pnlFiltrosCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 1),
                new EmptyBorder(12, 20, 12, 20)));

        JPanel pnlInputsDestino = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlInputsDestino.setOpaque(false);
        
        JLabel lblDesde = new JLabel("Desde:");
        lblDesde.setFont(fontLabels); lblDesde.setForeground(colorTextoGris);
        txtFechaInicio = crearCampoFechaLectura(fechaDesde);
        
        btnCalInicio = configurarBoton("▼", new Color(243, 244, 246), new Color(229, 231, 235));
        btnCalInicio.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        btnCalInicio.setForeground(colorTextoOscuro); 
        btnCalInicio.setPreferredSize(new Dimension(42, 34));

        JLabel lblHasta = new JLabel("Hasta:");
        lblHasta.setFont(fontLabels); lblHasta.setForeground(colorTextoGris);
        txtFechaFin = crearCampoFechaLectura(fechaHasta);
        
        btnCalFin = configurarBoton("▼", new Color(243, 244, 246), new Color(229, 231, 235));
        btnCalFin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCalFin.setForeground(colorTextoOscuro); 
        btnCalFin.setPreferredSize(new Dimension(42, 34));

        pnlInputsDestino.add(lblDesde); pnlInputsDestino.add(txtFechaInicio); pnlInputsDestino.add(btnCalInicio);
        pnlInputsDestino.add(Box.createRigidArea(new Dimension(10, 0)));
        pnlInputsDestino.add(lblHasta); pnlInputsDestino.add(txtFechaFin); pnlInputsDestino.add(btnCalFin);

        JPanel pnlAccionesFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlAccionesFiltro.setOpaque(false);
        
        btnFiltrarFechas = configurarBoton("Filtrar", new Color(107, 114, 128), new Color(75, 85, 99)); 
        btnFiltrarFechas.setPreferredSize(new Dimension(100, 32));
        
        btnRefrescar = configurarBoton("Limpiar", new Color(243, 244, 246), new Color(229, 231, 235)); 
        btnRefrescar.setForeground(colorTextoOscuro);
        btnRefrescar.setPreferredSize(new Dimension(100, 32));
        
        pnlAccionesFiltro.add(btnFiltrarFechas);
        pnlAccionesFiltro.add(btnRefrescar);

        pnlFiltrosCard.add(pnlInputsDestino, BorderLayout.WEST);
        pnlFiltrosCard.add(pnlAccionesFiltro, BorderLayout.EAST);

        // BALANCE CARD 
        JPanel cardCaja = new JPanel(new BorderLayout(10, 2));
        cardCaja.setBackground(Color.WHITE);
        cardCaja.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(16, 185, 129)), 
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorBorde, 1),
                        new EmptyBorder(15, 20, 15, 20)
                )
        ));
        
        JLabel lblCajaTxt = new JLabel("BALANCE RECAUDADO EN EL PERÍODO SELECCIONADO");
        lblCajaTxt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCajaTxt.setForeground(colorTextoGris);
        
        lblTotalCaja = new JLabel("$0.00");
        lblTotalCaja.setFont(fontCajaGrande);
        lblTotalCaja.setForeground(new Color(16, 185, 129));

        cardCaja.add(lblCajaTxt, BorderLayout.NORTH);
        cardCaja.add(lblTotalCaja, BorderLayout.CENTER);

        JPanel pnlNorteCompleto = new JPanel();
        pnlNorteCompleto.setLayout(new BoxLayout(pnlNorteCompleto, BoxLayout.Y_AXIS));
        pnlNorteCompleto.setOpaque(false);
        pnlNorteCompleto.add(pnlHeader);
        pnlNorteCompleto.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlNorteCompleto.add(pnlFiltrosCard);
        pnlNorteCompleto.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlNorteCompleto.add(cardCaja);
        add(pnlNorteCompleto, BorderLayout.NORTH);

        // TABLA 
        modeloFacturas = new DefaultTableModel(new Object[]{
                "Nº Factura", "ID Cita", "Mascota / Paciente", "Monto Total", "Fecha de Emisión", "Cajero / Generado Por", "Acción"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaFacturas = new JTable(modeloFacturas);
        tablaFacturas.setFont(fontNormal);
        tablaFacturas.setRowHeight(40); 
        tablaFacturas.setBackground(Color.WHITE);
        tablaFacturas.setShowVerticalLines(false);
        tablaFacturas.setGridColor(new Color(243, 244, 246));
        
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFacturas.getTableHeader().setReorderingAllowed(false);
        
        tablaFacturas.setSelectionBackground(new Color(243, 244, 246));
        tablaFacturas.setSelectionForeground(colorTextoOscuro);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaFacturas.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tablaFacturas.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaFacturas.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        tablaFacturas.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText("Ver Detalle");
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setForeground(new Color(37, 99, 235));
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return label;
            }
        });

        JTableHeader header = tablaFacturas.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(249, 250, 251));
        header.setForeground(colorTextoGris);
        header.setPreferredSize(new Dimension(header.getWidth(), 42)); 
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorBorde));

        JScrollPane scroll = new JScrollPane(tablaFacturas);
        scroll.setBorder(BorderFactory.createLineBorder(colorBorde, 1));
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // PAGINACIÓN 
        JPanel bottomMaster = new JPanel(new BorderLayout());
        bottomMaster.setOpaque(false);

        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelPaginacion.setOpaque(false);

        btnAnterior = configurarBoton("Ant", new Color(243, 244, 246), new Color(229, 231, 235));
        btnAnterior.setForeground(colorTextoOscuro);
        btnAnterior.setPreferredSize(new Dimension(85, 36));
        
        lblPaginaInfo = new JLabel("Pág. 1");
        lblPaginaInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPaginaInfo.setForeground(colorTextoGris);
        
        btnSiguiente = configurarBoton("Sig", new Color(243, 244, 246), new Color(229, 231, 235));
        btnSiguiente.setForeground(colorTextoOscuro);
        btnSiguiente.setPreferredSize(new Dimension(85, 36));

        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(lblPaginaInfo);
        panelPaginacion.add(btnSiguiente);
        bottomMaster.add(panelPaginacion, BorderLayout.CENTER);
        add(bottomMaster, BorderLayout.SOUTH);

        // CALENDARIO
        btnCalInicio.addActionListener(e -> desplegarCalendarioGrafico(txtFechaInicio));
        btnCalFin.addActionListener(e -> desplegarCalendarioGrafico(txtFechaFin));
    }

    private void desplegarCalendarioGrafico(JTextField campoDestino) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(topFrame, "Seleccionar Fecha", true);
        dialog.setSize(320, 280);
        dialog.setLocationRelativeTo(campoDestino);
        dialog.setLayout(new BorderLayout(5, 5));

        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(sdf.parse(campoDestino.getText()));
        } catch (Exception ex) {}

        JLabel lblMesAño = new JLabel("", JLabel.CENTER);
        lblMesAño.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JPanel pnlDias = new JPanel(new GridLayout(0, 7, 2, 2));
        
        Runnable pintarCalendario = () -> {
            pnlDias.removeAll();
            SimpleDateFormat mesSdf = new SimpleDateFormat("MMMM yyyy");
            lblMesAño.setText(mesSdf.format(cal.getTime()).toUpperCase());

            String[] diasSemana = {"Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sá"};
            for (String d : diasSemana) {
                JLabel l = new JLabel(d, JLabel.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                pnlDias.add(l);
            }

            Calendar calAux = (Calendar) cal.clone();
            calAux.set(Calendar.DAY_OF_MONTH, 1);
            int primerDia = calAux.get(Calendar.DAY_OF_WEEK) - 1;
            int maxDias = calAux.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 0; i < primerDia; i++) pnlDias.add(new JLabel(""));

            for (int dia = 1; dia <= maxDias; dia++) {
                final int diaSeleccionado = dia;
                JButton btnDia = new JButton(String.valueOf(dia));
                btnDia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                btnDia.setBackground(Color.WHITE);
                btnDia.setMargin(new Insets(2, 2, 2, 2));
                
                if (dia == cal.get(Calendar.DAY_OF_MONTH)) {
                    btnDia.setBackground(new Color(59, 130, 246));
                    btnDia.setForeground(Color.WHITE);
                }

                btnDia.addActionListener(ev -> {
                    cal.set(Calendar.DAY_OF_MONTH, diaSeleccionado);
                    SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
                    campoDestino.setText(sdfOut.format(cal.getTime()));
                    dialog.dispose();
                });
                pnlDias.add(btnDia);
            }
            pnlDias.revalidate(); pnlDias.repaint();
        };

        JButton btnMesAnt = new JButton("◀");
        btnMesAnt.addActionListener(ev -> { cal.add(Calendar.MONTH, -1); pintarCalendario.run(); });
        JButton btnMesSig = new JButton("▶");
        btnMesSig.addActionListener(ev -> { cal.add(Calendar.MONTH, 1); pintarCalendario.run(); });

        JPanel pnlNavegacion = new JPanel(new BorderLayout());
        pnlNavegacion.add(btnMesAnt, BorderLayout.WEST);
        pnlNavegacion.add(lblMesAño, BorderLayout.CENTER);
        pnlNavegacion.add(btnMesSig, BorderLayout.EAST);

        dialog.add(pnlNavegacion, BorderLayout.NORTH);
        dialog.add(pnlDias, BorderLayout.CENTER);
        
        pintarCalendario.run();
        dialog.setVisible(true);
    }

    private JTextField crearCampoFechaLectura(String valorInicial) {
        JTextField tf = new JTextField(valorInicial, 9);
        tf.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224), 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return tf;
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
            @Override public void mouseEntered(MouseEvent e) { if(btn.getForeground() == Color.WHITE) btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
        return btn;
    }
    
    private JButton configurarBoton(String texto, Color normal, Color hover) {
        return configurarBoton(texto, normal, hover, new Font("Segoe UI", Font.BOLD, 13));
    }
}