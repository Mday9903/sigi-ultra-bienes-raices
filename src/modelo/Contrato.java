package modelo;
import java.time.LocalDate;

public abstract class Contrato {
    protected Reserva reserva;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;
    protected double monto;
    protected String estado;

    public Contrato(Reserva reserva, LocalDate fechaInicio, LocalDate fechaFin, double monto) {
        this.reserva = reserva;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
        this.estado = "Vigente";
    }

    // Polimorfismo
    public abstract String getTipoContrato();

    // LÓGICA CU11: Verifica si el contrato vence en X cantidad de días
    public boolean venceEnProximosDias(int dias) {
        if (fechaFin == null || !estado.equals("Vigente")) {
            return false;
        }
        LocalDate limite = LocalDate.now().plusDays(dias);
        // Vence hoy o en el futuro, pero ANTES o IGUAL a la fecha límite calculada
        return !fechaFin.isBefore(LocalDate.now()) &&
                (fechaFin.isBefore(limite) || fechaFin.isEqual(limite));
    }

    // Getters
    public Reserva getReserva() { return reserva; }
    public LocalDate getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
}