package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import logica.Clases.Categoria;
import logica.DataTypes.DTCategoria;

import java.util.List;

public class CategoriaDAO {
    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarCategoria(Categoria cat) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(cat);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    public static void guardarCategoria(Categoria cat, Long idPadre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (idPadre != null) {
                Categoria padre = em.find(Categoria.class, idPadre);
                if (padre == null) {
                    throw new IllegalArgumentException("La categoría padre seleccionada ya no existe.");
                }
                cat.setPadre(padre);
            }
            em.persist(cat);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public static List<DTCategoria> listarCategoriasJerarquicas() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT new logica.DataTypes.DTCategoria(c.id, c.nombre, c.padre.id) " +
                            "FROM Categoria c ORDER BY c.nombre",
                    DTCategoria.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Categoria> listarCategorias() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nombre", Categoria.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static Categoria buscarPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)",
                            Categoria.class
                    )
                    .setParameter("nombre", nombre.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static Categoria buscarPorId(Long id) {
        if (id == null) return null;
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public static Categoria buscarCategoriaPorNombre(String nombre) {
        return buscarPorNombre(nombre);
    }

}
