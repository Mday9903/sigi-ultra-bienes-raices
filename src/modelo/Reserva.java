package modelo;
import java.time.LocalDate;

public abstract class Reserva {
    protected Inmueble inmueble;  // protected para que las subclases tengan acceso
    protected Cliente cliente;
    protected LocalDate fecha;
    protected LocalDate fechaVencimiento;
    protected String estado; // "Vigente", "Formalizada", "Vencida"

    public Reserva(Inmueble inmueble, Cliente cliente, LocalDate fechaVencimiento) {
        this.inmueble = inmueble;
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.fechaVencimiento = fechaVencimiento;
        this.estado = "Vigente";
    }

    // MÉTODOS ABSTRACTOS (POLIMORFISMO): Cada subclase lo implementará a su manera
    public abstract boolean requierePagoAdelantado();
    public abstract String getTipoReserva();

    // Lógica reutilizable del CU09 y CU10
    public boolean estaVigente() {
        return this.estado.equals("Vigente") &&
                (LocalDate.now().isBefore(fechaVencimiento) || LocalDate.now().isEqual(fechaVencimiento));
    }

    public void marcarFormalizada() {
        this.estado = "Formalizada";
    }

    // Getters base
    public Inmueble getInmueble() { return inmueble; }
    public Cliente getCliente() { return cliente; }
    public String getEstado() { return estado; }

    public String toString() { return "Reserva de " + cliente.getNombreCompleto() + " - " + inmueble.getDireccion(); }
}