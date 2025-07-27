package com.es2.bicicletario.DtoTests;

import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para os métodos de conversão em classes DTO.
 * Foca em testar a lógica de mapeamento de DTOs para Entidades e vice-versa.
 */
class DtoConversionTest {

    final String nomeTeste = "Jose da Silva";
    final String senhaTeste = "senha";

    @Test
    void deveConverterCiclistaRequestDtoParaEntidade() {

        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNumero("1111222233334444");
        cartaoDto.setNomeTitular(nomeTeste);
        cartaoDto.setValidade(YearMonth.of(2030, 10));
        cartaoDto.setCvv("987");

        CiclistaRequestDTO.PassaporteDto passaporteDto = new CiclistaRequestDTO.PassaporteDto();
        passaporteDto.setNumero("AB123456");
        passaporteDto.setPais("PORTUGAL");
        passaporteDto.setDataDeValidade(LocalDate.of(2035, 1, 1));

        CartaoDeCredito cartaoEntity = cartaoDto.toEntity();
        Passaporte passaporteEntity = passaporteDto.toEntity();

        assertThat(cartaoEntity.getNumeroCartao()).isEqualTo("1111222233334444");
        assertThat(cartaoEntity.getNomeNoCartao()).isEqualTo(nomeTeste);
        assertThat(cartaoEntity.getValidade()).isEqualTo(YearMonth.of(2030, 10));
        assertThat(cartaoEntity.getCodigoSeguranca()).isEqualTo("987");

        assertThat(passaporteEntity.getNumeroPassaporte()).isEqualTo("AB123456");
        assertThat(passaporteEntity.getPais()).isEqualTo("PORTUGAL");
        assertThat(passaporteEntity.getDataDeValidade()).isEqualTo(LocalDate.of(2035, 1, 1));
    }

    @Test
    void deveConverterEntidadeCiclistaParaResponseDto() {

        CartaoDeCredito cartao = new CartaoDeCredito("1234567812345678", nomeTeste, YearMonth.of(2028, 12), "123");
        Ciclista ciclista = new Ciclista(
                1,
                Status.ATIVO,
                LocalDate.of(1995, 5, 10),
                nomeTeste,
                new Cpf("11122233344"),
                senhaTeste,
                Nacionalidade.BRASILEIRO,
                new Email("silva@email.com"),
                "/path/foto",
                cartao,
                null
        );

        CiclistaResponseDTO dto = CiclistaResponseDTO.fromEntity(ciclista);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getNome()).isEqualTo(nomeTeste);
        assertThat(dto.getEmail()).isEqualTo("silva@email.com");
        assertThat(dto.getStatus()).isEqualTo(Status.ATIVO);
        assertThat(dto.getCpf()).isEqualTo("11122233344");
        assertThat(dto.getPassaporte()).isNull();
        assertThat(dto.getNomeTitularCartao()).isEqualTo(nomeTeste);
        assertThat(dto.getNumeroCartaoMascarado()).isEqualTo("************5678");
    }

    @Test
    void deveConverterEntidadeAluguelParaResponseDto() {

        Ciclista ciclista = new Ciclista();
        ciclista.setId(10);
        ciclista.setNomeCiclista(nomeTeste);

        Aluguel aluguel = new Aluguel(
                101,
                55,
                LocalDateTime.of(2024, 1, 15, 10, 0, 0),
                ciclista,
                202,
                Status.EM_ANDAMENTO
        );

        AluguelResponseDTO dto = AluguelResponseDTO.fromEntity(aluguel);

        assertThat(dto.getId()).isEqualTo(101);
        assertThat(dto.getIdBicicleta()).isEqualTo(55);
        assertThat(dto.getHoraInicio()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0, 0));
        assertThat(dto.getStatus()).isEqualTo(Status.EM_ANDAMENTO);
        assertThat(dto.getIdCiclista()).isEqualTo(10);
        assertThat(dto.getNomeCiclista()).isEqualTo(nomeTeste);
    }

    @Test
    void deveConverterEntidadeFuncionarioParaResponseDto() {

        Funcionario funcionario = new Funcionario(
                "F001",
                senhaTeste,
                senhaTeste,
                new Email("func@email.com"),
                nomeTeste,
                45,
                "Operador",
                new Cpf("88877766655")
        );

        FuncionarioResponseDTO dto = FuncionarioResponseDTO.fromEntity(funcionario);

        assertThat(dto.getMatricula()).isEqualTo("F001");
        assertThat(dto.getNome()).isEqualTo(nomeTeste);
        assertThat(dto.getEmail()).isEqualTo("func@email.com");
        assertThat(dto.getCpf()).isEqualTo("88877766655");
        assertThat(dto.getIdade()).isEqualTo(45);
        assertThat(dto.getFuncao()).isEqualTo("Operador");
    }
}
