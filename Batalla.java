import java.util.Random;
import java.util.Scanner;

public class Batalla {
    private final Scanner scanner;
    private final Random random;
    private final CatalogoPokemon catalogo;
    private final Usable[] items;
    private final Evento[] historial;
    private int totalEventos;

    public Batalla(Scanner scanner, Random random, CatalogoPokemon catalogo) {
        this.scanner = scanner;
        this.random = random;
        this.catalogo = catalogo;
        this.items = new Usable[]{new Pocion(), new SuperPocion()};
        this.historial = new Evento[40];
    }

    public boolean jugar(Jugador jugador, Partida partida) {
        totalEventos = 0;
        Pokemon enemigo = catalogo.crearSalvaje(random);
        Pokemon[] usados = new Pokemon[6];
        int cantidadUsados = 0;
        Pokemon actual = jugador.elegirPokemon(scanner);

        registrar("Empieza " + actual.getNombre() + " contra " + enemigo.getNombre() + ".");

        while (jugador.tienePokemonVivos() && !enemigo.estaDebilitado()) {
            mostrarEstado(actual, enemigo);
            System.out.println("1 - Atacar");
            System.out.println("2 - Usar pocion");
            System.out.println("3 - Usar super pocion");
            System.out.println("4 - Lanzar pokeball");
            System.out.println("I - Ver inventario");
            System.out.println("H - Ver historial");
            System.out.println("Q - Salir");
            System.out.print("Accion: ");
            String accion = scanner.nextLine().trim().toUpperCase();

            if ("Q".equals(accion)) {
                registrar("Se salio del combate.");
                mostrarHistorial();
                return false;
            }

            if ("1".equals(accion)) {
                int indice = elegirAtaque(actual);
                Ataque ataque = actual.getAtaques()[indice];
                if (!contiene(usados, cantidadUsados, actual)) {
                    registrar(actual.getNombre() + " entro al historial de experiencia.");
                    usados[cantidadUsados] = actual;
                    cantidadUsados++;
                }
                int danio = actual.atacar(enemigo, indice);
                double multi = TablaTipos.multiplicador(ataque.getTipo(), enemigo.getTipo());
                System.out.println(actual.getNombre() + " uso " + ataque.getNombre() + " y hizo " + danio + ".");
                registrar(actual.getNombre() + " uso " + ataque.getNombre() + ".");
                mostrarMensajeEfectividad(multi);

                if (!enemigo.estaDebilitado()) {
                    atacarEnemigo(actual, enemigo);
                }
            } else if ("2".equals(accion)) {
                String mensaje = items[0].usar(jugador, actual);
                System.out.println(mensaje);
                registrar(mensaje);
            } else if ("3".equals(accion)) {
                String mensaje = items[1].usar(jugador, actual);
                System.out.println(mensaje);
                registrar(mensaje);
            } else if ("4".equals(accion)) {
                if (intentarCaptura(jugador, enemigo, partida)) {
                    mostrarHistorial();
                    esperarEnter();
                    return true;
                }
            } else if ("I".equals(accion)) {
                jugador.mostrarEquipo();
            } else if ("H".equals(accion)) {
                mostrarHistorial();
            } else {
                System.out.println("Opcion invalida.");
            }

            if (actual.estaDebilitado() && jugador.tienePokemonVivos()) {
                System.out.println(actual.getNombre() + " se debilito. Elige otro.");
                registrar(actual.getNombre() + " se debilito.");
                actual = jugador.elegirPokemon(scanner);
            }
        }

        if (enemigo.estaDebilitado()) {
            System.out.println("Ganaste el combate.");
            registrar("Victoria de " + jugador.getNombre() + ".");
            catalogo.darExperiencia(usados, cantidadUsados, random);
            repartirRecompensa(jugador);
            partida.registrarVictoria(jugador, 10);
        } else {
            System.out.println("Perdiste el combate.");
            registrar("Derrota de " + jugador.getNombre() + ".");
            partida.registrarDerrota(jugador);
        }

        mostrarHistorial();
        esperarEnter();
        return true;
    }

