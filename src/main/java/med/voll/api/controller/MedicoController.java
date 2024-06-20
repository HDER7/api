package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaPaciente> registrarMedico(@RequestBody @Valid DatosRegistroMedico param, UriComponentsBuilder uriBuilder){
        Medico medico =medicoRepository.save(new Medico(param));
        DatosRespuestaPaciente respuestaMedico = new DatosRespuestaPaciente(medico.getId(),medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        URI url = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri(); ;
        return ResponseEntity.created(url).body(respuestaMedico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 10, sort = "nombre") Pageable paginacion) {
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaPaciente> actualizarMedico(@RequestBody @Valid DatosActualizarMedico actualizarMedico){
        Medico medico = medicoRepository.getReferenceById(actualizarMedico.id());
        medico.actualizarDatos(actualizarMedico);
        return ResponseEntity.ok(new DatosRespuestaPaciente(medico.getId(),medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),medico.getDireccion().getComplemento())));
    }

    @DeleteMapping("/{id}")
    @Transactional
    //DELETE logico
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.seFue();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaPaciente> retornaDatosMedico(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        var data = new DatosRespuestaPaciente(medico.getId(),medico.getNombre(),
                medico.getEmail(), medico.getTelefono(), medico.getDocumento(),
                new DatosDireccion(medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(), medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),medico.getDireccion().getComplemento()));
        return ResponseEntity.ok(data);
    }
}
