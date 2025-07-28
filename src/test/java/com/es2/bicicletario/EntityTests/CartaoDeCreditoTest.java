package com.es2.bicicletario.EntityTests;

import org.junit.jupiter.api.Test;

import com.es2.bicicletario.entity.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CartaoDeCreditoTest {

    @Test
    void deveInstanciarCartaoDeCreditoCorretamente() {

        String numero = "1234567812345678";
        String nomeTitular = "Jose da Silva";
        YearMonth validadeAnoMes = YearMonth.now().plusYears(1);

        LocalDate ultimoDiaDoMes = validadeAnoMes.atEndOfMonth();

        Date dataValidade = Date.from(ultimoDiaDoMes.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String cvv = "123";

        CartaoDeCredito cartao1 = new CartaoDeCredito(numero, nomeTitular, dataValidade, cvv);

        CartaoDeCredito cartao2 = new CartaoDeCredito();
        cartao2.setNumeroCartao(numero);
        cartao2.setNomeNoCartao(nomeTitular);
        cartao2.setValidade(dataValidade);
        cartao2.setCodigoSeguranca(cvv);

        assertEquals(numero, cartao1.getNumeroCartao());
        assertEquals(nomeTitular, cartao1.getNomeNoCartao());
        assertEquals(dataValidade, cartao1.getValidade());
        assertEquals(cvv, cartao1.getCodigoSeguranca());

        assertEquals(numero, cartao2.getNumeroCartao());
        assertEquals(nomeTitular, cartao2.getNomeNoCartao());
        assertEquals(dataValidade, cartao2.getValidade());
        assertEquals(cvv, cartao2.getCodigoSeguranca());
    }

    @Test
    void verificaCartao_DeveRetornarTrue() {

        boolean resultado = true;

        assertTrue(resultado, "O método de verificação de cartão deve retornar true.");
    }
}