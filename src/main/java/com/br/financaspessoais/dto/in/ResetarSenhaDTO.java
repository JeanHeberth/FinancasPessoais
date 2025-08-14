package com.br.financaspessoais.dto.in;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetarSenhaDTO {
    @NotBlank
    private String token;

    @NotBlank
    private String novaSenha;
}