    private int elegirAtaque(Pokemon pokemon) {
        Ataque[] ataques = pokemon.getAtaques();
        for (int i = 0; i < ataques.length; i++) {
            System.out.println(i + " - " + ataques[i]);
        }
        while (true) {
            System.out.print("Ataque: ");
            String texto = scanner.nextLine().trim();
            try {
                int indice = Integer.parseInt(texto);
                if (indice >= 0 && indice < ataques.length) {
                    return indice;
                }
            } catch (NumberFormatException e) {
                // mismo mensaje abajo
            }
            System.out.println("Ataque invalido.");
        }
    }

    private void atacarEnemigo(Pokemon jugador, Pokemon enemigo) {
        int indice = random.nextInt(enemigo.getAtaques().length);
        Ataque ataque = enemigo.getAtaques()[indice];
        int danio = enemigo.atacar(jugador, indice);
        System.out.println(enemigo.getNombre() + " uso " + ataque.getNombre() + " y te hizo " + danio + ".");
        registrar(enemigo.getNombre() + " ataco con " + ataque.getNombre() + ".");
    }

    private boolean intentarCaptura(Jugador jugador, Pokemon enemigo, Partida partida) {
        if (jugador.getPokeballs() <= 0) {
            System.out.println("No tienes pokeballs.");
            return false;
        }
        jugador.setPokeballs(jugador.getPokeballs() - 1);
        if (enemigo.getVidaActual() > enemigo.getVidaMaxima() / 4) {
            System.out.println("El pokemon sigue demasiado fuerte.");
            registrar("Fallo la captura.");
            return false;
        }
        if (!jugador.agregarPokemon(catalogo.crearPorNombre(enemigo.getNombre()))) {
            System.out.println("Tu equipo esta lleno.");
            return false;
        }
        System.out.println("Capturaste a " + enemigo.getNombre() + ".");
        registrar("Captura de " + enemigo.getNombre() + ".");
        partida.registrarCaptura(jugador);
        partida.registrarVictoria(jugador, 5);
        repartirRecompensa(jugador);
        return true;
    }

    private void repartirRecompensa(Jugador jugador) {
        if (random.nextBoolean()) {
            jugador.setPociones(jugador.getPociones() + 1);
            System.out.println("Recibiste 1 pocion.");
        }
        if (random.nextInt(4) == 0) {
            jugador.setSuperPociones(jugador.getSuperPociones() + 1);
            System.out.println("Recibiste 1 super pocion.");
        }
        if (random.nextBoolean()) {
            jugador.setPokeballs(jugador.getPokeballs() + 1);
            System.out.println("Recibiste 1 pokeball.");
        }
    }

    private void mostrarEstado(Pokemon jugador, Pokemon enemigo) {
        System.out.println("\n" + jugador.resumen());
        System.out.println(barra(jugador));
        System.out.println(enemigo.resumen());
        System.out.println(barra(enemigo));
    }

    private String barra(Pokemon pokemon) {
        int llenos = (pokemon.getVidaActual() * 10) / pokemon.getVidaMaxima();
        StringBuilder texto = new StringBuilder("[");
        for (int i = 0; i < 10; i++) {
            texto.append(i < llenos ? "#" : " ");
        }
        texto.append("]");
        return texto.toString();
    }

    private void mostrarMensajeEfectividad(double multi) {
        if (multi > 1.0) {
            System.out.println("Es super efectivo.");
        } else if (multi < 1.0) {
            System.out.println("No es muy efectivo.");
        }
    }

    private void registrar(String texto) {
        if (totalEventos < historial.length) {
            historial[totalEventos] = new Evento(totalEventos + 1, texto);
            totalEventos++;
        }
    }

    private void mostrarHistorial() {
        System.out.println("\nHistorial:");
        for (int i = 0; i < totalEventos; i++) {
            System.out.println(historial[i]);
        }
    }

    private boolean contiene(Pokemon[] usados, int total, Pokemon pokemon) {
        for (int i = 0; i < total; i++) {
            if (usados[i] == pokemon) {
                return true;
            }
        }
        return false;
    }

    private void esperarEnter() {
        System.out.print("ENTER para continuar...");
        scanner.nextLine();
    }
}
