package com.notlify.controllers;

import com.notlify.entidades.Usuario;
import com.notlify.enums.Rol;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.ImagenService;
import com.notlify.servicios.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    
    @Autowired 
    private ImagenService imagenService;
    
    @GetMapping("/registrar")
    public String form() {
        

        
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registrar(RedirectAttributes attr, @RequestParam(required = true) String nombre, @RequestParam(required = true) String apellido, @RequestParam(required = true) String fechaDeNacimiento
    , @RequestParam(required = true) Rol rol, @RequestParam(required = true) String correo, @RequestParam(required = true) String password,
    @RequestParam(required = true) String confirmarPassword, MultipartFile fotoPerfil) {
        
         try {
            usuarioService.crearYPersistir(correo, password, confirmarPassword, rol, nombre, apellido, fechaDeNacimiento, fotoPerfil);
        
        attr.addFlashAttribute("exito", "El usuario '" +apellido+"' se cargo exitosamente.");
        } catch (ErrorInputException  ex) {
            attr.addFlashAttribute("error", ex.getMessage());
        }
        
        
        return "registro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        modelo.put("usuarios", usuarios);
        return "list-usuarios.html";
    }
}
