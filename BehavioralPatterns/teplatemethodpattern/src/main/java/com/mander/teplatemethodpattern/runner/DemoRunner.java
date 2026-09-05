package com.mander.teplatemethodpattern.runner;

import com.mander.teplatemethodpattern.analyzer.AbstractAnalizadorIaC;
import com.mander.teplatemethodpattern.analyzer.AnalizadorMicroservicioGateway;
import com.mander.teplatemethodpattern.analyzer.AnalizadorMonolitoInterno;
import com.mander.teplatemethodpattern.loader.CargadorIaC;
import com.mander.teplatemethodpattern.model.Contenedor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {

    private final CargadorIaC cargador;

    public DemoRunner(CargadorIaC cargador) {
        this.cargador = cargador;
    }

    @Override
    public void run(String... args) {
        List<Contenedor> infra = cargador.cargar("infra.yml");

        ejecutar(new AnalizadorMicroservicioGateway(infra));
        ejecutar(new AnalizadorMonolitoInterno(infra));
    }

    private void ejecutar(AbstractAnalizadorIaC analizador) {
        System.out.println("\n=== " + analizador.getClass().getSimpleName() + " ===");
        System.out.println("Recursos analizados: " + analizador.size());
        List<String> hallazgos = analizador.analizar();
        if (hallazgos.isEmpty()) {
            System.out.println("Sin vulnerabilidades.");
        } else {
            hallazgos.forEach(System.out::println);
        }
    }
}
