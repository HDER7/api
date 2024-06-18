package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.DatosRespuestaPaciente;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity<med.voll.api.domain.paciente.DatosRespuestaPaciente> registrarPaciente(@RequestBody @Valid DatosRegistroPaciente param, UriComponentsBuilder uriBuilder){
        Paciente paciente =pacienteRepository.save(new Paciente(param));
        med.voll.api.domain.paciente.DatosRespuestaPaciente respuestaPaciente = new med.voll.api.domain.paciente.DatosRespuestaPaciente(paciente.getId(),paciente.getNombre(),
                paciente.getEmail(), paciente.getTelefono(), paciente.getDocumento(),
                new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(),paciente.getDireccion().getComplemento()));
        URI url  = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(url).body(respuestaPaciente);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoPaciente>> listadoPacientes(@PageableDefault(sort = "nombre") Pageable paginacion) {
        return ResponseEntity.ok(pacienteRepository.findByActivoTrue(paginacion).map(DatosListadoPaciente::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<med.voll.api.domain.paciente.DatosRespuestaPaciente> actualizarPaciente(@RequestBody @Valid DatosActualizarPaciente actualizarPaciente){
        Paciente paciente = pacienteRepository.getReferenceById(actualizarPaciente.id());
        paciente.actualizarDatos(actualizarPaciente);
        return ResponseEntity.ok(new med.voll.api.domain.paciente.DatosRespuestaPaciente(paciente.getId(),paciente.getNombre(),
                paciente.getEmail(), paciente.getTelefono(), paciente.getDocumento(),
                new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(),paciente.getDireccion().getComplemento())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        paciente.delete();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosMedico(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.getReferenceById(id);
        var data = new DatosRespuestaPaciente(paciente.getId(),paciente.getNombre(),
                paciente.getEmail(), paciente.getTelefono(), paciente.getDocumento(),
                new DatosDireccion(paciente.getDireccion().getCalle(),
                        paciente.getDireccion().getDistrito(), paciente.getDireccion().getCiudad(),
                        paciente.getDireccion().getNumero(),paciente.getDireccion().getComplemento()));
        return ResponseEntity.ok(data);
    }

}
