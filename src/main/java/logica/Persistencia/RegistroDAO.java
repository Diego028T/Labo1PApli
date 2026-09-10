package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Registro;

import java.util.List;

public class RegistroDAO {

    private final EntityManagerFactory entityManagerFactory;

    public RegistroDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
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