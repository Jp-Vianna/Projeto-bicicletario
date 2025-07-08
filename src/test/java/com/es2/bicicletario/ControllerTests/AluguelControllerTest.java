package com.es2.bicicletario.ControllerTests;

import com.es2.bicicletario.controller.AluguelController;
import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
import com.es2.bicicletario.dto.FuncionarioRequestDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.Cpf;
import com.es2.bicicletario.entity.Email;
import com.es2.bicicletario.entity.Funcionario;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Status;
import com.es2.bicicletario.service.AluguelService;
import com.es2.bicicletario.validation.RegraDeNegocioException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.eq; 

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    final String nomeTeste = "Jorge Peixoto";
    final String emailTeste = "nome.teste@xyzw.com";

    @BeforeEach
    void setUp() {
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista(nomeTeste);
        ciclistaRequestDTO.setEmail(emailTeste);
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf("14998043777");
        ciclistaRequestDTO.setDataNascimento(LocalDate.of(2000, 1, 1));
        ciclistaRequestDTO.setSenha("senhaForte123");
        ciclistaRequestDTO.setConfirmacaoSenha("senhaForte123");
        
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular(nomeTeste);
        cartaoDto.setNumero("1111222233334444");
        cartaoDto.setValidade(YearMonth.now().plusYears(1));
        cartaoDto.setCvv("123");
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);

        ciclistaResponseDTO = new CiclistaResponseDTO();
        ciclistaResponseDTO.setId(1);
        ciclistaResponseDTO.setNome(nomeTeste);
        ciclistaResponseDTO.setEmail(emailTeste);
        ciclistaResponseDTO.setStatus(Status.AGUARDANDO_ATIVAMENTO);
        ciclistaResponseDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaResponseDTO.setCpf("14998043777");
        ciclistaResponseDTO.setNomeTitularCartao(nomeTeste);
        ciclistaResponseDTO.setNumeroCartaoMascarado("************4444");
    }
    
    @Test
    void criarCiclista_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {

        given(aluguelService.criarCiclista(any(CiclistaRequestDTO.class))).willReturn(ciclistaResponseDTO);

        mockMvc.perform(post("/api/ciclistas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ciclistaRequestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nome").value(nomeTeste))
        .andExpect(jsonPath("$.email").value(emailTeste));
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

    @Nested
    class AtualizarCiclistaControllerTest {

        private CiclistaRequestDTO ciclistaUpdateDTO;

        @BeforeEach
        void setUp() {
            ciclistaUpdateDTO = new CiclistaRequestDTO();
            ciclistaUpdateDTO.setNomeCiclista("Nome Atualizado");
            ciclistaUpdateDTO.setEmail("novo.email@teste.com");
            ciclistaUpdateDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
            ciclistaUpdateDTO.setCpf("12345678901");
            ciclistaUpdateDTO.setDataNascimento(LocalDate.of(1999, 1, 1));
            ciclistaUpdateDTO.setSenha("novaSenha");
            ciclistaUpdateDTO.setConfirmacaoSenha("novaSenha");

            CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
            cartaoDto.setNomeTitular("Nome Atualizado");
            cartaoDto.setNumero("1111222233334444");
            cartaoDto.setValidade(YearMonth.now().plusYears(1));
            cartaoDto.setCvv("123");
            ciclistaUpdateDTO.setCartaoDeCredito(cartaoDto);
        }

        @Test
        void atualizarCiclista_ComDadosValidos_DeveRetornarStatus200Ok() throws Exception {
            // Arrange
            Integer ciclistaId = 1;
            CiclistaResponseDTO ciclistaAtualizadoResponse = new CiclistaResponseDTO();
            ciclistaAtualizadoResponse.setId(ciclistaId);
            ciclistaAtualizadoResponse.setNome("Nome Atualizado");
            ciclistaAtualizadoResponse.setEmail("novo.email@teste.com");

            given(aluguelService.atualizarCiclista(eq(ciclistaId), any(CiclistaRequestDTO.class)))
                .willReturn(ciclistaAtualizadoResponse);

            mockMvc.perform(put("/api/ciclista/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ciclistaId))
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.email").value("novo.email@teste.com"));
        }

        @Test
        void atualizarCiclista_QuandoServiceLancaExcecao_DeveRetornarStatus400BadRequest() throws Exception {

            Integer ciclistaId = 2;
            String mensagemErro = "Ciclista não pode ser atualizado por algum motivo.";
            
            given(aluguelService.atualizarCiclista(eq(ciclistaId), any(CiclistaRequestDTO.class)))
                .willThrow(new RegraDeNegocioException(mensagemErro));

            mockMvc.perform(put("/api/ciclista/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
        }

        @Test
        void atualizarCiclista_ComDadosInvalidosNoDTO_DeveRetornarStatus400BadRequest() throws Exception {

            Integer ciclistaId = 3;
            ciclistaUpdateDTO.setNomeCiclista(""); 

            mockMvc.perform(put("/api/ciclista/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class AtivarCiclistaControllerTest {

        @Test
        void ativarCiclista_QuandoSucesso_DeveRetornarStatus204NoContent() throws Exception {

            Integer ciclistaId = 1;

            doNothing().when(aluguelService).ativarCiclista(ciclistaId);

            mockMvc.perform(post("/api/ciclista/{idCiclista}/ativar", ciclistaId))
                .andExpect(status().isNoContent());
        }

        @Test
        void ativarCiclista_QuandoCiclistaJaEstaAtivo_DeveRetornarStatus400BadRequest() throws Exception {

            Integer ciclistaId = 2;
            String mensagemErro = "O ciclista não está inativo.";
            
            doThrow(new RegraDeNegocioException(mensagemErro))
                .when(aluguelService).ativarCiclista(ciclistaId);

            mockMvc.perform(post("/api/ciclista/{idCiclista}/ativar", ciclistaId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
        }

        @Test
        void ativarCiclista_QuandoCiclistaNaoExiste_DeveRetornarStatus404NotFound() throws Exception {

            Integer ciclistaId = 999;
            String mensagemErro = "Ciclista não encontrado.";

            doThrow(new RuntimeException(mensagemErro))
                .when(aluguelService).ativarCiclista(ciclistaId);

            mockMvc.perform(post("/api/ciclista/{idCiclista}/ativar", ciclistaId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class ExisteEmailControllerTest {

        @Test
        void existeEmail_QuandoEmailExiste_DeveRetornarStatus200OkComTrue() throws Exception {

            String emailExistente = "email.cadastrado@teste.com";
            given(aluguelService.existeEmail(emailExistente)).willReturn(true);

            mockMvc.perform(get("/api/ciclistas/verificar-email/{email}", emailExistente))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        }

        @Test
        void existeEmail_QuandoEmailNaoExiste_DeveRetornarStatus404NotFound() throws Exception {

            String emailInexistente = "email.novo@teste.com";
            String mensagemErro = "Ciclista não encontrado.";
            
            given(aluguelService.existeEmail(emailInexistente)).willThrow(new RuntimeException(mensagemErro));

            mockMvc.perform(get("/api/ciclistas/verificar-email/{email}", emailInexistente))
                .andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class AtualizarCartaoDeCreditoControllerTest {

        private CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto;

        @BeforeEach
        void setUp() {
            cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
            cartaoDto.setNomeTitular("Titular Válido");
            cartaoDto.setNumero("1111222233334444");
            cartaoDto.setValidade(YearMonth.now().plusYears(2));
            cartaoDto.setCvv("123");
        }

        @Test
        void atualizarCartaoDeCredito_QuandoSucesso_DeveRetornarStatus204NoContent() throws Exception {

            Integer ciclistaId = 1;
            doNothing().when(aluguelService).atualizarCartaoDeCredito(eq(ciclistaId), any(CiclistaRequestDTO.CartaoDeCreditoDto.class));

            mockMvc.perform(put("/api/cartao-de-credito/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isNoContent());
        }

        @Test
        void atualizarCartaoDeCredito_QuandoDtoInvalido_DeveRetornarStatus400BadRequest() throws Exception {

            Integer ciclistaId = 2;
            cartaoDto.setNumero("123"); 

            mockMvc.perform(put("/api/cartao-de-credito/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isBadRequest());
        }
        
        @Test
        void atualizarCartaoDeCredito_QuandoServiceLancaExcecao_DeveRetornarStatus400BadRequest() throws Exception {

            Integer ciclistaId = 3;
            String mensagemErro = "Cartão de crédito inválido ou ciclista não pode atualizar.";
            
            doThrow(new RegraDeNegocioException(mensagemErro))
                .when(aluguelService).atualizarCartaoDeCredito(eq(ciclistaId), any(CiclistaRequestDTO.CartaoDeCreditoDto.class));

            mockMvc.perform(put("/api/cartao-de-credito/{idCiclista}", ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class GetBicicletaAlugadaControllerTest {

        @Test
        void getBicicletaAlugada_QuandoAluguelExiste_DeveRetornarStatus200OkComIdBicicleta() throws Exception {

            Integer ciclistaId = 1;
            Integer idBicicleta = 101;
            given(aluguelService.getBicicletaAlugada(ciclistaId)).willReturn(idBicicleta);

            mockMvc.perform(get("/api/ciclista/{idCiclista}/bicicletaalugada", ciclistaId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(idBicicleta)));
        }

        @Test
        void getBicicletaAlugada_QuandoAluguelNaoExiste_DeveRetornarStatus404NotFound() throws Exception {

            Integer ciclistaId = 2;

            given(aluguelService.getBicicletaAlugada(ciclistaId)).willThrow(new RuntimeException("O ciclista não tem aluguéis no momento."));

            mockMvc.perform(get("/api/ciclista/{idCiclista}/bicicletaalugada", ciclistaId))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetFuncionarioByIdControllerTest {

        @Test
        void getFuncionarioById_QuandoFuncionarioExiste_DeveRetornarStatus200OkComDto() throws Exception {

            String matricula = "F123";
            FuncionarioResponseDTO funcionarioDto = new FuncionarioResponseDTO();
            funcionarioDto.setMatricula(matricula);
            funcionarioDto.setNome("Funcionario Teste");
            funcionarioDto.setEmail("func.teste@empresa.com");

            given(aluguelService.getFuncionarioById(matricula)).willReturn(Optional.of(funcionarioDto));

            mockMvc.perform(get("/api/funcionarios/{matricula}", matricula))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value(matricula))
                .andExpect(jsonPath("$.nome").value("Funcionario Teste"));
        }

        @Test
        void getFuncionarioById_QuandoFuncionarioNaoExiste_DeveRetornarStatus404NotFound() throws Exception {

            String matriculaInexistente = "F999";
            given(aluguelService.getFuncionarioById(matriculaInexistente)).willReturn(Optional.empty());

            mockMvc.perform(get("/api/funcionarios/{matricula}", matriculaInexistente))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    class AtualizarFuncionarioControllerTest {

        private FuncionarioRequestDTO funcionarioRequestDTO;

        @BeforeEach
        void setUp() {
            funcionarioRequestDTO = new FuncionarioRequestDTO();
            funcionarioRequestDTO.setNome("Nome Atualizado");
            funcionarioRequestDTO.setEmail("email.atualizado@empresa.com");
            funcionarioRequestDTO.setIdade(40);
            funcionarioRequestDTO.setFuncao("Gerente");
            funcionarioRequestDTO.setSenha("novaSenha123");
            funcionarioRequestDTO.setConfirmacaoSenha("novaSenha123");
        }

        @Test
        void atualizarFuncionario_ComDadosValidos_DeveRetornarStatus200OkComDto() throws Exception {

            String matricula = "F123";
            FuncionarioResponseDTO responseDto = new FuncionarioResponseDTO();
            responseDto.setMatricula(matricula);
            responseDto.setNome("Nome Atualizado");

            given(aluguelService.atualizarFuncionario(eq(matricula), any(FuncionarioRequestDTO.class)))
                .willReturn(responseDto);

            mockMvc.perform(put("/api/funcionarios/{matricula}", matricula)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(funcionarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value(matricula))
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
        }

        @Test
        void atualizarFuncionario_QuandoFuncionarioNaoExiste_DeveRetornarStatus404NotFound() throws Exception {

            String matricula = "F999";
            String mensagemErro = "Funcionário não encontrado com a matrícula: " + matricula;

            given(aluguelService.atualizarFuncionario(eq(matricula), any(FuncionarioRequestDTO.class)))
                .willThrow(new RegraDeNegocioException(mensagemErro));

            mockMvc.perform(put("/api/funcionarios/{matricula}", matricula)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(funcionarioRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
        }
        
        @Test
        void atualizarFuncionario_ComDadosInvalidosNoDto_DeveRetornarStatus400BadRequest() throws Exception {

            String matricula = "F456";
            funcionarioRequestDTO.setNome("");

            mockMvc.perform(put("/api/funcionarios/{matricula}", matricula)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(funcionarioRequestDTO)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class CriarFuncionarioControllerTest {

        private Funcionario novoFuncionario;

        @BeforeEach
        void setUp() {
            novoFuncionario = new Funcionario(
                "F789",
                "senhaValida",
                "senhaValida",
                new Email("novo.func@empresa.com"),
                "Funcionario Novo",
                25,
                "Junior",
                new Cpf("44455566677")
            );
        }

        @Test
        void criarFuncionario_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {

            given(aluguelService.criarFuncionario(any(Funcionario.class))).willReturn(novoFuncionario);

            mockMvc.perform(post("/api/funcionarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoFuncionario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricula").value("F789"))
                .andExpect(jsonPath("$.nome").value("Funcionario Novo"));
        }

        @Test
        void criarFuncionario_QuandoServiceLancaExcecao_DeveRetornarStatus400BadRequest() throws Exception {

            String mensagemErro = "Matrícula já existe.";
            given(aluguelService.criarFuncionario(any(Funcionario.class)))
                .willThrow(new RegraDeNegocioException(mensagemErro));

            mockMvc.perform(post("/api/funcionarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoFuncionario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class DeletarFuncionarioControllerTest {

        @Test
        void deletarFuncionario_QuandoServiceLancaExcecao_DeveRetornarStatus500InternalServerError() throws Exception {
            // Arrange
            String matricula = "F999";
            String mensagemErro = "Erro ao deletar funcionário.";

            doThrow(new RuntimeException(mensagemErro))
                .when(aluguelService).deletarFuncionario(matricula);

            mockMvc.perform(delete("/api/funcionarios/{matricula}", matricula))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class PermitirAluguelControllerTest {

        @Test
        void permitirAluguel_QuandoPermitido_DeveRetornarStatus200OkComTrue() throws Exception {

            Integer ciclistaId = 1;
            given(aluguelService.permiteAluguel(ciclistaId)).willReturn(true);

            mockMvc.perform(get("/api/ciclista/{idCiclista}/permitiraluguel", ciclistaId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        }

        @Test
        void permitirAluguel_QuandoNaoPermitido_DeveRetornarStatus200OkComFalse() throws Exception {

            Integer ciclistaId = 2;
            given(aluguelService.permiteAluguel(ciclistaId)).willReturn(false);

            mockMvc.perform(get("/api/ciclista/{idCiclista}/permitiraluguel", ciclistaId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
        }

        @Test
        void permitirAluguel_QuandoCiclistaNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
   
            Integer ciclistaId = 999;
            String mensagemErro = "Ciclista não encontrado.";
            
            given(aluguelService.permiteAluguel(ciclistaId)).willThrow(new RuntimeException(mensagemErro));

            mockMvc.perform(get("/api/ciclista/{idCiclista}/permitiraluguel", ciclistaId))
                .andExpect(status().isNotFound());
        }
    }
}
