public abstract class Pokemon implements Curable {
    private String nombre;
    private int nivel;
    private int vidaMaxima;
    private int vidaActual;
    protected String tipo;
    protected int experiencia;
    protected Ataque[] ataques;
    protected int etapa;

    protected Pokemon(String nombre, int nivel, int vidaMaxima, String tipo, Ataque[] ataques, int etapa) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.tipo = tipo;
        this.experiencia = 0;
        this.ataques = ataques;
        this.etapa = etapa;
    }

    public abstract String descripcionTipo();

    public abstract int bonoDanio();

    public int atacar(Pokemon rival, int indiceAtaque) {
        Ataque ataque = ataques[indiceAtaque];
        int base = ataque.getDanio() + bonoDanio();
        double multi = TablaTipos.multiplicador(ataque.getTipo(), rival.getTipo());
        int danio = (int) Math.round(base * multi);
        rival.recibirDanio(danio);
        return danio;
    }

    public void recibirDanio(int danio) {
        vidaActual -= danio;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
    }

    public int ganarExperiencia(int puntos) {
        int subidas = 0;
        experiencia += puntos;
        while (experiencia >= 20) {
            experiencia -= 20;
            nivel++;
            vidaMaxima += 10;
            vidaActual = vidaMaxima;
            subidas++;
        }
        return subidas;
    }

    public boolean estaDebilitado() {
        return vidaActual <= 0;
    }

    public String resumen() {
        return nombre + " (" + tipo + ") lvl " + nivel + " hp " + vidaActual + "/" + vidaMaxima;
    }

    @Override
    public void curar() {
        vidaActual = vidaMaxima;
    }

    @Override
    public void curar(int puntos) {
        vidaActual += puntos;
        if (vidaActual > vidaMaxima) {
            vidaActual = vidaMaxima;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVidaMaxima(int vidaMaxima) {
        this.vidaMaxima = vidaMaxima;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaActual, vidaMaxima));
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public Ataque[] getAtaques() {
        return ataques;
    }

    public void setAtaques(Ataque[] ataques) {
        this.ataques = ataques;
    }

    public int getEtapa() {
        return etapa;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    @Override
    public String toString() {
        return resumen() + " | exp " + experiencia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pokemon)) {
            return false;
        }
        Pokemon otro = (Pokemon) obj;
        return nombre.equalsIgnoreCase(otro.nombre) && tipo.equalsIgnoreCase(otro.tipo);
    }
}
