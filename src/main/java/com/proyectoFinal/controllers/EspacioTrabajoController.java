
package com.proyectoFinal.controllers;

import com.proyectoFinal.servicios.EspacioTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class EspacioTrabajoController {
    
    @Autowired
    private EspacioTrabajoService espacioTrabajoService;
    
    @GetMapping("/espacioTrabajo")
    public String espacioTrabajo(ModelMap modelo) {
        
        /*FALTAN AGREGAR LAS LISTAS DE OTRAS ENTIDADES*/
        
        return "/*HTML CORRORRESPONDIENTE AL ESPACIO TRABAJO*/";
    }
    
    @PostMapping("/espacioTrabajo")
    public String crear(RedirectAttributes attr, @RequestParam String nombre 
    /*String idTarea, String idImagen, String idUsuarios*/) {
        
        try{
            espacioTrabajoService.cargar(nombre, true);
            attr.addFlashAttribute("exito", "El espacio de trabajo con el nombre '" +nombre+"' se inició exitosamente.");
        }catch (Exception e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        
        return "/*HTML O REDIRECT CORRESPONDIENTE*/";
    }
}
