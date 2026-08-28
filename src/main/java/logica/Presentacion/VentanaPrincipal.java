package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class VentanaPrincipal extends JFrame {

    private final ISistema sistema;
    private final JDesktopPane escritorio;

    public VentanaPrincipal(ISistema sistema) {
        this.sistema = sistema;
        this.escritorio = new JDesktopPane();

        setTitle("eventos.uy");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(crearMenu());

        add(escritorio);
    }

    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuInstituciones = new JMenu("Instituciones");
        JMenuItem itemAltaInstitucion = new JMenuItem("Alta institución");
        itemAltaInstitucion.addActionListener(e -> mostrarAltaInstitucion());
        menuInstituciones.add(itemAltaInstitucion);

        JMenu menuUsuarios = new JMenu("Usuarios");
        JMenuItem itemModificarUsuario = new JMenuItem("Modificar usuario");
        itemModificarUsuario.addActionListener(e -> mostrarModificarUsuario());
        menuUsuarios.add(itemModificarUsuario);

        JMenu Ediciones = new JMenu("Ediciones");
        JMenuItem nuevaEdicion = new JMenuItem("Alta edicion");
        JMenuItem consultarEdicion = new JMenuItem("Consultar edicion");
        nuevaEdicion.addActionListener(e -> mostrarAltaEdicion());
        consultarEdicion.addActionListener(e -> mostrarEdiciones());
        Ediciones.add(nuevaEdicion);
        Ediciones.add(consultarEdicion);

        menuBar.add(Ediciones);
        menuBar.add(menuInstituciones);
        menuBar.add(menuUsuarios);

        return menuBar;
    }

    private void mostrarAltaInstitucion() {
        AltaInstitucionInternalFrame alta =
                new AltaInstitucionInternalFrame(sistema);

        escritorio.add(alta);
        alta.setVisible(true);
    }

    private void mostrarModificarUsuario() {
        ModificarUsuarioInternalFrame modificar =
                new ModificarUsuarioInternalFrame(sistema);

        escritorio.add(modificar);
        modificar.setVisible(true);
    }

    private void mostrarAltaEdicion() {
        AltaEdicionInternalFrame altaEdicion = new AltaEdicionInternalFrame(sistema);
        escritorio.add(altaEdicion);
        altaEdicion.setVisible(true);
    }

    private void mostrarEdiciones(){

    }
}
