package com.mander.teplatemethodpattern.analyzer;

import com.mander.teplatemethodpattern.model.Contenedor;

import java.util.AbstractList;
import java.util.List;

public abstract class AbstractAnalizadorIaC extends AbstractList<Contenedor> {

    private final List<Contenedor> contenedores;

    protected AbstractAnalizadorIaC(List<Contenedor> contenedores) {
        this.contenedores = contenedores;
    }

    @Override
    public Contenedor get(int index) {
        return contenedores.get(index);
    }

    @Override
    public int size() {
        return contenedores.size();
    }

    public final List<String> analizar() {
        return this.stream()
                .filter(this::esRelevante)
                .filter(this::esVulnerable)
                .map(this::describirHallazgo)
                .toList();
    }

    protected boolean esRelevante(Contenedor contenedor) {
        return true;
    }

    protected abstract boolean esVulnerable(Contenedor contenedor);

    protected abstract String contexto();

    private String describirHallazgo(Contenedor c) {
        return "[%s] Contenedor '%s' (imagen %s) publica %s:%d es VULNERABLE"
                .formatted(contexto(), c.nombre(), c.imagen(), c.host(), c.puerto());
    }
}
