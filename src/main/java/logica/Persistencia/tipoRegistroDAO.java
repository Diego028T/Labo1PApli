package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Edicion;
import logica.Clases.TipoRegistro;
import org.hibernate.exception.ConstraintViolationException;

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

            if (esNombreRepetido(e)) {
                throw new IllegalArgumentException(
                        "Ya existe un tipo de registro con ese nombre para esta edicion.",
                        e);
            }

            throw e;
        }

        finally {
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

    public static TipoRegistro buscarPorIdEnEdicion(Long idTipoRegistro, Long idEdicion) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT tr FROM TipoRegistro tr " +
                                    "JOIN FETCH tr.edicion " +
                                    "WHERE tr.id = :idTipoRegistro " +
                                    "AND tr.edicion.id = :idEdicion",
                            TipoRegistro.class)
                    .setParameter("idTipoRegistro", idTipoRegistro)
                    .setParameter("idEdicion", idEdicion)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    private static boolean esNombreRepetido(Throwable error) {
        for (Throwable causa = error; causa != null; causa = causa.getCause()) {
            if (causa instanceof ConstraintViolationException violacion
                    && "23505".equals(violacion.getSQLState())
                    && "uq_tipo_registro_edicion_nombre"
                    .equals(violacion.getConstraintName())) {
                return true;
            }
        }
        return false;
    }

}


