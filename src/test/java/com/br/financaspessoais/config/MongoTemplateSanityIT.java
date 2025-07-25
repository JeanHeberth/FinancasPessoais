package com.br.financaspessoais.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class MongoTemplateSanityIT {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void deveConectarAoBancoDeTestes() {
        String nomeDoBanco = mongoTemplate.getDb().getName();
        System.out.println(">>> MongoDB em uso (teste sanity): " + nomeDoBanco);
        assertEquals("financas-testes", nomeDoBanco, "Banco de dados incorreto nos testes!");
    }
}
