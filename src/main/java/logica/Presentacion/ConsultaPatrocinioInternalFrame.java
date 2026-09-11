package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Patrocinio;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsultaPatrocinioInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final CardLayout cardLayout;
    private final JPanel panelPrincipal;

    private final JList<Evento> listaEventos;
    private final JList<Edicion> listaEdiciones;
    private final JList<Patrocinio> listaPatrocinios;

    private final JLabel lblEventoSeleccionado;
    private final JLabel lblEdicionSeleccionada;
    private final JTextArea txtDetalles;

    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;

    public ConsultaPatrocinioInternalFrame(ISistema sistema) {
        super("Consulta de patrocinio", true, true, true, true);

        this.sistema = sistema;

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        listaEventos = new JList<>();
        listaEdiciones = new JList<>();
        listaPatrocinios = new JList<>();

        lblEventoSeleccionado = new JLabel();
        lblEdicionSeleccionada = new JLabel();

        txtDetalles = new JTextArea(10, 35);
        txtDetalles.setEditable(false);

        panelPrincipal.add(crearPanelEventos(), "EVENTOS");
        panelPrincipal.add(crearPanelEdiciones(), "EDICIONES");
        panelPrincipal.add(crearPanelPatrocinios(), "PATROCINIOS");
        panelPrincipal.add(crearPanelDetalles(), "DETALLES");

        setContentPane(panelPrincipal);

        configurarRenderizadores();
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

    private JPanel crearPanelPatrocinios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnDetalle = new JButton("Ver detalle");
        JButton btnVolver = new JButton("Volver");

        JPanel botones = new JPanel();
        botones.add(btnDetalle);
        botones.add(btnVolver);

        panel.add(lblEdicionSeleccionada, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaPatrocinios), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnDetalle.addActionListener(e -> mostrarDetalle());
        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "EDICIONES")
        );

        return panel;
    }

    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JButton btnVolver = new JButton("Volver a patrocinios");
        JButton btnCerrar = new JButton("Cerrar");

        JPanel botones = new JPanel();
        botones.add(btnVolver);
        botones.add(btnCerrar);

        panel.add(new JScrollPane(txtDetalles), BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                cardLayout.show(panelPrincipal, "PATROCINIOS")
        );
        btnCerrar.addActionListener(e -> dispose());

        return panel;
    }

    private void configurarRenderizadores() {
        listaEventos.setCellRenderer((lista, evento, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, evento, indice, seleccionado, foco);

            if (evento != null) {
                etiqueta.setText("ID " + evento.getId() + " - "
                        + evento.getNombre() + " - "
                        + evento.getDescripcion());
            }

            return etiqueta;
        });

        listaEdiciones.setCellRenderer((lista, edicion, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, edicion, indice, seleccionado, foco);

            if (edicion != null) {
                etiqueta.setText("ID " + edicion.getId() + " - "
                        + edicion.getNombre());
            }

            return etiqueta;
        });

        listaPatrocinios.setCellRenderer((lista, patrocinio, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, patrocinio, indice, seleccionado, foco);

            if (patrocinio != null) {
                etiqueta.setText(
                        patrocinio.getCodigo() + " - "
                                + patrocinio.getInstitucion().getNombre() + " - "
                                + patrocinio.getNivelPatrocinio()
                );
            }

            return etiqueta;
        });
    }

    private JLabel etiquetaLista(
            JList<?> lista,
            Object valor,
            int indice,
            boolean seleccionado,
            boolean foco
    ) {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        Component componente = renderer.getListCellRendererComponent(
                lista,
                valor,
                indice,
                seleccionado,
                foco
        );

        return (JLabel) componente;
    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();

        for (Evento evento : sistema.listarEventos()) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        DefaultListModel<Patrocinio> modelo = new DefaultListModel<>();
        List<Patrocinio> patrocinios =
                sistema.listarPatrocinios(edicionSeleccionada);

        for (Patrocinio patrocinio : patrocinios) {
            modelo.addElement(patrocinio);
        }

        listaPatrocinios.setModel(modelo);
        listaPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "PATROCINIOS");
    }

    private void mostrarDetalle() {
        Patrocinio patrocinio = listaPatrocinios.getSelectedValue();

        if (patrocinio == null) {
            mostrarAdvertencia("Debe seleccionar un patrocinio.");
            return;
        }

        txtDetalles.setText(
                "Código: " + patrocinio.getCodigo() + "\n" +
                        "Fecha de alta: " + patrocinio.getFecha() + "\n" +
                        "Institución: " + patrocinio.getInstitucion().getNombre() + "\n" +
                        "Edición: " + patrocinio.getEdicion().getNombre() + "\n" +
                        "Tipo de registro: " + patrocinio.getTipoRegistro().getNombre() + "\n" +
                        "Nivel: " + patrocinio.getNivelPatrocinio() + "\n" +
                        "Monto aportado: " + patrocinio.getMontoAportado() + "\n" +
                        "Registros gratuitos: " + patrocinio.getCantRegistros()
        );

        cardLayout.show(panelPrincipal, "DETALLES");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de patrocinio",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
