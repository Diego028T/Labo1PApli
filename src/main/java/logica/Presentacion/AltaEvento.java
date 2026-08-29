package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.*;

public class AltaEvento extends JInternalFrame {
    private JPanel PrincipalEvento;
    private JPanel FormularioEvento;
    private JLabel txtNombreEvento;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoSiglas;
    private JSpinner campoFecha;
    private JComboBox comboBox1;
    private JButton btnConfirmar;
    private ISistema sistema;

    public AltaEvento(ISistema sistema){
        super("Alta Evento", true,true,true,true);
        this.sistema = sistema;
        FormularioEvento.setVisible(true);
        setContentPane(FormularioEvento);
        pack();
        setLocation(100, 80);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
