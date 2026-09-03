package logica.Persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import logica.Clases.Usuario;

import java.util.List;

public class UsuarioDAO {

    private final EntityManagerFactory entityManagerFactory;

    public UsuarioDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void guardar(Usuario usuario) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(usuario);
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

    public Usuario buscarPorNickname(String nickname) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.nickname = :nickname",
                            Usuario.class
                    )
                    .setParameter("nickname", nickname)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public boolean existeNickname(String nickname) {
        return buscarPorNickname(nickname) != null;
    }

    public boolean existeCorreo(String correo) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE u.correo = :correo",
                            Long.class
                    )
                    .setParameter("correo", correo)
                    .getSingleResult();

            return cantidad > 0;
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarUsuarios() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u ORDER BY u.nickname",
                            Usuario.class
                    )
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void modificar(Usuario usuario) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(usuario);
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
}