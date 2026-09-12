package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Patrocinio;
import logica.sistema01.ISistema;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

public class ConsultaPatrocinioInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelPatrocinios;
    private JPanel panelDetalles;

    private JList<Evento> listaEventos;
    private JList<Edicion> listaEdiciones;
    private JList<Patrocinio> listaPatrocinios;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JTextArea txtDetalles;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnDetallePatrocinio;
    private JButton btnVolverEdiciones;
    private JButton btnVolverPatrocinios;
    private JButton btnCerrarDetalles;

    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;

    public ConsultaPatrocinioInternalFrame(ISistema sistema) {
        super("Consulta de patrocinio", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
        configurarRenderizadores();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setLocation(140, 80);
    }

    private void configurarInterfaz() {
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.add(panelEventos, "EVENTOS");
        panelPrincipal.add(panelEdiciones, "EDICIONES");
        panelPrincipal.add(panelPatrocinios, "PATROCINIOS");
        panelPrincipal.add(panelDetalles, "DETALLES");

        txtDetalles.setEditable(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnDetallePatrocinio.addActionListener(e -> mostrarDetalle());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverPatrocinios.addActionListener(e -> cardLayout.show(panelPrincipal, "PATROCINIOS"));
        btnCerrarDetalles.addActionListener(e -> dispose());
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
