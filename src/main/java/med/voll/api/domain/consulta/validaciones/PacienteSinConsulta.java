package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacienteSinConsulta implements ValidadorDeConsulta{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosAgendarConsulta data){
        var primerHorario = data.fecha().withHour(7);
        var ultimoHorario = data.fecha().withHour(18);

        var pacienteConsulta = repository.existsByPacienteIdAndFechaBetween(data.idPaciente(),primerHorario,ultimoHorario);

        if (pacienteConsulta){
            throw new ValidationException("No es posible programar más de una consulta en el mismo día para el mismo paciente");
        }
    }
}
