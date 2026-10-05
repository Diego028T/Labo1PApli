package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.DTTipoRegistro;
import logica.DataTypes.DTUsuario;
import logica.sistema01.ISistema;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.List;

public class AltaRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private JPanel panelPrincipal;
    private JList<DTEvento> listaEventos;
    private JList<DTEdicion> listaEdiciones;
    private JList<DTTipoRegistro> listaTiposRegistro;
    private JTextField txtCodigoPatrocinio;
    private JList<DTUsuario> listaAsistentes;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public AltaRegistroInternalFrame(ISistema sistema) {
        super("Registro a edición", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;
        configurarInterfaz();
        cargarEventos();

        setContentPane(panelPrincipal);
        pack();
        setSize(850, 500);
        setLocation(25, 25);
    }

    private void configurarInterfaz() {
        listaEventos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarEdiciones();
            }
        });

        listaEdiciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarTiposRegistro();
            }
        });

        btnRegistrar.addActionListener(e -> registrarAsistente());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void cargarEventos() {
        DefaultListModel<DTEvento> modelo = new DefaultListModel<>();
        List<DTEvento> eventos = sistema.listarEventos();

        for (DTEvento evento : eventos) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void cargarEdiciones() {
        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        DTEvento evento = listaEventos.getSelectedValue();

        if (evento != null) {
            for (DTEdicion edicion : sistema.listarEdiciones(evento.getId())) {
                modelo.addElement(edicion);
            }
        }

        listaEdiciones.setModel(modelo);
        listaTiposRegistro.setModel(new DefaultListModel<>());
    }

    private void cargarTiposRegistro() {
        DefaultListModel<DTTipoRegistro> modelo = new DefaultListModel<>();
        DTEdicion edicion = listaEdiciones.getSelectedValue();

        if (edicion != null) {
            for (DTTipoRegistro tipoRegistro : sistema.listarTiposRegistro(edicion.getId())) {
                modelo.addElement(tipoRegistro);
            }
        }

        listaTiposRegistro.setModel(modelo);
        cargarAsistentes();
    }

    private void cargarAsistentes() {
        DefaultListModel<DTUsuario> modelo = new DefaultListModel<>();

        for (DTUsuario asistente : sistema.listarAsistentes()) {
            modelo.addElement(asistente);
        }

        listaAsistentes.setModel(modelo);
    }

    private void registrarAsistente() {
        DTEvento evento = listaEventos.getSelectedValue();
        DTEdicion edicion = listaEdiciones.getSelectedValue();
        DTTipoRegistro tipoRegistro = listaTiposRegistro.getSelectedValue();
        DTUsuario asistente = listaAsistentes.getSelectedValue();

        if (evento == null || edicion == null
                || tipoRegistro == null || asistente == null) {
            mostrarAdvertencia(
                    "Debe seleccionar evento, edición, tipo de registro y asistente.");
            return;
        }

        try {
            sistema.altaRegistro(
                    asistente.nickname(),
                    edicion.getId(),
                    tipoRegistro.getId(),
                    txtCodigoPatrocinio.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "El asistente fue registrado correctamente.",
                    "Registro a edición",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "No se pudo realizar el registro",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Registro a edición",
                JOptionPane.WARNING_MESSAGE
        );
    }
}