package logica.Presentacion;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.Arrays;

public class ModificarUsuarioInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JPanel panelPrincipal;
    private JTextArea txtListadoUsuarios;
    private JTextField txtNicknameBusqueda;
    private JButton btnBuscar;

    private JPanel panelFormulario;

    private JTextField txtNickname;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtApellido;
    private JTextField txtFechaNacimiento;
    private JTextField txtDescripcion;
    private JTextField txtEnlace;
    private JLabel lblApellido;
    private JLabel lblFechaNacimiento;
    private JLabel lblDescripcion;
    private JLabel lblEnlace;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JPasswordField txtNuevaContrasena;
    private JPasswordField txtConfirmarNuevaContrasena;

    private DTDatosUsuario datosActuales;

    public ModificarUsuarioInternalFrame(ISistema sistema) {
        super("Modificar usuario", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
        cargarListadoUsuarios();

        setContentPane(panelPrincipal);
        pack();
        setLocation(120, 90);
    }

    private void configurarInterfaz() {
        txtListadoUsuarios.setEditable(false);
        txtNickname.setEditable(false);
        txtCorreo.setEditable(false);
        panelFormulario.setVisible(false);

        btnBuscar.addActionListener(e -> buscarUsuario());
        btnAceptar.addActionListener(e -> modificarUsuario());
        btnCancelar.addActionListener(e -> ocultarFormulario());
    }

    private void cargarListadoUsuarios() {
        Set<DTUsuario> usuarios = sistema.listarUsuarios();

        if (usuarios.isEmpty()) {
            txtListadoUsuarios.setText("No hay usuarios aun.");
            return;
        }

        StringBuilder listado = new StringBuilder();
        listado.append("Usuarios registrados:\n\n");

        for (DTUsuario usuario : usuarios) {
            listado.append("Nickname: ")
                    .append(usuario.nickname())
                    .append(" - Nombre: ")
                    .append(usuario.nombre())
                    .append("\n");
        }

        txtListadoUsuarios.setText(listado.toString());
    }

    private void buscarUsuario() {
        String nickname = txtNicknameBusqueda.getText().trim();

        if (nickname.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el nickname del usuario a modificar.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            datosActuales = sistema.mostrarDatosUsuario(nickname);
            limpiarCampos();

            if (datosActuales instanceof DTUsuarioAsist asistente) {
                mostrarFormularioAsistente(asistente);
            } else if (datosActuales instanceof DTUsuarioOrg organizador) {
                mostrarFormularioOrganizador(organizador);
            }

            panelFormulario.setVisible(true);
            ajustarVentana();

        } catch (RuntimeException e) {
            ocultarFormulario();
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Usuario no encontrado",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void mostrarFormularioAsistente(DTUsuarioAsist asistente) {
        txtNickname.setText(asistente.getNickname());
        txtNombre.setText(asistente.getNombre());
        txtCorreo.setText(asistente.getCorreo());
        txtApellido.setText(asistente.getApellido());
        txtFechaNacimiento.setText(asistente.getFechaNacimiento().toString());

        txtDescripcion.setText("");
        txtEnlace.setText("");
        mostrarCamposAsistente(true);
    }

    private void mostrarFormularioOrganizador(DTUsuarioOrg organizador) {
        txtNickname.setText(organizador.getNickname());
        txtNombre.setText(organizador.getNombre());
        txtCorreo.setText(organizador.getCorreo());
        txtDescripcion.setText(organizador.getDescripcion());
        txtEnlace.setText(organizador.getEnlace());

        txtApellido.setText("");
        txtFechaNacimiento.setText("");
        mostrarCamposAsistente(false);
    }

    private void mostrarCamposAsistente(boolean esAsistente) {
        lblApellido.setVisible(esAsistente);
        txtApellido.setVisible(esAsistente);
        lblFechaNacimiento.setVisible(esAsistente);
        txtFechaNacimiento.setVisible(esAsistente);

        lblDescripcion.setVisible(!esAsistente);
        txtDescripcion.setVisible(!esAsistente);
        lblEnlace.setVisible(!esAsistente);
        txtEnlace.setVisible(!esAsistente);
    }

    private void modificarUsuario() {
        char[] nuevaContrasena =
                txtNuevaContrasena.getPassword();
        char[] confirmacionNuevaContrasena =
                txtConfirmarNuevaContrasena.getPassword();

        try {
            if (datosActuales instanceof DTUsuarioAsist) {
                modificarAsistente(
                        nuevaContrasena,
                        confirmacionNuevaContrasena
                );
            } else if (datosActuales instanceof DTUsuarioOrg) {
                modificarOrganizador(
                        nuevaContrasena,
                        confirmacionNuevaContrasena
                );
            } else {
                throw new IllegalArgumentException(
                        "Primero debe buscar un usuario."
                );
            }

            cargarListadoUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario modificado correctamente."
            );

            ocultarFormulario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error al modificar usuario",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            Arrays.fill(nuevaContrasena, '\0');
            Arrays.fill(confirmacionNuevaContrasena, '\0');

            txtNuevaContrasena.setText("");
            txtConfirmarNuevaContrasena.setText("");
        }
    }

    private void modificarAsistente(
            char[] nuevaContrasena,
            char[] confirmacionNuevaContrasena) {

        DTUsuarioAsist datosModificados = new DTUsuarioAsist(
                txtNickname.getText(),
                txtNombre.getText(),
                txtCorreo.getText(),
                txtApellido.getText(),
                LocalDate.parse(txtFechaNacimiento.getText())
        );

        sistema.modificarDatosUsuario(
                datosModificados,
                nuevaContrasena,
                confirmacionNuevaContrasena
        );
    }

    private void modificarOrganizador(
            char[] nuevaContrasena,
            char[] confirmacionNuevaContrasena) {

        DTUsuarioOrg datosModificados = new DTUsuarioOrg(
                txtNickname.getText(),
                txtNombre.getText(),
                txtCorreo.getText(),
                txtDescripcion.getText(),
                txtEnlace.getText()
        );

        sistema.modificarDatosUsuario(
                datosModificados,
                nuevaContrasena,
                confirmacionNuevaContrasena
        );
    }

    private void limpiarCampos() {
        txtNickname.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtApellido.setText("");
        txtFechaNacimiento.setText("");
        txtDescripcion.setText("");
        txtEnlace.setText("");
        txtNuevaContrasena.setText("");
        txtConfirmarNuevaContrasena.setText("");
    }

    private void ocultarFormulario() {
        panelFormulario.setVisible(false);
        ajustarVentana();
    }

    private void ajustarVentana() {
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
        pack();
    }
}
