package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// essa classe será chamada para todo RestController, não precisa anotar nada nas classes controller
@RestControllerAdvice //serve como interceptador de exceptions para fazer tratamentos de erro
// existe tambem o @ControllerAdvice
public class ErroDeValidacaoHandler {

	@Autowired
	private MessageSource messageSource; // ajuda na captura da mensagem de erro
	
	@ ResponseStatus(code = HttpStatus.BAD_REQUEST) // informa qual status serrá retornado ao client
	@ExceptionHandler(MethodArgumentNotValidException.class)
	// informa ao spring que esse método deve ser chamado quando houver alguma exceção no em algum controller
	// passamos no parametro do exceptionhandler o tipo de erro que ele irá tratar
	// MethodArgumentNotValidException: trata erros de validação de formulário
	public List<ErroDeFormularioDto> handle (MethodArgumentNotValidException exception) { // recebe uma lista com todas as exceptions lançadas
		
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		List<FieldError> fielderror = exception.getBindingResult().getFieldErrors();
		
		fielderror.forEach(e ->{
			// e: menssagem de erro; LocaleContextHolder.getLocale: traduz a mensagem de erro para o idioma do cliente
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale()); 
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}
	
}
