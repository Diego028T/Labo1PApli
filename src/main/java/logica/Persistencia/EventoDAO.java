package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Categoria;
import logica.Clases.Evento;

import java.util.List;

public class EventoDAO {

    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarEvento(Evento evento) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Categoria> categoriasGestionadas = evento.getCategorias().stream()
                    .map(cat -> em.contains(cat) ? cat : em.merge(cat))
                    .toList();
            em.persist(evento);

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

    public static boolean existeEventoPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(e) FROM Evento e WHERE LOWER(e.nombre) = LOWER(:nombre)",
                            Long.class
                    )
                    .setParameter("nombre", nombre.trim())
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public static List<Evento> listarEventos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT e FROM Evento e LEFT JOIN FETCH e.categorias ORDER BY e.nombre", Evento.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}