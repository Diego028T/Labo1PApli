package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import logica.Clases.Edicion;
import logica.Clases.Evento;
import logica.Clases.Organizador;

import java.util.List;

public class EdicionDAO {

    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarEdicion(Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            if (edicion.getOrganizador() != null) {
                Organizador organizador = em.contains(edicion.getOrganizador())
                        ? edicion.getOrganizador()
                        : em.merge(edicion.getOrganizador());
                edicion.setOrganizador(organizador);
            }

            if (edicion.getEvento() != null) {
                Evento evento = em.contains(edicion.getEvento())
                        ? edicion.getEvento()
                        : em.merge(edicion.getEvento());
                edicion.setEvento(evento);
            }

            if (edicion.getId() == null) {
                em.persist(edicion);
            } else {
                em.merge(edicion);
            }

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

    public static boolean existeEdicionPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(e) FROM Edicion e WHERE LOWER(e.nombre) = LOWER(:nombre)",
                            Long.class
                    )
                    .setParameter("nombre", nombre.trim())
                    .getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public static Edicion buscarPorId(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Edicion.class, id);
        } finally {
            em.close();
        }
    }

    public static Edicion buscarPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Edicion e WHERE LOWER(e.nombre) = LOWER(:nombre)",
                            Edicion.class
                    )
                    .setParameter("nombre", nombre.trim())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static List<Edicion> listarEdiciones() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM Edicion e LEFT JOIN FETCH e.organizador LEFT JOIN FETCH e.evento ORDER BY e.nombre", Edicion.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public static List<Edicion> listarPorEvento(Long eventoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT e FROM Edicion e LEFT JOIN FETCH e.organizador LEFT JOIN FETCH e.evento WHERE e.evento.id = :eventoId ORDER BY e.nombre",
                            Edicion.class
                    )
                    .setParameter("eventoId", eventoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
