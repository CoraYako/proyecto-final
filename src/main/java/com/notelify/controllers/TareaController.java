package com.notelify.controllers;

import com.notelify.entidades.EspacioTrabajo;
import com.notelify.entidades.Tarea;
import com.notelify.enums.Estado;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.EspacioTrabajoService;
import com.notelify.servicios.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tarea")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private EspacioTrabajoService espacioTrabajoService;

    @PostMapping("/crear")
    public String crear(ModelMap modelo, @RequestParam String idEspacio, @RequestParam String titulo, @RequestParam(required = false) String descripcion, @RequestParam String idUsuario, RedirectAttributes attr) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();
        Tarea tarea = new Tarea();
        try {
            espacioTrabajo = espacioTrabajoService.buscarPorId(idEspacio);
            tarea = tareaService.crearYPersistir(titulo, descripcion, idUsuario);

            espacioTrabajoService.agregarTarea(espacioTrabajo.getId(), tarea.getId());

            modelo.put("espacioTrabajo", espacioTrabajo);
        } catch (ErrorInputException | ElementoNoEncontradoException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/espacio-trabajo/mi-espacio/" + espacioTrabajo.getId();
    }
    
    @PostMapping("/estado")
    public String cambiarEstado(RedirectAttributes attr ,@RequestParam String idEspacio ,@RequestParam String id, Estado estado) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();
        
        try {
            espacioTrabajoService.buscarPorId(idEspacio);
            tareaService.moverDeEstado(id, estado);
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/espacio-trabajo/mi-espacio/" + espacioTrabajo.getId();
    }

    @PostMapping("/editar")
    public String modificarTarea(@RequestParam String idEspacio, @RequestParam String id, @RequestParam String titulo, @RequestParam(required = false) String descripcion, RedirectAttributes attr) {
        try {
            tareaService.modificar(id, titulo, descripcion);
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/espacio-trabajo/mi-espacio/";

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
