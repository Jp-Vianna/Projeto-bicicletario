package com.es2.bicicletario.ServiceTests;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.entity.*;
import com.es2.bicicletario.repository.AluguelRepository;
import com.es2.bicicletario.repository.CiclistaRepository;
import com.es2.bicicletario.repository.DevolucaoRepository;
import com.es2.bicicletario.service.AluguelService;
import com.es2.bicicletario.validation.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTwoTest {

    // Constantes para dados do CiclistaRequestDTO
    private static final String NOME_REQUEST = "Joana Dark";
    private static final String EMAIL_REQUEST = "joana.dark@email.com";
    private static final LocalDate DATA_NASCIMENTO_REQUEST = LocalDate.of(1992, 8, 20);
    private static final String SENHA_REQUEST = "senha123";
    private static final String NUMERO_CARTAO_REQUEST = "1234567812345678";
    private static final String NOME_TITULAR_CARTAO_REQUEST = "Joana D.";
    private static final String CVV_CARTAO_REQUEST = "321";

    // Constantes para dados do Ciclista (entidade mockada)
    private static final String NOME_CICLISTA_BR = "Ciclista Ativo Brasileiro";
    private static final String CPF_CICLISTA_BR = "00270625709";
    private static final String EMAIL_CICLISTA_BR = "ciclista.br@email.com";
    private static final String NUMERO_CARTAO_CICLISTA_BR = "1111222233334444";
    private static final String NOME_TITULAR_CARTAO_CICLISTA_BR = "Ciclista BR";
    private static final String CVV_CARTAO_CICLISTA_BR = "123";

    //Constantes para erros
    private static final String CICLISTA_NAO_ENCONTRADO = "Ciclista não encontrado.";

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private CiclistaRepository ciclistaRepository;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private DevolucaoRepository devolucaoRepository;

    private CiclistaRequestDTO ciclistaRequestDTO;
    private Ciclista ciclistaBrasileiro;

    @BeforeEach
    void setUp() {
        
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista(NOME_REQUEST);
        ciclistaRequestDTO.setEmail(EMAIL_REQUEST);
        ciclistaRequestDTO.setDataNascimento(DATA_NASCIMENTO_REQUEST);
        ciclistaRequestDTO.setSenha(SENHA_REQUEST);
        ciclistaRequestDTO.setConfirmacaoSenha(SENHA_REQUEST);
        
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNumero(NUMERO_CARTAO_REQUEST);
        cartaoDto.setNomeTitular(NOME_TITULAR_CARTAO_REQUEST);
        YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

        LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

        Date dataValidade = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        cartaoDto.setValidade(dataValidade);
        cartaoDto.setCvv(CVV_CARTAO_REQUEST);
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);

        ciclistaBrasileiro = new Ciclista();
        ciclistaBrasileiro.setId(1);
        ciclistaBrasileiro.setNomeCiclista(NOME_CICLISTA_BR);
        ciclistaBrasileiro.setStatus(Status.ATIVO);
        ciclistaBrasileiro.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaBrasileiro.setCpf(new Cpf(CPF_CICLISTA_BR));
        ciclistaBrasileiro.setEmail(new Email(EMAIL_CICLISTA_BR));
        ciclistaBrasileiro.setCartao(new CartaoDeCredito(NUMERO_CARTAO_CICLISTA_BR, NOME_TITULAR_CARTAO_CICLISTA_BR, Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant()), CVV_CARTAO_CICLISTA_BR));
    }

    @Nested
    class PermissaoDeAluguelTest {

        @Test
        void permiteAluguel_ComCiclistaAtivoESemPendencias_DeveRetornarTrue() {
            when(aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclistaBrasileiro.getId(), List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE)))
                .thenReturn(Collections.emptyList());

            when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));
            
            boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro.getId());

            assertThat(permissao).isTrue();
        }

        @Test
        void permiteAluguel_ComCiclistaInativo_DeveRetornarFalse() {
            ciclistaBrasileiro.setStatus(Status.INATIVO);

            when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));
            
            boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro.getId());

            assertThat(permissao).isFalse();
        }

        // @Test
        // void permiteAluguel_ComAluguelEmAndamento_DeveRetornarFalse() {
        //     Aluguel aluguelPendente = new Aluguel();
        //     aluguelPendente.setStatus(Status.EM_ANDAMENTO);
            
        //     when(aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclistaBrasileiro.getId(), List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE)))
        //         .thenReturn(List.of(aluguelPendente));

        //     when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));

        //     boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro.getId());

        //     assertThat(permissao).isFalse();
        // }
    }

    @Nested
    class RealizacaoDevolucaoTest {

        @Test
        void realizarDevolucao_SemAluguelAtivoParaBicicleta_DeveLancarExcecao() {
            DevolucaoRequestDTO devolucaoDTO = new DevolucaoRequestDTO();
            devolucaoDTO.setIdBicicleta(99);
            devolucaoDTO.setIdTranca(10);
            
            when(aluguelRepository.findByIdBicicletaAndStatus(99, Status.EM_ANDAMENTO)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> aluguelService.realizarDevolucao(devolucaoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Não encontrou alugueis ativos com essa bicicleta.");
        }
    }

    @Nested
    class GetBicicletaAlugadaTest {
        @Test
        void getBicicletaAlugada_QuandoCiclistaNaoPossuiAluguelAtivo_DeveLancarExcecao() {

            when(aluguelRepository.findByCiclistaIdAndStatus(ciclistaBrasileiro.getId(), Status.EM_ANDAMENTO))
                .thenReturn(Optional.empty());

            assertThatThrownBy(() -> aluguelService.getBicicletaAlugada(ciclistaBrasileiro.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("O ciclista não tem aluguéis no momento.");
        }
    }

    @Nested
class ExisteEmailTest {

    @Test
    void existeEmail_QuandoEmailExiste_DeveRetornarTrue() {

        String emailExistente = "email.existente@teste.com";
        when(ciclistaRepository.findByEmailEndereco(emailExistente)).thenReturn(Optional.of(new Ciclista()));

        Boolean resultado = aluguelService.existeEmail(emailExistente);

        assertThat(resultado).isTrue();
    }

    @Test
    void existeEmail_QuandoEmailNaoExiste_DeveLancarExcecao() {

        String emailInexistente = "email.inexistente@teste.com";
        when(ciclistaRepository.findByEmailEndereco(emailInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aluguelService.existeEmail(emailInexistente))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(CICLISTA_NAO_ENCONTRADO);
        }
    }

    @Nested
class AtivarCiclistaTest {

    @Test
    void ativarCiclista_QuandoCiclistaEstaInativo_DeveMudarStatusParaAtivoESalvar() {
 
        ciclistaBrasileiro.setStatus(Status.INATIVO);
        when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));
        when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        aluguelService.ativarCiclista(ciclistaBrasileiro.getId());

        verify(ciclistaRepository).save(argThat(ciclistaSalvo ->
            ciclistaSalvo.getStatus() == Status.ATIVO &&
            ciclistaSalvo.getId().equals(ciclistaBrasileiro.getId())
        ));
    }

    @Test
    void ativarCiclista_QuandoCiclistaJaEstaAtivo_DeveLancarExcecao() {

        ciclistaBrasileiro.setStatus(Status.ATIVO);
        when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));

        assertThatThrownBy(() -> aluguelService.ativarCiclista(ciclistaBrasileiro.getId()))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("O ciclista não está inativo.");
    }

    @Test
    void ativarCiclista_QuandoCiclistaNaoExiste_DeveLancarExcecao() {

        Integer idInexistente = 999;
        when(ciclistaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aluguelService.ativarCiclista(idInexistente))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(CICLISTA_NAO_ENCONTRADO);
        }
    }
    
    @Nested
class AtualizarCartaoDeCreditoTest {

    private CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto;

    @BeforeEach
    void setUp() {
        cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular("Novo Titular");
        cartaoDto.setNumero("8888777766665555");
        YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

        LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

        Date dataValidade = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        cartaoDto.setValidade(dataValidade);
        cartaoDto.setCvv("987");
    }

    // @Test
    // void atualizarCartaoDeCredito_ComDadosValidos_DeveAtualizarCartaoDoCiclista() {

    //     when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));
    //     when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

    //     aluguelService.atualizarCartaoDeCredito(ciclistaBrasileiro.getId(), cartaoDto);

    //     verify(ciclistaRepository).save(argThat(ciclistaSalvo ->
    //         ciclistaSalvo.getCartao().getNumeroCartao().equals("8888777766665555") &&
    //         ciclistaSalvo.getCartao().getNomeNoCartao().equals("Novo Titular")
    //     ));
    // }

    @Test
    void atualizarCartaoDeCredito_QuandoDtoEhNulo_DeveLancarExcecao() {

        when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));

        assertThatThrownBy(() -> aluguelService.atualizarCartaoDeCredito(ciclistaBrasileiro.getId(), null))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("Os dados do cartão de crédito não podem ser nulos.");
    }

    @Test
    void atualizarCartaoDeCredito_QuandoCiclistaNaoExiste_DeveLancarExcecao() {

        Integer idInexistente = 999;
        when(ciclistaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aluguelService.atualizarCartaoDeCredito(idInexistente, cartaoDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(CICLISTA_NAO_ENCONTRADO);
        }
    }

    @Nested
    class GetCartaoDeCreditoTest {

        @Test
        void getCartaoDeCredito_QuandoCiclistaExiste_DeveRetornarCartao() {

            when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));
            
            CartaoDeCredito cartao = aluguelService.getCartaoDeCredito(ciclistaBrasileiro.getId());

            assertThat(cartao).isNotNull();
            assertThat(cartao.getNumeroCartao()).isEqualTo(NUMERO_CARTAO_CICLISTA_BR);
            assertThat(cartao.getNomeNoCartao()).isEqualTo(NOME_TITULAR_CARTAO_CICLISTA_BR);
        }

        @Test
        void getCartaoDeCredito_QuandoCiclistaNaoExiste_DeveLancarExcecao() {

            Integer idInexistente = 999;
            when(ciclistaRepository.findById(idInexistente)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> aluguelService.getCartaoDeCredito(idInexistente))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(CICLISTA_NAO_ENCONTRADO);
        }
    }

    @Nested
    class GetCiclistaByIdTest {

        @Test
        void getCiclistaById_QuandoCiclistaExiste_DeveRetornarOptionalComDto() {

            when(ciclistaRepository.findById(ciclistaBrasileiro.getId())).thenReturn(Optional.of(ciclistaBrasileiro));

            Optional<CiclistaResponseDTO> responseOptional = aluguelService.getCiclistaById(ciclistaBrasileiro.getId());

            assertThat(responseOptional).isPresent();
            responseOptional.ifPresent(dto -> {
                assertThat(dto.getId()).isEqualTo(ciclistaBrasileiro.getId());
                assertThat(dto.getNomeCiclista()).isEqualTo(ciclistaBrasileiro.getNomeCiclista());
                assertThat(dto.getEmail()).isEqualTo(ciclistaBrasileiro.getEmail().getEndereco());
            });
        }

        @Test
        void getCiclistaById_QuandoCiclistaNaoExiste_DeveRetornarOptionalVazio() {

            Integer idInexistente = 999;
            when(ciclistaRepository.findById(idInexistente)).thenReturn(Optional.empty());

            Optional<CiclistaResponseDTO> responseOptional = aluguelService.getCiclistaById(idInexistente);

            assertThat(responseOptional).isEmpty();
        }
    }

    @Nested
    class AtualizarCiclistaTest {

        private CiclistaRequestDTO ciclistaUpdateDto;

        @BeforeEach
        void setUp() {
            ciclistaUpdateDto = new CiclistaRequestDTO();
            ciclistaUpdateDto.setNomeCiclista("Nome Atualizado");
            ciclistaUpdateDto.setDataNascimento(LocalDate.of(1995, 10, 10));
            ciclistaUpdateDto.setEmail("email.atualizado@teste.com");
            ciclistaUpdateDto.setSenha("novaSenha123");
            ciclistaUpdateDto.setConfirmacaoSenha("novaSenha123");
            ciclistaUpdateDto.setCartaoDeCredito(ciclistaRequestDTO.getCartaoDeCredito());
        }

        @Test
        void atualizarCiclista_QuandoCiclistaNaoExiste_DeveLancarExcecao() {

            Integer idInexistente = 999;
            when(ciclistaRepository.findById(idInexistente)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> aluguelService.atualizarCiclista(idInexistente, ciclistaUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(CICLISTA_NAO_ENCONTRADO);
        }
    }
}
