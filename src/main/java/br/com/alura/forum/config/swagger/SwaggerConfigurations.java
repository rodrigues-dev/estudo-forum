package br.com.alura.forum.config.swagger;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.model.Usuario;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/*
 * Endereço que mostra a documentação da nossa API: /swagger-ui.html
 */

@Configuration
public class SwaggerConfigurations {
	
	@Bean //informa ao spring que estamos exportando o Docket
	public Docket forumApi () {
		/* Precisamos retornar um Docket devidamente configurado para ler a
		 * nossa API, além de poder adicionar outras configurações
		 */
		return new Docket(DocumentationType.SWAGGER_2) // tipo de documentação
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.alura.forum")) // a partir de qual pacote ele deve começar a leitura
				.paths(PathSelectors.ant("/**")) // quais endpoints deve ser analisado para montar a documentação (nesse caso, serão todos)
				.build()
				.ignoredParameterTypes(Usuario.class) //ignorar todos nos endpoints os parametros que trabalham com Usuário. por uma questão de segurança
				.globalOperationParameters(Arrays.asList( //adiciona paramentros na documentação
						new ParameterBuilder() //adicianando apenas o parametro do Authorization
						.name("Authorization") //nome do parametro
						.description("Header para token JWT") //descrição do parametro
						.modelRef(new ModelRef("string")) // formato do parametro
						.parameterType("header") // tipo do parametro. não é um parametro de url
						.required(false) // não obrigatorio
						.build()));
	}
	
	
}
