package com.br.financaspessoais.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class EmailTemplateFileService {

    private static final String TEMPLATE_PATH = "templates/email/";

    /**
     * Carrega um template HTML do arquivo e substitui as variáveis
     */
    private String carregarTemplate(String nomeTemplate) {
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH + nomeTemplate);
            Path path = resource.getFile().toPath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Erro ao carregar template: {}", nomeTemplate, e);
            throw new RuntimeException("Erro ao carregar template de email: " + nomeTemplate, e);
        }
    }

    /**
     * Substitui variáveis no template usando placeholders simples
     */
    private String substituirVariaveis(String template, String... substituicoes) {
        String resultado = template;
        for (int i = 0; i < substituicoes.length; i += 2) {
            if (i + 1 < substituicoes.length) {
                resultado = resultado.replace(substituicoes[i], substituicoes[i + 1]);
            }
        }
        return resultado;
    }

    /**
     * Cria email de redefinição de senha
     */
    public String criarEmailRedefinicaoSenha(String nomeUsuario, String linkRedefinicao) {
        log.debug("Criando email de redefinição para usuário: {}", nomeUsuario);
        
        String template = carregarTemplate("password-reset.html");
        
        return substituirVariaveis(template,
            "{{nomeUsuario}}", nomeUsuario,
            "{{linkRedefinicao}}", linkRedefinicao
        );
    }

    /**
     * Cria email de confirmação de senha redefinida
     */
    public String criarEmailConfirmacaoSenha(String nomeUsuario) {
        log.debug("Criando email de confirmação para usuário: {}", nomeUsuario);
        
        String template = carregarTemplate("password-confirmation.html");
        
        return substituirVariaveis(template,
            "{{nomeUsuario}}", nomeUsuario
        );
    }
}