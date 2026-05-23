public class PokemonAgua extends Pokemon {
    public PokemonAgua(String nombre, int nivel, int vidaMaxima, Ataque[] ataques, int etapa) {
        super(nombre, nivel, vidaMaxima, "agua", ataques, etapa);
    }

    @Override
    public String descripcionTipo() {
        return "Resiste bien y ataca con agua.";
    }

    @Override
    public int bonoDanio() {
        return 2;
    }
}
