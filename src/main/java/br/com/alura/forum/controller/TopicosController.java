package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
public class TopicosController {
	
	@Autowired //anotação para fazer injeção de dependência
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
	//para passar parametro no navegador: ?nomeCurso=Spring+Boot
	public List<TopicoDto> lista(String nomeCurso) {//recebe o parametro do navegador
		
		List<Topico> Topicos;
		
		if (nomeCurso == null) {
			//usando os beneficios da injeção de dependencia: topicoRepository.findAll() do Data JPA, retorna todos os registros do banco.
			Topicos = topicoRepository.findAll();	
		} else {
			//usando o padrao de nomenclatura do Data JPA (findByCurso_Nome) é criado automaticamente a quary filtrando por nome do curso.
			Topicos = topicoRepository.findByCurso_Nome(nomeCurso);
		}
		
		return TopicoDto.converter(Topicos);
	}

}
