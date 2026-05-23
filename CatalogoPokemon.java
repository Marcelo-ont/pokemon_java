import java.util.Random;

public class CatalogoPokemon {
    private final String[] nombres = {
            "Bulbasaur", "Ivysaur", "Venusaur",
            "Charmander", "Charmeleon", "Charizard",
            "Squirtle", "Wartortle", "Blastoise"
    };
    private final String[] tipos = {
            "planta", "planta", "planta",
            "fuego", "fuego", "fuego",
            "agua", "agua", "agua"
    };
    private final int[] niveles = {5, 16, 32, 5, 16, 32, 5, 16, 32};
    private final int[] vidas = {90, 115, 140, 85, 110, 135, 95, 120, 145};
    private final int[] etapas = {1, 2, 3, 1, 2, 3, 1, 2, 3};
    private final Ataque[][] ataques = {
            crearAtaques(new Ataque("Placaje", "normal", 12), new Ataque("Latigo Cepa", "planta", 18), new Ataque("Hoja Afilada", "planta", 20)),
            crearAtaques(new Ataque("Placaje", "normal", 14), new Ataque("Drenadoras", "planta", 22), new Ataque("Hoja Afilada", "planta", 26)),
            crearAtaques(new Ataque("Placaje", "normal", 16), new Ataque("Rayo Solar", "planta", 34), new Ataque("Tormenta Floral", "planta", 38)),
            crearAtaques(new Ataque("Araniazo", "normal", 12), new Ataque("Ascuas", "fuego", 20), new Ataque("Colmillo Igneo", "fuego", 22)),
            crearAtaques(new Ataque("Araniazo", "normal", 14), new Ataque("Lanzallamas", "fuego", 28), new Ataque("Giro Fuego", "fuego", 30)),
            crearAtaques(new Ataque("Ala de Acero", "normal", 16), new Ataque("Infierno", "fuego", 36), new Ataque("Llama Final", "fuego", 40)),
            crearAtaques(new Ataque("Placaje", "normal", 12), new Ataque("Pistola Agua", "agua", 18), new Ataque("Burbuja", "agua", 20)),
            crearAtaques(new Ataque("Placaje", "normal", 14), new Ataque("Hidropulso", "agua", 24), new Ataque("Aqua Cola", "agua", 28)),
            crearAtaques(new Ataque("Placaje", "normal", 16), new Ataque("Hidrobomba", "agua", 36), new Ataque("Cascada", "agua", 38))
    };

    public Pokemon crearInicial(Random random) {
        int[] indices = {0, 3, 6};
        return crearPorIndice(indices[random.nextInt(indices.length)]);
    }

    public Pokemon crearSalvaje(Random random) {
        return crearPorIndice(random.nextInt(nombres.length));
    }

    public Pokemon crearPorNombre(String nombre) {
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equalsIgnoreCase(nombre)) {
                return crearPorIndice(i);
            }
        }
        return crearPorIndice(0);
    }

    public void darExperiencia(Pokemon[] usados, int total, Random random) {
        Pokemon[] unicos = new Pokemon[total];
        int cantidad = 0;
        for (int i = 0; i < total; i++) {
            if (!contiene(unicos, cantidad, usados[i])) {
                unicos[cantidad] = usados[i];
                cantidad++;
            }
        }

        for (int i = 0; i < cantidad; i++) {
            Pokemon pokemon = unicos[i];
            int exp = 5 + random.nextInt(6);
            System.out.println(pokemon.getNombre() + " gano " + exp + " puntos de experiencia.");
            int subidas = pokemon.ganarExperiencia(exp);
            for (int j = 0; j < subidas; j++) {
                System.out.println(pokemon.getNombre() + " subio a nivel " + pokemon.getNivel() + ".");
            }
            String mensaje = evolucionarSiPuede(pokemon);
            if (!mensaje.isEmpty()) {
                System.out.println(mensaje);
            }
        }
    }

    public String evolucionarSiPuede(Pokemon pokemon) {
        int indice = indiceDe(pokemon.getNombre());
        if (indice < 0 || indice % 3 == 2) {
            return "";
        }
        boolean listoParaEvolucionar = pokemon.getEtapa() == 1 && pokemon.getNivel() >= 16
                || pokemon.getEtapa() == 2 && pokemon.getNivel() >= 32;
        if (!listoParaEvolucionar) {
            return "";
        }

        int nivel = pokemon.getNivel();
        int exp = pokemon.getExperiencia();
        String anterior = pokemon.getNombre();
        Pokemon evolucion = crearPorIndice(indice + 1);

        pokemon.setNombre(evolucion.getNombre());
        pokemon.setTipo(evolucion.getTipo());
        pokemon.setVidaMaxima(evolucion.getVidaMaxima());
        pokemon.setAtaques(evolucion.getAtaques());
        pokemon.setEtapa(evolucion.getEtapa());
        pokemon.setNivel(nivel);
        pokemon.setExperiencia(exp);
        pokemon.curar();
        return anterior + " evoluciono a " + pokemon.getNombre() + ".";
    }

    private int indiceDe(String nombre) {
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private boolean contiene(Pokemon[] arreglo, int total, Pokemon pokemon) {
        for (int i = 0; i < total; i++) {
            if (arreglo[i] == pokemon) {
                return true;
            }
        }
        return false;
    }

    private Pokemon crearPorIndice(int indice) {
        Ataque[] copia = copiarAtaques(ataques[indice]);
        if ("planta".equals(tipos[indice])) {
            return new PokemonPlanta(nombres[indice], niveles[indice], vidas[indice], copia, etapas[indice]);
        }
        if ("fuego".equals(tipos[indice])) {
            return new PokemonFuego(nombres[indice], niveles[indice], vidas[indice], copia, etapas[indice]);
        }
        return new PokemonAgua(nombres[indice], niveles[indice], vidas[indice], copia, etapas[indice]);
    }

    private Ataque[] crearAtaques(Ataque a1, Ataque a2, Ataque a3) {
        return new Ataque[]{a1, a2, a3};
    }

    private Ataque[] copiarAtaques(Ataque[] base) {
        Ataque[] copia = new Ataque[base.length];
        for (int i = 0; i < base.length; i++) {
            copia[i] = new Ataque(base[i].getNombre(), base[i].getTipo(), base[i].getDanio());
        }
        return copia;
    }
}
