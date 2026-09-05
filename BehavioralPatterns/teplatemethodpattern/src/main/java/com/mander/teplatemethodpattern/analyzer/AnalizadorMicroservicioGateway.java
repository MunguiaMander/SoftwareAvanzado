package com.mander.teplatemethodpattern.analyzer;

import com.mander.teplatemethodpattern.model.Contenedor;

import java.util.List;

public class AnalizadorMicroservicioGateway extends AbstractAnalizadorIaC {

    public AnalizadorMicroservicioGateway(List<Contenedor> contenedores) {
        super(contenedores);
    }

    @Override
    protected boolean esRelevante(Contenedor contenedor) {
        return !contenedor.nombre().contains("gateway");
    }

    @Override
    protected boolean esVulnerable(Contenedor contenedor) {
        return contenedor.expuestoAInternet();
    }

    @Override
    protected String contexto() {
        return "Microservicio+Gateway";
    }
}
