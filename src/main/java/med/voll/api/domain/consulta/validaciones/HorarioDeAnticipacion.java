package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class HorarioDeAnticipacion implements ValidadorDeConsulta{

    public void validar(DatosAgendarConsulta data){
        var ahora = LocalDateTime.now();
        var horaDeconsulta = data.fecha();

        var diferencia30Min = Duration.between(ahora, horaDeconsulta).toMinutes()<30;

        if (diferencia30Min){
            throw new ValidationException("Las consultas deben programarse con al menos 30 minutos de anticipación");
        }
    }
}
