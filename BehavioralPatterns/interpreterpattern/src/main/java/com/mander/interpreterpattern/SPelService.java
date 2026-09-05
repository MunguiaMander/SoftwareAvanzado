package com.mander.interpreterpattern;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SPelService {

    @PreAuthorize("hasRole('ADMIN')")
    public String borrarCuenta(){
        return "Cuenta borrada por ADMIn";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String verNombre(){
        return "Raton Vaquero";
    }

}
