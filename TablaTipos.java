public class TablaTipos {
    private TablaTipos() {
    }

    public static double multiplicador(String ataque, String defensa) {
        if ("fuego".equals(ataque) && "planta".equals(defensa)) {
            return 2.0;
        }
        if ("agua".equals(ataque) && "fuego".equals(defensa)) {
            return 2.0;
        }
        if ("planta".equals(ataque) && "agua".equals(defensa)) {
            return 2.0;
        }
        if ("fuego".equals(ataque) && "agua".equals(defensa)) {
            return 0.5;
        }
        if ("agua".equals(ataque) && "planta".equals(defensa)) {
            return 0.5;
        }
        if ("planta".equals(ataque) && "fuego".equals(defensa)) {
            return 0.5;
        }
        if (ataque.equals(defensa)) {
            return 0.5;
        }
        return 1.0;
    }
}
