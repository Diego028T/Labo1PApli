package logica.Presentacion;

import logica.sistema01.ISistema;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class AltaCategoriaInternalFrame extends JInternalFrame {

    private final ISistema sistema;

    private final JTree arbolCategorias;
    private final JTextField txtNombre;

    public AltaCategoriaInternalFrame(ISistema sistema) {
        super("Alta de categoría", true, true, true, true);

        this.sistema = sistema;

        arbolCategorias = new JTree();
        txtNombre = new JTextField(20);

        setContentPane(crearPanelPrincipal());

        cargarArbolCategorias();

        pack();
        setLocation(100, 80);
    }

    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "Categorías existentes",
                SwingConstants.CENTER
        );

        JPanel panelArbol = new JPanel(new BorderLayout());
        panelArbol.setBorder(
                BorderFactory.createTitledBorder("Árbol de categorías")
        );
        panelArbol.add(new JScrollPane(arbolCategorias), BorderLayout.CENTER);

        JPanel panelNuevaCategoria = new JPanel(new FlowLayout());

        JLabel lblNombre = new JLabel("Nueva categoría:");
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        panelNuevaCategoria.add(lblNombre);
        panelNuevaCategoria.add(txtNombre);
        panelNuevaCategoria.add(btnAceptar);
        panelNuevaCategoria.add(btnCancelar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(panelArbol, BorderLayout.CENTER);
        panel.add(panelNuevaCategoria, BorderLayout.SOUTH);

        btnAceptar.addActionListener(e -> confirmarAlta());
        btnCancelar.addActionListener(e -> dispose());

        return panel;
    }

    private void cargarArbolCategorias() {
        DefaultMutableTreeNode raiz =
                new DefaultMutableTreeNode("Categorías");

        for (String nombreCategoria : sistema.listarNombresCategorias()) {
            raiz.add(new DefaultMutableTreeNode(nombreCategoria));
        }

        DefaultTreeModel modelo = new DefaultTreeModel(raiz);

        arbolCategorias.setModel(modelo);
        arbolCategorias.expandRow(0);
    }

    private void confirmarAlta() {
        try {
            sistema.altaCategoria(txtNombre.getText());

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría dada de alta correctamente.",
                    "Alta de categoría",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarArbolCategorias();

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