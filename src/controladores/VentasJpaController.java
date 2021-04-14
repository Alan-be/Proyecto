/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Vendedores;
import entidades.Ventas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author alana
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public VentasJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ProyectoOriginalPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedores idVendedor = ventas.getIdVendedor();
            if (idVendedor != null) {
                idVendedor = em.getReference(idVendedor.getClass(), idVendedor.getId());
                ventas.setIdVendedor(idVendedor);
            }
            em.persist(ventas);
            if (idVendedor != null) {
                idVendedor.getVentasList().add(ventas);
                idVendedor = em.merge(idVendedor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getId());
            Vendedores idVendedorOld = persistentVentas.getIdVendedor();
            Vendedores idVendedorNew = ventas.getIdVendedor();
            if (idVendedorNew != null) {
                idVendedorNew = em.getReference(idVendedorNew.getClass(), idVendedorNew.getId());
                ventas.setIdVendedor(idVendedorNew);
            }
            ventas = em.merge(ventas);
            if (idVendedorOld != null && !idVendedorOld.equals(idVendedorNew)) {
                idVendedorOld.getVentasList().remove(ventas);
                idVendedorOld = em.merge(idVendedorOld);
            }
            if (idVendedorNew != null && !idVendedorNew.equals(idVendedorOld)) {
                idVendedorNew.getVentasList().add(ventas);
                idVendedorNew = em.merge(idVendedorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventas.getId();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
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
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            Vendedores idVendedor = ventas.getIdVendedor();
            if (idVendedor != null) {
                idVendedor.getVentasList().remove(ventas);
                idVendedor = em.merge(idVendedor);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
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

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
