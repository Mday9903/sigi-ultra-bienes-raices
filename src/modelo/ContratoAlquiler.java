package modelo;
import java.time.LocalDate;

public class ContratoAlquiler extends Contrato {
    public ContratoAlquiler(Reserva reserva, LocalDate fechaInicio, LocalDate fechaFin, double montoMensual) {
        super(reserva, fechaInicio, fechaFin, montoMensual);
    }

    @Override
    public String getTipoContrato() {
        return "Alquiler";
    }
}