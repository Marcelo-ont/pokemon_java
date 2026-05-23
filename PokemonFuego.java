public class PokemonFuego extends Pokemon {
    public PokemonFuego(String nombre, int nivel, int vidaMaxima, Ataque[] ataques, int etapa) {
        super(nombre, nivel, vidaMaxima, "fuego", ataques, etapa);
    }

    @Override
    public String descripcionTipo() {
        return "Tiene ataques rapidos y ardientes.";
    }

    @Override
    public int bonoDanio() {
        return 4;
    }
}
