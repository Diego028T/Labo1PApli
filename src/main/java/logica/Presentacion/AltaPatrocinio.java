package logica.Presentacion;


// El caso de uso comienza cuando el administrador desea dar de alta un
//nuevo patrocinio en el sistema. El sistema lista los eventos, el administra-
//dor elige uno y el sistema lista las ediciones. El administrador elige una
//edición y el sistema lista los tipos de registro. El sistema lista además las
//instituciones. El sistema pide los datos del patrocinio: edición del even-
//to, institución, nivel de patrocinio (Platino, Oro, Plata o Bronce), aporte
//económico, tipo y cantidad de registros gratuitos a otorgar, y código de
//patrocinio. Si ya existe un patrocinio de la institución para la edición del
//evento, o si el costo de los registros gratuitos a otorgar supera el 20 % del
//aporte económico, el sistema informa al administrador, quien puede optar
//por editar el patrocinio o cancelar el caso de uso. Finalmente el sistema
//registra toda la información del patrocinio, incluyendo la fecha de alta.

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.sistema01.ISistema;

import javax.swing.*;

import java.util.List;

public class AltaPatrocinio extends JInternalFrame {
    private ISistema sistema;
    private JPanel principalJpanel;
    private JPanel eventosDisponibles;
    private JList<Evento> listaEventos;
    private JLabel TituloEventos;
    private JButton btnConfirmarEvento;
    private JList<Edicion> listaEdiciones;
    private JLabel TituloEdiciones;
    private JPanel edicionesDisponibles;
    private JButton btnConfirmarEdicion;
    private Evento eventoSeleccionado;
    private Edicion edicionSeleccionada;


    public AltaPatrocinio(ISistema sistema) {
        super("Alta de patrocinio", true, true, true, true);
        this.sistema = sistema;
        edicionesDisponibles.setVisible(false);

        setContentPane(eventosDisponibles);

        cargarEventos();

        btnConfirmarEvento.addActionListener(e -> {
            eventoSeleccionado = listaEventos.getSelectedValue();
            listarEdiciones();
        });

        pack();
        setLocation(100, 80);
    }

    private void cargarEventos() {
        DefaultListModel<Evento> modelo = new DefaultListModel<>();
        List<Evento> eventos = sistema.listarEventos();
        for (Evento e : eventos) {
            modelo.addElement(e);
        }
        listaEventos.setModel(modelo);
    }

    private void obtenerEdiciones(){
        DefaultListModel<Edicion> modelo = new DefaultListModel<>();
        List<Edicion> ediciones = sistema.listarEdiciones(eventoSeleccionado);
        for (Edicion e : ediciones) {
            modelo.addElement(e);
        }
        listaEdiciones.setModel(modelo);
    }

    private void listarEdiciones(){
        eventosDisponibles.setVisible(false);
        edicionesDisponibles.setVisible(true);

        setContentPane(edicionesDisponibles);
        obtenerEdiciones();
        btnConfirmarEdicion.addActionListener(e->{
            edicionSeleccionada = listaEdiciones.getSelectedValue();
            cargarRegistro();
        });

    }

    private void cargarRegistro(){

    }

}
