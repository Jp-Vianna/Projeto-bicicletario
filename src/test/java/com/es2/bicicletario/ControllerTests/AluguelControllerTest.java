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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AluguelController.class)
class AluguelControllerTest {

    // Constantes para dados de teste
    private static final String NOME_TESTE = "Jorge Peixoto";
    private static final String EMAIL_TESTE = "nome.teste@xyzw.com";
    private static final String CPF_TESTE = "14998043777";
    private static final String SENHA_TESTE = "senha123";
    private static final String SENHA_NOVA = "senhaForte123";
    private static final String NUMERO_CARTAO_TESTE = "1111222233334444";
    private static final String CVV_TESTE = "123";
    private static final LocalDate DATA_NASCIMENTO_TESTE = LocalDate.of(2000, 1, 1);
    private static final Integer CICLISTA_ID_TESTE = 1;
    private static final String NOME_CAMPO_JSON = "$.nomeCiclista";
    private static final String NOME_CAMPO_JSON_FUNC = "$.nome";
    private static final String MATRICULA_CAMPO_JSON = "$.matricula";
    private static final String NOME_ATUALIZADO = "Jorge Aragão";
    private static final String EMAIL_ATUALIZADO = "novo.email@teste.com";

    // Constantes para mensagens de erro
    private static final String ERRO_ALUGUEL_NAO_AUTORIZADO = "O aluguel não foi autorizado.";
    private static final String ERRO_ALUGUEL_ATIVO_NAO_ENCONTRADO = "Não encontrou alugueis ativos com essa bicicleta.";
    private static final String ERRO_CICLISTA_NAO_ENCONTRADO = "Ciclista não encontrado.";
    private static final String ERRO_CICLISTA_JA_ATIVO = "O ciclista não está inativo.";
    private static final String ERRO_FUNCIONARIO_NAO_ENCONTRADO = "Funcionário não encontrado com a matrícula: F999";
    private static final String ERRO_ATUALIZACAO_CICLISTA = "Ciclista não pode ser atualizado por algum motivo.";
    private static final String ERRO_ATUALIZACAO_CARTAO = "Cartão de crédito inválido ou ciclista não pode atualizar.";

    // Constantes para caminhos end
    private static final String CAMINHO_ATT_CICLISTA = "/api/ciclista/{idCiclista}";
    private static final String CAMINHO_ATIVA_CICLISTA = "/api/ciclista/{idCiclista}/ativar";
    private static final String CAMINHO_ATT_CARTAO = "/api/cartao-de-credito/{idCiclista}";
    private static final String CAMINHO_BUSCA_FUNC = "/api/funcionarios/{matricula}";
    private static final String CAMINHO_PERMITE_ALUGUEL = "/api/ciclista/{idCiclista}/permitiraluguel";

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
        ciclistaRequestDTO.setNomeCiclista(NOME_TESTE);
        ciclistaRequestDTO.setEmail(EMAIL_TESTE);
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf(CPF_TESTE);
        ciclistaRequestDTO.setDataNascimento(DATA_NASCIMENTO_TESTE);
        ciclistaRequestDTO.setSenha(SENHA_TESTE);
        ciclistaRequestDTO.setConfirmacaoSenha(SENHA_TESTE);
        
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoRequestDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoRequestDto.setNomeTitular(NOME_TESTE);
        cartaoRequestDto.setNumero(NUMERO_CARTAO_TESTE);
        cartaoRequestDto.setValidade(YearMonth.now().plusYears(1));
        cartaoRequestDto.setCvv(CVV_TESTE);
        ciclistaRequestDTO.setCartaoDeCredito(cartaoRequestDto);

        ciclistaResponseDTO = new CiclistaResponseDTO();
        ciclistaResponseDTO.setId(CICLISTA_ID_TESTE);
        ciclistaResponseDTO.setNomeCiclista(NOME_TESTE);
        ciclistaResponseDTO.setEmail(EMAIL_TESTE);
        ciclistaResponseDTO.setStatus(Status.AGUARDANDO_ATIVAMENTO);
        ciclistaResponseDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaResponseDTO.setCpf(CPF_TESTE);
        
