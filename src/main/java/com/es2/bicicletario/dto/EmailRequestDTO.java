package com.es2.bicicletario.dto;

public class EmailRequestDTO {

    private String email;
    private String assunto;
    private String mensagem;

    // Construtor padrão
    public EmailRequestDTO() {
    }

    // Construtor com todos os campos
    public EmailRequestDTO(String email, String assunto, String mensagem) {
        this.email = email;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    // Getters e Setters
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
        return "EmailRequestDTO{" +
                "email='" + email + '\'' +
                ", assunto='" + assunto + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }
}
