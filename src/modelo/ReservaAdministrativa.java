package modelo;
import java.time.LocalDate;

public class ReservaAdministrativa extends Reserva {

    public ReservaAdministrativa(Inmueble inmueble, Cliente cliente, LocalDate fechaVencimiento) {
        super(inmueble, cliente, fechaVencimiento);
    }

    @Override
    public boolean requierePagoAdelantado() {
        return false; // Las administrativas no requieren dinero
    }

    @Override
    public String getTipoReserva() {
        return "Administrativa";
    }
}