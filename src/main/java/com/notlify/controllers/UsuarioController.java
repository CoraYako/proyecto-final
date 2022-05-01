package com.notlify.controllers;

import com.notlify.entidades.Usuario;
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

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public String registrar(@RequestParam String email, @RequestParam String clave, @RequestParam String confirmarClave) {
        //TODO
        //terminar el llamado del método correspondiente y solicitar los parámetros necesarios.
        return "index.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        modelo.put("usuarios", usuarios);
        return "list-usuarios.html";
    }
}
