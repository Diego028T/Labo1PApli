package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class ConsultaUsuarioInternalFrame extends JInternalFrame {
    private static final int ANCHO_VENTANA = 500;
    private static final int ALTO_VENTANA = 380;

    private final ISistema sistema;
    private JPanel JPanelPrincipal;
    private JPanel PanelUsuarios;
    private JList<String> listUsuarios;
    private JLabel txtUsuarios;
    private JButton btnSelecUsuario;
    private JLabel txtEspecifico;
    private JTextPane paneEspecifico;
    private JList<Object> listEspecifico;
    private JButton btnEspecifico;
    private JButton btnDetalleEdicion;
    private JButton btnVolverUsuarios;
    private JPanel panelEspecifico;
    private JTextPane paneSeleccionado;
    private JPanel panelSeleccionado;
    private JLabel txtSeleccionado;
    private JButton btnVolverEspecifico;
    private String nicknameSeleccionado;

    public ConsultaUsuarioInternalFrame(ISistema sistema) {
        super("Consulta Usuario", true, true, true, true);
        this.sistema = sistema;

        configurarPantallaInicial();
        cargarListadoUsuarios();

        btnSelecUsuario.addActionListener(e -> seleccionarUsuario());
        btnEspecifico.addActionListener(e -> mostrarDetalleRegistro());
        btnDetalleEdicion.addActionListener(e -> mostrarDetalleEdicion());
        btnVolverUsuarios.addActionListener(e -> volverAUsuarios());
        btnVolverEspecifico.addActionListener(e -> mostrarPanel(panelEspecifico));

        setContentPane(PanelUsuarios);
        setTitle("Consulta usuario");
        ajustarTamanoVentana();
    }

    private void configurarPantallaInicial() {
        panelEspecifico.setVisible(false);
        panelSeleccionado.setVisible(false);
        paneEspecifico.setEditable(false);
        paneSeleccionado.setEditable(false);
        listUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEspecifico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarRenderizadorListaEspecifica();
    }

    private void configurarRenderizadorListaEspecifica() {
        listEspecifico.setCellRenderer((lista, valor, indice, seleccionado, foco) -> {
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            JLabel etiqueta = (JLabel) renderer.getListCellRendererComponent(
                    lista,
                    valor,
                    indice,
                    seleccionado,
                    foco
            );

            if (valor instanceof DTRegistroMin registro) {
                etiqueta.setText(registro.toString());
            } else if (valor instanceof Edicion edicion) {
                etiqueta.setText("ID edición: " + edicion.getId()
                        + " - " + edicion.getNombre()
                        + " (" + edicion.getSigla() + ")");
            }

            return etiqueta;
        });
    }

    private void cargarListadoUsuarios() {
        Set<DTUsuario> usuarios = sistema.listarUsuarios();
        DefaultListModel<String> modelo = new DefaultListModel<>();

        if (usuarios.isEmpty()) {
            listUsuarios.setVisible(false);
            txtUsuarios.setText("No hay usuarios aun.");
            return;
        }

        for (DTUsuario usuario : usuarios) {
            modelo.addElement(usuario.nickname());
        }

        listUsuarios.setModel(modelo);
        listUsuarios.setVisible(true);
    }

    private void seleccionarUsuario() {
        nicknameSeleccionado = listUsuarios.getSelectedValue();

        if (nicknameSeleccionado == null || nicknameSeleccionado.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor seleccione un usuario de la lista.",
                    "Usuario no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cargarEspecifico();
        mostrarPanel(panelEspecifico);
    }

    private void cargarEspecifico() {
        try {
            DTDatosUsuario datos = sistema.mostrarDatosUsuario(nicknameSeleccionado);
            DefaultListModel<Object> modeloLista = new DefaultListModel<>();

            if (datos instanceof DTUsuarioAsist asistente) {
                cargarDatosAsistente(asistente, modeloLista);
            } else if (datos instanceof DTUsuarioOrg organizador) {
                cargarDatosOrganizador(organizador, modeloLista);
            }

            listEspecifico.setModel(modeloLista);
            panelSeleccionado.setVisible(false);

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los datos del usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarDatosAsistente(
            DTUsuarioAsist asistente,
            DefaultListModel<Object> modeloLista
    ) {
        txtEspecifico.setText("Información del Asistente (registros a ediciones):");
        paneEspecifico.setText(
                "Nickname: " + asistente.getNickname() + "\n" +
                        "Nombre: " + asistente.getNombre() + " " + asistente.getApellido() + "\n" +
                        "Correo: " + asistente.getCorreo() + "\n" +
                        "Fecha de Nacimiento: " + asistente.getFechaNacimiento()
        );

        List<DTRegistroMin> registros = sistema.listarRegistrosAsistente(asistente.getNickname());

        for (DTRegistroMin registro : registros) {
            modeloLista.addElement(registro);
        }

        if (registros.isEmpty()) {
            paneEspecifico.setText(
                    paneEspecifico.getText()
                            + "\n\nNo se encuentra registrado en ninguna edición."
            );
        }

        btnEspecifico.setVisible(true);
        btnEspecifico.setText("Ver detalle registro");
        btnDetalleEdicion.setVisible(true);
        btnDetalleEdicion.setText("Ver detalle edición");
    }

    private void cargarDatosOrganizador(
            DTUsuarioOrg organizador,
            DefaultListModel<Object> modeloLista
    ) {
        txtEspecifico.setText("Información del Organizador (ediciones asociadas):");
        paneEspecifico.setText(
                "Nickname: " + organizador.getNickname() + "\n" +
                        "Nombre: " + organizador.getNombre() + "\n" +
                        "Correo: " + organizador.getCorreo() + "\n" +
                        "Descripción: " + organizador.getDescripcion() + "\n" +
                        "Sitio Web / Enlace: " + (organizador.getEnlace() != null
                        ? organizador.getEnlace()
                        : "No especificado")
        );

        for (Evento evento : sistema.listarEventos()) {
            for (Edicion edicion : sistema.listarEdiciones(evento)) {
                if (edicion.getOrganizador() != null
                        && edicion.getOrganizador().getNickname()
                        .equalsIgnoreCase(organizador.getNickname())) {
                    modeloLista.addElement(edicion);
                }
            }
        }

        if (modeloLista.isEmpty()) {
            paneEspecifico.setText(
                    paneEspecifico.getText()
                            + "\n\nNo tiene ediciones asociadas actualmente."
            );
        }

        btnEspecifico.setVisible(false);
        btnDetalleEdicion.setVisible(true);
        btnDetalleEdicion.setText("Ver detalle edición");
    }

    private void mostrarDetalleRegistro() {
        Object seleccionado = listEspecifico.getSelectedValue();

        if (!(seleccionado instanceof DTRegistroMin registro)) {
            mostrarAdvertencia("Debe seleccionar un registro.");
            return;
        }

        try {
            DTRegistro detalle = sistema.mostrarDatosRegistro(
                    nicknameSeleccionado,
                    registro.getId()
            );

            txtSeleccionado.setText("Detalle del registro:");
            paneSeleccionado.setText(
                    "Fecha: " + detalle.getFecha() + "\n" +
                            "Edición: " + detalle.getNombreEdicion() + "\n" +
                            "Tipo de registro: " + detalle.getNombreTipoRegistro() + "\n" +
                            "Descripción del tipo: " + detalle.getDescripcionTipoRegistro() + "\n" +
                            "Costo: " + detalle.getCosto() + "\n" +
                            "Patrocinado: " + (detalle.isPatrocinado() ? "Sí" : "No")
            );

            mostrarPanel(panelSeleccionado);
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarDetalleEdicion() {
        Object seleccionado = listEspecifico.getSelectedValue();

        if (seleccionado == null) {
            mostrarAdvertencia("Debe seleccionar una edición o un registro.");
            return;
        }

        Long idEdicion = obtenerIdEdicion(seleccionado);

        if (idEdicion == null) {
            mostrarAdvertencia("No se pudo identificar la edición seleccionada.");
            return;
        }

        try {
            txtSeleccionado.setText("Detalle de la edición:");
            paneSeleccionado.setText(sistema.mostrarDatosEdicion(idEdicion));
            mostrarPanel(panelSeleccionado);
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private Long obtenerIdEdicion(Object seleccionado) {
        if (seleccionado instanceof DTRegistroMin registro) {
            return registro.getIdEdicion();
        }

        if (seleccionado instanceof Edicion edicion) {
            return edicion.getId();
        }

        return null;
    }

    private void mostrarPanel(JPanel panel) {
        panel.setVisible(true);
        setContentPane(panel);
        revalidate();
        repaint();
        ajustarTamanoVentana();
    }

    private void volverAUsuarios() {
        nicknameSeleccionado = null;
        listEspecifico.clearSelection();
        setContentPane(PanelUsuarios);
        revalidate();
        repaint();
        ajustarTamanoVentana();
    }

    private void ajustarTamanoVentana() {
        pack();
        setSize(
                Math.max(getWidth(), ANCHO_VENTANA),
                Math.max(getHeight(), ALTO_VENTANA)
        );
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta usuario",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta usuario",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
