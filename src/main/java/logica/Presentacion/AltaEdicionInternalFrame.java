package logica.Presentacion;

import logica.Clases.Evento;
import logica.Clases.Organizador;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.awt.*;

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
    private JTextField txtFechaAlta;
    private JTextField txtFechaFin;
    private JTextField txtCiudad;
    private JTextField txtPais;
    private Evento eventoSeleccionado;
    private Organizador organizadorSeleccionado;

    private final Evento[] eventos = {
            new Evento("Conferencia Java", "Conferencia sobre java", "JV2026"),
            new Evento("Conferencia Python", "Conferencia sobre python", "PY2026")
    };

    private final Organizador[] organizadores = {
            new Organizador("Juancito", "juanchi", "", "Organizador de conferencias", ""),
            new Organizador("Pepe","Pepote", "", "Organizador de eventos", "")
    };

    public AltaEdicionInternalFrame(ISistema sistema){
        super("Alta de eventos", true, true, true, true);
        this.sistema = sistema;

        setContentPane(eventosListados);

        cargarEventos();
        cargarOrganizadores();
        altaEdicionButton.addActionListener(e -> seleccionarEventoyOrganizador());

        pack();
        setLocation(100, 80);

    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();
        for (Evento e : eventos) {
            modelo.addElement(e);
        }
        listaEventos.setModel(modelo);
    }

    private void cargarOrganizadores() {
        DefaultListModel<Organizador> modelo = new DefaultListModel<>();
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
        txtFechaAlta = new JTextField();
        txtFechaFin = new JTextField();
        txtCiudad = new JTextField();
        txtPais = new JTextField();

        JPanel datos = new JPanel(new GridLayout(6, 2, 10, 10));

        datos.add(new JLabel("Nombre edición:"));
        datos.add(txtNombreEdicion);

        datos.add(new JLabel("Sigla edición:"));
        datos.add(txtSiglaEdicion);

        datos.add(new JLabel("Fecha alta:"));
        datos.add(txtFechaAlta);

        datos.add(new JLabel("Fecha fin:"));
        datos.add(txtFechaFin);

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

        panelFormularioEdicion.setLayout(new BorderLayout(10, 10));
        panelFormularioEdicion.add(
                new JLabel("Alta de edición para el evento: " + eventoSeleccionado.getNombre() + " - Organizador: " + organizadorSeleccionado.getNombre()),
                BorderLayout.NORTH
        );
        panelFormularioEdicion.add(datos, BorderLayout.CENTER);
        panelFormularioEdicion.add(botones, BorderLayout.SOUTH);

        panelFormularioEdicion.removeAll();
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
        String fechaAlta = txtFechaAlta.getText().trim();
        String fechaFin = txtFechaFin.getText().trim();
        String ciudad = txtCiudad.getText().trim();
        String pais = txtPais.getText().trim();

        if (nombreEdicion.isBlank() || siglaEdicion.isBlank() || fechaAlta.isBlank() || fechaFin.isBlank() || ciudad.isBlank() || pais.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(
                this, "Se crearía la edición '" + nombreEdicion + "' para el evento '" + eventoSeleccionado.getNombre() + "'. Con el organizador: " + organizadorSeleccionado);
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

}
