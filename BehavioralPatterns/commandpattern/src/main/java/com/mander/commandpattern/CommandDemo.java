package com.mander.commandpattern;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

/**
 *
 *   RECEIVER = TV         -> hace el trabajo real.
 *   COMMAND  = Command    -> "una orden", solo sabe execute().
 *   INVOKER  = boton      -> guarda un comando y lo dispara, sin saber que hace.
 */
@Component
public class CommandDemo implements CommandLineRunner {

    // RECEIVER: sabe hacer el trabajo de verdad.
    static class TV {
        void subirVolumen()  { System.out.println("   TV: sube volumen"); }
        void cambiarCanal()  { System.out.println("   TV: cambia canal"); }
        void apagar()        { System.out.println("   TV: se apaga"); }
    }

    // COMMAND: una orden solo sabe ejecutarse.
    interface Command { void execute(); }

    @Override
    public void run(String... args) {
        TV tv = new TV();

        // CONCRETE COMMANDS: cada orden guarda la TV y le pide el trabajo.
        // Como Command tiene un solo metodo, cada orden es una simple lambda.
        Map<String, Command> ordenes = Map.of(
                "vol",   tv::subirVolumen,
                "canal", tv::cambiarCanal,
                "apagar", tv::apagar
        );

        // INVOKER: el boton. Solo guarda un comando y lo dispara.
        Command[] boton = { ordenes.get("vol") };

        System.out.println("\nComandos: press | assign vol|canal|apagar | quit");
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String[] in = sc.nextLine().trim().split("\\s+");
                switch (in[0]) {
                    case "press"  -> boton[0].execute();
                    case "assign" -> { boton[0] = ordenes.get(in[1]);
                                       System.out.println("   (boton reprogramado -> " + in[1] + ")"); }
                    case "quit"   -> { return; }
                    default       -> System.out.println("   ?");
                }
                System.out.print("\n> ");
            }
        }
    }
}
