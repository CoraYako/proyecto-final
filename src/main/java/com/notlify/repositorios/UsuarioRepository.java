package com.notlify.repositorios;

import com.notlify.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    //Buscar por Email
    @Query("SELECT u FROM Usuario u WHERE u.email LIKE :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    // Buscar por Rol
    @Query("SELECT u FROM Usuario u WHERE u.rol like :rol")
    public Usuario buscarPorRol(@Param("rol") String rol);

    // Buscar por Nombre 
    @Query("SELECT u FROM Usuario u WHERE u.nombre like :nombre")
    public Usuario buscarPorNombre(@Param("nombre") String nombre);

    // Buscar por Apellido
    @Query("SELECT u FROM Usuario u WHERE u.apellido like :apellido")
    public Usuario buscarPorApellido(@Param("apellido") String apellido);

}
