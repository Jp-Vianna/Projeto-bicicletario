package com.es2.bicicletario.ServiceTests;

import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.entity.*;
import com.es2.bicicletario.repository.AluguelRepository;
import com.es2.bicicletario.repository.CiclistaRepository;
import com.es2.bicicletario.repository.DevolucaoRepository;
import com.es2.bicicletario.repository.FuncionarioRepository;
import com.es2.bicicletario.service.AluguelService;
import com.es2.bicicletario.validation.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AluguelServiceTest {

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
        ciclistaRequestDTO.setNomeCiclista("João Viana");
        ciclistaRequestDTO.setEmail("joao.viana@example.com");
        ciclistaRequestDTO.setSenha("senha123");
        ciclistaRequestDTO.setConfirmacaoSenha("senha123");
        ciclistaRequestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf("111.222.333-96");
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular("João Viana");
        cartaoDto.setNumero("1234567812345678");
        cartaoDto.setValidade(YearMonth.of(2025, 12));
        cartaoDto.setCvv("123");
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);
    }

    @Test
    void criarCiclista_ComDadosValidos_DeveRetornarCiclistaResponseDTO() {
        when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> {
            Ciclista c = invocation.getArgument(0);
            c.setId(1);
            return c;
        });

        CiclistaResponseDTO response = aluguelService.criarCiclista(ciclistaRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getNome()).isEqualTo("João Viana");
        assertThat(response.getEmail()).isEqualTo("joao.viana@example.com");
    }

    @Test
    void criarCiclista_ComSenhasDiferentes_DeveLancarExcecao() {
        ciclistaRequestDTO.setConfirmacaoSenha("senhaErrada");

        assertThrows(RegraDeNegocioException.class, () -> {
            aluguelService.criarCiclista(ciclistaRequestDTO);
        });
    }

    @Test
    void realizarAluguel_ComCiclistaValido_DeveRetornarAluguelResponseDTO() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(Status.ATIVO);
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO();
        aluguelRequestDTO.setIdCiclista(1);
        aluguelRequestDTO.setIdTranca(123);

        AluguelResponseDTO response = aluguelService.realizarAluguel(aluguelRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Status.EM_ANDAMENTO);
        assertThat(response.getIdCiclista()).isEqualTo(1);
    }

    @Test
    void realizarAluguel_ComCiclistaInativo_DeveLancarExcecao() {
        Ciclista ciclista = new Ciclista();
        ciclista.setId(1);
        ciclista.setStatus(Status.INATIVO);
        when(ciclistaRepository.findById(1)).thenReturn(Optional.of(ciclista));

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO();
        aluguelRequestDTO.setIdCiclista(1);
        aluguelRequestDTO.setIdTranca(123);

        assertThrows(RegraDeNegocioException.class, () -> {
            aluguelService.realizarAluguel(aluguelRequestDTO);
        });
    }
}
