package com.proyectoFinal.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "espacio_de_trabajo")
public class EspacioTrabajo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_espacio")
    private String id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    private List<Tarea> tareas;

    @OneToOne
    @Column(name = "imagen_fondo")
    private Imagen fondo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @OneToMany
    @JoinColumn(name = "usuarios", nullable = false)
    private List<Usuario> usuarios;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    public EspacioTrabajo(String id, String nombre, List<Tarea> tareas, Imagen fondo, Date fechaCreacion, List<Usuario> usuarios, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.tareas = tareas;
        this.fondo = fondo;
        this.fechaCreacion = fechaCreacion;
        this.usuarios = usuarios;
        this.activo = activo;
    }

    public EspacioTrabajo() {
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the tareas
     */
    public List<Tarea> getTareas() {
        return tareas;
    }

    /**
     * @param tareas the tareas to set
     */
    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    /**
     * @return the fondo
     */
    public Imagen getFondo() {
        return fondo;
    }

    /**
     * @param fondo the fondo to set
     */
    public void setFondo(Imagen fondo) {
        this.fondo = fondo;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the usuarios
     */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * @param usuarios the usuarios to set
     */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * @return the activo
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "EspacioTrabajo{" + "id=" + id + ", nombre=" + nombre + ", tareas=" + tareas + ", fondo=" + fondo + ", fechaCreacion=" + fechaCreacion + ", usuarios=" + usuarios + ", activo=" + activo + '}';
    }

}
