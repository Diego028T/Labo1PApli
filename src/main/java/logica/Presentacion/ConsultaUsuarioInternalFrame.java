package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.DataTypes.*;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;
import java.util.Set;

public class ConsultaUsuarioInternalFrame extends JInternalFrame{
    private ISistema sistema;
    private JPanel JPanelPrincipal;
    private JPanel PanelUsuarios;
    private JList<String> listUsuarios;
    private JLabel txtUsuarios;
    private JButton btnSelecUsuario;
    private JLabel txtEspecifico;
    private JTextPane paneEspecifico;
    private JList<String> listEspecifico;
    private JButton btnEspecifico;
    private JPanel panelEspecifico;
    private JTextPane paneSeleccionado;
    private JPanel panelSeleccionado;
    private JLabel txtSeleccionado;
    private String nicknameSeleccionado;
    private String eventoOedicionSeleccionada;


    public ConsultaUsuarioInternalFrame(ISistema sistema){
        super("Consulta Usuario", true, true, true, true);
        this.sistema = sistema;


        setContentPane(PanelUsuarios);
        setTitle("Consulta usuario");
        cargarListadoUsuarios();

        btnSelecUsuario.addActionListener(e ->{
            nicknameSeleccionado = listUsuarios.getSelectedValue();
            listarInfoUsuario();
        });


        pack();
        setSize(400, 300);
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

    private void cargarEspecifico() {
        if (nicknameSeleccionado == null || nicknameSeleccionado.isBlank()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DTDatosUsuario datos = sistema.mostrarDatosUsuario(nicknameSeleccionado);
            DefaultListModel<String> modeloLista = new DefaultListModel<>();

            Set<DTUsuario> asistentes = sistema.listarAsistentes();
            boolean esAsistente = false;
            for (DTUsuario a : asistentes) {
                if (a.nickname().equalsIgnoreCase(nicknameSeleccionado)) {
                    esAsistente = true;
                    break;
                }
            }

            if (esAsistente) {
                DTUsuarioAsist asistente = (DTUsuarioAsist) datos;
                txtEspecifico.setText("Información del Asistente (Ediciones registradas):");
                paneEspecifico.setText(
                        "Nickname: " + asistente.getNickname() + "\n" +
                                "Nombre: " + asistente.getNombre() + " " + asistente.getApellido() + "\n" +
                                "Correo: " + asistente.getCorreo() + "\n" +
                                "Fecha de Nacimiento: " + asistente.getFechaNacimiento()
                );

                List<DTRegistroMin> registros = sistema.listarRegistrosAsistente(asistente.getNickname());
                for (DTRegistroMin registro : registros) {
                    modeloLista.addElement(registro.getNombreEdicion() + " - Tipo: " + registro.getNombreTipoRegistro() + " (Fecha: " + registro.getFecha() + ")");
                }

                if (modeloLista.isEmpty()) {
                    modeloLista.addElement("No se encuentra registrado en ninguna edición.");
                }

            } else {
                DTUsuarioOrg organizador = (DTUsuarioOrg) datos;
                txtEspecifico.setText("Información del Organizador (Eventos que organiza):");
                paneEspecifico.setText(
                        "Nickname: " + organizador.getNickname() + "\n" +
                                "Nombre: " + organizador.getNombre() + "\n" +
                                "Correo: " + organizador.getCorreo() + "\n" +
                                "Descripción: " + organizador.getDescripcion() + "\n" +
                                "Sitio Web / Enlace: " + (organizador.getEnlace() != null ? organizador.getEnlace() : "No especificado")
                );

                List<Evento> eventos = sistema.listarEventos();
                for (Evento evento : eventos) {
                    List<Edicion> ediciones = sistema.listarEdiciones(evento);
                    boolean organizaEvento = ediciones.stream().anyMatch(ed ->
                            ed.getOrganizador() != null &&
                                    ed.getOrganizador().getNickname().equalsIgnoreCase(organizador.getNickname())
                    );

                    if (organizaEvento) {
                        modeloLista.addElement(evento.getNombre() + " (" + evento.getSigla() + ")");
                    }
                }

                if (modeloLista.isEmpty()) {
                    modeloLista.addElement("No organiza ningún evento actualmente.");
                }
            }

            listEspecifico.setModel(modeloLista);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos del usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarInfoUsuario(){
            if (nicknameSeleccionado == null || nicknameSeleccionado.isBlank()) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione un usuario de la lista.", "Usuario no seleccionado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            PanelUsuarios.setVisible(false);
            panelEspecifico.setVisible(true);
            setContentPane(panelEspecifico);

            cargarEspecifico();

            btnEspecifico.addActionListener(e -> {

                listarInfoEspecifico();
            });

            pack();
    }

    private void cargarSeleccionado(){

    }

    private void listarInfoEspecifico(){
        panelEspecifico.setVisible(false);
        panelSeleccionado.setVisible(true);;
        setContentPane(panelSeleccionado);

        cargarSeleccionado();

        pack();
    }



}
