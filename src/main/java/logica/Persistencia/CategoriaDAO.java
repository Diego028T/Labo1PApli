package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Categoria;

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

    public static List<Categoria> listarCategorias(){
        EntityManager em = entityManagerFactory.createEntityManager();
        return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nombre", Categoria.class).getResultList();
    }

    public static boolean buscarCategoriaPorNombre(String nombre ){
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(c) FROM Categoria c WHERE LOWER(c.nombre) = LOWER(:nombre)", Long.class).setParameter("nombre", nombre.trim()).getSingleResult();
            return cantidad != null && cantidad > 0;
        } finally {
            em.close();
        }
    }
}
