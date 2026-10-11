package modelo;

public class Inmueble {
    private int idInmueble;
    private String direccion;
    private String estado; // "Disponible", "Reservado", "Alquilado", "Vendido"

    public Inmueble(int idInmueble, String direccion) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.estado = "Disponible";
    }

    // Getters y Setters
    public int getIdInmueble() { return idInmueble; }
    public String getDireccion() { return direccion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toString() { return direccion; }
}