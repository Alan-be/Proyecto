/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import entidades.Vendedores;
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
public class VendedoresJpaController implements Serializable {

    public VendedoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
     public VendedoresJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ProyectoOriginalPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vendedores vendedores) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vendedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vendedores vendedores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vendedores = em.merge(vendedores);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vendedores.getId();
                if (findVendedores(id) == null) {
                    throw new NonexistentEntityException("The vendedores with id " + id + " no longer exists.");
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
            Vendedores vendedores;
            try {
                vendedores = em.getReference(Vendedores.class, id);
                vendedores.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vendedores with id " + id + " no longer exists.", enfe);
            }
            em.remove(vendedores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vendedores> findVendedoresEntities() {
        return findVendedoresEntities(true, -1, -1);
    }

    public List<Vendedores> findVendedoresEntities(int maxResults, int firstResult) {
        return findVendedoresEntities(false, maxResults, firstResult);
    }

    private List<Vendedores> findVendedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vendedores.class));
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

    public Vendedores findVendedores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vendedores.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendedoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vendedores> rt = cq.from(Vendedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
