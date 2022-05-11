package com.notelify.controllers;

import com.notelify.entidades.Usuario;
import com.notelify.enums.Rol;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.UsuarioService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String form(ModelMap modelo) {
        modelo.put("roles", Rol.values());
        return "registro.html";
    }

    /**
     *
     * Controlador para registrar el usuario. Recibe los atributos listados
     * abajo y llama al metodo crear y persistir del usuarioService.
     *
     * @param modelo
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
     */
    @PostMapping("/registro")
    public String registrar(ModelMap modelo, MultipartFile archivo, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaNacimiento, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, Rol rol, @RequestParam String clave1, @RequestParam String clave2) {
        try {
            usuarioService.crearYPersistir(correo, clave1, clave2, rol, nombre, apellido, fechaNacimiento, archivo);
        } catch (ErrorInputException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("roles", Rol.values());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("correo", correo);
            return "registro.html";
        }

        return "inicio.html";
    }

    /**
     *
     * Controlador para visualizar el usuario seleccionado. Recibe un único
     * parámetro desde el front y es variable : Id
     *
     * @param modelo
     * @param id
     * @PathVariable id
     *
     * @return el objeto buscado.
     */
    @GetMapping("/editar{id}")
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

            return "registro.html";
        }

        return "modificarDatos.html";
    }

    @PostMapping("/editar")
    public String editar(ModelMap modelo, MultipartFile archivo, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaNacimiento, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String correo, Rol rol, @RequestParam String clave1, @RequestParam String clave2) {
        Usuario usuario = new Usuario();

        try {
            if (id != null && !id.trim().isEmpty()) {
                usuario = usuarioService.modificarYPersistir(archivo, id, nombre, apellido, correo, clave1, clave2, fechaNacimiento);

            }
            modelo.put("perfil", usuario);
            modelo.put("exito", "Perfecto!!!!");
            modelo.put("descripcion", "El perfil fue modificado exitosamente");
        } catch (ErrorInputException | ElementoNoEncontradoException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("roles", Rol.values());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("correo", correo);
            return "modificarDatos.html";
        }

        return "modificarDatos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        modelo.put("usuarios", usuarios);
        return "list-usuarios.html";
    }

}
