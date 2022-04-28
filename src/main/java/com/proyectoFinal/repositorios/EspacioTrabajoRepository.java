
package com.proyectoFinal.repositorios;

import com.proyectoFinal.entidades.EspacioTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EspacioTrabajoRepository extends JpaRepository<EspacioTrabajo, String> {
    
    @Query("SELECT e FROM  EspacioTrabajo e WHERE e.id LIKE  :id ")
    public EspacioTrabajo buscarPorId(@Param("id")String id);
    
    @Query("SELECT e FROM EspacioTrabajo e WHERE e.alta = true")
    public List<EspacioTrabajo> buscarActivos();
    
}
