package modelo;

public class Medicamento {
    private String nombre;
    private String dosis;
    private String frecuencia;

    public Medicamento(String nombre, String dosis, String frecuencia) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
    }

    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return "Medicamento: " + nombre + " | Dosis: " + dosis + " | Frecuencia: " + frecuencia;
    }
}
