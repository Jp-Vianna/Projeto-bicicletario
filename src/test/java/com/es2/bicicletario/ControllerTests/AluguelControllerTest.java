package com.es2.bicicletario.ControllerTests;

import com.es2.bicicletario.controller.AluguelController;
import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Status;
import com.es2.bicicletario.service.AluguelService;
import com.es2.bicicletario.validation.RegraDeNegocioException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AluguelController.class)
class AluguelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AluguelService aluguelService;

    private CiclistaRequestDTO ciclistaRequestDTO;
    private CiclistaResponseDTO ciclistaResponseDTO;

    @BeforeEach
    void setUp() {
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista("Jorge Peixoto");
        ciclistaRequestDTO.setEmail("novo.ciclista@email.com");
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf("14998043777");
        ciclistaRequestDTO.setDataNascimento(LocalDate.of(2000, 1, 1));
        ciclistaRequestDTO.setSenha("senhaForte123");
        ciclistaRequestDTO.setConfirmacaoSenha("senhaForte123");
        
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular("Jorge P");
        cartaoDto.setNumero("1111222233334444");
        cartaoDto.setValidade(YearMonth.now().plusYears(1));
        cartaoDto.setCvv("123");
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);

        ciclistaResponseDTO = new CiclistaResponseDTO();
        ciclistaResponseDTO.setId(1);
        ciclistaResponseDTO.setNome("Jorge Peixoto");
        ciclistaResponseDTO.setEmail("novo.ciclista@email.com");
        ciclistaResponseDTO.setStatus(Status.AGUARDANDO_ATIVAMENTO);
        ciclistaResponseDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaResponseDTO.setCpf("14998043777");
        ciclistaResponseDTO.setNomeTitularCartao("Jorge P");
        ciclistaResponseDTO.setNumeroCartaoMascarado("************4444");
    }
    
    @Test
    void criarCiclista_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {

        given(aluguelService.criarCiclista(any(CiclistaRequestDTO.class))).willReturn(ciclistaResponseDTO);

        mockMvc.perform(post("/api/ciclistas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ciclistaRequestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value("Jorge Peixoto"))
        .andExpect(jsonPath("$.email").value("novo.ciclista@email.com"));
    }

    @Test
    void getCiclistaById_QuandoCiclistaExiste_DeveRetornarStatus200Ok() throws Exception {
        final Integer ciclistaId = 1;
        given(aluguelService.getCiclistaById(ciclistaId)).willReturn(Optional.of(ciclistaResponseDTO));
        mockMvc.perform(get("/api/ciclistas/{id}", ciclistaId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ciclistaId))
        .andExpect(jsonPath("$.status").value(Status.AGUARDANDO_ATIVAMENTO.toString()));
    }

    @Test
    void realizarAluguel_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {
        AluguelRequestDTO aluguelRequest = new AluguelRequestDTO();
        aluguelRequest.setIdCiclista(1);
        aluguelRequest.setIdTranca(101);

        AluguelResponseDTO aluguelResponse = new AluguelResponseDTO();
        aluguelResponse.setId(1);
        aluguelResponse.setIdCiclista(1);
        aluguelResponse.setStatus(Status.EM_ANDAMENTO);
        aluguelResponse.setHoraInicio(LocalDateTime.now());

        given(aluguelService.realizarAluguel(any(AluguelRequestDTO.class))).willReturn(aluguelResponse);

        mockMvc.perform(post("/api/alugueis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aluguelRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idCiclista").value(1))
        .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));
    }

    @Test
    void realizarAluguel_QuandoCiclistaNaoPodeAlugar_DeveRetornarStatus400BadRequest() throws Exception {

        AluguelRequestDTO aluguelRequest = new AluguelRequestDTO();
        aluguelRequest.setIdCiclista(2);
        aluguelRequest.setIdTranca(102);

        given(aluguelService.realizarAluguel(any(AluguelRequestDTO.class)))
            .willThrow(new RegraDeNegocioException("O aluguel não foi autorizado."));

        mockMvc.perform(post("/api/alugueis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aluguelRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("O aluguel não foi autorizado."));
    }

     @Test
    void realizarDevolucao_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {

        DevolucaoRequestDTO devolucaoRequest = new DevolucaoRequestDTO();
        devolucaoRequest.setIdBicicleta(101);
        devolucaoRequest.setIdTranca(202);

        DevolucaoResponseDTO devolucaoResponse = new DevolucaoResponseDTO();
        devolucaoResponse.setId(1);
        devolucaoResponse.setTrancaFinal(202);
        devolucaoResponse.setCobranca(new BigDecimal("15.50"));
        devolucaoResponse.setIdAluguel(50);
        
        given(aluguelService.realizarDevolucao(any(DevolucaoRequestDTO.class))).willReturn(devolucaoResponse);

        mockMvc.perform(post("/api/devolucoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(devolucaoRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idAluguel").value(50))
        .andExpect(jsonPath("$.trancaFinal").value(202));
    }

    @Test
    void realizarDevolucao_QuandoBicicletaNaoTemAluguelAtivo_DeveRetornarStatus404NotFound() throws Exception {
 
        DevolucaoRequestDTO devolucaoRequest = new DevolucaoRequestDTO();
        devolucaoRequest.setIdBicicleta(999);
        devolucaoRequest.setIdTranca(203);

        String mensagemErro = "Não encontrou alugueis ativos com essa bicicleta.";
        given(aluguelService.realizarDevolucao(any(DevolucaoRequestDTO.class)))
            .willThrow(new RuntimeException(mensagemErro));

        mockMvc.perform(post("/api/devolucoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(devolucaoRequest)))
        .andExpect(status().isNotFound())
        .andExpect(content().string(mensagemErro));
    }
}
