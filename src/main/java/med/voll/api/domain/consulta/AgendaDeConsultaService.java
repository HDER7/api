package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.HorarioDeAnticipacion;
import med.voll.api.domain.consulta.validaciones.ValidadorCancelamiento;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errors.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    List<ValidadorDeConsulta> validadores;
    @Autowired
    private List<ValidadorCancelamiento> cancelamientos;

    public DatosDetalleConsulta agendar(DatosAgendarConsulta data){

        if(pacienteRepository.findById(data.idPaciente()).isEmpty()){
            throw new ValidacionDeIntegridad("Este id para el paciente no fue encontrado");
        }
        if(data.idMedico() != null && !medicoRepository.existsById(data.idMedico())){
            throw new ValidacionDeIntegridad("Este id para el medico no fue encontrado");
        }

        validadores.forEach(v->v.validar(data));

        var paciente = pacienteRepository.findById(data.idPaciente()).get();
        var medico = seleccionarMedico(data);
        if (medico == null){
            throw new ValidacionDeIntegridad("No hay medicos diponibles para este horario y especialidad");
        }
        var  consulta =new Consulta(medico, paciente,data.fecha());

        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }

    public void cancelar(DatosCancelamiento data){
        if (!consultaRepository.existsById(data.id())){
            throw new ValidacionDeIntegridad("Id de consulta no existe");
        }

        cancelamientos.forEach(v->v.validar(data));

        var consulta =consultaRepository.getReferenceById(data.id());
        consulta.cancelar(data.motivo());

    }

    private Medico seleccionarMedico(DatosAgendarConsulta data) {
        if(data.idMedico() != null){
            return medicoRepository.getReferenceById(data.idMedico());
        }
        if(data.especialidad()==null){
            throw new ValidacionDeIntegridad("Debe seleccionar una especialidad");
        }
        return medicoRepository.seleccionarMedicoEspecialidadFecha(data.especialidad(),data.fecha());
    }
}
