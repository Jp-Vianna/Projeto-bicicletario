package com.es2.bicicletario.controller;

import com.es2.bicicletario.dto.*;
import com.es2.bicicletario.entity.CartaoDeCredito;
import com.es2.bicicletario.entity.Funcionario;
import com.es2.bicicletario.service.AluguelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api") 
public class AluguelController {

    private final AluguelService service;

    public AluguelController(AluguelService service) {
        this.service = service;
    }

    /**
     * Cria um novo ciclista no sistema.
     * @param ciclistaRequestDTO DTO com os dados do novo ciclista.
     * @return O ciclista criado com status 201 Created.
     */
    @PostMapping("/ciclistas")
    public ResponseEntity<CiclistaResponseDTO> criarCiclista(@Valid @RequestBody CiclistaRequestDTO ciclistaRequestDTO) {
        CiclistaResponseDTO ciclistaSalvo = service.criarCiclista(ciclistaRequestDTO);
        return new ResponseEntity<>(ciclistaSalvo, HttpStatus.CREATED);
    }

    /**
     * Busca um ciclista pelo seu ID.
     * @param id O ID do ciclista.
     * @return O ciclista encontrado (200 OK) ou 404 Not Found.
     */
    @GetMapping("/ciclistas/{id}")
    public ResponseEntity<CiclistaResponseDTO> getCiclistaById(@PathVariable Integer id) {
        return service.getCiclistaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Verifica se um e-mail já existe no sistema.
     * @param email O e-mail a ser verificado.
     * @return true se o e-mail existir, false caso contrário.
     */
    @GetMapping("/ciclistas/verificar-email/{email}")
    public ResponseEntity<Boolean> existeEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.existeEmail(email));
    }

    // --- Endpoints para Funcionario ---

    /**
     * Retorna uma lista com todos os funcionários.
     * @return Lista de funcionários.
     */
    @GetMapping("/funcionarios")
    public ResponseEntity<List<FuncionarioResponseDTO>> getFuncionarios() {
        return ResponseEntity.ok(service.getFuncionarios());
    }
    
    /**
     * Busca um funcionário pela sua matrícula.
     * @param matricula A matrícula do funcionário.
     * @return O funcionário encontrado (200 OK) ou 404 Not Found.
     */
    @GetMapping("/funcionarios/{matricula}")
    public ResponseEntity<FuncionarioResponseDTO> getFuncionarioById(@PathVariable String matricula) {
        return service.getFuncionarioById(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo funcionário.
     * @param novoFuncionario Objeto com os dados do novo funcionário.
     * @return O funcionário criado com status 201 Created.
     */
    @PostMapping("/funcionarios")
    public ResponseEntity<Funcionario> criarFuncionario(@Valid @RequestBody Funcionario novoFuncionario) {

        Funcionario funcionarioSalvo = service.criarFuncionario(novoFuncionario);
        
        return new ResponseEntity<>(funcionarioSalvo, HttpStatus.CREATED);
    }
    
    /**
     * Remove um funcionário do sistema.
     * @param matricula A matrícula do funcionário a ser removido.
     * @return Status 204 No Content em caso de sucesso.
     */
    @DeleteMapping("/funcionarios/{matricula}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable String matricula) {
        service.deletarFuncionario(matricula);
        return ResponseEntity.noContent().build();
    }


    // --- Endpoints para Aluguel e Devolução ---

    /**
     * Realiza um novo aluguel de bicicleta.
     * @param aluguelRequestDTO DTO com os dados do aluguel.
     * @return Os dados do aluguel realizado com status 201 Created.
     */
    @PostMapping("/alugueis")
    public ResponseEntity<AluguelResponseDTO> realizarAluguel(@Valid @RequestBody AluguelRequestDTO aluguelRequestDTO) {
        AluguelResponseDTO aluguelRealizado = service.realizarAluguel(aluguelRequestDTO);
        return new ResponseEntity<>(aluguelRealizado, HttpStatus.CREATED);
    }
    
    /**
     * Realiza a devolução de uma bicicleta alugada.
     * @param devolucaoRequestDTO DTO com os dados da devolução.
     * @return Os dados da devolução realizada com status 201 Created.
     */
    @PostMapping("/devolucoes")
    public ResponseEntity<DevolucaoResponseDTO> realizarDevolucao(@Valid @RequestBody DevolucaoRequestDTO devolucaoRequestDTO) {
  
        DevolucaoResponseDTO devolucaoRealizada = service.realizarDevolucao(devolucaoRequestDTO);
        return new ResponseEntity<>(devolucaoRealizada, HttpStatus.CREATED);
        
    }
}
