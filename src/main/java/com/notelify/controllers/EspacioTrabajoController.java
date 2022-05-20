package com.notelify.controllers;

import com.notelify.entidades.EspacioTrabajo;
import com.notelify.entidades.Tarea;
import com.notelify.entidades.Usuario;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.EspacioTrabajoService;
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

    @GetMapping("/mi-espacio/{id}")
    public String espacioTrabajo(ModelMap modelo, @PathVariable String id) {
        try {
            EspacioTrabajo miEspacio = espacioTrabajoService.buscarPorId(id);

            List<Tarea> listaTareas = miEspacio.getListaTareas();
            modelo.put("listaTareas", listaTareas);

            List<Usuario> listaUsuarios = miEspacio.getListaUsuarios();
            modelo.put("listaUsuarios", listaUsuarios);
        } catch (ElementoNoEncontradoException | ErrorInputException ex) {
            modelo.put("error", ex.getMessage());
        }

        return "espacioTrabajo.html";
    }

    @PostMapping("/crear")
    public String crear(RedirectAttributes attr, @RequestParam(required = false) MultipartFile archivo, @RequestParam String nombre, @RequestParam String idUsuario) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();
        try {
            espacioTrabajo = espacioTrabajoService.crearYPersistir(archivo, nombre, idUsuario);
            attr.addFlashAttribute("exito", "El espacio de trabajo con el nombre '" + nombre + "' se inició exitosamente.");
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/espacio-trabajo/mi-espacio/" + espacioTrabajo.getId();
    }
}
