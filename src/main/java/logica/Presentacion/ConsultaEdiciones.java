package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.*;

public class ConsultaEdiciones extends JInternalFrame {
    private JPanel panel1;
    private JTable listaEventos;
    private JButton btnSeleccionar;
    private JPanel consultaEdiciones;
    private ISistema sistema;

    public ConsultaEdiciones(ISistema sistema){
        super("Consulta Ediciones", true, true,true,true);
        this.sistema = sistema;

        setContentPane(consultaEdiciones);

    }

}


