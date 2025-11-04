package modelo;

public class Usuario {
    private String nombre;
    private int edad;
    private String correo;
    private String telefono;

    public Usuario(String nombre, int edad, String correo, String telefono) {
        this.nombre = nombre;
        this.edad = edad;
        this.correo = correo;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() {
        return "\n--- Datos del Usuario ---" +
                "\nNombre: " + nombre +
                "\nEdad: " + edad +
                "\nCorreo: " + correo +
                "\nTeléfono: " + telefono;
    }
}
