package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //habilita a spring security no projeto
@Configuration 	   //ao iniciar a aplicação, carrega e le as configurações desse classe
public class SecurityConfigurations extends WebSecurityConfigurerAdapter { // por default tudo fica bloqueado. desbloqueamos a partir das configurações
	//WebSecurityConfigurerAdapter: possui os métodos (configure) que devemos configurar as regras de segurança da nossa aplicação.
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	// configurações de autenticação. exemplo: perfis
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder()); //senha 123456
	}
	
	// configurações de autorização. exemplo: acesso a URL's
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll() // libera a acesso para essa URI com o método GET
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll() // libera a acesso para essa URI com o método GET
			.anyRequest().authenticated() //todas as demais requests precisa de autenticação
			.and().formLogin(); //gera um formulário em bootstrap de autenticação (login). precisa implementar a logica de login.
	}
	
	// configurações de arquivos estáticos, exemplo: css, imagens e etc..
	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}

}
