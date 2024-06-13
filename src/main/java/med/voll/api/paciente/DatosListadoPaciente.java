package med.voll.api.paciente;

public record DatosListadoPaciente(Long id,
                                   String nombre,
                                   String documento,
                                   String email) {
    public DatosListadoPaciente(Paciente paciente) {
        this(paciente.getId(), paciente.getNombre(), paciente.getDocumento(), paciente.getEmail());
    }
}
