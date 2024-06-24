package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DatosAgendarConsulta> agendarConsultaJacksonTester;

    @Autowired
    private JacksonTester<DatosDetalleConsulta> detalleConsultaJacksonTester;

    @MockBean
    private AgendaDeConsultaService agendaDeConsultaService;

    @Test
    @DisplayName("Deberia retornar estado 400 cuando los datos sean invalidos")
    @WithMockUser
    void agendarEscenario1() throws Exception{
        //given //when
       var res =  mockMvc.perform(post("/consultas")).andReturn().getResponse();

       //then
       assertEquals(HttpStatus.BAD_REQUEST.value(),res.getStatus());
    }

    @Test
    @DisplayName("Deberia retornar estado 200 cuando los datos sean validos")
    @WithMockUser
    void agendarEscenario2() throws Exception{
        //given
        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.CARDIOLOGIA;
        var data = new DatosDetalleConsulta(null,5L,4L, fecha);

        // when

        when(agendaDeConsultaService.agendar(any())).thenReturn(data);
        var res =  mockMvc.perform(post("/consultas")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(agendarConsultaJacksonTester.write(new DatosAgendarConsulta(null,5L,4L,fecha, especialidad)).getJson()))
                .andReturn().getResponse();

        //then
        assertEquals(HttpStatus.OK.value(),res.getStatus());

        var esperado =  detalleConsultaJacksonTester.write(data).getJson();

        assertEquals(esperado,res.getContentAsString());
    }


}