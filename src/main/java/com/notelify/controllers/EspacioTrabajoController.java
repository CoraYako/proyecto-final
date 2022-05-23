package com.notelify.controllers;

import com.notelify.entidades.EspacioTrabajo;
import com.notelify.entidades.Tarea;
import com.notelify.entidades.Usuario;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.EspacioTrabajoService;
import com.notelify.servicios.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.lang.annotation.RequiredTypes;
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

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/mi-espacio/{id}")
    public String espacioTrabajo(RedirectAttributes attr, ModelMap modelo, @PathVariable String id) {
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();

        try {
            espacioTrabajo = espacioTrabajoService.buscarPorId(id);

            List<Tarea> tareas = espacioTrabajo.getListaTareas();
            modelo.put("tareas", tareas);

            List<Usuario> usuarios = espacioTrabajo.getListaUsuarios();
            modelo.put("usuarios", usuarios);

            modelo.put("espacio", espacioTrabajo);
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

        return "redirect:/espacio-trabajo/mi-espacio/" + espacioTrabajo.getId();
    }
}
