package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Edicion;
import logica.Clases.Institucion;
import logica.Clases.Patrocinio;
import logica.Clases.TipoRegistro;

import java.util.List;

public class PatrocinioDAO {

    private final EntityManagerFactory entityManagerFactory;

    public PatrocinioDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void guardar(Patrocinio patrocinio) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            Edicion edicion = em.merge(patrocinio.getEdicion());
            Institucion institucion = em.merge(patrocinio.getInstitucion());
            TipoRegistro tipoRegistro = em.merge(patrocinio.getTipoRegistro());

            patrocinio.setEdicion(edicion);
            patrocinio.setInstitucion(institucion);
            patrocinio.setTipoRegistro(tipoRegistro);

            em.persist(patrocinio);

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

    public boolean existePorInstitucionYEdicion(
            Institucion institucion,
            Edicion edicion
    ) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(p) FROM Patrocinio p " +
                                    "WHERE p.institucion.id = :institucionId " +
                                    "AND p.edicion.id = :edicionId",
                            Long.class
                    )
                    .setParameter("institucionId", institucion.getId())
                    .setParameter("edicionId", edicion.getId())
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public boolean existeCodigo(String codigo) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(p) FROM Patrocinio p " +
                                    "WHERE LOWER(p.codigo) = LOWER(:codigo)",
                            Long.class
                    )
                    .setParameter("codigo", codigo.trim())
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public List<Patrocinio> listarPorEdicion(Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Patrocinio p " +
                                    "JOIN FETCH p.institucion " +
                                    "JOIN FETCH p.edicion " +
                                    "JOIN FETCH p.tipoRegistro " +
                                    "WHERE p.edicion.id = :edicionId " +
                                    "ORDER BY p.codigo",
                            Patrocinio.class
                    )
                    .setParameter("edicionId", edicion.getId())
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
