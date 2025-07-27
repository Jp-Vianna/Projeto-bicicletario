package com.es2.bicicletario.dto;

public class EmailResponseDTO {

    private Long id;
    private String email;
    private String assunto;
    private String mensagem;

    // Construtor padrão
    public EmailResponseDTO() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "EmailResponseDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", assunto='" + assunto + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }
}
