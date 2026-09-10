package logica.Presentacion;

import logica.Clases.Evento;
import logica.Clases.Organizador;
import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaEdicionInternalFrame extends JInternalFrame {
    private final ISistema sistema;
    private JLabel txtEventos;
    private JList<Evento> listaEventos;
    private JPanel eventosListados;
    private JButton altaEdicionButton;
    private JPanel panelFormularioEdicion;
    private JList<Organizador> listaOrganizadores;
    private JTextField txtNombreEdicion;
    private JTextField txtSiglaEdicion;
    private JSpinner txtFechaAlta;
    private JSpinner txtFechaInicio;
    private JSpinner txtFechaFin;
    private JTextField txtCiudad;
    private JTextField txtPais;
    private Evento eventoSeleccionado;
    private Organizador organizadorSeleccionado;

    public AltaEdicionInternalFrame(ISistema sistema){
        super("Alta de edición de evento", true, true, true, true);
        this.sistema = sistema;

        crearPantallaInicial();
        setContentPane(eventosListados);

        cargarEventos();
        cargarOrganizadores();
        altaEdicionButton.addActionListener(e -> seleccionarEventoyOrganizador());

        pack();
        setLocation(100, 80);

    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();
        List<Evento> eventos = sistema.listarEventos();
        for (Evento e : eventos) {
            modelo.addElement(e);
        }
        listaEventos.setModel(modelo);
    }

    private void cargarOrganizadores() {
        DefaultListModel<Organizador> modelo = new DefaultListModel<>();
        List<Organizador> organizadores = sistema.listarOrganizadores();
        for (Organizador o : organizadores) {
            modelo.addElement(o);
        }
        listaOrganizadores.setModel(modelo);
    }

    private void seleccionarEventoyOrganizador() {
        eventoSeleccionado = listaEventos.getSelectedValue();
        organizadorSeleccionado = listaOrganizadores.getSelectedValue();
        if (eventoSeleccionado == null || organizadorSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un evento y un organizador.",
                    "Evento no Seleccionado o Organizador no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        mostrarFormularioEdicion();
    }

    private void mostrarFormularioEdicion() {
        txtNombreEdicion = new JTextField();
        txtSiglaEdicion = new JTextField();
        txtFechaInicio = crearSelectorFecha();
        txtFechaFin = crearSelectorFecha();
        txtFechaAlta = crearSelectorFecha();
        txtCiudad = new JTextField();
        txtPais = new JTextField();

        JPanel datos = new JPanel(new GridLayout(7, 2, 10, 10));

        datos.add(new JLabel("Nombre edición:"));
        datos.add(txtNombreEdicion);

        datos.add(new JLabel("Sigla edición:"));
        datos.add(txtSiglaEdicion);

        datos.add(new JLabel("Fecha de inicio:"));
        datos.add(txtFechaInicio);

        datos.add(new JLabel("Fecha fin:"));
        datos.add(txtFechaFin);

        datos.add(new JLabel("Fecha de alta:"));
        datos.add(txtFechaAlta);

        datos.add(new JLabel("Ciudad:"));
        datos.add(txtCiudad);

        datos.add(new JLabel("País:"));
        datos.add(txtPais);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> altaEdicion());
        btnCancelar.addActionListener(e -> ocultarFormularioEdicion());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        panelFormularioEdicion.removeAll();
        panelFormularioEdicion.setLayout(new BorderLayout(10, 10));
        panelFormularioEdicion.add(
                new JLabel("Alta de edicion para el evento: " + eventoSeleccionado.getNombre() + " - Organizador: " + organizadorSeleccionado.getNombre()),
                BorderLayout.NORTH
        );
        panelFormularioEdicion.add(datos, BorderLayout.CENTER);
        panelFormularioEdicion.add(botones, BorderLayout.SOUTH);
        panelFormularioEdicion.setVisible(true);
        altaEdicionButton.setVisible(false);

        panelFormularioEdicion.revalidate();
        panelFormularioEdicion.repaint();
        pack();
    }

    private void ocultarFormularioEdicion() {
        if (panelFormularioEdicion != null) {
            panelFormularioEdicion.removeAll();
            panelFormularioEdicion.setVisible(false);
            altaEdicionButton.setVisible(true);
            panelFormularioEdicion.revalidate();
            panelFormularioEdicion.repaint();
            pack();
        }
    }

    private void altaEdicion() {
        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un evento antes de crear la edición.",
                    "Evento no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (organizadorSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un Organizador antes de crear la edicion del evento",
                    "Organizador no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String nombreEdicion = txtNombreEdicion.getText().trim();
        String siglaEdicion = txtSiglaEdicion.getText().trim();
        String ciudad = txtCiudad.getText().trim();
        String pais = txtPais.getText().trim();

        DTFecha fechaInicio = convertirAFecha(txtFechaInicio);
        DTFecha fechaFin = convertirAFecha(txtFechaFin);
        DTFecha fechaAlta = convertirAFecha(txtFechaAlta);
        if (nombreEdicion.isBlank() || siglaEdicion.isBlank()
                || fechaInicio == null || fechaFin == null || fechaAlta == null
                || ciudad.isBlank() || pais.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (esAnterior(fechaFin, fechaInicio)) {
            JOptionPane.showMessageDialog(this, "La fecha de fin no puede ser anterior a la fecha de inicio.",
                    "Fechas inválidas", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            sistema.altaEdicion(
                    eventoSeleccionado, organizadorSeleccionado, nombreEdicion,
                    siglaEdicion, fechaInicio, fechaFin, fechaAlta, ciudad, pais);
            JOptionPane.showMessageDialog(this, "Edición dada de alta correctamente.");
            dispose();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error en el alta de edición",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private JSpinner crearSelectorFecha() {
        JSpinner selector = new JSpinner(new SpinnerDateModel());
        selector.setEditor(new JSpinner.DateEditor(selector, "dd/MM/yyyy"));
        return selector;
    }

    private DTFecha convertirAFecha(JSpinner selector) {
        Date fechaSeleccionada = (Date) selector.getValue();
        if (fechaSeleccionada == null) {
            return null;
        }
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(fechaSeleccionada);
        return new DTFecha(
                fecha.get(Calendar.YEAR),
                fecha.get(Calendar.MONTH) + 1,
                fecha.get(Calendar.DAY_OF_MONTH));
    }

    private boolean esAnterior(DTFecha primera, DTFecha segunda) {
        if (primera.getAnio() != segunda.getAnio()) {
            return primera.getAnio() < segunda.getAnio();
        }
        if (primera.getMes() != segunda.getMes()) {
            return primera.getMes() < segunda.getMes();
        }
        return primera.getDia() < segunda.getDia();
    }

    private void crearPantallaInicial() {
        eventosListados = new JPanel(new BorderLayout(10, 10));

        txtEventos = new JLabel(
                "Seleccione un evento y un organizador.",
                SwingConstants.CENTER
        );

        listaEventos = new JList<>();
        listaOrganizadores = new JList<>();
        altaEdicionButton = new JButton("Alta edición");

        panelFormularioEdicion = new JPanel();
        panelFormularioEdicion.setVisible(false);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel panelEventos = new JPanel(new BorderLayout());
        panelEventos.add(new JLabel("Eventos disponibles"), BorderLayout.NORTH);
        panelEventos.add(new JScrollPane(listaEventos), BorderLayout.CENTER);

        JPanel panelOrganizadores = new JPanel(new BorderLayout());
        panelOrganizadores.add(
                new JLabel("Organizadores"),
                BorderLayout.NORTH
        );
        panelOrganizadores.add(
                new JScrollPane(listaOrganizadores),
                BorderLayout.CENTER
        );

        panelListas.add(panelEventos);
        panelListas.add(panelOrganizadores);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        panelSur.add(altaEdicionButton, BorderLayout.NORTH);
        panelSur.add(panelFormularioEdicion, BorderLayout.CENTER);

        eventosListados.add(txtEventos, BorderLayout.NORTH);
        eventosListados.add(panelListas, BorderLayout.CENTER);
        eventosListados.add(panelSur, BorderLayout.SOUTH);
    }

}
