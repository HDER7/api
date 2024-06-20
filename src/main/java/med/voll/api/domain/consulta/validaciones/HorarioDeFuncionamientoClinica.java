package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class HorarioDeFuncionamientoClinica implements ValidadorDeConsulta {

    public void validar(DatosAgendarConsulta data){

        var domingo = DayOfWeek.SUNDAY.equals(data.fecha().getDayOfWeek());
        var antesDeApertura = data.fecha().getHour()<7 ;
        var despuesDeCierre = data.fecha().getHour()>19;
        if(domingo || antesDeApertura || despuesDeCierre){
            throw new ValidationException("El horario de atención de la clínica es de lunes a sábado, de 07:00 a 19:00 horas");
        }
    }
}
