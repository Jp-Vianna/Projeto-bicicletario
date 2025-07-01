// no pacote com.es2.bicicletario.validation ou similar
package com.es2.bicicletario.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NacionalidadeValidator.class)
@Documented
public @interface ValidNacionalidade {
    String message() default "Dados de identificação inválidos para a nacionalidade selecionada.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}