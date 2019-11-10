package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizacaoTopicoForm {

	@NotNull @NotEmpty
	private String titulo;
	
	@NotNull @NotEmpty
	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		
		Topico topico = topicoRepository.getOne(id); //recuperando o recurso que sera atualizado.
		
		topico.setTitulo(this.titulo); // sobrescrevendo com os novos dados vindos no json da requisição do cliente 
		
		topico.setMensagem(this.mensagem); // sobrescrevendo com os novos dados da requisição vindos no json da requisição do cliente 
		
		return topico; // devolve o recurso atualizado para ser persistido no banco
	}
	
	
	
	
}
