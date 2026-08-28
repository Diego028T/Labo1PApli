package logica.Presentacion;

import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.sistema01.ISistema;

import javax.swing.*;
import java.util.List;

public class ConsultaEdiciones extends JInternalFrame {
    private JPanel panel1;
    private JList<Evento> listaEventos;
    private JButton btnSeleccionar;
    private JPanel consultaEdiciones;
    private JPanel FormularioDetalles;
    private JList<Edicion> listaEdiciones;
    private JLabel txtEdiciones;
    private JButton btnListarDetalles;
    private JPanel detallesEdicion;
    private JTextArea txtDetalles;
    private ISistema sistema;
    private Evento evento;
    private Edicion edicionSeleccionada;

    public ConsultaEdiciones(ISistema sistema){
        super("Consulta Ediciones", true, true,true,true);
        this.sistema = sistema;
        FormularioDetalles.setVisible(false);
        setContentPane(consultaEdiciones);

        cargarEventos();

        btnSeleccionar.addActionListener(e -> {
            evento = listaEventos.getSelectedValue();
            ocultarConsultaEdiciones();
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

    private void ocultarConsultaEdiciones() {
        consultaEdiciones.setVisible(false);
        FormularioDetalles.setVisible(true);

        setContentPane(FormularioDetalles);
        obtenerEdiciones();
        btnListarDetalles.addActionListener(e->{
            edicionSeleccionada = listaEdiciones.getSelectedValue();
            listarDetalles();
        });
    }

    private void obtenerEdiciones(){
        DefaultListModel<Edicion> modelo = new DefaultListModel<>();
        List<Edicion> ediciones = sistema.listarEdiciones(evento);
        for (Edicion e : ediciones) {
            modelo.addElement(e);
        }
        listaEdiciones.setModel(modelo);
    }

    private void listarDetalles(){
        FormularioDetalles.setVisible(false);
        detallesEdicion.setVisible(true);
        setContentPane(detallesEdicion);
        txtDetalles.setText(edicionSeleccionada.obtenerDetalles());
        pack();
        setLocation(100, 80);
    }

}



