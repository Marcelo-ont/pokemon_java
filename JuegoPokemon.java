import java.util.Random;
import java.util.Scanner;

public class JuegoPokemon {
    private final Scanner scanner;
    private final Random random;
    private final CatalogoPokemon catalogo;
    private Jugador[] jugadores;
    private Partida[] partidas;
    private int totalJugadores;
    private int totalPartidas;

    public JuegoPokemon() {
        scanner = new Scanner(System.in);
        random = new Random();
        catalogo = new CatalogoPokemon();
        jugadores = new Jugador[10];
        partidas = new Partida[10];
    }

    public void iniciar() {
        boolean seguir = true;
        while (seguir) {
            System.out.println("\n1 - Crear nuevo jugador");
            System.out.println("2 - Ver jugadores");
            System.out.println("3 - Jugar");
            System.out.println("4 - Ver estadisticas");
            System.out.println("5 - Salir");
            int opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    altaJugador();
                    break;
                case 2:
                    mostrarJugadores();
                    break;
                case 3:
                    jugarPartida();
                    break;
                case 4:
                    verEstadisticas();
                    break;
                case 5:
                    seguir = false;
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private void altaJugador() {
        System.out.print("Nombre del jugador: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("Nombre invalido.");
            return;
        }
        if (buscarJugador(nombre) != null) {
            System.out.println("Ese jugador ya existe.");
            return;
        }
        if (totalJugadores == jugadores.length) {
            Jugador[] nuevo = new Jugador[jugadores.length * 2];
            System.arraycopy(jugadores, 0, nuevo, 0, jugadores.length);
            jugadores = nuevo;
        }
        jugadores[totalJugadores] = new Jugador(nombre);
        totalJugadores++;
        System.out.println("Jugador registrado.");
    }

    private void mostrarJugadores() {
        if (totalJugadores == 0) {
            System.out.println("No hay jugadores.");
            return;
        }
        for (int i = 0; i < totalJugadores; i++) {
            System.out.println(i + " - " + jugadores[i].getNombre());
        }
    }

    private void jugarPartida() {
        if (totalJugadores < 2) {
            System.out.println("Necesitas al menos 2 jugadores registrados.");
            return;
        }

        mostrarJugadores();
        int cantidad = leerEntero("Cuantos jugadores participaran? ");
        if (cantidad < 2 || cantidad > totalJugadores) {
            System.out.println("Cantidad invalida.");
            return;
        }

        Jugador[] participantes = elegirParticipantes(cantidad);
        if (participantes == null) {
            return;
        }

        Partida partida = new Partida(totalPartidas + 1, participantes);
        Batalla batalla = new Batalla(scanner, random, catalogo);

        for (Jugador jugador : participantes) {
            jugador.prepararEquipo(catalogo, random);
        }

        boolean seguirPartida = true;
        for (Jugador jugador : participantes) {
            if (!seguirPartida) {
                break;
            }
            System.out.println("\nTurno de " + jugador.getNombre());
            int combates = 0;
            while (combates < 2 && jugador.tienePokemonVivos()) {
                if (!batalla.jugar(jugador, partida)) {
                    seguirPartida = false;
                    break;
                } else {
                    combates++;
                }
            }
        }

        partida.cerrar();
        partida.mostrarResumen();

        if (totalPartidas == partidas.length) {
            Partida[] nuevo = new Partida[partidas.length * 2];
            System.arraycopy(partidas, 0, nuevo, 0, partidas.length);
            partidas = nuevo;
        }
        partidas[totalPartidas] = partida;
        totalPartidas++;
    }

    private void verEstadisticas() {
        System.out.println("\nEstadisticas por jugador:");
        for (int i = 0; i < totalJugadores; i++) {
            System.out.println(jugadores[i].resumenEstadisticas());
        }

        System.out.println("\nEstadisticas por partida:");
        for (int i = 0; i < totalPartidas; i++) {
            partidas[i].mostrarResumen();
        }
    }

    private Jugador[] elegirParticipantes(int cantidad) {
        Jugador[] participantes = new Jugador[cantidad];
        for (int i = 0; i < cantidad; i++) {
            int indice = leerEntero("Indice del jugador " + (i + 1) + ": ");
            if (indice < 0 || indice >= totalJugadores || yaEsta(participantes, jugadores[indice], i)) {
                System.out.println("Seleccion invalida.");
                return null;
            }
            participantes[i] = jugadores[indice];
        }
        return participantes;
    }

    private boolean yaEsta(Jugador[] participantes, Jugador jugador, int total) {
        for (int i = 0; i < total; i++) {
            if (participantes[i] == jugador) {
                return true;
            }
        }
        return false;
    }

    private Jugador buscarJugador(String nombre) {
        for (int i = 0; i < totalJugadores; i++) {
            if (jugadores[i].getNombre().equalsIgnoreCase(nombre)) {
                return jugadores[i];
            }
        }
        return null;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
        }
    }
}
