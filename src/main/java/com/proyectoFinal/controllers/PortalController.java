package com.proyectoFinal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {
    
    @GetMapping("/registro")
    public String registro() {
        return "registro.html";
    }
   
    @GetMapping("/login")
    public String login(ModelMap modelo, @RequestParam(required = false) String error, @RequestParam(required = false) String logout) {
        
        if (error != null) {
            modelo.put("error", "Usuario o Clave incorrectos >:(");
        }
        
        if (logout != null) {
            modelo.put("logout", "Has cerrado sesión exitosamente :)");
        }
        
        return "login.html";
    }
    
}
