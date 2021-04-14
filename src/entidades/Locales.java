/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alana
 */
@Entity
@Table(name = "locales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Locales.findAll", query = "SELECT l FROM Locales l")
    , @NamedQuery(name = "Locales.findById", query = "SELECT l FROM Locales l WHERE l.id = :id")
    , @NamedQuery(name = "Locales.findByCalle", query = "SELECT l FROM Locales l WHERE l.calle = :calle")
    , @NamedQuery(name = "Locales.findByNumero", query = "SELECT l FROM Locales l WHERE l.numero = :numero")
    , @NamedQuery(name = "Locales.findByColonia", query = "SELECT l FROM Locales l WHERE l.colonia = :colonia")
    , @NamedQuery(name = "Locales.findByCp", query = "SELECT l FROM Locales l WHERE l.cp = :cp")})
public class Locales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;
    @Basic(optional = false)
    @Column(name = "colonia")
    private String colonia;
    @Basic(optional = false)
    @Column(name = "cp")
    private int cp;

    public Locales() {
    }

    public Locales(Integer id) {
        this.id = id;
    }

    public Locales(Integer id, String calle, int numero, String colonia, int cp) {
        this.id = id;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.cp = cp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Locales)) {
            return false;
        }
        Locales other = (Locales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Locales[ id=" + id + " ]";
    }
    
}
