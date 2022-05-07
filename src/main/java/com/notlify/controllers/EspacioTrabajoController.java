package com.notlify.controllers;

import com.notlify.entidades.Tarea;
import com.notlify.entidades.Usuario;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.EspacioTrabajoService;
import com.notlify.servicios.TareaService;
import com.notlify.servicios.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class EspacioTrabajoController {

    @Autowired
    private EspacioTrabajoService espacioTrabajoService;

    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/espacioTrabajo")
    public String espacioTrabajo(ModelMap modelo) {
        List<Tarea> listaTareas = tareaService.listarTodas();
        modelo.put("tareas", listaTareas);

        List<Usuario> listaUsuarios = usuarioService.listarTodos();
        modelo.put("usuarios", listaUsuarios);
        return "espacioTrabajo.html";
    }

    @PostMapping("/espacioTrabajo")
    public String crear(RedirectAttributes attr, @RequestParam MultipartFile archivo, @RequestParam String nombre, 
            @RequestParam String idUsuario, @RequestParam String idTarea) {

        try {
            espacioTrabajoService.crearYPersistir(archivo, nombre, idUsuario);
            attr.addFlashAttribute("exito", "El espacio de trabajo con el nombre '" + nombre + "' se inició exitosamente.");
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "espacioTrabajo.html";
    }
}
