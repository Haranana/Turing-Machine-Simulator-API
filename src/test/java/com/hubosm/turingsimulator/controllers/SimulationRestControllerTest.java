package com.hubosm.turingsimulator.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubosm.turingsimulator.domain.TuringMachine;
import com.hubosm.turingsimulator.dtos.CreateTuringMachineDto;
import com.hubosm.turingsimulator.dtos.SimulationCreatedDto;
import com.hubosm.turingsimulator.dtos.SimulationStatusDto;
import com.hubosm.turingsimulator.dtos.SimulationStepDto;
import com.hubosm.turingsimulator.services.SimulationService;
import com.hubosm.turingsimulator.services.SimulationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.ErrorResponse;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SimulationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private SimulationServiceImpl simulationService;
    private JacksonTester<CreateTuringMachineDto>createTmDtoJacksonTester;
    private JacksonTester<SimulationStatusDto>simulationStatusDtoJacksonTester;
    private JacksonTester<SimulationStepDto>simulationStepDtoJacksonTester;
    private JacksonTester<ErrorResponse> errorResponseJacksonTester;

    @BeforeEach
    void setup(){
        JacksonTester.initFields(this, mapper);
    }
    CreateTuringMachineDto getFirstAndLastCharactersEqualityTmDto(String input){

        final List<String> program = List.of(
                "qStart,0,0Right,0,R",
                "qStart,1,1Right,1,R",
                "qStart,_,qAcc,_,S",
                "0Right,0,0Right,0,R",
                "0Right,1,0Right,1,R",
                "0Right,_,0Left,_,R",
                "0Left,0,qAcc,0,S",
                "0Left,1,qRej,1,S",
                "0Left,_,qAcc,_,S",
                "1Right,0,1Right,0,R",
                "1Right,1,1Right,1,R",
                "1Right,_,1Left,1,L",
                "1Left,0,qRej,0,S",
                "1Left,1,qAcc,1,S",
                "1Left,_,qAcc,_,S"
        );
        final char sep = ',';
        final String initialState = "qStart";
        final String acceptState = "qAcc";
        final String rejectState = "qRej";

        return new CreateTuringMachineDto(initialState, acceptState, rejectState, program, sep, input);
    }

    @Test
    @DisplayName("simulate() should return proper response when called with proper input")
    void shouldReturnSimulationCreatedDtoWhenCalled() throws Exception {

        CreateTuringMachineDto createDto = getFirstAndLastCharactersEqualityTmDto("101");
        UUID jobId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        SimulationCreatedDto returnDto = new SimulationCreatedDto(jobId);
        given(simulationService.queueSimulation(any(CreateTuringMachineDto.class))).willReturn(jobId);

        String body = createTmDtoJacksonTester.write(createDto).getJson();
        mockMvc.perform(post("/api/simulations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)).andExpect(status().isAccepted()).andExpect(jsonPath("$.jobId").value(jobId.toString()))
                .andExpect(header().string("Location", "/api/simulations/"+jobId.toString()));
    }

    /*
    @Test
    @DisplayName("getStatus() should return proper response")
    void shouldReturnSimulationStatusDtoWhenCalled() throws Exception{
        SimulationStatusDto simulationStatusDto = new SimulationStatusDto("DONE");
        UUID jobId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        mockMvc.perform(get("/api/simulations/"+jobId.toString()).contentType(MediaType.APPLICATION_JSON)
                .content())
    }*/
}
