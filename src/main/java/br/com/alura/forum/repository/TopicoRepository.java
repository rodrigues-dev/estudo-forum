package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.model.Topico;

/*
 * Padrão Repository: alternativa ao padrão DAO. Herdando de JpaReporitory teremos o crud completo já implementado
 * precisamos apenas passar a Entity (nesse caso: Topico) e o tipo do id usado na entity (nesse caso: Long).
 * Obs: essa interface será injetada nos controllers que for necessário (injeção de dependencia).
 * 
 * o Spring Data JPA consegue gerar a query de consulta ao banco de dados baseado no nome do método na classe repository.
 */
public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//cria uma query automaticamente usando a convenão do Data JPA repository: findBy<<nome do atributo>> ou <<classe relacionamento_atributo da classe>>
	List<Topico> findByCurso_Nome(String nomeCurso);//so com essa assinatura o jpa já monta a quary.
	
	/*
	 * caso eu queira crias na mão a mesma coisa que o metodo findByCurso_Nome faz.
	 * devo escrever a consulta em JPQL.
	 */
	//@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	//List<Topico> carregarPorNomeDoCurso (@Param("nomeCurso") String nomeCurso);

}
