package com.es2.bicicletario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FuncionarioRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O formato do email é inválido.")
    private String email;

    @Min(value = 18, message = "O funcionário deve ter no mínimo 18 anos.")
    private Integer idade;

    @NotBlank(message = "A função é obrigatória.")
    private String funcao;

    @Pattern(regexp = "\\d{11}", message = "Se informado, o CPF deve conter 11 dígitos.")
    private String cpf;

    @Size(min = 8, message = "Se informada, a senha deve ter no mínimo 8 caracteres.")
    private String senha;

    private String confirmacaoSenha;
}
