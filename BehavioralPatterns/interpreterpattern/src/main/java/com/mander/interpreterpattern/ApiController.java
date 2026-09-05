package com.mander.interpreterpattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final SPelService service;
    public ApiController(SPelService service){ this.service = service; }

    @GetMapping("/publico")
    public String publico(){
        return "public";
    }

    @GetMapping("/usuario")
    public String usuario(){
        return "Usuario log";
    }

    @GetMapping("/admin")
    public String admin(){
        return service.verNombre();
    }

}
