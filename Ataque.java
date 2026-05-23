public class Ataque {
    private String nombre;
    private String tipo;
    private int danio;

    public Ataque(String nombre, String tipo, int danio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.danio = danio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDanio() {
        return danio;
    }

    public void setDanio(int danio) {
        this.danio = danio;
    }

    @Override
    public String toString() {
        return nombre + " | " + tipo + " | danio " + danio;
    }
}
