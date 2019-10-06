package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
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
	//dentro do ResponseEntity vem o tipo de retorno que virá dentro do corpo da requisição.
	public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { 
		// RequestBody: recebe os dados por parametro do corpo da requisição.
		// Valid: é uma especificação do Bean Validation que informa ao spring que deve rodar as validações que estão anotadas no TopicoForm.
		// UriComponentsBuilder: é injetodo automaticamente pelo spring. ajuda na construção da uri.
		// padrão DTO (TopicoForm): manda dados do cliente para a api.
		Topico topico = form.converter(cursoRepository);

		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}")//inicio da uri do recurso.
				.buildAndExpand(topico.getId())//pega o id do topico e inclui na formação da uri de forma dinamica.
				.toUri();//cria a uri completa, incluindo o servidor.
		
		//o metodo created retorna o codigo 201 que precisa do uma URI e Uma representação do recurso no corpo da requisição.
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		//metodos com retorno void, caso ocorra tudo certo, retorna o codigo 200 (ok) por padrão.
	}
	
	@GetMapping("/{id}") // URL dinamica: recebe como parte da URL o id de forma dinamica 
	// @PathVariable: indica que recebera o id pela URL mapeado no @GetMapping("/{id}") e ambos precisam ter o mesmo nome (id).
	public DetalhesDoTopicoDto detalhar (@PathVariable Long id) { // para usar outra nome no parametro do método: @PathVariable("id") Long codigo
		
		Topico topico = topicoRepository.getOne(id); // retorna um registro pelo id
		
		return new DetalhesDoTopicoDto(topico);
	}

}
