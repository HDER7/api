package med.voll.api.domain.paciente;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.direccion.DatosDireccion;

public record DatosActualizarPaciente(@NotNull Long id,
                                      String nombre,
                                      String documento,
                                      DatosDireccion direccion) {

}
