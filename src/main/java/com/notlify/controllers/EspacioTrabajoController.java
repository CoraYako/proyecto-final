package com.notlify.controllers;

import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.EspacioTrabajoService;
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
    
    @GetMapping("/espacioTrabajo")
    public String espacioTrabajo(ModelMap modelo) {
        
        /*FALTAN AGREGAR LAS LISTAS DE OTRAS ENTIDADES*/
        
        return "/*HTML CORRORRESPONDIENTE AL ESPACIO TRABAJO*/";
    }
    
    @PostMapping("/espacioTrabajo")
    public String crear(RedirectAttributes attr, @RequestParam MultipartFile archivo, @RequestParam String nombre, String idUsuario) {
        
        try{
            espacioTrabajoService.crearYPersistir(archivo, nombre, idUsuario);
            attr.addFlashAttribute("exito", "El espacio de trabajo con el nombre '" +nombre+"' se inició exitosamente.");
        }catch (ElementoNoEncontradoException | ErrorInputException e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        
        return "/*HTML O REDIRECT CORRESPONDIENTE*/";
    }
}
