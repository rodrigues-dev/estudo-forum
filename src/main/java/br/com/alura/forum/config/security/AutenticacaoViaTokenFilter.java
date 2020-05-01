package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/* classe que intercepta 
 * 
 */
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository;
	// recebemos o tokenService e o usuarioRepository, pois não é possivel injetar diretamente essas classes
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = recuperarToken(request);
		
		boolean valido = tokenService.isTokenValido(token);
		
		if (valido)
			autenticarCliente(token);
			
		filterChain.doFilter(request, response); //informa que a requisição pode seguir
	}

	//processo de autenticação
	private void autenticarCliente(String token) {
		//recuperamos o id do usuario
		Long idUsuario = tokenService.getIdUsuario(token);
		
		//recuperamos o objeto usuário
		Usuario usuario = usuarioRepository.findById(idUsuario).get(); //carrega o objeto
		
		//validamos 
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		
		//autenticamos uma requisição específica 
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		
		String token = request.getHeader("Authorization");
		
		//verifica se o token está no formato válido
		if (token == null || token.isEmpty() || !token.startsWith("Bearer "))
			return null;
		//devolve apenas o token. sem o "Bearer "
		return token.substring(7, token.length());
	}

}
