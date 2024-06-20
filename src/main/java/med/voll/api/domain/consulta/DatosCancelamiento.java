package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DatosCancelamiento(@NotNull
                                 Long id,
                                 @NotNull
                                 MotivoCancelamiento motivo) {
}
