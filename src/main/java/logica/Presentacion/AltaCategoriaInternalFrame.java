package logica.Presentacion;

import logica.DataTypes.DTCategoria;
import logica.sistema01.ISistema;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltaCategoriaInternalFrame extends JInternalFrame {

    private JPanel principalPanel;
    private JLabel lblTitulo;
    private JTree arbolCategorias;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton btnAceptar;
    private JButton btnCancelar;

    private final ISistema sistema;

    public AltaCategoriaInternalFrame(ISistema sistema) {
        super("Alta de categoría", true, true, true, true);

        if (sistema == null) {
            throw new IllegalArgumentException("El sistema no puede ser null.");
        }

        this.sistema = sistema;

        setContentPane(principalPanel);

        cargarCategorias();

        btnAceptar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        pack();
        setLocation(100, 80);
    }

    private void cargarCategorias() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Categorías");
        List<DTCategoria> categorias = sistema.listarCategoriasJerarquicas();
        Map<Long, DefaultMutableTreeNode> nodos = new HashMap<>();

        if (categorias != null) {
            for (DTCategoria categoria : categorias) {
                nodos.put(categoria.id(), new DefaultMutableTreeNode(categoria));
            }
            for (DTCategoria categoria : categorias) {
                DefaultMutableTreeNode nodo = nodos.get(categoria.id());
                DefaultMutableTreeNode padre = categoria.idPadre() == null
                        ? raiz
                        : nodos.get(categoria.idPadre());
                (padre == null ? raiz : padre).add(nodo);
            }
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);
        arbolCategorias.setModel(modelo);

        for (int i = 0; i < arbolCategorias.getRowCount(); i++) {
            arbolCategorias.expandRow(i);
        }
        arbolCategorias.setSelectionPath(new javax.swing.tree.TreePath(raiz.getPath()));
    }

    private void confirmarAlta() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El nombre de la categoría no puede estar vacío.",
                        "Error en alta de categoría",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode)
                    arbolCategorias.getLastSelectedPathComponent();
            Object seleccion = nodoSeleccionado == null ? null : nodoSeleccionado.getUserObject();
            Long idPadre = seleccion instanceof DTCategoria categoria ? categoria.id() : null;

            sistema.altaCategoria(nombre, idPadre);

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría dada de alta correctamente.",
                    "Alta de categoría",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarCategorias();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error en alta de categoría",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar la categoría. " + e.getMessage(),
                    "Error en alta de categoría",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