        CiclistaResponseDTO.CartaoDeCreditoDto cartaoResponseDto = new CiclistaResponseDTO.CartaoDeCreditoDto();
        cartaoResponseDto.setNomeTitular(NOME_TESTE);
        cartaoResponseDto.setNumero(NUMERO_CARTAO_TESTE);
        ciclistaResponseDTO.setCartaoDeCredito(cartaoResponseDto);
    }
    
    @Test
    void criarCiclista_ComDadosValidos_DeveRetornarStatus201Created() throws Exception {
        given(aluguelService.criarCiclista(any(CiclistaRequestDTO.class))).willReturn(ciclistaResponseDTO);

        mockMvc.perform(post("/api/ciclistas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ciclistaRequestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(NOME_CAMPO_JSON ).value(NOME_TESTE))
        .andExpect(jsonPath("$.email").value(EMAIL_TESTE));
    }

    @Test
    void getCiclistaById_QuandoCiclistaExiste_DeveRetornarStatus200Ok() throws Exception {
        given(aluguelService.getCiclistaById(CICLISTA_ID_TESTE)).willReturn(Optional.of(ciclistaResponseDTO));
        mockMvc.perform(get("/api/ciclistas/{id}", CICLISTA_ID_TESTE)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(CICLISTA_ID_TESTE))
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
            .willThrow(new RegraDeNegocioException(ERRO_ALUGUEL_NAO_AUTORIZADO));

        mockMvc.perform(post("/api/alugueis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aluguelRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(ERRO_ALUGUEL_NAO_AUTORIZADO));
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

        given(aluguelService.realizarDevolucao(any(DevolucaoRequestDTO.class)))
            .willThrow(new RuntimeException(ERRO_ALUGUEL_ATIVO_NAO_ENCONTRADO));

        mockMvc.perform(post("/api/devolucoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(devolucaoRequest)))
        .andExpect(status().isNotFound())
        .andExpect(content().string(ERRO_ALUGUEL_ATIVO_NAO_ENCONTRADO));
    }

    @Nested
    class AtualizarCiclistaControllerTest {

        private CiclistaRequestDTO ciclistaUpdateDTO;

        @BeforeEach
        void setUp() {
            ciclistaUpdateDTO = new CiclistaRequestDTO();
            ciclistaUpdateDTO.setNomeCiclista(NOME_ATUALIZADO);
            ciclistaUpdateDTO.setEmail(EMAIL_ATUALIZADO);
            ciclistaUpdateDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
            ciclistaUpdateDTO.setCpf("12345678901");
            ciclistaUpdateDTO.setDataNascimento(LocalDate.of(1999, 1, 1));
            ciclistaUpdateDTO.setSenha("novaSenha");
            ciclistaUpdateDTO.setConfirmacaoSenha("novaSenha");

            CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
            cartaoDto.setNomeTitular(NOME_ATUALIZADO);
            cartaoDto.setNumero(NUMERO_CARTAO_TESTE);
            cartaoDto.setValidade(YearMonth.now().plusYears(1));
            cartaoDto.setCvv(CVV_TESTE);
            ciclistaUpdateDTO.setCartaoDeCredito(cartaoDto);
        }

        @Test
        void atualizarCiclista_ComDadosValidos_DeveRetornarStatus200Ok() throws Exception {
            CiclistaResponseDTO ciclistaAtualizadoResponse = new CiclistaResponseDTO();
            ciclistaAtualizadoResponse.setId(CICLISTA_ID_TESTE);
            ciclistaAtualizadoResponse.setNomeCiclista(NOME_ATUALIZADO);
            ciclistaAtualizadoResponse.setEmail(EMAIL_ATUALIZADO);

            given(aluguelService.atualizarCiclista(eq(CICLISTA_ID_TESTE), any(CiclistaRequestDTO.class)))
                .willReturn(ciclistaAtualizadoResponse);

            mockMvc.perform(put(CAMINHO_ATT_CICLISTA, CICLISTA_ID_TESTE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CICLISTA_ID_TESTE))
                .andExpect(jsonPath(NOME_CAMPO_JSON ).value(NOME_ATUALIZADO))
                .andExpect(jsonPath("$.email").value(EMAIL_ATUALIZADO));
        }

        @Test
        void atualizarCiclista_QuandoServiceLancaExcecao_DeveRetornarStatus400BadRequest() throws Exception {
            Integer ciclistaId = 2;
            
            given(aluguelService.atualizarCiclista(eq(ciclistaId), any(CiclistaRequestDTO.class)))
                .willThrow(new RegraDeNegocioException(ERRO_ATUALIZACAO_CICLISTA));

            mockMvc.perform(put(CAMINHO_ATT_CICLISTA, ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERRO_ATUALIZACAO_CICLISTA));
        }

        @Test
        void atualizarCiclista_ComDadosInvalidosNoDTO_DeveRetornarStatus400BadRequest() throws Exception {
            Integer ciclistaId = 3;
            ciclistaUpdateDTO.setNomeCiclista(""); 

            mockMvc.perform(put(CAMINHO_ATT_CICLISTA, ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(ciclistaUpdateDTO)))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class AtivarCiclistaControllerTest {

        @Test
        void ativarCiclista_QuandoSucesso_DeveRetornarStatus204NoContent() throws Exception {
            doNothing().when(aluguelService).ativarCiclista(CICLISTA_ID_TESTE);

            mockMvc.perform(post(CAMINHO_ATIVA_CICLISTA, CICLISTA_ID_TESTE))
                .andExpect(status().isNoContent());
        }

        @Test
        void ativarCiclista_QuandoCiclistaJaEstaAtivo_DeveRetornarStatus400BadRequest() throws Exception {
            Integer ciclistaId = 2;
            
            doThrow(new RegraDeNegocioException(ERRO_CICLISTA_JA_ATIVO))
                .when(aluguelService).ativarCiclista(ciclistaId);

            mockMvc.perform(post(CAMINHO_ATIVA_CICLISTA, ciclistaId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERRO_CICLISTA_JA_ATIVO));
        }

        @Test
        void ativarCiclista_QuandoCiclistaNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
            Integer ciclistaId = 999;

            doThrow(new RuntimeException(ERRO_CICLISTA_NAO_ENCONTRADO))
                .when(aluguelService).ativarCiclista(ciclistaId);

            mockMvc.perform(post(CAMINHO_ATIVA_CICLISTA, ciclistaId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ERRO_CICLISTA_NAO_ENCONTRADO));
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
            
            given(aluguelService.existeEmail(emailInexistente)).willThrow(new RuntimeException(ERRO_CICLISTA_NAO_ENCONTRADO));

            mockMvc.perform(get("/api/ciclistas/verificar-email/{email}", emailInexistente))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ERRO_CICLISTA_NAO_ENCONTRADO));
        }
    }

    @Nested
    class AtualizarCartaoDeCreditoControllerTest {

        private CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto;

        @BeforeEach
        void setUp() {
            cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
            cartaoDto.setNomeTitular("Titular Válido");
            cartaoDto.setNumero(NUMERO_CARTAO_TESTE);
            cartaoDto.setValidade(YearMonth.now().plusYears(2));
            cartaoDto.setCvv(CVV_TESTE);
        }

        @Test
        void atualizarCartaoDeCredito_QuandoSucesso_DeveRetornarStatus204NoContent() throws Exception {
            doNothing().when(aluguelService).atualizarCartaoDeCredito(eq(CICLISTA_ID_TESTE), any(CiclistaRequestDTO.CartaoDeCreditoDto.class));

            mockMvc.perform(put(CAMINHO_ATT_CARTAO, CICLISTA_ID_TESTE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isNoContent());
        }

        @Test
        void atualizarCartaoDeCredito_QuandoDtoInvalido_DeveRetornarStatus400BadRequest() throws Exception {
            Integer ciclistaId = 2;
            cartaoDto.setNumero("123"); 

            mockMvc.perform(put(CAMINHO_ATT_CARTAO, ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isBadRequest());
        }
        
        @Test
        void atualizarCartaoDeCredito_QuandoServiceLancaExcecao_DeveRetornarStatus400BadRequest() throws Exception {
            Integer ciclistaId = 3;
            
            doThrow(new RegraDeNegocioException(ERRO_ATUALIZACAO_CARTAO))
                .when(aluguelService).atualizarCartaoDeCredito(eq(ciclistaId), any(CiclistaRequestDTO.CartaoDeCreditoDto.class));

            mockMvc.perform(put(CAMINHO_ATT_CARTAO, ciclistaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartaoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERRO_ATUALIZACAO_CARTAO));
        }
    }

    @Nested
    class GetBicicletaAlugadaControllerTest {

        @Test
        void getBicicletaAlugada_QuandoAluguelExiste_DeveRetornarStatus200OkComIdBicicleta() throws Exception {
            Integer idBicicleta = 101;
            given(aluguelService.getBicicletaAlugada(CICLISTA_ID_TESTE)).willReturn(idBicicleta);

            mockMvc.perform(get("/api/ciclista/{idCiclista}/bicicletaalugada", CICLISTA_ID_TESTE))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(idBicicleta)));
        }

        @Test
        void getBicicletaAlugada_QuandoAluguelNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
            Integer ciclistaId = 2;
            String erroMsg = "O ciclista não tem aluguéis no momento.";
            given(aluguelService.getBicicletaAlugada(ciclistaId)).willThrow(new RuntimeException(erroMsg));

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

            mockMvc.perform(get(CAMINHO_BUSCA_FUNC, matricula))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MATRICULA_CAMPO_JSON).value(matricula))
                .andExpect(jsonPath(NOME_CAMPO_JSON_FUNC ).value("Funcionario Teste"));
        }

        @Test
        void getFuncionarioById_QuandoFuncionarioNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
            String matriculaInexistente = "F999";
            given(aluguelService.getFuncionarioById(matriculaInexistente)).willReturn(Optional.empty());

            mockMvc.perform(get(CAMINHO_BUSCA_FUNC, matriculaInexistente))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    class AtualizarFuncionarioControllerTest {

        private FuncionarioRequestDTO funcionarioRequestDTO;

        @BeforeEach
        void setUp() {
            funcionarioRequestDTO = new FuncionarioRequestDTO();
            funcionarioRequestDTO.setNome(NOME_ATUALIZADO);
            funcionarioRequestDTO.setEmail(EMAIL_ATUALIZADO);
            funcionarioRequestDTO.setIdade(40);
            funcionarioRequestDTO.setFuncao("Gerente");
            funcionarioRequestDTO.setSenha(SENHA_NOVA);
            funcionarioRequestDTO.setConfirmacaoSenha(SENHA_NOVA);
        }

        @Test
        void atualizarFuncionario_ComDadosValidos_DeveRetornarStatus200OkComDto() throws Exception {
            String matricula = "F123";
            FuncionarioResponseDTO responseDto = new FuncionarioResponseDTO();
            responseDto.setMatricula(matricula);
            responseDto.setNome(NOME_ATUALIZADO);

            given(aluguelService.atualizarFuncionario(eq(matricula), any(FuncionarioRequestDTO.class)))
                .willReturn(responseDto);

            mockMvc.perform(put(CAMINHO_BUSCA_FUNC, matricula)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(funcionarioRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MATRICULA_CAMPO_JSON).value(matricula))
                .andExpect(jsonPath(NOME_CAMPO_JSON_FUNC).value(NOME_ATUALIZADO));
        }

        @Test
        void atualizarFuncionario_QuandoFuncionarioNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
            String matricula = "F999";

            given(aluguelService.atualizarFuncionario(eq(matricula), any(FuncionarioRequestDTO.class)))
                .willThrow(new RegraDeNegocioException(ERRO_FUNCIONARIO_NAO_ENCONTRADO));

            mockMvc.perform(put(CAMINHO_BUSCA_FUNC, matricula)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(funcionarioRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ERRO_FUNCIONARIO_NAO_ENCONTRADO));
        }
        
        @Test
        void atualizarFuncionario_ComDadosInvalidosNoDto_DeveRetornarStatus400BadRequest() throws Exception {
            String matricula = "F456";
            funcionarioRequestDTO.setNome("");

            mockMvc.perform(put(CAMINHO_BUSCA_FUNC, matricula)
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
                .andExpect(jsonPath(MATRICULA_CAMPO_JSON).value("F789"))
                .andExpect(jsonPath(NOME_CAMPO_JSON_FUNC).value("Funcionario Novo"));
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
            String matricula = "F999";
            String mensagemErro = "Erro ao deletar funcionário.";

            doThrow(new RuntimeException(mensagemErro))
                .when(aluguelService).deletarFuncionario(matricula);

            mockMvc.perform(delete(CAMINHO_BUSCA_FUNC, matricula))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(mensagemErro));
        }
    }

    @Nested
    class PermitirAluguelControllerTest {

        @Test
        void permitirAluguel_QuandoPermitido_DeveRetornarStatus200OkComTrue() throws Exception {
            given(aluguelService.permiteAluguel(CICLISTA_ID_TESTE)).willReturn(true);

            mockMvc.perform(get(CAMINHO_PERMITE_ALUGUEL, CICLISTA_ID_TESTE))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        }

        @Test
        void permitirAluguel_QuandoNaoPermitido_DeveRetornarStatus200OkComFalse() throws Exception {
            Integer ciclistaId = 2;
            given(aluguelService.permiteAluguel(ciclistaId)).willReturn(false);

            mockMvc.perform(get(CAMINHO_PERMITE_ALUGUEL, ciclistaId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
        }

        @Test
        void permitirAluguel_QuandoCiclistaNaoExiste_DeveRetornarStatus404NotFound() throws Exception {
            Integer ciclistaId = 999;
            
            given(aluguelService.permiteAluguel(ciclistaId)).willThrow(new RuntimeException(ERRO_CICLISTA_NAO_ENCONTRADO));

            mockMvc.perform(get(CAMINHO_PERMITE_ALUGUEL, ciclistaId))
                .andExpect(status().isNotFound());
        }
    }
}
