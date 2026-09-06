package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Edicion;
import logica.Clases.TipoRegistro;

import java.util.List;

public class tipoRegistroDAO {

    private static final EntityManagerFactory entityManagerFactory =
            JPAUtil.getEntityManagerFactory();

    public static void guardarConEdicion(TipoRegistro tipoRegistro, Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            Edicion edicionPersistida;

            if (edicion.getId() == null) {
                em.persist(edicion);
                edicionPersistida = edicion;
            } else {
                edicionPersistida = em.merge(edicion);
            }

            tipoRegistro.setEdicion(edicionPersistida);
            em.persist(tipoRegistro);

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

    public static List<TipoRegistro> listarPorEdicion(Edicion edicion) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT tr FROM TipoRegistro tr " +
                                    "WHERE tr.edicion.id = :edicionId " +
                                    "ORDER BY tr.nombre",
                            TipoRegistro.class
                    )
                    .setParameter("edicionId", edicion.getId())
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
