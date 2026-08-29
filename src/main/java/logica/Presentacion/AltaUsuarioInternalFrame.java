package logica.Presentacion;

import logica.DataTypes.EstadoAltaUsuario;
import logica.sistema01.ISistema;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AltaUsuarioInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JTextField txtNickname;
    private JTextField txtNombre;
    private JTextField txtCorreo;

    private JRadioButton rbAsistente;
    private JRadioButton rbOrganizador;

    private JPanel panelDatosEspecificos;

    private JTextField txtApellido;
    private JTextField txtFechaNacimiento;
    private JTextField txtInstitucion;
    private JTextField txtDescripcion;
    private JTextField txtEnlace;

    private final Color colorNormal;
    private final Color colorError;

    public AltaUsuarioInternalFrame(ISistema sistema) {
        super("Alta de usuario", true, true, true, true);

        this.sistema = sistema;
        this.colorNormal = Color.WHITE;
        this.colorError = new Color(255, 210, 210);

        construirInterfaz();

        pack();
        setLocation(140, 100);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        panelPrincipal.add(construirPanelDatosComunes(), BorderLayout.NORTH);

        panelDatosEspecificos = new JPanel(new BorderLayout(10, 10));
        panelDatosEspecificos.setVisible(false);
        panelPrincipal.add(panelDatosEspecificos, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel construirPanelDatosComunes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel datos = new JPanel(new GridLayout(4, 2, 10, 10));

        txtNickname = new JTextField();
        txtNombre = new JTextField();
        txtCorreo = new JTextField();

        rbAsistente = new JRadioButton("Asistente", true);
        rbOrganizador = new JRadioButton("Organizador");

        ButtonGroup grupoTipoUsuario = new ButtonGroup();
        grupoTipoUsuario.add(rbAsistente);
        grupoTipoUsuario.add(rbOrganizador);

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTipo.add(rbAsistente);
        panelTipo.add(rbOrganizador);

        datos.add(new JLabel("Nickname:"));
        datos.add(txtNickname);
        datos.add(new JLabel("Nombre:"));
        datos.add(txtNombre);
        datos.add(new JLabel("Correo:"));
        datos.add(txtCorreo);
        datos.add(new JLabel("Tipo de usuario:"));
        datos.add(panelTipo);

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.addActionListener(e -> continuar());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnContinuar);

        panel.add(datos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    private void continuar() {
        limpiarErroresDatosComunes();

        String nickname = txtNickname.getText().trim();
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nickname.isBlank() || nombre.isBlank() || correo.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nickname, nombre y correo son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        EstadoAltaUsuario estado = sistema.chequearUsuario(nickname, correo);

        if (estado != EstadoAltaUsuario.OK) {
            marcarCamposRepetidos(estado);
            JOptionPane.showMessageDialog(
                    this,
                    "El nickname o el correo ya están en uso.",
                    "Usuario repetido",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (rbAsistente.isSelected()) {
            mostrarFormularioAsistente();
        } else {
            mostrarFormularioOrganizador();
        }
    }

    private void limpiarErroresDatosComunes() {
        txtNickname.setBackground(colorNormal);
        txtCorreo.setBackground(colorNormal);
    }

    private void marcarCamposRepetidos(EstadoAltaUsuario estado) {
        if (estado == EstadoAltaUsuario.NICKNAME_REPETIDO
                || estado == EstadoAltaUsuario.NICKNAME_Y_CORREO_REPETIDOS) {
            txtNickname.setBackground(colorError);
        }

        if (estado == EstadoAltaUsuario.CORREO_REPETIDO
                || estado == EstadoAltaUsuario.NICKNAME_Y_CORREO_REPETIDOS) {
            txtCorreo.setBackground(colorError);
        }
    }

    private void mostrarFormularioAsistente() {
        panelDatosEspecificos.removeAll();

        txtApellido = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtInstitucion = new JTextField();

        JPanel datos = new JPanel(new GridLayout(3, 2, 10, 10));
        datos.add(new JLabel("Apellido:"));
        datos.add(txtApellido);
        datos.add(new JLabel("Fecha nacimiento:"));
        datos.add(txtFechaNacimiento);
        datos.add(new JLabel("Institución opcional:"));
        datos.add(txtInstitucion);

        cargarPanelEspecifico(datos);
    }

    private void mostrarFormularioOrganizador() {
        panelDatosEspecificos.removeAll();

        txtDescripcion = new JTextField();
        txtEnlace = new JTextField();

        JPanel datos = new JPanel(new GridLayout(2, 2, 10, 10));
        datos.add(new JLabel("Descripción:"));
        datos.add(txtDescripcion);
        datos.add(new JLabel("Enlace:"));
        datos.add(txtEnlace);

        cargarPanelEspecifico(datos);
    }

    private void cargarPanelEspecifico(JPanel datos) {
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        panelDatosEspecificos.add(datos, BorderLayout.CENTER);
        panelDatosEspecificos.add(botones, BorderLayout.SOUTH);
        panelDatosEspecificos.setVisible(true);

        panelDatosEspecificos.revalidate();
        panelDatosEspecificos.repaint();
        pack();
    }

    private void confirmarAlta() {
        try {
            if (rbAsistente.isSelected()) {
                altaAsistente();
            } else {
                altaOrganizador();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario dado de alta correctamente."
            );
            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en el alta",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void altaAsistente() {
        String apellido = txtApellido.getText().trim();
        String fechaTexto = txtFechaNacimiento.getText().trim();
        String nombreInstitucion = txtInstitucion.getText().trim();

        if (apellido.isBlank() || fechaTexto.isBlank()) {
            throw new IllegalArgumentException(
                    "Apellido y fecha de nacimiento son obligatorios."
            );
        }

        LocalDate fechaNacimiento;

        try {
            fechaNacimiento = LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "La fecha debe tener formato yyyy-mm-dd."
            );
        }

        sistema.altaAsistente(
                txtNickname.getText().trim(),
                txtNombre.getText().trim(),
                txtCorreo.getText().trim(),
                apellido,
                fechaNacimiento,
                nombreInstitucion
        );
    }

    private void altaOrganizador() {
        String descripcion = txtDescripcion.getText().trim();
        String enlace = txtEnlace.getText().trim();

        if (descripcion.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción es obligatoria para el organizador."
            );
        }

        sistema.altaOrganizador(
                txtNickname.getText().trim(),
                txtNombre.getText().trim(),
                txtCorreo.getText().trim(),
                descripcion,
                enlace
        );
    }
}
