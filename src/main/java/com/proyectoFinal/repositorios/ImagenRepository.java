
package com.proyectoFinal.repositorios;

import com.proyectoFinal.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, String> {
    
    
    
}
