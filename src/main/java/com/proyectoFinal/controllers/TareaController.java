package com.proyectoFinal.controllers;

import com.proyectoFinal.entidades.Usuario;
import com.proyectoFinal.enums.Estado;
import com.proyectoFinal.servicios.TareaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tareas")
public class TareaController {
    
    @Autowired
    private TareaService tareaService;
    
    @PostMapping("/crear")
    public String crearTarea(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam Estado estado, List<Usuario> usuarios) {
        
        tareaService.save(titulo, descripcion, estado, usuarios);
        
        return "index.html";
        
    }
    
}
