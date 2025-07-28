package com.br.financaspessoais.mapper;

import com.br.financaspessoais.dto.in.UsuarioRequestDTO;
import com.br.financaspessoais.dto.out.UsuarioResponseDTO;
import com.br.financaspessoais.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring" )
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);
    UsuarioResponseDTO toResponseDTO(Usuario usuario);
}
