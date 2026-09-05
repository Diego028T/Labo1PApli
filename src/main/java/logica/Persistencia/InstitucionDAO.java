package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Institucion;

import java.util.List;

public class InstitucionDAO {

    private final EntityManagerFactory entityManagerFactory;

    public InstitucionDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void guardar(Institucion institucion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(institucion);
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

    public Institucion buscarPorNombre(String nombre) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT i FROM Institucion i WHERE LOWER(i.nombre) = :nombre",
                            Institucion.class
                    )
                    .setParameter("nombre", nombre.trim().toLowerCase())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean existeNombre(String nombre) {
        return buscarPorNombre(nombre) != null;
    }

    public List<Institucion> listarInstituciones() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT i FROM Institucion i ORDER BY i.nombre",
                            Institucion.class
                    )
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> listarNombres() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT i.nombre FROM Institucion i ORDER BY i.nombre",
                            String.class
                    )
                    .getResultList();
        } finally {
            em.close();
        }
    }
}