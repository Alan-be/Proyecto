/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import entidades.Vendedores;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Ventas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
        if (vendedores.getVentasList() == null) {
            vendedores.setVentasList(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ventas> attachedVentasList = new ArrayList<Ventas>();
            for (Ventas ventasListVentasToAttach : vendedores.getVentasList()) {
                ventasListVentasToAttach = em.getReference(ventasListVentasToAttach.getClass(), ventasListVentasToAttach.getId());
                attachedVentasList.add(ventasListVentasToAttach);
            }
            vendedores.setVentasList(attachedVentasList);
            em.persist(vendedores);
            for (Ventas ventasListVentas : vendedores.getVentasList()) {
                Vendedores oldIdVendedorOfVentasListVentas = ventasListVentas.getIdVendedor();
                ventasListVentas.setIdVendedor(vendedores);
                ventasListVentas = em.merge(ventasListVentas);
                if (oldIdVendedorOfVentasListVentas != null) {
                    oldIdVendedorOfVentasListVentas.getVentasList().remove(ventasListVentas);
                    oldIdVendedorOfVentasListVentas = em.merge(oldIdVendedorOfVentasListVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vendedores vendedores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vendedores = em.merge(vendedores);
            Vendedores persistentVendedores = em.find(Vendedores.class, vendedores.getId());
            List<Ventas> ventasListOld = persistentVendedores.getVentasList();
            List<Ventas> ventasListNew = vendedores.getVentasList();
            List<String> illegalOrphanMessages = null;
            for (Ventas ventasListOldVentas : ventasListOld) {
                if (!ventasListNew.contains(ventasListOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasListOldVentas + " since its idVendedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Ventas> attachedVentasListNew = new ArrayList<Ventas>();
            for (Ventas ventasListNewVentasToAttach : ventasListNew) {
                ventasListNewVentasToAttach = em.getReference(ventasListNewVentasToAttach.getClass(), ventasListNewVentasToAttach.getId());
                attachedVentasListNew.add(ventasListNewVentasToAttach);
            }
            ventasListNew = attachedVentasListNew;
            vendedores.setVentasList(ventasListNew);
            
            for (Ventas ventasListNewVentas : ventasListNew) {
                if (!ventasListOld.contains(ventasListNewVentas)) {
                    Vendedores oldIdVendedorOfVentasListNewVentas = ventasListNewVentas.getIdVendedor();
                    ventasListNewVentas.setIdVendedor(vendedores);
                    ventasListNewVentas = em.merge(ventasListNewVentas);
                    if (oldIdVendedorOfVentasListNewVentas != null && !oldIdVendedorOfVentasListNewVentas.equals(vendedores)) {
                        oldIdVendedorOfVentasListNewVentas.getVentasList().remove(ventasListNewVentas);
                        oldIdVendedorOfVentasListNewVentas = em.merge(oldIdVendedorOfVentasListNewVentas);
                    }
                }
            }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Ventas> ventasListOrphanCheck = vendedores.getVentasList();
            for (Ventas ventasListOrphanCheckVentas : ventasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vendedores (" + vendedores + ") cannot be destroyed since the Ventas " + ventasListOrphanCheckVentas + " in its ventasList field has a non-nullable idVendedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
