package com.br.financaspessoais.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void enviarEmail(String destino, String assunto, String corpo) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");


            helper.setTo(destino);
            helper.setSubject(assunto);
            helper.setText(corpo, true);


            javaMailSender.send(mensagem);

            log.info("Email enviado para: {}", destino);
        }catch (MessagingException e){
            log.error("Erro ao enviar email para: {}", destino, e);
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}
