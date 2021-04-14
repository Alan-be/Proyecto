/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Locales;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author alana
 */
public class LocalesJpaController implements Serializable {

    public LocalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public LocalesJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ProyectoOriginalPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Locales locales) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(locales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Locales locales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            locales = em.merge(locales);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = locales.getId();
                if (findLocales(id) == null) {
                    throw new NonexistentEntityException("The locales with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Locales locales;
            try {
                locales = em.getReference(Locales.class, id);
                locales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The locales with id " + id + " no longer exists.", enfe);
            }
            em.remove(locales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Locales> findLocalesEntities() {
        return findLocalesEntities(true, -1, -1);
    }

    public List<Locales> findLocalesEntities(int maxResults, int firstResult) {
        return findLocalesEntities(false, maxResults, firstResult);
    }

    private List<Locales> findLocalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Locales.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Locales findLocales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Locales.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Locales> rt = cq.from(Locales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
