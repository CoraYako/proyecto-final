package com.notlify.controllers;

import com.notlify.entidades.Usuario;
import com.notlify.enums.Rol;

import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;

import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.ImagenService;

import com.notlify.servicios.UsuarioService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    

     /**
     *
     * Controlador para registrar el usuario.
     * Recibe los atributos listados abajo y llama al  metodo crear y persistir del usuarioService.
     *
     * @param correo
     * @param clave1
     * @param clave2
     * @param rol
     * @param nombre
     * @param apellido
     * @param fechaNacimiento
     * @param archivo
     * 
     * @return Template: index.html
     * 
     * @throws ErrorInputException cuando los argumentos son nulos o vienen
     * vacíos.
     * 
     */
    
    @PostMapping("/registro")
    public String registrar(ModelMap modelo,MultipartFile archivo,@RequestParam Date fechaNacimiento, @RequestParam String nombre,@RequestParam String apellido, @RequestParam String correo, Rol rol, @RequestParam String clave1, @RequestParam String clave2){
    
        modelo.put("roles", Rol.values());
        
        try {
            usuarioService.crearYPersistir( correo,  clave1,  clave2,
             rol,  nombre,  apellido,  fechaNacimiento,
             archivo);
        } catch (ErrorInputException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("correo", correo);
            return "usuario-registro.html";
        }
           
                
                
        
        return "index.html";

    }

   
    
    
     /**
     *
     * Controlador para visualizar el usuario seleccionado.
     * Recibe un único parámetro desde el front y es variable : Id
     *
     * @PathVariable id
     *
     * @return el objeto buscado.
     * @throws ElementoNoEncontradoException 
    
     */
    
    
    @GetMapping("/registro{id}")
    public String registro(ModelMap modelo, @PathVariable String id) {
        Usuario usuario = new Usuario();

        modelo.put("roles", Rol.values());
        

        try {
            if (id != null && !id.trim().isEmpty()) {
                usuario = usuarioService.buscarPorId(id);
            }

            modelo.put("perfil", usuario);
        } catch (ElementoNoEncontradoException | ErrorInputException ex) {
            
            modelo.put("error", ex.getMessage());
        }

        return "usuario-registro.html";
    }

    
     @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        modelo.put("usuarios", usuarios);
        return "list-usuarios.html";
    }
    
    
    
}
