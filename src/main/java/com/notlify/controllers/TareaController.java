package com.notlify.controllers;

import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.servicios.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
