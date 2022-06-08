package com.notelify.controllers;

import com.notelify.servicios.UsuarioService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuario")
public class UsuarioRestController {

    private Map<String, Object> response;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        response = new HashMap<>();
        response.put("usuarios", usuarioService.listarTodos());
        return ResponseEntity.status(200).body(response);
    }

}
