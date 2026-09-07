package logica.Presentacion;

import logica.Clases.Categoria;
import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;

public class ConsultaEventoInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    private final JList<Evento> listaEventos;
    private final JTextArea txtDatosEvento;
    private final JList<String> listaCategorias;
    private final JList<Edicion> listaEdiciones;

    private Evento eventoSeleccionado;

    public ConsultaEventoInternalFrame(ISistema sistema) {
        super("Consulta de evento", true, true, true, true);

        this.sistema = sistema;

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listaEventos = new JList<>();
        txtDatosEvento = new JTextArea(6, 30);
        listaCategorias = new JList<>();
        listaEdiciones = new JList<>();

        panelPrincipal.add(crearPanelSeleccion(), "SELECCION");
        panelPrincipal.add(crearPanelDetalle(), "DETALLE");

        setContentPane(panelPrincipal);

        cargarEventos();

        pack();
        setLocation(100, 80);
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Seleccione un evento para consultar",
                SwingConstants.CENTER
        );

        JButton btnConsultar = new JButton("Consultar evento");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnConsultar);
        botones.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnConsultar.addActionListener(e -> consultarEvento());
        btnCerrar.addActionListener(e -> dispose());

        return panel;
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        txtDatosEvento.setEditable(false);
        txtDatosEvento.setLineWrap(true);
        txtDatosEvento.setWrapStyleWord(true);

        JPanel panelDatos = new JPanel(new BorderLayout());
        panelDatos.setBorder(
                BorderFactory.createTitledBorder("Datos del evento")
        );
        panelDatos.add(new JScrollPane(txtDatosEvento), BorderLayout.CENTER);

        JPanel panelCategorias = new JPanel(new BorderLayout());
        panelCategorias.setBorder(
                BorderFactory.createTitledBorder("Categorías")
        );
        panelCategorias.add(new JScrollPane(listaCategorias), BorderLayout.CENTER);

        JPanel panelEdiciones = new JPanel(new BorderLayout());
        panelEdiciones.setBorder(
                BorderFactory.createTitledBorder("Ediciones")
        );
        panelEdiciones.add(new JScrollPane(listaEdiciones), BorderLayout.CENTER);

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.add(panelCategorias);
        panelListas.add(panelEdiciones);

        JButton btnVerEdicion = new JButton("Ver detalle de edición");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnVerEdicion);
        botones.add(btnVolver);

        panel.add(panelDatos, BorderLayout.NORTH);
        panel.add(panelListas, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnVerEdicion.addActionListener(e -> mostrarDetalleEdicion());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "SELECCION")
        );

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

    private void consultarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un evento.",
                    "Consulta de evento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cargarDatosEvento();
        cargarCategorias();
        cargarEdiciones();

        cardLayout.show(panelPrincipal, "DETALLE");
    }

    private void cargarDatosEvento() {
        txtDatosEvento.setText(
                "Nombre: " + eventoSeleccionado.getNombre() + "\n" +
                        "Sigla: " + eventoSeleccionado.getSigla() + "\n" +
                        "Descripción: " + eventoSeleccionado.getDescripcion() + "\n" +
                        "Fecha de alta: " + eventoSeleccionado.getFechaAlta()
        );
    }

    private void cargarCategorias() {
        DefaultListModel<String> modelo = new DefaultListModel<>();

        for (Categoria categoria : eventoSeleccionado.getCategorias()) {
            modelo.addElement(categoria.getNombre());
        }

        listaCategorias.setModel(modelo);
    }

    private void cargarEdiciones() {
        DefaultListModel<Edicion> modelo = new DefaultListModel<>();

        for (Edicion edicion : sistema.listarEdiciones(eventoSeleccionado)) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
        listaEdiciones.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
    }

    private void mostrarDetalleEdicion() {
        Edicion edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una edición.",
                    "Consulta de evento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                edicionSeleccionada.obtenerDetalles(),
                "Detalle de edición",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}