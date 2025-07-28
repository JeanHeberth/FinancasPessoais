package com.br.financaspessoais.mapper;

import com.br.financaspessoais.dto.in.LancamentoRequestDTO;
import com.br.financaspessoais.dto.out.LancamentoResponseDTO;
import com.br.financaspessoais.model.Lancamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring" )
public interface LancamentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Lancamento toEntity(LancamentoRequestDTO lancamentoRequestDTO);
    LancamentoResponseDTO toResponseDTO(Lancamento lancamento);
}
