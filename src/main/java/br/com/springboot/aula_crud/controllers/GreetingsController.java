package br.com.springboot.aula_crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.aula_crud.model.Usuario;
import br.com.springboot.aula_crud.repository.UsuarioRepository;

/**
 *
 * A sample greetings controller to return greeting text
 */
@RestController
public class GreetingsController {
	
	@Autowired //IC ou CDI - Injeção de dependencia
	private UsuarioRepository usuarioRepository;
	
    /**
     *
     * @param name the name to greet
     * @return greeting text
     */
    @RequestMapping(value = "/mostranome/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
    	name = "Felipe Magalhães de Aguiar";
        return "Hello " + name + "!";
    }
    
    @RequestMapping(value = "/olamundo/{nome}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String metodoRetorno(@PathVariable String nome) {
    	// Teste
    	Usuario usuario = new Usuario();
    	usuario.setNome(nome);
    	
    	usuarioRepository.save(usuario); // Grava no banco de dados
    	
    	return "Olá mundo " + nome;
    }
    
   
    /* Primeiro método de API*/
    @GetMapping(value = "listatodos") // quase igual ao @RequestMapping
    @ResponseBody // Retorna os dados para o corpo da resposta
    public ResponseEntity<List<Usuario>> listaUsuario(){ // retornamos uma lista de usuários
    	
    	List<Usuario> usuarios = usuarioRepository.findAll(); // Execulta a consulta no banco de dados
    	
    	return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK); // Retorna a lista em JSON
    }
    
    // METODO PARA SALVAR    
    @PostMapping(value = "salvar") // minha url
    @ResponseBody //Requisição depois de salvar - Descrição da resposta
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){ // @RequestBody recebe os dados para salvar
    	
    	Usuario user = usuarioRepository.save(usuario); // instancio o usuario
    	
    	return new ResponseEntity<Usuario>(usuario, HttpStatus.CREATED);  // cria a retorna o usuário
    	/*
    	 * No Postman - Como criamos um Post, devemos selecionar o Post, mudar a url para salvar, ir em Body e selecionar 
    	 * raw e selecionar JSON
    	 * Escrevo os dados no postman - OBS: sem o Id, pois ele cria sozinho
    	 * {
        	"nome": "Felipe Aguiar",
        	"idade": 34
    		}
    	 */	
    }
    
    // METODO PARA ATUALIZAÇÃO
    @PutMapping(value = "atualizar") // minha url
    @ResponseBody //Requisição depois de salvar - Descrição da resposta
    public ResponseEntity<?> atualizar(@RequestBody Usuario usuario){ // @RequestBody recebe os dados para salvar
    	
    	//VERIFICAR SE O ID EXISTE
    	if (usuario.getId() == null) {
    		return new ResponseEntity<String>("Id não foi informado para atualização.", HttpStatus.OK);
    		
    	}
    	Usuario user = usuarioRepository.saveAndFlush(usuario); // instancio o usuario
    	
    	return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);  // cria a retorna o usuário
    	/*
    	 * No Postman - Como criamos um PUT, devemos selecionar o PUT, mudar a url para atualizar, ir em Body e selecionar 
    	 * raw e selecionar JSON
    	 * Escrevo os dados no postman que quero atualizar - OBS: é preciso ter o id para atualizar
    	 * {
         "id": 1,
         "nome": "Mel",
         "idade": 15
         }
    	 */	
    }
    
    
    // METODO PARA DELETAR
    @DeleteMapping(value = "delete") // minha url
    @ResponseBody //Requisição depois de salvar - Descrição da resposta
    public ResponseEntity<String> delete(@RequestParam Long iduser){ // @RequestBody recebe os dados para salvar
    	
    	usuarioRepository.deleteById(iduser);; // Deleta o usuario
    	
    	return new ResponseEntity<String>("User deletado com sucesso", HttpStatus.OK);  
    	
    	/*
    	 * No Postman - Como criamos um Delete, devemos selecionar o Delete, mudar a url para delete, ir em Body e selecionar 
    	 * x-www-form-urlencoded
    	 * 
    	 * KEY - iduser - NOME QUE UTILIZAMOS
    	 * VALUE - O ID QUE QUER DELETAR
    	 */	
    }
    
    // BUSCAR USUARIO POR ID
    @GetMapping(value = "buscaruserid") // minha url
    @ResponseBody //Requisição depois de salvar - Descrição da resposta
    public ResponseEntity<Usuario>buscaruserid(@RequestParam(name = "iduser") Long iduser){ // @RequestBody recebe os dados para consultar
    	
    	Usuario usuario = usuarioRepository.findById(iduser).get();
    	
    	
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK); 
    	
    	/*
    	 * No Postman - Como criamos uma Pesquisa, devemos selecionar o GET, mudar a url para buscaruserid, ir em Body e selecionar 
    	 * form-data
    	 * 
    	 * KEY - iduser - NOME QUE UTILIZAMOS
    	 * VALUE - O ID QUE QUER DELETAR
    	 */	
    }
    
    // BUSCAR USUARIO POR NOME
    @GetMapping(value = "buscarPorNome")
    @ResponseBody
    public ResponseEntity<List<Usuario>>buscarPorNome(@RequestParam (name = "name") String name){
    	
    	List<Usuario> usuario = usuarioRepository.buscarPorNome(name.trim().toUpperCase()); // o .trim() - tira os espaços    	
    	return new ResponseEntity<List<Usuario>>(usuario, HttpStatus.OK);
    }
    
    /*
     * Ir no UsuarioRepository e digitar
     * 	@Query(value = "select u from Usuario u Where upper(trim(u.nome)) like %?1%")
		List<Usuario> buscarPorNome(String name);	
		
		No Postman - Como criamos uma Pesquisa, devemos selecionar o GET, mudar a url para buscarPorNome, ir em Body e selecionar 
    	 * form-data
    	 * 
    	 * KEY - iduser - NOME QUE UTILIZAMOS
    	 * VALUE - o nome que procura
     */
    
    
}
