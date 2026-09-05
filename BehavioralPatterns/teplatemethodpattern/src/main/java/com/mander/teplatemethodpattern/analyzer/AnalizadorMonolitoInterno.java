package com.mander.teplatemethodpattern.analyzer;

import com.mander.teplatemethodpattern.model.Contenedor;

import java.util.List;

public class AnalizadorMonolitoInterno extends AbstractAnalizadorIaC {

    public AnalizadorMonolitoInterno(List<Contenedor> contenedores) {
        super(contenedores);
    }

    @Override
    protected boolean esVulnerable(Contenedor contenedor) {
        return contenedor.expuestoAInternet();
    }

    @Override
    protected String contexto() {
        return "Monolito interno";
    }
}
