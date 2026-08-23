package logica.Presentacion;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.sistema01.ISistema;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Set;

public class ModificarUsuarioInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JTextArea txtListadoUsuarios;
    private JTextField txtNicknameBusqueda;

    private JPanel panelFormulario;

    private JTextField txtNickname;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtApellido;
    private JTextField txtFechaNacimiento;
    private JTextField txtDescripcion;
    private JTextField txtEnlace;

    private DTDatosUsuario datosActuales;

    public ModificarUsuarioInternalFrame(ISistema sistema) {
        super("Modificar usuario", true, true, true, true);

        this.sistema = sistema;

        construirInterfaz();

        pack();
        setLocation(120, 90);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        panelPrincipal.add(construirPanelConsulta(), BorderLayout.NORTH);

        panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setVisible(false);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel construirPanelConsulta() {
        JPanel panelConsulta = new JPanel(new BorderLayout(10, 10));

        txtListadoUsuarios = new JTextArea(8, 45);
        txtListadoUsuarios.setEditable(false);
        cargarListadoUsuarios();

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtNicknameBusqueda = new JTextField(18);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarUsuario());

        panelBusqueda.add(new JLabel("Nickname a modificar:"));
        panelBusqueda.add(txtNicknameBusqueda);
        panelBusqueda.add(btnBuscar);

        panelConsulta.add(new JScrollPane(txtListadoUsuarios), BorderLayout.CENTER);
        panelConsulta.add(panelBusqueda, BorderLayout.SOUTH);

        return panelConsulta;
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
            pack();

        } catch (RuntimeException e) {
            panelFormulario.setVisible(false);
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Usuario no encontrado",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void inicializarCamposFormulario() {
        txtNickname = new JTextField();
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtApellido = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtDescripcion = new JTextField();
        txtEnlace = new JTextField();

        txtNickname.setEditable(false);
        txtCorreo.setEditable(false);
    }

    private void mostrarFormularioAsistente(DTUsuarioAsist asistente) {
        inicializarCamposFormulario();

        txtNickname.setText(asistente.getNickname());
        txtNombre.setText(asistente.getNombre());
        txtCorreo.setText(asistente.getCorreo());
        txtApellido.setText(asistente.getApellido());
        txtFechaNacimiento.setText(asistente.getFechaNacimiento().toString());

        JPanel datos = new JPanel(new GridLayout(5, 2, 10, 10));
        datos.add(new JLabel("Nickname:"));
        datos.add(txtNickname);
        datos.add(new JLabel("Nombre:"));
        datos.add(txtNombre);
        datos.add(new JLabel("Correo:"));
        datos.add(txtCorreo);
        datos.add(new JLabel("Apellido:"));
        datos.add(txtApellido);
        datos.add(new JLabel("Fecha nacimiento:"));
        datos.add(txtFechaNacimiento);

        cargarFormulario(datos);
    }

    private void mostrarFormularioOrganizador(DTUsuarioOrg organizador) {
        inicializarCamposFormulario();

        txtNickname.setText(organizador.getNickname());
        txtNombre.setText(organizador.getNombre());
        txtCorreo.setText(organizador.getCorreo());
        txtDescripcion.setText(organizador.getDescripcion());
        txtEnlace.setText(organizador.getEnlace());

        JPanel datos = new JPanel(new GridLayout(5, 2, 10, 10));
        datos.add(new JLabel("Nickname:"));
        datos.add(txtNickname);
        datos.add(new JLabel("Nombre:"));
        datos.add(txtNombre);
        datos.add(new JLabel("Correo:"));
        datos.add(txtCorreo);
        datos.add(new JLabel("Descripcion:"));
        datos.add(txtDescripcion);
        datos.add(new JLabel("Enlace:"));
        datos.add(txtEnlace);

        cargarFormulario(datos);
    }

    private void cargarFormulario(JPanel datos) {
        panelFormulario.removeAll();

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        btnAceptar.addActionListener(e -> modificarUsuario());
        btnCancelar.addActionListener(e -> panelFormulario.setVisible(false));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnAceptar);
        botones.add(btnCancelar);

        panelFormulario.add(datos, BorderLayout.CENTER);
        panelFormulario.add(botones, BorderLayout.SOUTH);

        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    private void modificarUsuario() {
        try {
            if (datosActuales instanceof DTUsuarioAsist) {
                modificarAsistente();
            } else if (datosActuales instanceof DTUsuarioOrg) {
                modificarOrganizador();
            }

            cargarListadoUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario modificado correctamente."
            );

            panelFormulario.setVisible(false);
            pack();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error al modificar usuario",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void modificarAsistente() {
        DTUsuarioAsist datosModificados = new DTUsuarioAsist(
                txtNickname.getText(),
                txtNombre.getText(),
                txtCorreo.getText(),
                txtApellido.getText(),
                LocalDate.parse(txtFechaNacimiento.getText())
        );

        sistema.modificarDatosUsuario(datosModificados);
    }

    private void modificarOrganizador() {
        DTUsuarioOrg datosModificados = new DTUsuarioOrg(
                txtNickname.getText(),
                txtNombre.getText(),
                txtCorreo.getText(),
                txtDescripcion.getText(),
                txtEnlace.getText()
        );

        sistema.modificarDatosUsuario(datosModificados);
    }

    private void limpiarCampos() {
        txtNickname = null;
        txtNombre = null;
        txtCorreo = null;
        txtApellido = null;
        txtFechaNacimiento = null;
        txtDescripcion = null;
        txtEnlace = null;
    }
}
