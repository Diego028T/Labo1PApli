package logica.Presentacion;

import logica.DataTypes.DTDatosUsuario;
import logica.DataTypes.DTUsuario;
import logica.DataTypes.DTUsuarioAsist;
import logica.DataTypes.DTUsuarioOrg;
import logica.sistema01.ISistema;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Set;

public class ConsultaUsuarioInternalFrame extends JInternalFrame {

    private final ISistema sistema;
    private final JList<DTUsuario> listaUsuarios;
    private final JTextArea txtDatos;

    public ConsultaUsuarioInternalFrame(ISistema sistema) {
        super("Consulta de usuario", true, true, true, true);

        this.sistema = sistema;
        this.listaUsuarios = new JList<>();
        this.txtDatos = new JTextArea(12, 40);

        construirInterfaz();
        cargarUsuarios();

        pack();
        setLocation(120, 90);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        listaUsuarios.setCellRenderer((lista, usuario, indice, seleccionado, tieneFoco) -> {
            JLabel etiqueta = new JLabel(
                    usuario.nickname() + " - " + usuario.nombre()
            );

            if (seleccionado) {
                etiqueta.setOpaque(true);
                etiqueta.setBackground(lista.getSelectionBackground());
                etiqueta.setForeground(lista.getSelectionForeground());
            }

            return etiqueta;
        });

        JPanel panelUsuarios = new JPanel(new BorderLayout(5, 5));
        panelUsuarios.add(new JLabel("Usuarios registrados:"), BorderLayout.NORTH);
        panelUsuarios.add(new JScrollPane(listaUsuarios), BorderLayout.CENTER);

        JButton btnSeleccionar = new JButton("Ver datos");
        btnSeleccionar.addActionListener(e -> mostrarDatosUsuario());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnSeleccionar);
        panelUsuarios.add(panelBoton, BorderLayout.SOUTH);

        txtDatos.setEditable(false);
        txtDatos.setLineWrap(true);
        txtDatos.setWrapStyleWord(true);

        JPanel panelDatos = new JPanel(new BorderLayout(5, 5));
        panelDatos.add(new JLabel("Datos del usuario:"), BorderLayout.NORTH);
        panelDatos.add(new JScrollPane(txtDatos), BorderLayout.CENTER);

        panelPrincipal.add(panelUsuarios, BorderLayout.WEST);
        panelPrincipal.add(panelDatos, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private void cargarUsuarios() {
        DefaultListModel<DTUsuario> modelo = new DefaultListModel<>();
        Set<DTUsuario> usuarios = sistema.listarUsuarios();

        for (DTUsuario usuario : usuarios) {
            modelo.addElement(usuario);
        }

        listaUsuarios.setModel(modelo);
    }

    private void mostrarDatosUsuario() {
        DTUsuario usuarioSeleccionado = listaUsuarios.getSelectedValue();

        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un usuario.",
                    "Consulta de usuario",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        DTDatosUsuario datos = sistema.mostrarDatosUsuario(
                usuarioSeleccionado.nickname()
        );

        if (datos instanceof DTUsuarioAsist asistente) {
            txtDatos.setText(
                    "Tipo de usuario: Asistente\n\n"
                            + "Nickname: " + asistente.getNickname() + "\n"
                            + "Nombre: " + asistente.getNombre() + "\n"
                            + "Apellido: " + asistente.getApellido() + "\n"
                            + "Correo: " + asistente.getCorreo() + "\n"
                            + "Fecha de nacimiento: "
                            + asistente.getFechaNacimiento()
            );

        } else if (datos instanceof DTUsuarioOrg organizador) {
            txtDatos.setText(
                    "Tipo de usuario: Organizador\n\n"
                            + "Nickname: " + organizador.getNickname() + "\n"
                            + "Nombre: " + organizador.getNombre() + "\n"
                            + "Correo: " + organizador.getCorreo() + "\n"
                            + "Descripción: " + organizador.getDescripcion() + "\n"
                            + "Enlace: " + organizador.getEnlace()
            );
        }
    }
}