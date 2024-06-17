package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String documento;
    private String telefono;
    private boolean activo;
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    @Embedded
    private Direccion direccion;


    public Medico(DatosRegistroMedico param) {
        this.activo = true;
        this.nombre = param.nombre();
        this.email = param.email();
        this.telefono = param.telefono();
        this.documento = param.documento();
        this.especialidad = param.especialidad();
        this.direccion = new Direccion(param.direccion());

    }

    public void actualizarDatos(DatosActualizarMedico actualizarMedico) {
        if(actualizarMedico.nombre() != null){
            this.nombre = actualizarMedico.nombre();
        }
        if (actualizarMedico.documento() != null){
            this.documento = actualizarMedico.documento();
        }
        if (actualizarMedico.direccion() != null){
            this.direccion = direccion.actualizarDireccion(actualizarMedico.direccion());
        }
    }

    public void seFue() {
        this.activo = false;
    }
}
