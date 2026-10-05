package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTTipoRegistro;
import logica.sistema01.ISistema;

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
import java.util.List;

public class ConsultaTipoRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private final CardLayout cardLayout = new CardLayout();

    private JPanel panelPrincipal;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelTipos;
    private JPanel panelDetalles;

    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;

    private JLabel lblEventoSeleccionado;
    private JLabel lblEdicionSeleccionada;
    private JTextArea txtDetalles;

    private JButton btnSeleccionarEvento;
    private JButton btnCerrarEventos;
    private JButton btnSeleccionarEdicion;
    private JButton btnVolverEventos;
    private JButton btnVerDetalle;
    private JButton btnVolverEdiciones;
    private JButton btnVolverTipos;
    private JButton btnCerrarDetalles;

    private DTEvento eventoSeleccionado;
    private DTEdicion edicionSeleccionada;

    public ConsultaTipoRegistroInternalFrame(ISistema sistema) {
        super("Consulta de tipo de registro", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
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
        panelPrincipal.add(panelTipos, "TIPOS");
        panelPrincipal.add(panelDetalles, "DETALLES");

        txtDetalles.setEditable(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnCerrarEventos.addActionListener(e -> dispose());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnVolverEventos.addActionListener(e -> cardLayout.show(panelPrincipal, "EVENTOS"));
        btnVerDetalle.addActionListener(e -> mostrarDetalle());
        btnVolverEdiciones.addActionListener(e -> cardLayout.show(panelPrincipal, "EDICIONES"));
        btnVolverTipos.addActionListener(e -> cardLayout.show(panelPrincipal, "TIPOS"));
        btnCerrarDetalles.addActionListener(e -> dispose());
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

        for (DTEdicion edicion : sistema.listarEdiciones(eventoSeleccionado.getId())) {
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

        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();
        List<DTTipoRegistro> tiposRegistro =
                sistema.listarTiposRegistro(edicionSeleccionada.getId());

        for (DTTipoRegistro tipo : tiposRegistro) {
            modelo.addElement(tipo);
        }

        listaTiposRegistro.setModel(modelo);
        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre()
        );

        cardLayout.show(panelPrincipal, "TIPOS");
    }

    private void mostrarDetalle() {
        DTTipoRegistro tipoSeleccionado =
                listaTiposRegistro.getSelectedValue();

        if (tipoSeleccionado == null) {
            mostrarAdvertencia(
                    "Debe seleccionar un tipo de registro."
            );
            return;
        }

        txtDetalles.setText(FormatoDetalles.tipoRegistro(tipoSeleccionado));

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