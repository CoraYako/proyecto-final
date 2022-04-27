package com.proyectoFinal.repositorios;

import com.proyectoFinal.entidades.Tarea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, String>{
    
    @Query("SELECT t FROM Tarea t WHERE t.estado LIKE :estado")
    public List<Tarea> buscarPorEstado(@Param("estado") String estado);
    
}
