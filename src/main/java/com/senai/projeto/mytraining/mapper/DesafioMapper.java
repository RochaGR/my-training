package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.model.Desafio;
import org.springframework.stereotype.Component;

@Component
public class DesafioMapper {

    public Desafio toEntity(DesafioRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Desafio desafio = new Desafio();
        desafio.setTitulo(dto.titulo());
        desafio.setDescricao(dto.descricao());
        desafio.setDataInicio(dto.dataInicio());
        desafio.setDataFim(dto.dataFim());
        desafio.setObjetivoValor(dto.objetivoValor());
        desafio.setUnidade(dto.unidade());
        desafio.setStatus(dto.status());

        return desafio;
    }

    public DesafioResponseDTO toResponseDTO(Desafio desafio) {
        if (desafio == null) {
            return null;
        }

        return new DesafioResponseDTO(
                desafio.getId(),
                desafio.getTitulo(),
                desafio.getDescricao(),
                desafio.getDataInicio(),
                desafio.getDataFim(),
                desafio.getObjetivoValor(),
                desafio.getUnidade(),
                desafio.getStatus()
        );
    }

    public void updateEntityFromDTO(DesafioRequestDTO dto, Desafio desafio) {
        if (dto == null || desafio == null) {
            return;
        }

        desafio.setTitulo(dto.titulo());
        desafio.setDescricao(dto.descricao());
        desafio.setDataInicio(dto.dataInicio());
        desafio.setDataFim(dto.dataFim());
        desafio.setObjetivoValor(dto.objetivoValor());
        desafio.setUnidade(dto.unidade());
        desafio.setStatus(dto.status());
    }
}