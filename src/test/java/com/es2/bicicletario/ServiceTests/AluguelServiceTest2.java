package com.es2.bicicletario.ServiceTests;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest2 {

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
        // Objeto DTO para os testes de criação
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista("Joana Dark");
        ciclistaRequestDTO.setEmail("joana.dark@email.com");
        ciclistaRequestDTO.setDataNascimento(LocalDate.of(1992, 8, 20));
        ciclistaRequestDTO.setSenha("senha123");
        ciclistaRequestDTO.setConfirmacaoSenha("senha123");
        
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNumero("1234567812345678");
        cartaoDto.setNomeTitular("Joana D.");
        cartaoDto.setValidade(YearMonth.now().plusYears(2));
        cartaoDto.setCvv("321");
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);

        // Objeto de Entidade completo para os testes de regras de negócio
        ciclistaBrasileiro = new Ciclista();
        ciclistaBrasileiro.setId(1);
        ciclistaBrasileiro.setNomeCiclista("Ciclista Ativo Brasileiro");
        ciclistaBrasileiro.setStatus(Status.ATIVO);
        ciclistaBrasileiro.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaBrasileiro.setCpf(new Cpf("11122233344"));
        ciclistaBrasileiro.setEmail(new Email("ciclista.br@email.com"));
        ciclistaBrasileiro.setCartao(new CartaoDeCredito("1111222233334444", "Ciclista BR", YearMonth.now().plusYears(3), "123"));
    }

    @Nested
    class CriacaoDeCiclistaTest {

        @Test
        void criarCiclista_Estrangeiro_SemPassaporte_DeveLancarExcecao() {
            ciclistaRequestDTO.setNacionalidade(Nacionalidade.ESTRANGEIRO);
            ciclistaRequestDTO.setCpf(null);
            ciclistaRequestDTO.setPassaporte(null);

            assertThatThrownBy(() -> aluguelService.criarCiclista(ciclistaRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Para estrangeiros, um passaporte válido é obrigatório.");
        }

        @Test
        void criarCiclista_Brasileiro_ComCpfInvalido_DeveLancarExcecao() {
            ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
            ciclistaRequestDTO.setCpf("11111111111"); // CPF com todos os dígitos iguais é inválido

            assertThatThrownBy(() -> aluguelService.criarCiclista(ciclistaRequestDTO))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Para brasileiros, um CPF válido é obrigatório.");
        }
    }

    @Nested
    class PermissaoDeAluguelTest {

        @Test
        void permiteAluguel_ComCiclistaAtivoESemPendencias_DeveRetornarTrue() {
            when(aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclistaBrasileiro.getId(), List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE)))
                .thenReturn(Collections.emptyList());
            
            boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro);

            assertThat(permissao).isTrue();
        }

        @Test
        void permiteAluguel_ComCiclistaInativo_DeveRetornarFalse() {
            ciclistaBrasileiro.setStatus(Status.INATIVO);
            
            boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro);

            assertThat(permissao).isFalse();
        }

        @Test
        void permiteAluguel_ComAluguelEmAndamento_DeveRetornarFalse() {
            Aluguel aluguelPendente = new Aluguel();
            aluguelPendente.setStatus(Status.EM_ANDAMENTO);
            when(aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclistaBrasileiro.getId(), List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE)))
                .thenReturn(List.of(aluguelPendente));

            boolean permissao = aluguelService.permiteAluguel(ciclistaBrasileiro);

            assertThat(permissao).isFalse();
        }
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

        @Test
        void realizarDevolucao_ComSucesso_DeveAtualizarAluguelESalvarDevolucao() {
            // Arrange
            DevolucaoRequestDTO devolucaoDTO = new DevolucaoRequestDTO();
            devolucaoDTO.setIdBicicleta(101);
            devolucaoDTO.setIdTranca(202);

            Aluguel aluguelAtivo = new Aluguel();
            aluguelAtivo.setIdAluguel(50);
            aluguelAtivo.setStatus(Status.EM_ANDAMENTO);
            aluguelAtivo.setCiclista(ciclistaBrasileiro);

            when(aluguelRepository.findByIdBicicletaAndStatus(101, Status.EM_ANDAMENTO)).thenReturn(Optional.of(aluguelAtivo));
            when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(inv -> inv.getArgument(0));
            when(devolucaoRepository.save(any(Devolucao.class))).thenAnswer(inv -> {
                Devolucao d = inv.getArgument(0);
                d.setIdDevolucao(1);
                return d;
            });

            // Act
            DevolucaoResponseDTO response = aluguelService.realizarDevolucao(devolucaoDTO);

            // Assert
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1);
            assertThat(response.getIdAluguel()).isEqualTo(50);

            verify(aluguelRepository).save(argThat(aluguel -> aluguel.getStatus() == Status.FINALIZADO));
            verify(devolucaoRepository).save(argThat(devolucao -> 
                devolucao.getTrancaFinal().equals(202) &&
                devolucao.getAluguel().getIdAluguel().equals(50)
            ));
        }
    }
}
