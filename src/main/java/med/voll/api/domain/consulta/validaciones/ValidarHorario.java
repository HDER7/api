package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosCancelamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidarHorario implements  ValidadorCancelamiento{

    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DatosCancelamiento data) {
        var consulta = repository.getReferenceById(data.id());
        var ahora = LocalDateTime.now();
        var diferncia = Duration.between(ahora, consulta.getFecha()).toHours();

        if (diferncia < 24){
            throw new ValidationException("La consulta solo puede ser cancelada 24 horas antes");
        }
    }
}
