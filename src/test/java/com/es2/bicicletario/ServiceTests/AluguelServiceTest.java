package com.es2.bicicletario.ServiceTests;

import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.FuncionarioRequestDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.*;
import com.es2.bicicletario.repository.AluguelRepository;
import com.es2.bicicletario.repository.CiclistaRepository;
import com.es2.bicicletario.repository.DevolucaoRepository;
import com.es2.bicicletario.repository.FuncionarioRepository;
import com.es2.bicicletario.service.AluguelService;
import com.es2.bicicletario.validation.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AluguelServiceTest {

    // Constantes para os dados de teste
    private static final String NOME_TESTE = "João Viana";
    private static final String EMAIL_TESTE = "joao.viana@example.com";
    private static final String SENHA_TESTE = "senha123";
    private static final String SENHA_NOVA = "senhaNova123";
    private static final LocalDate DATA_NASCIMENTO_TESTE = LocalDate.of(1990, 1, 1);
    private static final Nacionalidade NACIONALIDADE_TESTE = Nacionalidade.BRASILEIRO;
    private static final String CPF_TESTE = "111.222.333-96";
    private static final String NUMERO_CARTAO_TESTE = "1234567812345678";

    static YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

    static LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

    private static final Date VALIDADE_CARTAO_TESTE = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final String CVV_CARTAO_TESTE = "123";

    @Mock
    private FuncionarioRepository funcionarioRepository;
    @Mock
    private DevolucaoRepository devolucaoRepository;
    @Mock
    private AluguelRepository aluguelRepository;
    @Mock
    private CiclistaRepository ciclistaRepository;

    @InjectMocks
    private AluguelService aluguelService;
    private CiclistaRequestDTO ciclistaRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista(NOME_TESTE);
        ciclistaRequestDTO.setEmail(EMAIL_TESTE);
        ciclistaRequestDTO.setSenha(SENHA_TESTE);
        ciclistaRequestDTO.setConfirmacaoSenha(SENHA_TESTE);
        ciclistaRequestDTO.setDataNascimento(DATA_NASCIMENTO_TESTE);
        ciclistaRequestDTO.setNacionalidade(NACIONALIDADE_TESTE);
        ciclistaRequestDTO.setCpf(CPF_TESTE);
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular(NOME_TESTE);
        cartaoDto.setNumero(NUMERO_CARTAO_TESTE);
        cartaoDto.setValidade(VALIDADE_CARTAO_TESTE);
        cartaoDto.setCvv(CVV_CARTAO_TESTE);
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);
    }

    @Test
    void criarCiclista_ComSenhasDiferentes_DeveLancarExcecao() {
        ciclistaRequestDTO.setConfirmacaoSenha("senhaErrada");

        assertThrows(RegraDeNegocioException.class, () -> {
            aluguelService.criarCiclista(ciclistaRequestDTO);
        });
    }

    @Test
    void realizarAluguel_ComCiclistaInativo_DeveLancarExcecao() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(Status.INATIVO);
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO();
        aluguelRequestDTO.setCiclista("1");
        aluguelRequestDTO.setTrancaInicio("123");

        assertThrows(RegraDeNegocioException.class, () -> {
            aluguelService.realizarAluguel(aluguelRequestDTO);
        });
    }

    @Nested
    class VerificaDadosCiclistaTest {

        @Test
        void criarCiclista_QuandoSenhasNaoConferem_DeveLancarExcecao() {
            ciclistaRequestDTO.setSenha(SENHA_TESTE);
            ciclistaRequestDTO.setConfirmacaoSenha(SENHA_NOVA);

            assertThatThrownBy(() -> aluguelService.criarCiclista(ciclistaRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("A senha e a confirmação de senha não coincidem.");
        }

        @Test
        void criarCiclista_QuandoEmailTemFormatoInvalido_DeveLancarExcecao() {
            ciclistaRequestDTO.setEmail("email-invalido.com");

            assertThatThrownBy(() -> aluguelService.criarCiclista(ciclistaRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("O e-mail está em um formato inválido.");
        }
    }

    @Nested
    class DeletarFuncionarioTest {

        @Test
        void deletarFuncionario_ComMatriculaValida_DeveChamarDeleteByIdERetornarTrue() {

            String matriculaParaDeletar = "F001";
            doNothing().when(funcionarioRepository).deleteById(matriculaParaDeletar);

            Boolean resultado = aluguelService.deletarFuncionario(matriculaParaDeletar);

            verify(funcionarioRepository, times(1)).deleteById(matriculaParaDeletar);
            
            assertThat(resultado).isTrue();
        }
    }

    @Nested
    class GetFuncionariosTest {

        @Mock
        private FuncionarioRepository funcionarioRepository; 

        @InjectMocks
        private AluguelService aluguelService; 
    }

    @Nested
    class GetFuncionarioByIdTest {

        @Test
        void getFuncionarioById_QuandoFuncionarioExiste_DeveRetornarOptionalComDto() {

            String matricula = "F123";
            Funcionario funcionario = new Funcionario(
                matricula,
                "senhaSegura",
                null,
                new Email("funcionario.teste@email.com"),
                "Funcionario Teste",
                35,
                "Analista",
                new Cpf("33344455566")
            );

            when(funcionarioRepository.findById(matricula)).thenReturn(Optional.of(funcionario));

            Optional<FuncionarioResponseDTO> responseOptional = aluguelService.getFuncionarioById(matricula);

            assertThat(responseOptional).isPresent();
            responseOptional.ifPresent(dto -> {
                assertThat(dto.getMatricula()).isEqualTo(matricula);
                assertThat(dto.getNome()).isEqualTo("Funcionario Teste");
                assertThat(dto.getEmail()).isEqualTo("funcionario.teste@email.com");
            });
        }

        @Test
        void getFuncionarioById_QuandoFuncionarioNaoExiste_DeveRetornarOptionalVazio() {

            String matricula = "F999";
            when(funcionarioRepository.findById(matricula)).thenReturn(Optional.empty());

            Optional<FuncionarioResponseDTO> responseOptional = aluguelService.getFuncionarioById(matricula);

            assertThat(responseOptional).isEmpty();
        }
    }

    @Nested
    class AtualizarFuncionarioTest {

        private Funcionario funcionarioExistente;
        private FuncionarioRequestDTO funcionarioRequestDTO;

        @BeforeEach
        void setUp() {
            funcionarioExistente = new Funcionario(
                "F123",
                "senhaAntiga",
                null,
                new Email("antigo@email.com"),
                "Nome Antigo",
                30,
                "Cargo Antigo",
                new Cpf("11122233344")
            );

            funcionarioRequestDTO = new FuncionarioRequestDTO();
            funcionarioRequestDTO.setNome("Nome Novo");
            funcionarioRequestDTO.setEmail("novo@email.com");
            funcionarioRequestDTO.setIdade(35);
            funcionarioRequestDTO.setFuncao("Cargo Novo");
            funcionarioRequestDTO.setCpf("11122233344");
            funcionarioRequestDTO.setSenha(SENHA_NOVA);
            funcionarioRequestDTO.setConfirmacaoSenha(SENHA_NOVA);
        }

        @Test
        void atualizarFuncionario_ComDadosValidos_DeveRetornarDtoAtualizado() {

            when(funcionarioRepository.findById("F123")).thenReturn(Optional.of(funcionarioExistente));
            when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            FuncionarioResponseDTO responseDTO = aluguelService.atualizarFuncionario("F123", funcionarioRequestDTO);

            assertThat(responseDTO).isNotNull();
            assertThat(responseDTO.getNome()).isEqualTo("Nome Novo");
            assertThat(responseDTO.getEmail()).isEqualTo("novo@email.com");
            assertThat(responseDTO.getIdade()).isEqualTo(35);
            
            verify(funcionarioRepository).save(argThat(savedFuncionario ->
                savedFuncionario.getSenha().equals(SENHA_NOVA)
            ));
        }

        @Test
        void atualizarFuncionario_QuandoFuncionarioNaoExiste_DeveLancarExcecao() {

            String matriculaInexistente = "F999";
            when(funcionarioRepository.findById(matriculaInexistente)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> aluguelService.atualizarFuncionario(matriculaInexistente, funcionarioRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Funcionário não encontrado com a matrícula: " + matriculaInexistente);
        }

        @Test
        void atualizarFuncionario_QuandoSenhasNaoConferem_DeveLancarExcecao() {

            funcionarioRequestDTO.setConfirmacaoSenha("senhaDiferente");
            when(funcionarioRepository.findById("F123")).thenReturn(Optional.of(funcionarioExistente));

            assertThatThrownBy(() -> aluguelService.atualizarFuncionario("F123", funcionarioRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("A senha e a confirmação de senha não coincidem.");
        }

        @Test
        void atualizarFuncionario_SemNovaSenha_NaoDeveAlterarSenhaOriginal() {

            funcionarioRequestDTO.setSenha(null);
            funcionarioRequestDTO.setConfirmacaoSenha(null);

            when(funcionarioRepository.findById("F123")).thenReturn(Optional.of(funcionarioExistente));
            when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            aluguelService.atualizarFuncionario("F123", funcionarioRequestDTO);

            verify(funcionarioRepository).save(argThat(savedFuncionario ->
                savedFuncionario.getSenha().equals("senhaAntiga")
            ));
        }
    }

    @Nested
    class CriarFuncionarioTest {

        @Test
        void criarFuncionario_ComDadosValidos_DeveSalvarERetornarFuncionario() {

            Funcionario novoFuncionario = new Funcionario(
                "F456",
                "senhaForte",
                null,
                new Email("novo.funcionario@email.com"),
                "Novo Funcionario",
                28,
                "Operador",
                new Cpf("77788899900")
            );

            when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Funcionario funcionarioSalvo = aluguelService.criarFuncionario(novoFuncionario);

            assertThat(funcionarioSalvo).isNotNull();
            assertThat(funcionarioSalvo.getMatricula()).isEqualTo("F456");
            assertThat(funcionarioSalvo.getNome()).isEqualTo("Novo Funcionario");

            verify(funcionarioRepository, times(1)).save(novoFuncionario);
        }
    }
}
