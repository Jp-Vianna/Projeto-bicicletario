package com.es2.bicicletario.ValidationTests;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.validation.ValidNacionalidade;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NacionalidadeValidatorTest {

    private static Validator validator;
    private CiclistaRequestDTO ciclistaRequestDTO;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        ciclistaRequestDTO = new CiclistaRequestDTO();
        ciclistaRequestDTO.setNomeCiclista("Nome Valido");
        ciclistaRequestDTO.setEmail("email@valido.com");
        ciclistaRequestDTO.setDataNascimento(LocalDate.of(2000, 1, 1));
        ciclistaRequestDTO.setSenha("senhaValida");
        ciclistaRequestDTO.setConfirmacaoSenha("senhaValida");

        CiclistaRequestDTO.CartaoDeCreditoDto cartaoDto = new CiclistaRequestDTO.CartaoDeCreditoDto();
        cartaoDto.setNomeTitular("Titular Cartao");
        cartaoDto.setNumero("1111222233334444");
        cartaoDto.setValidade(YearMonth.now().plusYears(1));
        cartaoDto.setCvv("123");
        ciclistaRequestDTO.setCartaoDeCredito(cartaoDto);
    }

    @Test
    void deveSerValido_QuandoBrasileiroComCpfCorreto() {
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf("12345678901");
        ciclistaRequestDTO.setPassaporte(null);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void deveSerValido_QuandoEstrangeiroComPassaporteCorreto() {
        CiclistaRequestDTO.PassaporteDto passaporteDto = new CiclistaRequestDTO.PassaporteDto();
        passaporteDto.setNumero("ABC12345");
        passaporteDto.setPais("PORTUGAL");
        passaporteDto.setDataDeValidade(LocalDate.now().plusYears(2));

        ciclistaRequestDTO.setNacionalidade(Nacionalidade.ESTRANGEIRO);
        ciclistaRequestDTO.setPassaporte(passaporteDto);
        ciclistaRequestDTO.setCpf(null);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void deveSerInvalido_QuandoBrasileiroSemCpf() {
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf(null);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("O CPF é obrigatório para a nacionalidade BRASILEIRO.");
    }

    @Test
    void deveSerInvalido_QuandoBrasileiroComPassaporte() {
        CiclistaRequestDTO.PassaporteDto passaporteDto = new CiclistaRequestDTO.PassaporteDto();
        passaporteDto.setNumero("DEF67890");
        passaporteDto.setPais("ARGENTINA");
        passaporteDto.setDataDeValidade(LocalDate.now().plusYears(1));

        ciclistaRequestDTO.setNacionalidade(Nacionalidade.BRASILEIRO);
        ciclistaRequestDTO.setCpf("12345678901");
        ciclistaRequestDTO.setPassaporte(passaporteDto);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("O passaporte não deve ser preenchido para a nacionalidade BRASILEIRO.");
    }

    @Test
    void deveSerInvalido_QuandoEstrangeiroSemPassaporte() {
        ciclistaRequestDTO.setNacionalidade(Nacionalidade.ESTRANGEIRO);
        ciclistaRequestDTO.setPassaporte(null);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("O passaporte é obrigatório para a nacionalidade ESTRANGEIRO.");
    }

    @Test
    void deveSerInvalido_QuandoEstrangeiroComCpf() {
        CiclistaRequestDTO.PassaporteDto passaporteDto = new CiclistaRequestDTO.PassaporteDto();
        passaporteDto.setNumero("GHI112233");
        passaporteDto.setPais("ESPANHA");
        passaporteDto.setDataDeValidade(LocalDate.now().plusYears(3));

        ciclistaRequestDTO.setNacionalidade(Nacionalidade.ESTRANGEIRO);
        ciclistaRequestDTO.setPassaporte(passaporteDto);
        ciclistaRequestDTO.setCpf("12345678901");

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("O CPF não deve ser preenchido para a nacionalidade ESTRANGEIRO.");
    }

    @Test
    void deveSerValido_QuandoNacionalidadeForNula() {
        ciclistaRequestDTO.setNacionalidade(null);

        Set<ConstraintViolation<CiclistaRequestDTO>> violations = validator.validate(ciclistaRequestDTO);
        
        long validNacionalidadeViolations = violations.stream()
            .filter(v -> v.getConstraintDescriptor().getAnnotation().annotationType().equals(ValidNacionalidade.class))
            .count();
        
        assertThat(validNacionalidadeViolations).isZero();
    }
}