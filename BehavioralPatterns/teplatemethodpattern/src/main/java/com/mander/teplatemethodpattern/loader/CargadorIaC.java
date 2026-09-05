package com.mander.teplatemethodpattern.loader;

import com.mander.teplatemethodpattern.model.Contenedor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class CargadorIaC {

    @SuppressWarnings("unchecked")
    public List<Contenedor> cargar(String archivo) {
        try (InputStream in = new ClassPathResource(archivo).getInputStream()) {
            Map<String, Object> raiz = new Yaml().load(in);
            Map<String, Map<String, Object>> services =
                    (Map<String, Map<String, Object>>) raiz.get("services");

            return services.entrySet().stream()
                    .map(e -> aContenedor(e.getKey(), e.getValue()))
                    .toList();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar el IaC: " + archivo, e);
        }
    }

    private Contenedor aContenedor(String nombre, Map<String, Object> datos) {
        String[] hostPuerto = ((String) datos.get("port")).split(":");
        return new Contenedor(
                nombre,
                (String) datos.get("image"),
                hostPuerto[0],
                Integer.parseInt(hostPuerto[1]));
    }
}
