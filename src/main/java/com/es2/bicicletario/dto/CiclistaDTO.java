package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CiclistaDTO {

    private Integer id;
    private String nome;
    private String email;
    private Status status;
    private Nacionalidade nacionalidade;
    private String cpf;
    private String passaporte;

    // Dados do cartão de crédito de forma segura
    private String nomeTitularCartao;
    private String numeroCartaoMascarado;

    /**
     * Método de fábrica para converter a entidade Ciclista em seu DTO.
     * @param ciclista A entidade a ser convertida.
     * @return O DTO preenchido com dados seguros.
     */
    public static CiclistaDTO fromEntity(Ciclista ciclista) {

        String cartaoCompleto = ciclista.getCartao().getNumeroCartao();
        String cartaoMascarado = "************" + cartaoCompleto.substring(cartaoCompleto.length() - 4);

        String numeroPassaporte = (ciclista.getPassaporte() != null) ? ciclista.getPassaporte().getNumeroPassaporte() : null;

        return new CiclistaDTO(
                ciclista.getId(),
                ciclista.getNomeCiclista(),
                ciclista.getEmail().getEmail(), 
                ciclista.getStatus(),
                ciclista.getNacionalidade(),
                ciclista.getCpf().getNumero(), 
                numeroPassaporte,
                ciclista.getCartao().getNomeNoCartao(),
                cartaoMascarado 
        );
    }
}
