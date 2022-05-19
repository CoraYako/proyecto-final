package com.notelify.controllers;

import com.notelify.entidades.Tarea;
import com.notelify.enums.Estado;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.TareaService;
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
@RequestMapping("/tarea")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/crear")
    public String crearTarea(@RequestParam String titulo, @RequestParam(required = false) String descripcion, @RequestParam(required = false) String idUsuario, RedirectAttributes attr) {

        try {
            
            tareaService.crearYPersistir(titulo, descripcion);
                    
            
        } catch (ErrorInputException | ElementoNoEncontradoException e) {

            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/espacioTrabajo";

        }

        return "espacioTrabajo.html";

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

    @PostMapping("/eliminar")
    public String deshabilitarTarea(@RequestParam String id, RedirectAttributes attr) {

        try {

            tareaService.deshabilitar(id);

        } catch (ElementoNoEncontradoException | ErrorInputException e) {

            attr.addFlashAttribute("error", e.getMessage());
            return "redirect:/tareas/eliminar/" + id;

        }

        return "redirect:/espacioTrabajo";

    }

}
