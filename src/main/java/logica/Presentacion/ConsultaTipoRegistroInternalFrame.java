package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.TipoRegistro;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsultaTipoRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    private final JList<Evento> listaEventos;
    private final JList<Edicion> listaEdiciones;
    private final JList<TipoRegistro> listaTiposRegistro;

    private final JLabel lblEventoSeleccionado;
    private final JLabel lblEdicionSeleccionada;
    private final JTextArea txtDetalles;

    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;

    public ConsultaTipoRegistroInternalFrame(ISistema sistema) {
        super("Consulta de tipo de registro", true, true, true, true);

        this.sistema = sistema;

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listaEventos = new JList<>();
        listaEdiciones = new JList<>();
        listaTiposRegistro = new JList<>();

        lblEventoSeleccionado = new JLabel();
        lblEdicionSeleccionada = new JLabel();

        txtDetalles = new JTextArea(8, 30);
        txtDetalles.setEditable(false);

        panelPrincipal.add(crearPanelEventos(), "EVENTOS");
        panelPrincipal.add(crearPanelEdiciones(), "EDICIONES");
        panelPrincipal.add(crearPanelTipos(), "TIPOS");
        panelPrincipal.add(crearPanelDetalles(), "DETALLES");

        setContentPane(panelPrincipal);

        cargarEventos();

        pack();
        setLocation(140, 80);
    }

    private JPanel crearPanelEventos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Seleccione un evento",
                SwingConstants.CENTER
        );

        JButton btnSeleccionar = new JButton("Seleccionar evento");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnSeleccionar);
        botones.add(btnCerrar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEventos), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnSeleccionar.addActionListener(e -> seleccionarEvento());
        btnCerrar.addActionListener(e -> dispose());

        return panel;
    }

    private JPanel crearPanelEdiciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnSeleccionar = new JButton("Seleccionar edición");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnSeleccionar);
        botones.add(btnVolver);

        panel.add(lblEventoSeleccionado, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaEdiciones), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnSeleccionar.addActionListener(e -> seleccionarEdicion());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "EVENTOS")
        );

        return panel;
    }

    private JPanel crearPanelTipos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnSeleccionar = new JButton("Ver detalle");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnSeleccionar);
        botones.add(btnVolver);

        panel.add(lblEdicionSeleccionada, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaTiposRegistro), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnSeleccionar.addActionListener(e -> mostrarDetalle());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "EDICIONES")
        );

        return panel;
    }

    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnVolver = new JButton("Volver a tipos de registro");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnVolver);
        botones.add(btnCerrar);

        panel.add(new JScrollPane(txtDetalles), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "TIPOS")
        );

        btnCerrar.addActionListener(e -> dispose());

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

        lblEventoSeleccionado.setText(
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

        DefaultListModel<TipoRegistro> modelo = new DefaultListModel<>();

        List<TipoRegistro> tiposRegistro = sistema.listarTiposRegistro(edicionSeleccionada);

        for (TipoRegistro tipo : tiposRegistro) {
            modelo.addElement(tipo);
        }

        listaTiposRegistro.setModel(modelo);
        listaTiposRegistro.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "TIPOS");
    }

    private void mostrarDetalle() {
        TipoRegistro tipoSeleccionado =
                listaTiposRegistro.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia(
                    "Debe seleccionar un tipo de registro."
            );
            return;
        }

        txtDetalles.setText(
                "Nombre: " + tipoSeleccionado.getNombre() + "\n" +
                        "Descripción: " + tipoSeleccionado.getDescripcion() + "\n" +
                        "Costo: " + tipoSeleccionado.getCosto() + "\n" +
                        "Cupo: " + tipoSeleccionado.getCupo()
        );

        cardLayout.show(panelPrincipal, "DETALLES");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de tipo de registro",
                JOptionPane.WARNING_MESSAGE
        );
    }
}