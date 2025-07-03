package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.CartaoDeCredito;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Passaporte;
import com.es2.bicicletario.validation.ValidNacionalidade; // Importe
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@ValidNacionalidade
public class CiclistaRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nomeCiclista;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O formato do email é inválido.")
    private String email;

    @NotNull(message = "A data de nascimento é obrigatória.")
    private LocalDate dataNascimento;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String senha;

    @NotBlank(message = "A confirmação de senha é obrigatória.")
    private String confirmacaoSenha;
    
    // A obrigatoriedade será validada pela anotação @ValidNacionalidade
    private String cpf;

    @NotNull(message = "A nacionalidade é obrigatória.")
    private Nacionalidade nacionalidade;

    @NotNull(message = "Os dados do cartão de crédito são obrigatórios.")
    @Valid 
    private CartaoDeCreditoDto cartaoDeCredito;

    // A obrigatoriedade será validada pela anotação @ValidNacionalidade
    @Valid 
    private PassaporteDto passaporte;

    @Data
    public static class CartaoDeCreditoDto {
        @NotBlank(message = "O nome do titular do cartão é obrigatório.")
        private String nomeTitular;

        @NotBlank(message = "O número do cartão é obrigatório.")
        @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter 16 dígitos.")
        private String numero;

        @NotNull(message = "A data de validade do cartão é obrigatória.")
        @Future(message = "O cartão de crédito está expirado.")
        private YearMonth validade;

        @NotBlank(message = "O CVV do cartão é obrigatório.")
        @Pattern(regexp = "\\d{3,4}", message = "O CVV deve ter 3 ou 4 dígitos.")
        private String cvv;

        public CartaoDeCredito toEntity(){
            CartaoDeCredito novoCartao = new CartaoDeCredito();

            novoCartao.setCodigoSeguranca(cvv);
            novoCartao.setNomeNoCartao(nomeTitular);
            novoCartao.setNumeroCartao(numero);
            novoCartao.setValidade(validade);

            return novoCartao;
        }
    }

    @Data
    public static class PassaporteDto {
        @NotBlank(message = "O número do passaporte é obrigatório.")
        private String numero;

        @NotNull(message = "A data de validade do passaporte é obrigatória.")
        @Future(message = "O passaporte está expirado.")
        private LocalDate dataDeValidade;

        @NotBlank(message = "O país do passaporte é obrigatório.")
        private String pais;

        public Passaporte toEntity(){
            Passaporte novoPassaporte = new Passaporte();
            novoPassaporte.setNumeroPassaporte(numero);
            novoPassaporte.setDataDeValidade(dataDeValidade);
            novoPassaporte.setPais(pais);
            
            return novoPassaporte; 
        }
    }
}
