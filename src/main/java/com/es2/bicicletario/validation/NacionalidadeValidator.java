// no mesmo pacote da anotação
package com.es2.bicicletario.validation;

import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.entity.Nacionalidade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NacionalidadeValidator implements ConstraintValidator<ValidNacionalidade, CiclistaRequestDTO> {

    @Override
    public boolean isValid(CiclistaRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getNacionalidade() == null) {
            // A anotação @NotNull no campo já cuida disso, mas é bom ter defesa.
            return true; 
        }

        boolean isValid = true;
        
        if (Nacionalidade.BRASILEIRO.equals(dto.getNacionalidade())) {
            if (dto.getCpf() == null || dto.getCpf().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O CPF é obrigatório para a nacionalidade BRASILEIRO.")
                       .addPropertyNode("cpf").addConstraintViolation();
                isValid = false;
            }
            if (dto.getPassaporte() != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O passaporte não deve ser preenchido para a nacionalidade BRASILEIRO.")
                       .addPropertyNode("passaporte").addConstraintViolation();
                isValid = false;
            }
        } else if (Nacionalidade.ESTRANGEIRO.equals(dto.getNacionalidade())) {
            if (dto.getPassaporte() == null) {
                 context.disableDefaultConstraintViolation();
                 context.buildConstraintViolationWithTemplate("O passaporte é obrigatório para a nacionalidade ESTRANGEIRO.")
                        .addPropertyNode("passaporte").addConstraintViolation();
                isValid = false;
            }
            if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("O CPF não deve ser preenchido para a nacionalidade ESTRANGEIRO.")
                       .addPropertyNode("cpf").addConstraintViolation();
                isValid = false;
            }
        }
        
        return isValid;
    }
}
