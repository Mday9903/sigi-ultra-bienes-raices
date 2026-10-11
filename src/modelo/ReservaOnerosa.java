package modelo;
import java.time.LocalDate;

public class ReservaOnerosa extends Reserva {
    private double montoSeña;

    public ReservaOnerosa(Inmueble inmueble, Cliente cliente, LocalDate fechaVencimiento, double montoSeña) {
        super(inmueble, cliente, fechaVencimiento); // Llama al constructor del padre
        this.montoSeña = montoSeña;
    }

    @Override
    public boolean requierePagoAdelantado() {
        return true;
    }

    @Override
    public String getTipoReserva() {
        return "Onerosa";
    }

    public double getMontoSeña() { return montoSeña; }
}