package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.TipoRegistro;
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
    private JList<Evento> listaEventos;
    private JList<Edicion> listaEdiciones;
    private JList<TipoRegistro> listaTiposRegistro;
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
        DefaultListModel<Evento> modelo = new DefaultListModel<>();
        List<Evento> eventos = sistema.listarEventos();

        for (Evento evento : eventos) {
            modelo.addElement(evento);
        }

        listaEventos.setModel(modelo);
    }

    private void cargarEdiciones() {
        DefaultListModel<Edicion> modelo = new DefaultListModel<>();
        Evento evento = listaEventos.getSelectedValue();

        if (evento != null) {
            for (Edicion edicion : sistema.listarEdiciones(evento)) {
                modelo.addElement(edicion);
            }
        }

        listaEdiciones.setModel(modelo);
        listaTiposRegistro.setModel(new DefaultListModel<>());
    }

    private void cargarTiposRegistro() {
        DefaultListModel<TipoRegistro> modelo = new DefaultListModel<>();
        Edicion edicion = listaEdiciones.getSelectedValue();

        if (edicion != null) {
            for (TipoRegistro tipoRegistro : sistema.listarTiposRegistro(edicion)) {
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
        Evento evento = listaEventos.getSelectedValue();
        Edicion edicion = listaEdiciones.getSelectedValue();
        TipoRegistro tipoRegistro = listaTiposRegistro.getSelectedValue();
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
                    edicion,
                    tipoRegistro,
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

