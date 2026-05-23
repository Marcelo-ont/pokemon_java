public class PokemonPlanta extends Pokemon {
    public PokemonPlanta(String nombre, int nivel, int vidaMaxima, Ataque[] ataques, int etapa) {
        super(nombre, nivel, vidaMaxima, "planta", ataques, etapa);
    }

    @Override
    public String descripcionTipo() {
        return "Usa ataques de hojas y enredaderas.";
    }

    @Override
    public int bonoDanio() {
        return 3;
    }
}
