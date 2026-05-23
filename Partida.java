public class Partida {
    private int numero;
    private Jugador[] jugadores;
    private int[] puntos;
    private int[] victorias;
    private int[] derrotas;
    private int[] capturas;

    public Partida(int numero, Jugador[] jugadores) {
        this.numero = numero;
        this.jugadores = jugadores;
        this.puntos = new int[jugadores.length];
        this.victorias = new int[jugadores.length];
        this.derrotas = new int[jugadores.length];
        this.capturas = new int[jugadores.length];
    }

    public void registrarVictoria(Jugador jugador, int puntosGanados) {
        int i = buscarIndice(jugador);
        if (i < 0) {
            return;
        }
        puntos[i] += puntosGanados;
        victorias[i]++;
        jugador.sumarPuntos(puntosGanados);
        jugador.registrarVictoria();
    }

    public void registrarDerrota(Jugador jugador) {
        int i = buscarIndice(jugador);
        if (i < 0) {
            return;
        }
        derrotas[i]++;
        jugador.registrarDerrota();
    }

    public void registrarCaptura(Jugador jugador) {
        int i = buscarIndice(jugador);
        if (i < 0) {
            return;
        }
        capturas[i]++;
        puntos[i] += 5;
        jugador.sumarPuntos(5);
    }

    public void cerrar() {
        for (Jugador jugador : jugadores) {
            jugador.registrarPartida();
        }
    }

    public void mostrarResumen() {
        System.out.println("\nPartida " + numero + ":");
        for (int i = 0; i < jugadores.length; i++) {
            System.out.println(jugadores[i].getNombre()
                    + " | puntos " + puntos[i]
                    + " | victorias " + victorias[i]
                    + " | derrotas " + derrotas[i]
                    + " | capturas " + capturas[i]);
        }
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public int[] getPuntos() {
        return puntos;
    }

    private int buscarIndice(Jugador jugador) {
        for (int i = 0; i < jugadores.length; i++) {
            if (jugadores[i] == jugador) {
                return i;
            }
        }
        return -1;
    }
}
