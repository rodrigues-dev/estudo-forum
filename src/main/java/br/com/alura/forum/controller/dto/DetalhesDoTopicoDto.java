package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.model.Resposta;
import br.com.alura.forum.model.StatusTopico;
import br.com.alura.forum.model.Topico;

public class DetalhesDoTopicoDto {
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	private String nomeAutor;
	private StatusTopico status;
	private List<RespostaDto> respostas;
	
	public DetalhesDoTopicoDto(Topico topico) {
		
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
		this.nomeAutor = topico.getAutor().getNome();
		this.status = topico.getSatatus();
		this.respostas = new ArrayList<>();
		/*
		 *  Java8: .stream().map(RespostaDto::new): para cada resposta da lista é feito um mapeamento com a respostaDto
		 *  .collect(Collectors.toList()): coleta a lista com as respotas mapeados para respostasDto e passa para uma nova lista.
		 */
		this.respostas.addAll(topico.getRespostas().stream().map(RespostaDto::new).collect(Collectors.toList()));
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public String getNomeAutor() {
		return nomeAutor;
	}

	public StatusTopico getStatus() {
		return status;
	}

	public List<RespostaDto> getRespostas() {
		return respostas;
	}
	
}
