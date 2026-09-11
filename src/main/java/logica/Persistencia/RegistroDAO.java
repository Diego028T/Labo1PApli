package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Asistente;
import logica.Clases.Edicion;
import logica.Clases.Registro;
import logica.Clases.TipoRegistro;

import java.util.List;

public class RegistroDAO {

    private final EntityManagerFactory entityManagerFactory;

    public RegistroDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void guardar(Registro registro) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();

            Asistente asistente = em.merge(registro.getAsistente());
            Edicion edicion = em.merge(registro.getEdicion());
            TipoRegistro tipoRegistro = em.merge(registro.getTipoRegistro());

            registro.setAsistente(asistente);
            registro.setEdicion(edicion);
            registro.setTipoRegistro(tipoRegistro);

            em.persist(registro);
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

    public boolean existeParaAsistenteYEdicion(
            Asistente asistente,
            Edicion edicion
    ) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(r) FROM Registro r " +
                                    "WHERE r.asistente.id = :asistenteId " +
                                    "AND r.edicion.id = :edicionId",
                            Long.class
                    )
                    .setParameter("asistenteId", asistente.getId())
                    .setParameter("edicionId", edicion.getId())
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public long cantidadPorTipoRegistro(TipoRegistro tipoRegistro) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT COUNT(r) FROM Registro r " +
                                    "WHERE r.tipoRegistro.id = :tipoRegistroId",
                            Long.class
                    )
                    .setParameter("tipoRegistroId", tipoRegistro.getId())
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Registro> listarPorAsistente(String nickname) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Registro r " +
                                    "JOIN FETCH r.edicion " +
                                    "JOIN FETCH r.tipoRegistro " +
                                    "WHERE LOWER(r.asistente.nickname) = LOWER(:nickname) " +
                                    "ORDER BY r.fecha",
                            Registro.class
                    )
                    .setParameter("nickname", nickname.trim())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Registro buscarPorIdYAsistente(Long idRegistro, String nickname) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Registro r " +
                                    "JOIN FETCH r.edicion " +
                                    "JOIN FETCH r.tipoRegistro " +
                                    "WHERE r.id = :idRegistro " +
                                    "AND LOWER(r.asistente.nickname) = LOWER(:nickname)",
                            Registro.class
                    )
                    .setParameter("idRegistro", idRegistro)
                    .setParameter("nickname", nickname.trim())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }
}