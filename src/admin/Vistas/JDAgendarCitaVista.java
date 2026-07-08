package admin.Vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JDAgendarCitaVista extends JDialog {

    public JTextField txtFecha;
    public JButton btnCalendario;
    public JSpinner spnHora;   
    public JTextArea txtMotivo;
    public JComboBox<String> cbMascotasClientes; 
    public JComboBox<String> cbVeterinarios;
    public JButton btnGuardarCita, btnCancelar, btnVolver;

    public JDAgendarCitaVista(Frame parent, boolean modal) {
        super(parent, "Agendamiento de Citas", modal);
        setSize(700, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(248, 249, 250));
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        setContentPane(mainPanel);

        Font fontTitulos = new Font("Segoe UI", Font.BOLD, 15);
        Font fontTextos = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontBotones = new Font("Segoe UI", Font.BOLD, 13);
        Font fontHeader = new Font("Segoe UI", Font.BOLD, 18);

        JPanel panelHeaderMaster = new JPanel(new BorderLayout());
        panelHeaderMaster.setOpaque(false);
        JLabel lblTitulo = new JLabel("Agendamiento y Asignación de Citas Médicas");
        lblTitulo.setFont(fontHeader);
        lblTitulo.setForeground(new Color(45, 55, 72));
        btnVolver = configurarBoton("Cerrar", new Color(113, 128, 150), new Color(74, 85, 104), fontBotones);
        btnVolver.setPreferredSize(new Dimension(130, 36));
        panelHeaderMaster.add(lblTitulo, BorderLayout.WEST);
        panelHeaderMaster.add(btnVolver, BorderLayout.EAST);
        mainPanel.add(panelHeaderMaster, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 15, 20)); 
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                " Datos de la Nueva Cita ", 
                TitledBorder.LEFT, TitledBorder.TOP, fontTitulos, new Color(74, 85, 104)
        ));
        ((TitledBorder)form.getBorder()).setBorder(BorderFactory.createCompoundBorder(
                ((TitledBorder)form.getBorder()).getBorder(), new EmptyBorder(20, 30, 20, 30)));

        Dimension dimensionCuadros = new Dimension(250, 38);

        cbMascotasClientes = new JComboBox<>();
        cbMascotasClientes.setFont(fontTextos);
        cbMascotasClientes.setBackground(Color.WHITE);
        cbMascotasClientes.setForeground(new Color(45, 55, 72));
        cbMascotasClientes.setPreferredSize(dimensionCuadros);

        cbVeterinarios = new JComboBox<>();
        cbVeterinarios.setFont(fontTextos);
        cbVeterinarios.setBackground(Color.WHITE);
        cbVeterinarios.setForeground(new Color(45, 55, 72));
        cbVeterinarios.setPreferredSize(dimensionCuadros);

        JPanel panelFechaHora = new JPanel(new BorderLayout(10, 0));
        panelFechaHora.setOpaque(false);

        // Parte 1: Campo de Fecha 
        txtFecha = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtFecha.setEditable(false);
        txtFecha.setFont(fontTextos);
        txtFecha.setBackground(Color.WHITE);
        txtFecha.setHorizontalAlignment(JTextField.CENTER);
        txtFecha.setBorder(new CompoundBorder(new LineBorder(new Color(203, 213, 224), 1), new EmptyBorder(2, 5, 2, 5)));

        btnCalendario = configurarBoton("▼", new Color(243, 244, 246), new Color(229, 231, 235), new Font("Segoe UI", Font.BOLD, 12));
        btnCalendario.setForeground(new Color(45, 55, 72));
        btnCalendario.setPreferredSize(new Dimension(40, 38));
        btnCalendario.addActionListener(e -> desplegarCalendarioGrafico(txtFecha));

        JPanel pnlFecha = new JPanel(new BorderLayout());
        pnlFecha.add(txtFecha, BorderLayout.CENTER);
        pnlFecha.add(btnCalendario, BorderLayout.EAST);

        // Parte 2: Spinner de Hora
        SpinnerDateModel modelHora = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spnHora = new JSpinner(modelHora);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spnHora, "HH:mm");
        spnHora.setEditor(timeEditor);
        ((JSpinner.DefaultEditor) spnHora.getEditor()).getTextField().setEditable(false);
        spnHora.setFont(fontTextos);
        spnHora.setPreferredSize(new Dimension(90, 38));
        spnHora.setBorder(new CompoundBorder(new LineBorder(new Color(203, 213, 224), 1), new EmptyBorder(2, 5, 2, 5)));

        panelFechaHora.add(pnlFecha, BorderLayout.CENTER);
        panelFechaHora.add(spnHora, BorderLayout.EAST);

        form.add(crearLabel("Seleccionar Paciente:", fontTitulos));
        form.add(cbMascotasClientes);
        form.add(crearLabel("Fecha y Hora de la Cita:", fontTitulos));
        form.add(panelFechaHora); 
        form.add(crearLabel("Asignar Médico Disponible:", fontTitulos));
        form.add(cbVeterinarios);

        JPanel panelMotivo = new JPanel(new BorderLayout(0, 8));
        panelMotivo.setOpaque(false);
        panelMotivo.add(crearLabel("Motivo de la Consulta:", fontTitulos), BorderLayout.NORTH);
        
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setFont(fontTextos);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.setMargin(new Insets(8, 10, 8, 10));
        txtMotivo.setForeground(new Color(45, 55, 72));
        
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 224)));
        panelMotivo.add(scrollMotivo, BorderLayout.CENTER);
        
        JPanel panelCuerpo = new JPanel(new BorderLayout(0, 15));
        panelCuerpo.setOpaque(false);
        panelCuerpo.add(form, BorderLayout.NORTH);
        panelCuerpo.add(panelMotivo, BorderLayout.CENTER);
        mainPanel.add(panelCuerpo, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        panelBotones.setOpaque(false);

        btnCancelar = configurarBoton("Limpiar Campos", new Color(224, 122, 95), new Color(193, 93, 69), fontBotones);
        btnGuardarCita = configurarBoton("Guardar Cita", new Color(42, 157, 143), new Color(34, 128, 116), fontBotones);

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardarCita);
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
        
        btnVolver.addActionListener(e -> dispose());
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(new Color(74, 85, 104));
        return label;
    }

    private JButton configurarBoton(String texto, Color normal, Color hover, Font fuente) {
        JButton btn = new JButton(texto);
        btn.setFont(fuente);
        btn.setPreferredSize(new Dimension(170, 40));
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

    private void desplegarCalendarioGrafico(JTextField campoDestino) {
        JDialog dialog = new JDialog(this, "Seleccionar Fecha", true);
        dialog.setSize(320, 280);
        dialog.setLocationRelativeTo(campoDestino);
        dialog.setLayout(new BorderLayout(5, 5));

        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(sdf.parse(campoDestino.getText()));
        } catch (Exception ex) {}

        Calendar hoy = Calendar.getInstance();
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);

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
                
                Calendar fechaBoton = (Calendar) cal.clone();
                fechaBoton.set(Calendar.DAY_OF_MONTH, dia);

                if (fechaBoton.before(hoy)) {
                    btnDia.setEnabled(false);
                    btnDia.setBackground(new Color(243, 244, 246));
                    btnDia.setForeground(new Color(170, 170, 170));
                } else {
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
                }
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
}