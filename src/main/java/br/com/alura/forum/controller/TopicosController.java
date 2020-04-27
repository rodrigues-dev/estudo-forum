package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
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
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, 
			@PageableDefault(sort = "id", direction = Direction.DESC, size = 2) Pageable paginacao) {//recebe os dados por parametros da url.
		// Exemplo de Pageable na URL: http://localhost:8080/topicos?page=0&size=3&sort=dataCriacao,asc
		
		//padrão DTO (TopicoDto): manda dados da api para o cliente.
		
		/* ## PAGINACAO - do DATA JPA do Spring ##
		 * Pageable: interface que ajuda a trabalhar com paginação.
		 * PageRequest.of(): classe que através do método estático .of cria uma paginacao.
		 * Page<?>: interface que recebe além dos registros, informações como, total de registros, registros por pagina etc.
		 * Ordenação: ordena a partir de um atributo da classe e o Enum Direction.
		 * @PageableDefault: define valores default para os atributos do Pageable, caso não sejam informados na requisição.
		 */
		
		/* Uso do Objeto Pageable recebido por parâmetro:
		 * page: informa a pagina 
		 * size: informa a quantidade de registro por pagina
		 * sort: ordena os registros por um atributo específico e define se ordena de forma crescente (ASC) ou decrescente (DESC)
		 Obs:o sort pode ser passado mais de uma vez. 
		 Obs: o Pageable deixa opcional o uso dos atributos (page, size, sort)*/ 
		
		Page<Topico> topicos;
		
		if (nomeCurso == null) {
			//usando os beneficios da injeção de dependencia: topicoRepository.findAll() do Data JPA, retorna todos os registros do banco.
			 topicos = topicoRepository.findAll(paginacao);
		} else {
			//usando o padrao de nomenclatura do Data JPA (findByCurso_Nome) é criado automaticamente a quary filtrando por nome do curso.
			topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
		}
		return TopicoDto.converter(topicos);
	}
	
	@PostMapping //usando o metodo POST.
	@Transactional // Anotação recomendada sempre que envolver uma operação de salvar, atualizar ou deletar (transactional)
	//dentro do ResponseEntity vem o tipo de retorno que virá dentro do corpo da requisição.
	public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) { 
		// RequestBody: recebe os dados por parametro do corpo da requisição.
		// Valid: é uma especificação do Bean Validation que informa ao spring que deve rodar as validações que estão anotadas no TopicoForm.
		// UriComponentsBuilder: o dominio da aplicação (ex.: http://minhaapi.com) é injetodo automaticamente pelo spring. ajuda na construção da uri.
		// padrão DTO (TopicoForm): manda dados do cliente para a api.
		Topico topico = form.converter(cursoRepository);

		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}")//inicio da uri do recurso.
				.buildAndExpand(topico.getId())//pega o id do topico e inclui na formação da uri de forma dinamica.
				.toUri();//cria a uri completa, incluindo o servidor.
		
		//o metodo created retorna o codigo 201 que precisa do uma URI e Uma representação do recurso no corpo da requisição. (BOA PRÁTICA!!)
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		//metodos com retorno void, caso ocorra tudo certo, retorna o codigo 200 (ok) por padrão.
	}
	
	@GetMapping("/{id}") // URL dinamica: recebe como parte da URL o id de forma dinamica 
	// @PathVariable: indica que recebera o id pela URL mapeado no @GetMapping("/{id}") e ambos precisam ter o mesmo nome (id).
	public ResponseEntity<DetalhesDoTopicoDto>  detalhar (@PathVariable Long id) { // para usar outra nome no parametro do método: @PathVariable("id") Long codigo

		//Topico topico = topicoRepository.getOne(id); // retorna um registro pelo id. sempre considera que o id passado existe
		
		Optional<Topico> topico = topicoRepository.findById(id); // retorna um optional que verifica se existe um recurso com o id informado
		
		if (topico.isPresent())  // se existe
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get())); // topico.get(): por ser um optional precisa do .get para pegar o objeto topico.
		
		
		return ResponseEntity.notFound().build(); // retorno o status 404 de recurso não encontrado
	}
	
	@PutMapping("/{id}") // método utilizado para realizar atualização de registros. id informa qual recurso será atualizado.
	// Obs: existe também o método @PatchMapping para atualização. o put atualiza tudo e o patch atualiza apenas os atributos alterados.
	@Transactional // informa ao spring que após a execução do método, deve executar as alterações no banco de dados
	public ResponseEntity<TopicoDto> atualizar (@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) { 
			Topico topico = form.atualizar(id, topicoRepository);
			
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover (@PathVariable Long id) {
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			topicoRepository.deleteById(id); // basta chamar o método deleteById do Repository para deletar um recurso do banco
			
			return ResponseEntity.ok().build();	
		}
		
		return ResponseEntity.notFound().build();
	}
}
