package com.notelify.controllers;

import com.notelify.entidades.EspacioTrabajo;
import com.notelify.entidades.Tarea;
import com.notelify.enums.Estado;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.EspacioTrabajoService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/espacio-trabajo")
public class EspacioTrabajoController {

    @Autowired
    private EspacioTrabajoService espacioTrabajoService;

    @GetMapping("/mi-espacio/{id}/{idUsuario}")
    public String espacioTrabajo(RedirectAttributes attr, ModelMap modelo, @PathVariable String id, @PathVariable String idUsuario) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();

        List<Tarea> pendientes = new ArrayList<>();
        List<Tarea> realizandose = new ArrayList<>();
        List<Tarea> finalizadas = new ArrayList<>();

        try {
            List<EspacioTrabajo> espaciosDelUsuarioLogeado = espacioTrabajoService.espaciosDelUsuario(idUsuario);
            
            espacioTrabajo = espacioTrabajoService.buscarPorId(id);
            modelo.put("espacio", espacioTrabajo);

            List<Tarea> tareas = espacioTrabajo.getListaTareas();
            tareas.stream().map((tarea) -> {
                if (tarea.getEstado().name().equals("TODO")) {
                    pendientes.add(tarea);
                }
                return tarea;
            }).map((tarea) -> {
                if (tarea.getEstado().name().equals("IN_PROGRESS")) {
                    realizandose.add(tarea);
                }
                return tarea;
            }).filter((tarea) -> (tarea.getEstado().name().equals("FINISHED"))).forEachOrdered((tarea) -> {
                finalizadas.add(tarea);
            });

            modelo.put("espaciosDelUsuarioLogeado", espaciosDelUsuarioLogeado);
            modelo.put("pendientes", pendientes);
            modelo.put("realizandose", realizandose);
            modelo.put("finalizadas", finalizadas);
            modelo.put("estados", Estado.values());
        } catch (ElementoNoEncontradoException | ErrorInputException ex) {
            attr.addFlashAttribute("error", ex.getMessage());
        }

        return "espacioTrabajo.html";
    }

    @GetMapping("/lista/{idUsuario}")
    public String lista(ModelMap modelo, @PathVariable String idUsuario) {
        try {
            List<EspacioTrabajo> espaciosDelUsuarioLogeado = espacioTrabajoService.espaciosDelUsuario(idUsuario);
            modelo.put("espacios", espaciosDelUsuarioLogeado);
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            modelo.put("error", e.getMessage());
        }

        return "listaEspaciosTrabajo.html";
    }

    @PostMapping("/crear")
    public String crear(RedirectAttributes attr, ModelMap modelo, @RequestParam(required = false) MultipartFile archivo, @RequestParam String nombre, @RequestParam String idUsuario) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();

        try {
            espacioTrabajo = espacioTrabajoService.crearYPersistir(archivo, nombre, idUsuario);
            modelo.put("espacioTrabajo", espacioTrabajo);
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/inicio";
    }
}
