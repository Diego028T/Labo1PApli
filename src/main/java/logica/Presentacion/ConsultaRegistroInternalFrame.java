package logica.Presentacion;

import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
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
import java.util.List;
import java.util.Set;

public class ConsultaRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JTextArea txtListadoAsistentes;
    private JTextField txtNickname;

    private JPanel panelRegistros;
    private JTextArea txtListadoRegistros;
    private JTextField txtIdRegistro;

    private JPanel panelDetalle;
    private JTextArea txtDetalleRegistro;

    private String nicknameSeleccionado;

    public ConsultaRegistroInternalFrame(ISistema sistema) {
        super("Consulta de registro", true, true, true, true);

        this.sistema = sistema;

        construirInterfaz();
        cargarListadoAsistentes();

        pack();
        setLocation(150, 90);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        panelPrincipal.add(construirPanelAsistentes(), BorderLayout.NORTH);

        panelRegistros = construirPanelRegistros();
        panelRegistros.setVisible(false);
        panelPrincipal.add(panelRegistros, BorderLayout.CENTER);

        panelDetalle = construirPanelDetalle();
        panelDetalle.setVisible(false);
        panelPrincipal.add(panelDetalle, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel construirPanelAsistentes() {
        JPanel panelAsistentes = new JPanel(new BorderLayout(10, 10));

        txtListadoAsistentes = new JTextArea(8, 55);
        txtListadoAsistentes.setEditable(false);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtNickname = new JTextField(18);

        JButton btnBuscarRegistros = new JButton("Buscar registros");
        btnBuscarRegistros.addActionListener(e -> buscarRegistros());

        panelBusqueda.add(new JLabel("Nickname del asistente:"));
        panelBusqueda.add(txtNickname);
        panelBusqueda.add(btnBuscarRegistros);

        panelAsistentes.add(new JScrollPane(txtListadoAsistentes), BorderLayout.CENTER);
        panelAsistentes.add(panelBusqueda, BorderLayout.SOUTH);

        return panelAsistentes;
    }

    private JPanel construirPanelRegistros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        txtListadoRegistros = new JTextArea(6, 55);
        txtListadoRegistros.setEditable(false);

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtIdRegistro = new JTextField(8);

        JButton btnVerDetalle = new JButton("Ver detalle");
        btnVerDetalle.addActionListener(e -> verDetalleRegistro());

        panelSeleccion.add(new JLabel("Id del registro:"));
        panelSeleccion.add(txtIdRegistro);
        panelSeleccion.add(btnVerDetalle);

        panel.add(new JScrollPane(txtListadoRegistros), BorderLayout.CENTER);
        panel.add(panelSeleccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel construirPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        txtDetalleRegistro = new JTextArea(7, 55);
        txtDetalleRegistro.setEditable(false);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCerrar);

        panel.add(new JScrollPane(txtDetalleRegistro), BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarListadoAsistentes() {
        Set<DTUsuario> asistentes = sistema.listarAsistentes();

        if (asistentes.isEmpty()) {
            txtListadoAsistentes.setText("No hay asistentes registrados.");
            return;
        }

        StringBuilder listado = new StringBuilder();
        listado.append("Asistentes registrados:\n\n");

        for (DTUsuario asistente : asistentes) {
            listado.append("Nickname: ")
                    .append(asistente.nickname())
                    .append(" - Nombre: ")
                    .append(asistente.nombre())
                    .append("\n");
        }

        txtListadoAsistentes.setText(listado.toString());
    }

    private void buscarRegistros() {
        String nickname = txtNickname.getText().trim();

        if (nickname.isBlank()) {
            mostrarAdvertencia("Ingrese el nickname del asistente.");
            return;
        }

        try {
            List<DTRegistroMin> registros = sistema.listarRegistrosAsistente(nickname);
            nicknameSeleccionado = nickname;
            cargarListadoRegistros(registros);

            panelRegistros.setVisible(true);
            panelDetalle.setVisible(false);
            pack();
        } catch (RuntimeException e) {
            panelRegistros.setVisible(false);
            panelDetalle.setVisible(false);
            mostrarError(e.getMessage());
        }
    }

    private void cargarListadoRegistros(List<DTRegistroMin> registros) {
        if (registros.isEmpty()) {
            txtListadoRegistros.setText("El asistente seleccionado no tiene registros.");
            return;
        }

        StringBuilder listado = new StringBuilder();
        listado.append("Registros del asistente:\n\n");

        for (DTRegistroMin registro : registros) {
            listado.append("Id: ")
                    .append(registro.getId())
                    .append(" - Fecha: ")
                    .append(registro.getFecha())
                    .append(" - Edicion: ")
                    .append(registro.getNombreEdicion())
                    .append(" - Tipo: ")
                    .append(registro.getNombreTipoRegistro())
                    .append("\n");
        }

        txtListadoRegistros.setText(listado.toString());
    }

    private void verDetalleRegistro() {
        if (nicknameSeleccionado == null || nicknameSeleccionado.isBlank()) {
            mostrarAdvertencia("Primero debe seleccionar un asistente.");
            return;
        }

        try {
            int idRegistro = Integer.parseInt(txtIdRegistro.getText().trim());
            DTRegistro registro = sistema.mostrarDatosRegistro(nicknameSeleccionado, idRegistro);

            txtDetalleRegistro.setText(
                    "Fecha: " + registro.getFecha() + "\n"
                            + "Edicion: " + registro.getNombreEdicion() + "\n"
                            + "Tipo de registro: " + registro.getNombreTipoRegistro() + "\n"
                            + "Descripcion del tipo: " + registro.getDescripcionTipoRegistro() + "\n"
                            + "Costo: " + registro.getCosto() + "\n"
                            + "Patrocinado: " + (registro.isPatrocinado() ? "Si" : "No")
            );

            panelDetalle.setVisible(true);
            pack();
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El id del registro debe ser un numero.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de registro",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Consulta de registro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
