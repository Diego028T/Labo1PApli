package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.TipoRegistro;
import logica.DataTypes.DTFecha;
import logica.DataTypes.DTUsuario;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class RegistroEdicionInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    private final JList<Evento> listaEventos;
    private final JList<Edicion> listaEdiciones;
    private final JList<TipoRegistro> listaTiposRegistro;
    private final JList<DTUsuario> listaAsistentes;

    private final JLabel lblEvento;
    private final JLabel lblEdicion;
    private final JSpinner spinnerFecha;

    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;

    public RegistroEdicionInternalFrame(ISistema sistema) {
        super("Registro a edición de evento", true, true, true, true);

        this.sistema = sistema;

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listaEventos = new JList<>();
        listaEdiciones = new JList<>();
        listaTiposRegistro = new JList<>();
        listaAsistentes = new JList<>();

        lblEvento = new JLabel();
        lblEdicion = new JLabel();

        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(
                new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy")
        );

        panelPrincipal.add(crearPanelEventos(), "EVENTOS");
        panelPrincipal.add(crearPanelEdiciones(), "EDICIONES");
        panelPrincipal.add(crearPanelRegistro(), "REGISTRO");

        setContentPane(panelPrincipal);

        cargarEventos();

        pack();
        setLocation(120, 80);
    }

    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Seleccione un evento",
                SwingConstants.CENTER
        );

        JButton btnSiguiente = new JButton("Siguiente");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnSiguiente);
        botones.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnSiguiente.addActionListener(e -> seleccionarEvento());
        btnCerrar.addActionListener(e -> dispose());

        return panel;
    }

    private JPanel crearPanelEdiciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnSiguiente = new JButton("Siguiente");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnSiguiente);
        botones.add(btnVolver);

        panel.add(lblEvento, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEdiciones), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnSiguiente.addActionListener(e -> seleccionarEdicion());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "EVENTOS")
        );

        return panel;
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel datos = new JPanel(new GridLayout(3, 2, 10, 10));

        datos.add(new JLabel("Tipo de registro:"));
        datos.add(new JScrollPane(listaTiposRegistro));

        datos.add(new JLabel("Asistente:"));
        datos.add(new JScrollPane(listaAsistentes));

        datos.add(new JLabel("Fecha de registro:"));
        datos.add(spinnerFecha);

        JButton btnConfirmar = new JButton("Confirmar registro");
        JButton btnVolver = new JButton("Volver");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel botones = new JPanel();
        botones.add(btnConfirmar);
        botones.add(btnVolver);
        botones.add(btnCancelar);

        panel.add(lblEdicion, BorderLayout.NORTH);
        panel.add(datos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnConfirmar.addActionListener(e -> confirmarRegistro());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "EDICIONES")
        );
        btnCancelar.addActionListener(e -> dispose());

        return panel;
    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();

        for (Evento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
        listaEventos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void seleccionarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        DefaultListModel<Edicion> modelo = new DefaultListModel<>();

        for (Edicion edicion : sistema.listarEdiciones(eventoSeleccionado)) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        listaEdiciones.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        lblEvento.setText(
                "Evento seleccionado: " + eventoSeleccionado.getNombre()
        );

        cardLayout.show(panelPrincipal, "EDICIONES");
    }

    private void seleccionarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        DefaultListModel<TipoRegistro> modeloTipos =
                new DefaultListModel<>();

        for (TipoRegistro tipo :
                sistema.listarTiposRegistro(edicionSeleccionada)) {
            modeloTipos.addElement(tipo);
        }

        listaTiposRegistro.setModel(modeloTipos);
        listaTiposRegistro.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        DefaultListModel<DTUsuario> modeloAsistentes =
                new DefaultListModel<>();

        for (DTUsuario asistente : sistema.listarAsistentes()) {
            modeloAsistentes.addElement(asistente);
        }

        listaAsistentes.setModel(modeloAsistentes);
        listaAsistentes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        listaAsistentes.setCellRenderer(
                new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {
                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof DTUsuario asistente) {
                            setText(
                                    asistente.nickname()
                                            + " - "
                                            + asistente.nombre()
                            );
                        }

                        return this;
                    }
                }
        );

        lblEdicion.setText(
                "Edición seleccionada: "
                        + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "REGISTRO");
    }

    private void confirmarRegistro() {
        TipoRegistro tipoSeleccionado =
                listaTiposRegistro.getSelectedValue();

        DTUsuario asistenteSeleccionado =
                listaAsistentes.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia(
                    "Debe seleccionar un tipo de registro."
            );
            return;
        }

        if (asistenteSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un asistente.");
            return;
        }

        try {
            sistema.registrarAsistenteEdicion(
                    asistenteSeleccionado.nickname(),
                    edicionSeleccionada,
                    tipoSeleccionado,
                    convertirAFecha(spinnerFecha)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Registro realizado correctamente.",
                    "Registro a edición",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaSeleccionada);

        return new DTFecha(
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH) + 1,
                calendario.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Registro a edición",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Registro a edición",
                JOptionPane.ERROR_MESSAGE
        );
    }
}