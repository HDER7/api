package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelamiento;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/consultas")
@SecurityRequirement(name = "bearer-key")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultaService service;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleConsulta> agendar(@RequestBody @Valid DatosAgendarConsulta data){
        var response = service.agendar(data);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> eliminar(@RequestBody @Valid DatosCancelamiento data){
        service.cancelar(data);
        return ResponseEntity.noContent().build();
    }
}
