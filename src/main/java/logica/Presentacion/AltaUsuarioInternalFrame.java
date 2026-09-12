package logica.Presentacion;

import logica.DataTypes.EstadoAltaUsuario;
import logica.sistema01.ISistema;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AltaUsuarioInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JPanel panelPrincipal;
    private JTextField txtNickname;
    private JTextField txtNombre;
    private JTextField txtCorreo;

    private JRadioButton rbAsistente;
    private JRadioButton rbOrganizador;

    private JPanel panelDatosEspecificos;
    private JPanel panelAsistente;
    private JPanel panelOrganizador;

    private JTextField txtApellido;
    private JTextField txtFechaNacimiento;
    private JTextField txtInstitucion;
    private JTextField txtDescripcion;
    private JTextField txtEnlace;

    private JButton btnContinuar;
    private JButton btnAceptar;
    private JButton btnCancelar;

    private final Color colorNormal;
    private final Color colorError;

    public AltaUsuarioInternalFrame(ISistema sistema) {
        super("Alta de usuario", true, true, true, true);

        this.sistema = sistema;
        this.colorNormal = Color.WHITE;
        this.colorError = new Color(255, 210, 210);

        configurarInterfaz();

        setContentPane(panelPrincipal);
        pack();
        setLocation(140, 100);
    }

    private void configurarInterfaz() {
        ButtonGroup grupoTipoUsuario = new ButtonGroup();
        grupoTipoUsuario.add(rbAsistente);
        grupoTipoUsuario.add(rbOrganizador);
        rbAsistente.setSelected(true);

        panelDatosEspecificos.setVisible(false);
        panelAsistente.setVisible(false);
        panelOrganizador.setVisible(false);
        btnAceptar.setVisible(false);
        btnCancelar.setVisible(false);

        btnContinuar.addActionListener(e -> continuar());
        btnAceptar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());
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
        panelDatosEspecificos.setVisible(true);
        panelAsistente.setVisible(true);
        panelOrganizador.setVisible(false);
        btnAceptar.setVisible(true);
        btnCancelar.setVisible(true);
        ajustarVentana();
    }

    private void mostrarFormularioOrganizador() {
        panelDatosEspecificos.setVisible(true);
        panelAsistente.setVisible(false);
        panelOrganizador.setVisible(true);
        btnAceptar.setVisible(true);
        btnCancelar.setVisible(true);
        ajustarVentana();
    }

    private void ajustarVentana() {
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
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
