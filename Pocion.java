public class Pocion implements Usable {
    private final int curacion;

    public Pocion() {
        this(20);
    }

    public Pocion(int curacion) {
        this.curacion = curacion;
    }

    @Override
    public String usar(Jugador jugador, Pokemon pokemon) {
        if (jugador.getPociones() <= 0) {
            return "No tienes pociones.";
        }
        jugador.setPociones(jugador.getPociones() - 1);
        pokemon.curar(curacion);
        return pokemon.getNombre() + " recupero " + curacion + " puntos de vida.";
    }
}
