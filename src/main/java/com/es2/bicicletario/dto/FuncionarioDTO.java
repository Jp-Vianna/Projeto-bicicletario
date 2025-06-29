package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private String matricula;
    private String nome;
    private String email;
    private String cpf;
    private Integer idade;
    private String funcao;

    /**
     * Método de fábrica para converter a entidade Funcionario em seu DTO.
     * @param funcionario A entidade a ser convertida.
     * @return O DTO preenchido com dados seguros para exposição.
     */
    public static FuncionarioDTO fromEntity(Funcionario funcionario) {
        return new FuncionarioDTO(
                funcionario.getMatricula(),
                funcionario.getNome(),
                funcionario.getEmail().getEmail(), 
                funcionario.getCpf().getNumero(),   
                funcionario.getIdade(),
                funcionario.getFuncao()
        );
    }
}
