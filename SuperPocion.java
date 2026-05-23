public class SuperPocion implements Usable {
    @Override
    public String usar(Jugador jugador, Pokemon pokemon) {
        if (jugador.getSuperPociones() <= 0) {
            return "No tienes super pociones.";
        }
        jugador.setSuperPociones(jugador.getSuperPociones() - 1);
        pokemon.curar();
        return pokemon.getNombre() + " recupero toda su vida.";
    }
}
