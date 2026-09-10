package logica.Presentacion;

import logica.Clases.Categoria;
import logica.sistema01.ISistema;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;

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
        List<Categoria> categorias = sistema.listarNombresCategorias();

        if (categorias != null) {
            for (Categoria categoria : categorias) {
                raiz.add(new DefaultMutableTreeNode(categoria.getNombre()));
            }
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);
        arbolCategorias.setModel(modelo);

        for (int i = 0; i < arbolCategorias.getRowCount(); i++) {
            arbolCategorias.expandRow(i);
        }
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

            sistema.altaCategoria(nombre);

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
        }
    }
}