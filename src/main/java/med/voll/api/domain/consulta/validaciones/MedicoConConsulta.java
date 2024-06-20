package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoConConsulta implements ValidadorDeConsulta{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosAgendarConsulta data){
        if(data.idMedico() == null){
            return;
        }
        var medicoConConsulta = repository.existsByMedicoIdAndFecha(data.idMedico(),data.fecha());
        if(medicoConConsulta){
            throw new ValidationException("No es posible programar una cita con un médico que ya tiene otra cita programada en la misma fecha/hora");
        }
    }
}
