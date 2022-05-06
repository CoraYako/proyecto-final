package com.notlify.controllers;

import com.notlify.entidades.Tarea;
import com.notlify.enums.Estado;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/crear")
    public String crearTarea(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String idUsuario) {

        try {
            tareaService.crearYPersistir(titulo, descripcion, idUsuario);
        } catch (ErrorInputException | ElementoNoEncontradoException ex) {
            //TODO
            // tratar la excepción enviándola a la vista correspondiente.
        }

        return "index.html";

    }

    @GetMapping("/editar/{id}")
    public String modificar(@PathVariable String id, ModelMap model) {

        try {

            Tarea tarea = tareaService.buscarPorId(id);

            model.put("tarea", tarea);

        } catch (ElementoNoEncontradoException | ErrorInputException e) {

            e.printStackTrace();

        }

        return "//VISTA PARA MODIFICAR LA TAREA";

    }

    @PostMapping("/editar")
    public String modificarTarea(@RequestParam String id, @RequestParam String titulo, @RequestParam String descripcion, RedirectAttributes attr) {

        try {

            tareaService.modificar(id, titulo, descripcion);

        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/tareas/editar/" + id;

        }

        return "redirect:/espacioTrabajo";

    }
    
    @GetMapping("/eliminar/{id}")
    public String deshabilitar(@PathVariable String id, ModelMap model) {
        
        try {
            
            Tarea tarea = tareaService.buscarPorId(id);
            
            model.put("tarea", tarea);
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return "//VISTA PARA CONFIRMAR LA DESHABILITACIÓN DE LA TAREA";
        
    }
    
    @PostMapping("/eliminar")
    public String deshabilitarTarea(@RequestParam String id, RedirectAttributes attr) {
        
        try {
            
            tareaService.deshabilitar(id);
            
        } catch (Exception e) {
            
            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/tareas/eliminar/" + id;
            
        }
        
        return "redirect:/espacioTrabajo";
        
    }

}
