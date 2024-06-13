package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public void registrarPaciente(@RequestBody @Valid DatosRegistroPaciente param){
        pacienteRepository.save(new Paciente(param));
    }

    @GetMapping
    public Page<DatosListadoPaciente> listadoPacientes(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
        return pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new);
    }

    @PutMapping
    @Transactional
    public void actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente actualizarPaciente){
        Paciente paciente = pacienteRepository.getReferenceById(actualizarPaciente.id());
        paciente.actualizarDatos(actualizarPaciente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.delete();
    }

}
