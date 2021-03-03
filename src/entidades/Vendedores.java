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
@Table(name = "vendedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vendedores.findAll", query = "SELECT v FROM Vendedores v")
    , @NamedQuery(name = "Vendedores.findById", query = "SELECT v FROM Vendedores v WHERE v.id = :id")
    , @NamedQuery(name = "Vendedores.findByNombre", query = "SELECT v FROM Vendedores v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "Vendedores.findByAp", query = "SELECT v FROM Vendedores v WHERE v.ap = :ap")
    , @NamedQuery(name = "Vendedores.findByAm", query = "SELECT v FROM Vendedores v WHERE v.am = :am")
    , @NamedQuery(name = "Vendedores.findByCorreo", query = "SELECT v FROM Vendedores v WHERE v.correo = :correo")
    , @NamedQuery(name = "Vendedores.findByNum", query = "SELECT v FROM Vendedores v WHERE v.num = :num")})
public class Vendedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ap")
    private String ap;
    @Basic(optional = false)
    @Column(name = "am")
    private String am;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "num")
    private int num;

    public Vendedores() {
    }

    public Vendedores(Integer id) {
        this.id = id;
    }

    public Vendedores(Integer id, String nombre, String ap, String am, String correo, int num) {
        this.id = id;
        this.nombre = nombre;
        this.ap = ap;
        this.am = am;
        this.correo = correo;
        this.num = num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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
        if (!(object instanceof Vendedores)) {
            return false;
        }
        Vendedores other = (Vendedores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Vendedores[ id=" + id + " ]";
    }
    
}
