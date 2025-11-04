package com.senai.projeto.mytraining.mapper;

import com.senai.projeto.mytraining.dto.request.DesafioRequestDTO;
import com.senai.projeto.mytraining.dto.response.DesafioResponseDTO;
import com.senai.projeto.mytraining.model.Desafio;
import com.senai.projeto.mytraining.model.Usuario;
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
        desafio.setProgressoAtual(dto.progressoAtual() != null ? dto.progressoAtual() : 0.0);
        desafio.setUnidade(dto.unidade());
        desafio.setStatus(dto.status());

        return desafio;
    }

    public Desafio toEntity(DesafioRequestDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Desafio desafio = new Desafio();
        desafio.setTitulo(dto.titulo());
        desafio.setDescricao(dto.descricao());
        desafio.setDataInicio(dto.dataInicio());
        desafio.setDataFim(dto.dataFim());
        desafio.setObjetivoValor(dto.objetivoValor());
        desafio.setProgressoAtual(dto.progressoAtual() != null ? dto.progressoAtual() : 0.0);
        desafio.setUnidade(dto.unidade());
        desafio.setStatus(dto.status());
        desafio.setUsuario(usuario);

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
                desafio.getProgressoAtual(),
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
        desafio.setProgressoAtual(dto.progressoAtual() != null ? dto.progressoAtual() : desafio.getProgressoAtual());
        desafio.setUnidade(dto.unidade());
        desafio.setStatus(dto.status());
    }
}