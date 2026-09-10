package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.NivelPatrocinio;
import logica.Clases.TipoRegistro;
import logica.DataTypes.DTFecha;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AltaPatrocinio extends JInternalFrame {
    private final ISistema sistema;
    private JPanel principalJpanel;
    private JPanel eventosDisponibles;
    private JList<Evento> listaEventos;
    private JLabel TituloEventos;
    private JButton btnConfirmarEvento;
    private JList<Edicion> listaEdiciones;
    private JLabel TituloEdiciones;
    private JPanel edicionesDisponibles;
    private JButton btnConfirmarEdicion;
    private JPanel formularioPatrocinio;
    private JList<TipoRegistro> listaTiposRegistro;
    private JComboBox<String> comboInstituciones;
    private JComboBox<NivelPatrocinio> comboNivelPatrocinio;
    private JTextField txtMontoAportado;
    private JTextField txtCantRegistros;
    private JTextField txtCodigo;
    private JSpinner spinnerFechaAlta;
    private JButton btnGuardarPatrocinio;
    private JButton btnCancelarPatrocinio;
    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;

    public AltaPatrocinio(ISistema sistema) {
        super("Alta de patrocinio", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        configurarPantallaInicial();
        configurarRenderizadores();
        configurarFecha();
        configurarNivelesPatrocinio();
        cargarEventos();

        btnConfirmarEvento.addActionListener(e -> confirmarEvento());
        btnConfirmarEdicion.addActionListener(e -> confirmarEdicion());
        btnGuardarPatrocinio.addActionListener(e -> guardarPatrocinio());
        btnCancelarPatrocinio.addActionListener(e -> dispose());

        setContentPane(eventosDisponibles);
        pack();
        setLocation(100, 80);
    }

    private void configurarPantallaInicial() {
        edicionesDisponibles.setVisible(false);
        formularioPatrocinio.setVisible(false);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEdiciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTiposRegistro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        listaTiposRegistro.setCellRenderer((lista, tipoRegistro, indice, seleccionado, foco) -> {
            JLabel etiqueta = etiquetaLista(lista, tipoRegistro, indice, seleccionado, foco);

            if (tipoRegistro != null) {
                etiqueta.setText("ID " + tipoRegistro.getId() + " - "
                        + tipoRegistro.getNombre() + " - "
                        + tipoRegistro.getDescripcion() + " - $"
                        + tipoRegistro.getCosto());
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

    private void configurarFecha() {
        spinnerFechaAlta.setModel(new SpinnerDateModel());
        spinnerFechaAlta.setEditor(
                new JSpinner.DateEditor(spinnerFechaAlta, "dd/MM/yyyy")
        );
    }

    private void configurarNivelesPatrocinio() {
        comboNivelPatrocinio.setModel(
                new DefaultComboBoxModel<>(NivelPatrocinio.values())
        );
    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();
        List<Evento> eventos = sistema.listarEventos();

        for (Evento evento : eventos) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void confirmarEvento() {
        eventoSeleccionado = listaEventos.getSelectedValue();

        if (eventoSeleccionado == null) {
            mostrarAdvertencia("Debe seleccionar un evento.");
            return;
        }

        cargarEdiciones();
        cambiarPanel(edicionesDisponibles);
    }

    private void cargarEdiciones() {
        DefaultListModel<Edicion> modelo = new DefaultListModel<>();
        List<Edicion> ediciones = sistema.listarEdiciones(eventoSeleccionado);

        for (Edicion edicion : ediciones) {
            modelo.addElement(edicion);
        }

        listaEdiciones.setModel(modelo);
    }

    private void confirmarEdicion() {
        edicionSeleccionada = listaEdiciones.getSelectedValue();

        if (edicionSeleccionada == null) {
            mostrarAdvertencia("Debe seleccionar una edición.");
            return;
        }

        cargarTiposRegistro();
        cargarInstituciones();
        cambiarPanel(formularioPatrocinio);
    }

    private void cargarTiposRegistro() {
        DefaultListModel<TipoRegistro> modelo = new DefaultListModel<>();
        List<TipoRegistro> tiposRegistro = sistema.listarTiposRegistro(edicionSeleccionada);

        for (TipoRegistro tipoRegistro : tiposRegistro) {
            modelo.addElement(tipoRegistro);
        }

        listaTiposRegistro.setModel(modelo);
    }

    private void cargarInstituciones() {
        comboInstituciones.removeAllItems();

        for (String nombreInstitucion : sistema.listarNombresInstituciones()) {
            comboInstituciones.addItem(nombreInstitucion);
        }
    }

    private void guardarPatrocinio() {
        try {
            TipoRegistro tipoRegistro = listaTiposRegistro.getSelectedValue();
            String nombreInstitucion = (String) comboInstituciones.getSelectedItem();
            NivelPatrocinio nivelPatrocinio = (NivelPatrocinio) comboNivelPatrocinio.getSelectedItem();

            if (tipoRegistro == null) {
                mostrarAdvertencia("Debe seleccionar un tipo de registro.");
                return;
            }

            if (nombreInstitucion == null || nombreInstitucion.isBlank()) {
                mostrarAdvertencia("Debe seleccionar una institución.");
                return;
            }

            sistema.altaPatrocinio(
                    edicionSeleccionada,
                    nombreInstitucion,
                    tipoRegistro,
                    nivelPatrocinio,
                    leerMontoAportado(),
                    leerCantidadRegistros(),
                    txtCodigo.getText().trim(),
                    convertirAFecha(spinnerFechaAlta)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Patrocinio dado de alta correctamente.",
                    "Alta de Patrocinio",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException e) {
            mostrarError("El monto aportado y la cantidad de registros deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }

    private float leerMontoAportado() {
        return Float.parseFloat(txtMontoAportado.getText().trim());
    }

    private int leerCantidadRegistros() {
        return Integer.parseInt(txtCantRegistros.getText().trim());
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

    private void cambiarPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
        pack();
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Alta de Patrocinio",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error en el alta de patrocinio",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
