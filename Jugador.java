import java.util.Random;
import java.util.Scanner;

public class Jugador {
    private String nombre;
    private Pokemon[] pokemones;
    private int cantidadPokemones;
    private int pociones;
    private int superPociones;
    private int pokeballs;
    private int puntosTotales;
    private int partidasJugadas;
    private int combatesGanados;
    private int combatesPerdidos;

    public Jugador(String nombre) {
        this.nombre = nombre;
        pokemones = new Pokemon[6];
    }

    public void prepararEquipo(CatalogoPokemon catalogo, Random random) {
        pokemones = new Pokemon[6];
        cantidadPokemones = 0;
        for (int i = 0; i < 3; i++) {
            agregarPokemon(catalogo.crearInicial(random));
        }
        pociones = 3;
        superPociones = 1;
        pokeballs = 3;
    }

    public boolean agregarPokemon(Pokemon pokemon) {
        if (cantidadPokemones == pokemones.length) {
            return false;
        }
        pokemones[cantidadPokemones] = pokemon;
        cantidadPokemones++;
        return true;
    }

    public boolean tienePokemonVivos() {
        for (int i = 0; i < cantidadPokemones; i++) {
            if (!pokemones[i].estaDebilitado()) {
                return true;
            }
        }
        return false;
    }

    public Pokemon elegirPokemon(Scanner scanner) {
        while (true) {
            mostrarEquipo();
            System.out.print("Elige pokemon: ");
            String texto = scanner.nextLine().trim();
            try {
                int indice = Integer.parseInt(texto);
                if (indice >= 0 && indice < cantidadPokemones && !pokemones[indice].estaDebilitado()) {
                    return pokemones[indice];
                }
            } catch (NumberFormatException e) {
                // se maneja abajo con el mismo mensaje
            }
            System.out.println("Opcion invalida.");
        }
    }

    public void mostrarEquipo() {
        System.out.println("\nEquipo de " + nombre + ":");
        for (int i = 0; i < cantidadPokemones; i++) {
            System.out.println(i + " - " + pokemones[i].resumen());
        }
        System.out.println("Pokeballs: " + pokeballs + " | Pociones: " + pociones
                + " | Super pociones: " + superPociones);
    }

    public void sumarPuntos() {
        puntosTotales++;
    }

    public void sumarPuntos(int puntos) {
        puntosTotales += puntos;
    }

    public void registrarVictoria() {
        combatesGanados++;
    }

    public void registrarDerrota() {
        combatesPerdidos++;
    }

    public void registrarPartida() {
        partidasJugadas++;
    }

    public String resumenEstadisticas() {
        return nombre + " | puntos " + puntosTotales
                + " | partidas " + partidasJugadas
                + " | victorias " + combatesGanados
                + " | derrotas " + combatesPerdidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pokemon[] getPokemones() {
        return pokemones;
    }

    public int getCantidadPokemones() {
        return cantidadPokemones;
    }

    public int getPociones() {
        return pociones;
    }

    public void setPociones(int pociones) {
        this.pociones = pociones;
    }

    public int getSuperPociones() {
        return superPociones;
    }

    public void setSuperPociones(int superPociones) {
        this.superPociones = superPociones;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    public void setPokeballs(int pokeballs) {
        this.pokeballs = pokeballs;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }
}
