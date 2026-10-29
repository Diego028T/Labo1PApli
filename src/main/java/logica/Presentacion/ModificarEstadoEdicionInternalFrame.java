package logica.Presentacion;

import logica.DataTypes.DTEdicion;
import logica.DataTypes.DTEvento;
import logica.DataTypes.EstadoEdicion;
import logica.sistema01.ISistema;

import javax.swing.*;

public class ModificarEstadoEdicionInternalFrame extends JInternalFrame{
    private JPanel JPanelPrincipal;
    private JPanel JPanelEdiciones;
    private JLabel JLabelTitulo;
    private JButton rechazarButton;
    private JButton aprobarButton;
    private JList<DTEdicion> JListEdiciones;
    private JLabel JLabelEvento;
    private final ISistema sistema;
    private JComboBox<DTEvento> comboEventos;
    private Long edicionSeleccionada;

    public ModificarEstadoEdicionInternalFrame(ISistema Sistema){

        super("Modificar Estado Edicion", true, true, true, true);

        this.sistema = Sistema;

        setContentPane(JPanelPrincipal);

        for (DTEvento evento : sistema.listarEventos()) {
            comboEventos.addItem(evento);
        }
        comboEventos.addActionListener(e -> {
            DTEvento evento = (DTEvento) comboEventos.getSelectedItem();
            if (evento != null) {
                cargarEdiciones(evento.getId());
            }
        });
        if (comboEventos.getItemCount() > 0) {
            cargarEdiciones(((DTEvento) comboEventos.getSelectedItem()).getId());
        }

        aprobarButton.addActionListener(e -> cambiarEstado(EstadoEdicion.APROBADA));
        rechazarButton.addActionListener(e -> cambiarEstado(EstadoEdicion.RECHAZADA));

        this.pack();
        this.setVisible(true);

        this.setLocation(100, 100);
    }



    private void cargarEdiciones(Long idEvento) {
        DefaultListModel<DTEdicion> modelo = new DefaultListModel<>();
        for (DTEdicion edicion : sistema.listarEdicionesIngresadas(idEvento)) {
            modelo.addElement(edicion);
        }
        JListEdiciones.setModel(modelo);
    }

    private void cambiarEstado(EstadoEdicion nuevoEstado) {
        DTEdicion edicion = JListEdiciones.getSelectedValue();
        if (edicion == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una edición.",
                    "Modificar estado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            sistema.modificarEstadoEdicion(edicion.getId(), nuevoEstado);
            JOptionPane.showMessageDialog(this, "Estado actualizado.");
            cargarEdiciones(((DTEvento) comboEventos.getSelectedItem()).getId());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Modificar estado", JOptionPane.ERROR_MESSAGE);
        }
    }


}
