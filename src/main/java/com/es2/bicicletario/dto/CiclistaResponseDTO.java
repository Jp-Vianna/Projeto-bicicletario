package com.es2.bicicletario.dto;

import com.es2.bicicletario.entity.Ciclista;
import com.es2.bicicletario.entity.Nacionalidade;
import com.es2.bicicletario.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class CiclistaResponseDTO {

    private Integer id;
    private String nomeCiclista;
    private String email;
    private String fotoDocumento;
    private LocalDate dataNascimento;
    private Status status; 
    private String senha; 
    private String cpf; 
    private Nacionalidade nacionalidade;
    
    private CartaoDeCreditoDto cartaoDeCredito;
    private PassaporteDto passaporte;

    @Data
    public static class CartaoDeCreditoDto {
        private String nomeTitular;
        private String numero;
        private YearMonth validade;
        private String cvv;
    }

    @Data
    public static class PassaporteDto {
        private String numero;
        private LocalDate dataDeValidade;
        private String pais;
    }

    public static CiclistaResponseDTO fromEntity(Ciclista ciclista) {
        CiclistaResponseDTO response = new CiclistaResponseDTO();
        
        response.setId(ciclista.getId());
        response.setStatus(ciclista.getStatus());
        response.setNomeCiclista(ciclista.getNomeCiclista());
        response.setDataNascimento(ciclista.getDataNascimento());
        response.setSenha(ciclista.getSenha()); 
        response.setNacionalidade(ciclista.getNacionalidade());
        response.setFotoDocumento(ciclista.getFotoDocumento());

        if (ciclista.getEmail() != null) {
            response.setEmail(ciclista.getEmail().getEndereco());
        }

        if (ciclista.getCpf() != null) {
            response.setCpf(ciclista.getCpf().getNumero());
        }

        if (ciclista.getCartao() != null) {
            CartaoDeCreditoDto cartaoDto = new CartaoDeCreditoDto();
            cartaoDto.setNomeTitular(ciclista.getCartao().getNomeNoCartao());
            cartaoDto.setNumero(ciclista.getCartao().getNumeroCartao());
            cartaoDto.setValidade(ciclista.getCartao().getValidade());
            cartaoDto.setCvv(ciclista.getCartao().getCodigoSeguranca()); 
            response.setCartaoDeCredito(cartaoDto);
        }

        if (ciclista.getPassaporte() != null) {
            PassaporteDto passaporteDto = new PassaporteDto();
            passaporteDto.setNumero(ciclista.getPassaporte().getNumeroPassaporte());
            passaporteDto.setDataDeValidade(ciclista.getPassaporte().getDataDeValidade());
            passaporteDto.setPais(ciclista.getPassaporte().getPais());
            response.setPassaporte(passaporteDto);
        }

        return response;
    }
}
