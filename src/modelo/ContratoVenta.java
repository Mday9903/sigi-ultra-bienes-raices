package modelo;
import java.time.LocalDate;

public class ContratoVenta extends Contrato {
    public ContratoVenta(Reserva reserva, LocalDate fechaInicio, double montoTotal) {
        // En una venta, la fecha fin de contrato no aplica como en un alquiler
        super(reserva, fechaInicio, null, montoTotal);
    }

    @Override
    public String getTipoContrato() {
        return "Venta";
    }
}