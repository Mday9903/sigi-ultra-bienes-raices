package modelo;

public class Cliente {
    private int idCliente;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;

    public Cliente(String dni, String nombre, String apellido) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Constructor sobrecargado: se usa al recuperar clientes desde la BD para cargarlos en una lista que luego se ordena y se recorre con búsqueda binaria.
    public Cliente(int idCliente, String dni, String nombre, String apellido, String telefono) {
        this(dni, nombre, apellido); // Reutiliza el constructor anterior
        this.idCliente = idCliente;
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }

    public String toString() { return getNombreCompleto(); }

}
