package logica.Presentacion;

import logica.DataTypes.DTRegistro;
import logica.DataTypes.DTRegistroMin;
import logica.DataTypes.DTUsuario;
import logica.sistema01.ISistema;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.List;
import java.util.Set;

public class ConsultaRegistroInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private JPanel panelPrincipal;
    private JTextArea txtListadoAsistentes;
    private JTextField txtNickname;
    private JButton btnBuscarRegistros;

    private JPanel panelRegistros;
    private JTextArea txtListadoRegistros;
    private JTextField txtIdRegistro;
    private JButton btnVerDetalle;

    private JPanel panelDetalle;
    private JTextArea txtDetalleRegistro;
    private JButton btnCerrar;

    private String nicknameSeleccionado;

    public ConsultaRegistroInternalFrame(ISistema sistema) {
        super("Consulta de registro", true, true, true, true);

        this.sistema = sistema;

        configurarInterfaz();
        cargarListadoAsistentes();

        setContentPane(panelPrincipal);
        pack();
        setLocation(150, 90);
    }

    private void configurarInterfaz() {
        txtListadoAsistentes.setEditable(false);
        txtListadoRegistros.setEditable(false);
        txtDetalleRegistro.setEditable(false);
        panelRegistros.setVisible(false);
        panelDetalle.setVisible(false);

        btnBuscarRegistros.addActionListener(e -> buscarRegistros());
        btnVerDetalle.addActionListener(e -> verDetalleRegistro());
        btnCerrar.addActionListener(e -> dispose());
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
            ajustarVentana();
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
            Long idRegistro = Long.parseLong(txtIdRegistro.getText().trim());
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
            ajustarVentana();
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El id del registro debe ser un numero.");
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void ajustarVentana() {
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
        pack();
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
