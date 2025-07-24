package com.br.financaspessoais.mapper;

import com.br.financaspessoais.dto.in.LancamentoRequestDTO;
import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.model.Lancamento;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring" )
public interface LancamentoMapper {

    Lancamento toEntity(LancamentoRequestDTO lancamentoRequestDTO);
    LancamentoResponseDTO toResponseDTO(Lancamento lancamento);
}
