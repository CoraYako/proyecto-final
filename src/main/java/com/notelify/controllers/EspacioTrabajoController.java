package com.notelify.controllers;

import com.notelify.entidades.Tarea;
import com.notelify.entidades.Usuario;
import com.notelify.exceptions.ElementoNoEncontradoException;
import com.notelify.exceptions.ErrorInputException;
import com.notelify.servicios.EspacioTrabajoService;
import com.notelify.servicios.TareaService;
import com.notelify.servicios.UsuarioService;
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
@RequestMapping("/espacio-trabajo")
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

    @PostMapping("/crear")
    public String crear(RedirectAttributes attr,@RequestParam(required = false) MultipartFile archivo, @RequestParam String nombre, 
            @RequestParam String idUsuario) {

        try {
            espacioTrabajoService.crearYPersistir(archivo, nombre, idUsuario);
            attr.addFlashAttribute("exito", "El espacio de trabajo con el nombre '" + nombre + "' se inició exitosamente.");
        } catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/inicio"; //Acá el llamado es con redirect porque el método post dirige datos desde el front-end hacia el back-end.
    }
}
