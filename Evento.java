public class Evento {
    private int turno;
    private String texto;

    public Evento(int turno, String texto) {
        this.turno = turno;
        this.texto = texto;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return turno + ". " + texto;
    }
}
