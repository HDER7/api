package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.AutoConfigureDataCassandra;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager en;

    @Test
    @DisplayName("deberia retornar nulo cuando el medico se encuentre en consulta en ese horario")
    void seleccionarMedicoEspecialidadFechaEscenario1() {
        //given
        var proximoLunes10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico=registrarMedico("Jose","jose@mail.com","12348",Especialidad.CARDIOLOGIA);
        var paciente = registrarPaciente("Antonio","antonio@mail.com","12398"); ;
        registrarConsulta(medico,paciente, proximoLunes10);

        //when
        var medicoLibre = medicoRepository.seleccionarMedicoEspecialidadFecha(Especialidad.CARDIOLOGIA,proximoLunes10);

        //then
        assertNull(medicoLibre);
    }

    @Test
    @DisplayName("deberia retornar un medico cuando realice la consulta en la base de datos para ese horario")
    void seleccionarMedicoEspecialidadFechaEscenario2() {
        //given
        var proximoLunes10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);
        var medico=registrarMedico("Jose","jose@mail.com","12348",Especialidad.CARDIOLOGIA);

        //when
        var medicoLibre = medicoRepository.seleccionarMedicoEspecialidadFecha(Especialidad.CARDIOLOGIA,proximoLunes10);

        //then
        assertEquals(medicoLibre,medico);
    }

    private DatosRegistroMedico datosMedico(String nombre,String email, String documento, Especialidad especialidad){
        return new DatosRegistroMedico(
                nombre,
                email,
                "11626326226",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime fecha){
        en.persist(new Consulta(null,medico,paciente,fecha,null));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre,email,documento,especialidad));
        en.persist(medico);
        return medico;
    }

    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre,email,documento));
        en.persist(paciente);
        return paciente;
    }

    private DatosRegistroPaciente datosPaciente(String nombre,String email, String documento){
        return new DatosRegistroPaciente(
                nombre,
                email,
                "9441516131310",
                documento,
                datosDireccion()
        );
    }

    private DatosDireccion datosDireccion(){
        return new DatosDireccion(
                "local",
                "azul",
                "Acapulco",
                "321",
                "12"
        );
    }

    @Test
    void findActivoById() {
    }
}