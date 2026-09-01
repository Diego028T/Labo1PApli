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
        JMenuItem itemAltaUsuario = new JMenuItem("Alta usuario");
        itemAltaUsuario.addActionListener(e -> mostrarAltaUsuario());
        menuUsuarios.add(itemAltaUsuario);

        JMenuItem itemModificarUsuario = new JMenuItem("Modificar usuario");
        itemModificarUsuario.addActionListener(e -> mostrarModificarUsuario());
        menuUsuarios.add(itemModificarUsuario);

        JMenuItem itemConsultaUsuario = new JMenuItem("Consulta de usuario");
        itemConsultaUsuario.addActionListener(e -> mostrarConsultaUsuario());
        menuUsuarios.add(itemConsultaUsuario);

        JMenu Ediciones = new JMenu("Ediciones");
        JMenuItem nuevaEdicion = new JMenuItem("Alta edicion");
        JMenuItem consultarEdicion = new JMenuItem("Consultar edicion");
        nuevaEdicion.addActionListener(e -> mostrarAltaEdicion());
        consultarEdicion.addActionListener(e -> mostrarEdiciones());
        Ediciones.add(nuevaEdicion);
        Ediciones.add(consultarEdicion);

        JMenu Patrocinios = new JMenu("Patrocinios");
        JMenuItem altaPatrocinio = new JMenuItem("Alta patrocinio");
        altaPatrocinio.addActionListener(e -> mostrarAltaPatrocinio());
        Patrocinios.add(altaPatrocinio);

        JMenu Eventos = new JMenu("Eventos");
        JMenuItem altaEvento = new JMenuItem("Alta evento");
        altaEvento.addActionListener(e -> mostrarAltaEvento());
        Eventos.add(altaEvento);

        JMenuItem altaTipoRegistro =
                new JMenuItem("Alta tipo de registro");

        altaTipoRegistro.addActionListener(e -> mostrarAltaTipoRegistro());

        Ediciones.add(altaTipoRegistro);

        JMenuItem consultaTipoRegistro =
                new JMenuItem("Consulta tipo de registro");
        consultaTipoRegistro.addActionListener(e -> mostrarConsultaTipoRegistro());
        Ediciones.add(consultaTipoRegistro);

        menuBar.add(Ediciones);
        menuBar.add(menuInstituciones);
        menuBar.add(menuUsuarios);
        menuBar.add(Patrocinios);
        menuBar.add(Eventos);

        return menuBar;
    }

    private void mostrarAltaInstitucion() {
        AltaInstitucionInternalFrame alta =
                new AltaInstitucionInternalFrame(sistema);

        escritorio.add(alta);
        alta.setVisible(true);
    }

    private void mostrarAltaUsuario() {
        AltaUsuarioInternalFrame alta =
                new AltaUsuarioInternalFrame(sistema);

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
        ConsultaEdiciones consulta = new ConsultaEdiciones(sistema);
        escritorio.add(consulta);
        consulta.setVisible(true);
    }

    private void mostrarAltaPatrocinio(){
        AltaPatrocinio altaPatrocinio = new AltaPatrocinio(sistema);
        escritorio.add(altaPatrocinio);
        altaPatrocinio.setVisible(true);
    }

    private void mostrarAltaEvento(){
        AltaEvento altaEvento = new AltaEvento(sistema);
        escritorio.add(altaEvento);
        altaEvento.setVisible(true);
    }

    private void mostrarAltaTipoRegistro() {
        AltaTipoRegistroInternalFrame alta =
                new AltaTipoRegistroInternalFrame(sistema);

        escritorio.add(alta);
        alta.setVisible(true);
    }

    private void mostrarConsultaTipoRegistro() {
        ConsultaTipoRegistroInternalFrame consulta =
                new ConsultaTipoRegistroInternalFrame(sistema);

        escritorio.add(consulta);
        consulta.setVisible(true);
    }

    private void mostrarConsultaUsuario() {
        ConsultaUsuarioInternalFrame consulta =
                new ConsultaUsuarioInternalFrame(sistema);

        escritorio.add(consulta);
        consulta.setVisible(true);
    }
}
