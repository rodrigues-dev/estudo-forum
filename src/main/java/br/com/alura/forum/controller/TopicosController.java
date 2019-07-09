package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos") //quando o RequestMapping está sob a classe ele se torna um prefixo para acessar todos os metodos dela.
public class TopicosController {
	
	@Autowired //anotação para fazer injeção de dependência
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping //usando o metodo GET.
	//para passar parametro no navegador: ?nomeCurso=Spring+Boot
	public List<TopicoDto> lista(String nomeCurso) {//recebe os dados por parametros da url.
		//padrão DTO (TopicoDto): manda dados da api para o cliente.
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
	
	@PostMapping //usando o metodo POST.
	public void cadastrar (@RequestBody TopicoForm form) { // RequestBody: recebe os dados por parametro do corpo da requisição.
		//padrão DTO (TopicoForm): manda dados do cliente para a api.
		Topico topico = form.converter(cursoRepository);
		
		topicoRepository.save(topico);
	}

}
