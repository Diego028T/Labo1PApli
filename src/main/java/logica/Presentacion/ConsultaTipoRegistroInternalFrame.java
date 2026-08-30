package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.TipoRegistro;
import logica.sistema01.ISistema;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.List;

public class ConsultaTipoRegistroInternalFrame extends JInternalFrame {
    private JPanel principalPanel;
    private JPanel panelEventos;
    private JPanel panelEdiciones;
    private JPanel panelTiposRegistro;
    private JPanel panelDetalles;
    private javax.swing.JList<Evento> listaEventos;
    private javax.swing.JList<Edicion> listaEdiciones;
    private javax.swing.JList<TipoRegistro> listaTiposRegistro;
    private javax.swing.JLabel lblEventoSeleccionado;
    private javax.swing.JLabel lblEdicionSeleccionada;
    private javax.swing.JTextArea txtDetalles;
    private javax.swing.JButton btnSeleccionarEvento;
    private javax.swing.JButton btnSeleccionarEdicion;
    private javax.swing.JButton btnSeleccionarTipo;
    private javax.swing.JButton btnVolverEvento;
    private javax.swing.JButton btnVolverEdicion;
    private javax.swing.JButton btnCerrar;

    private final ISistema sistema;
    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;
    private TipoRegistro tipoRegistroSeleccionado;

    public ConsultaTipoRegistroInternalFrame(ISistema sistema) {
        super("Consulta de tipo de registro", true, true, true, true);
        this.sistema = sistema;

        setContentPane(principalPanel);
        configurarVisibilidadInicial();
        cargarEventos();

        btnSeleccionarEvento.addActionListener(e -> seleccionarEvento());
        btnSeleccionarEdicion.addActionListener(e -> seleccionarEdicion());
        btnSeleccionarTipo.addActionListener(e -> seleccionarTipoRegistro());
        btnVolverEvento.addActionListener(e -> mostrarEventos());
        btnVolverEdicion.addActionListener(e -> mostrarEdiciones());
        btnCerrar.addActionListener(e -> dispose());

        pack();
        setLocation(140, 80);
    }

    private void configurarVisibilidadInicial() {
        panelEventos.setVisible(true);
        panelEdiciones.setVisible(false);
        panelTiposRegistro.setVisible(false);
        panelDetalles.setVisible(false);
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
        List<Edicion> ediciones = sistema.listarEdiciones(eventoSeleccionado);
        for (Edicion edicion : ediciones) {
            modelo.addElement(edicion);
        }
        listaEdiciones.setModel(modelo);
        lblEventoSeleccionado.setText(
                "Evento seleccionado: " + eventoSeleccionado.getNombre());

        panelEventos.setVisible(false);
        panelEdiciones.setVisible(true);
        panelEdiciones.revalidate();
        panelEdiciones.repaint();
    }

    private void seleccionarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();
        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        DefaultListModel<TipoRegistro> modelo = new DefaultListModel<>();
        for (TipoRegistro tipo : edicionSeleccionada.getTiposRegistro()) {
            modelo.addElement(tipo);
        }
        listaTiposRegistro.setModel(modelo);
        lblEdicionSeleccionada.setText(
                "Edición seleccionada: " + edicionSeleccionada.getNombre());

        panelEdiciones.setVisible(false);
        panelTiposRegistro.setVisible(true);
        panelTiposRegistro.revalidate();
        panelTiposRegistro.repaint();
    }

    private void seleccionarTipoRegistro() {
        tipoRegistroSeleccionado = listaTiposRegistro.getSelectedValue();
        if (tipoRegistroSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un tipo de registro.");
            return;
        }

        txtDetalles.setText(
                "Nombre: " + tipoRegistroSeleccionado.getNombre() + "\n"
                        + "Descripción: " + tipoRegistroSeleccionado.getDescripcion() + "\n"
                        + "Costo: " + tipoRegistroSeleccionado.getCosto() + "\n"
                        + "Cupo: " + tipoRegistroSeleccionado.getCupo()
        );

        panelTiposRegistro.setVisible(false);
        panelDetalles.setVisible(true);
        panelDetalles.revalidate();
        panelDetalles.repaint();
    }

    private void mostrarEventos() {
        panelEdiciones.setVisible(false);
        panelEventos.setVisible(true);
        panelEventos.revalidate();
        panelEventos.repaint();
    }

    private void mostrarEdiciones() {
        panelTiposRegistro.setVisible(false);
        panelEdiciones.setVisible(true);
        panelEdiciones.revalidate();
        panelEdiciones.repaint();
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
