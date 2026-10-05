package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTPatrocinio;
import logica.DataTypes.DTRegistroEdicion;
import logica.DataTypes.DTTipoRegistro;
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

public class ConsultaEdiciones extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelDetalleEdicion;
    private JPanel panelDetalleSeleccionado;

    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;
    private JList<DTPatrocinio> listaPatrocinios;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JLabel lblDetalleSeleccionado;
    private JTextArea txtDetalleEdicion;
    private JTextArea txtRegistros;
    private JTextArea txtDetalleSeleccionado;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnDetalleTipoRegistro;
    private JButton btnDetallePatrocinio;
    private JButton btnVolverEdiciones;
    private JButton btnVolverDetalleEdicion;
    private JButton btnCerrarDetalleSeleccionado;

    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public ConsultaEdiciones(ISistema sistema) {
        super("Consulta Ediciones", true, true, true, true);
        this.sistema = sistema;

        configurarInterfaz();
        configurarRenderizadores();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setLocation(100, 80);
    }

    private void configurarInterfaz() {
        panelPrincipal.removeAll();
        panelPrincipal.setLayout(cardLayout);
        panelPrincipal.add(panelEventos, "EVENTOS");
        panelPrincipal.add(panelEdiciones, "EDICIONES");
        panelPrincipal.add(panelDetalleEdicion, "DETALLE_EDICION");
        panelPrincipal.add(panelDetalleSeleccionado, "DETALLE_SELECCIONADO");

        txtDetalleEdicion.setEditable(false);
        txtRegistros.setEditable(false);
        txtDetalleSeleccionado.setEditable(false);

        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnDetalleTipoRegistro.addActionListener(e -> mostrarDetalleTipoRegistro());
        btnDetallePatrocinio.addActionListener(e -> mostrarDetallePatrocinio());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverDetalleEdicion.addActionListener(e -> cardLayout.show(panelPrincipal, "DETALLE_EDICION"));
        btnCerrarDetalleSeleccionado.addActionListener(e -> dispose());
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

        listaTiposRegistro.setCellRenderer((lista, tipo, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, tipo, indice, seleccionado, foco);

            if (tipo != null) {
                etiqueta.setText("ID " + tipo.getId() + " - "
                        + tipo.getNombre() + " - $"
                        + tipo.getCosto());
            }

            return etiqueta;
        });

        listaPatrocinios.setCellRenderer((lista, patrocinio, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, patrocinio, indice, seleccionado, foco);

            if (patrocinio != null) {
                etiqueta.setText(
                        patrocinio.getCodigo() + " - "
                                + patrocinio.getNombreInstitucion() + " - "
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
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();

        for (DTEvento evento : sistema.listarEventos()) {
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

        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        List<DTEdicion> ediciones = sistema.listarEdiciones(eventoSeleccionado.getId());

        for (DTEdicion edicion : ediciones) {
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

        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );
        txtDetalleEdicion.setText(FormatoDetalles.edicion(edicionSeleccionada));
        cargarTiposRegistro();
        cargarRegistros();
        cargarPatrocinios();

        cardLayout.show(panelPrincipal, "DETALLE_EDICION");
    }

    private void cargarTiposRegistro() {
        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();

        for (DTTipoRegistro tipoRegistro : sistema.listarTiposRegistro(edicionSeleccionada.getId())) {
            modelo.addElement(tipoRegistro);
        }

        listaTiposRegistro.setModel(modelo);
    }

    private void cargarRegistros() {
        List<DTRegistroEdicion> registros = sistema.listarRegistrosEdicion(edicionSeleccionada.getId());

        if (registros.isEmpty()) {
            txtRegistros.setText("No hay registros para esta edición.");
            return;
        }

        StringBuilder texto = new StringBuilder();
        texto.append("Registros:\n\n");

        for (DTRegistroEdicion registro : registros) {
            texto.append("Id: ")
                    .append(registro.getId())
                    .append(" - Fecha: ")
                    .append(registro.getFecha())
                    .append(" - Asistente: ")
                    .append(registro.getNicknameAsistente())
                    .append(" - Tipo: ")
                    .append(registro.getNombreTipoRegistro())
                    .append(" - Costo: ")
                    .append(registro.getCosto())
                    .append(" - Patrocinado: ")
                    .append(registro.isPatrocinado() ? "Si" : "No")
                    .append("\n");
        }

        txtRegistros.setText(texto.toString());
    }

    private void cargarPatrocinios() {
        DefaultListModel<DTPatrocinio> modelo = new DefaultListModel<>();

        for (DTPatrocinio patrocinio : sistema.listarPatrocinios(edicionSeleccionada.getId())) {
            modelo.addElement(patrocinio);
        }

        listaPatrocinios.setModel(modelo);
    }

    private void mostrarDetalleTipoRegistro() {
        DTTipoRegistro tipoSeleccionado = listaTiposRegistro.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un tipo de registro.");
            return;
        }

        lblDetalleSeleccionado.setText("Detalle del tipo de registro:");
        txtDetalleSeleccionado.setText(FormatoDetalles.tipoRegistro(tipoSeleccionado));

        cardLayout.show(panelPrincipal, "DETALLE_SELECCIONADO");
    }

    private void mostrarDetallePatrocinio() {
        DTPatrocinio patrocinio = listaPatrocinios.getSelectedValue();

        if (patrocinio == null) {
            mostrarAdvertencia("Debe seleccionar un patrocinio.");
            return;
        }

        lblDetalleSeleccionado.setText("Detalle del patrocinio:");
        txtDetalleSeleccionado.setText(FormatoDetalles.patrocinio(patrocinio));

        cardLayout.show(panelPrincipal, "DETALLE_SELECCIONADO");
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de edición",
                JOptionPane.WARNING_MESSAGE
        );
    }
}