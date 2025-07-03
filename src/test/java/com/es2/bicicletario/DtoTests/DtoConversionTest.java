package com.es2.bicicletario.DtoTests;

import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para os métodos de conversão em classes DTO.
 * Foca em testar a lógica de mapeamento de DTOs para Entidades e vice-versa.
 */
class DtoConversionTest {

    @Test
    void deveConverterCiclistaRequestDtoParaEntidade() {
        // Arrange
        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNumero("1111222233334444");
        cartaoDto.setNomeTitular("Teste Titular");
        cartaoDto.setValidade(YearMonth.of(2030, 10));
        cartaoDto.setCvv("987");

        CiclistaRequestDTO.PassaporteDto passaporteDto = new CiclistaRequestDTO.PassaporteDto();
        passaporteDto.setNumero("AB123456");
        passaporteDto.setPais("PORTUGAL");
        passaporteDto.setDataDeValidade(LocalDate.of(2035, 1, 1));

        // Act
        CartaoDeCredito cartaoEntity = cartaoDto.toEntity();
        Passaporte passaporteEntity = passaporteDto.toEntity();

        // Assert
        assertThat(cartaoEntity.getNumeroCartao()).isEqualTo("1111222233334444");
        assertThat(cartaoEntity.getNomeNoCartao()).isEqualTo("Teste Titular");
        assertThat(cartaoEntity.getValidade()).isEqualTo(YearMonth.of(2030, 10));
        assertThat(cartaoEntity.getCodigoSeguranca()).isEqualTo("987");

        assertThat(passaporteEntity.getNumeroPassaporte()).isEqualTo("AB123456");
        assertThat(passaporteEntity.getPais()).isEqualTo("PORTUGAL");
        assertThat(passaporteEntity.getDataDeValidade()).isEqualTo(LocalDate.of(2035, 1, 1));
    }

    @Test
    void deveConverterEntidadeCiclistaParaResponseDto() {
        // Arrange
        CartaoDeCredito cartao = new CartaoDeCredito("1234567812345678", "Jose da Silva", YearMonth.of(2028, 12), "123");
        Ciclista ciclista = new Ciclista(
                1,
                Status.ATIVO,
                LocalDate.of(1995, 5, 10),
                "Jose da Silva",
                new Cpf("11122233344"),
                "senha",
                Nacionalidade.BRASILEIRO,
                new Email("jose.silva@email.com"),
                "/path/foto",
                cartao,
                null
        );

        // Act
        CiclistaResponseDTO dto = CiclistaResponseDTO.fromEntity(ciclista);

        // Assert
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getNome()).isEqualTo("Jose da Silva");
        assertThat(dto.getEmail()).isEqualTo("jose.silva@email.com");
        assertThat(dto.getStatus()).isEqualTo(Status.ATIVO);
        assertThat(dto.getCpf()).isEqualTo("11122233344");
        assertThat(dto.getPassaporte()).isNull();
        assertThat(dto.getNomeTitularCartao()).isEqualTo("Jose da Silva");
        assertThat(dto.getNumeroCartaoMascarado()).isEqualTo("************5678");
    }

    @Test
    void deveConverterEntidadeAluguelParaResponseDto() {
        // Arrange
        Ciclista ciclista = new Ciclista();
        ciclista.setId(10);
        ciclista.setNomeCiclista("Maria Oliveira");

        Aluguel aluguel = new Aluguel(
                101,
                55,
                LocalDateTime.of(2024, 1, 15, 10, 0, 0),
                ciclista,
                202,
                Status.EM_ANDAMENTO
        );

        // Act
        AluguelResponseDTO dto = AluguelResponseDTO.fromEntity(aluguel);

        // Assert
        assertThat(dto.getId()).isEqualTo(101);
        assertThat(dto.getIdBicicleta()).isEqualTo(55);
        assertThat(dto.getHoraInicio()).isEqualTo(LocalDateTime.of(2024, 1, 15, 10, 0, 0));
        assertThat(dto.getStatus()).isEqualTo(Status.EM_ANDAMENTO);
        assertThat(dto.getIdCiclista()).isEqualTo(10);
        assertThat(dto.getNomeCiclista()).isEqualTo("Maria Oliveira");
    }

    @Test
    void deveConverterEntidadeDevolucaoParaResponseDto() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setIdAluguel(500);

        Devolucao devolucao = new Devolucao(
                25,
                909,
                LocalDateTime.of(2024, 1, 15, 11, 30, 0),
                new BigDecimal("25.50"),
                aluguel
        );

        // Act
        DevolucaoResponseDTO dto = DevolucaoResponseDTO.fromEntity(devolucao);

        // Assert
        assertThat(dto.getId()).isEqualTo(25);
        assertThat(dto.getTrancaFinal()).isEqualTo(909);
        assertThat(dto.getHoraFim()).isEqualTo(LocalDateTime.of(2024, 1, 15, 11, 30, 0));
        assertThat(dto.getCobranca()).isEqualTo(new BigDecimal("25.50"));
        assertThat(dto.getIdAluguel()).isEqualTo(500);
    }

    @Test
    void deveConverterEntidadeFuncionarioParaResponseDto() {
        // Arrange
        Funcionario funcionario = new Funcionario(
                "F001",
                "senha",
                "senha",
                new Email("func@email.com"),
                "Carlos Alberto",
                45,
                "Operador",
                new Cpf("88877766655")
        );

        // Act
        FuncionarioResponseDTO dto = FuncionarioResponseDTO.fromEntity(funcionario);

        // Assert
        assertThat(dto.getMatricula()).isEqualTo("F001");
        assertThat(dto.getNome()).isEqualTo("Carlos Alberto");
        assertThat(dto.getEmail()).isEqualTo("func@email.com");
        assertThat(dto.getCpf()).isEqualTo("88877766655");
        assertThat(dto.getIdade()).isEqualTo(45);
        assertThat(dto.getFuncao()).isEqualTo("Operador");
    }
}
