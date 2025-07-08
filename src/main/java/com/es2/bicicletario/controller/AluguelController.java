package com.es2.bicicletario.controller;

import com.es2.bicicletario.dto.*;
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
     * Atualiza os dados de um ciclista.
     * @param idCiclista O ID do ciclista a ser atualizado.
     * @param ciclistaRequestDTO DTO com os novos dados do ciclista.
     * @return O ciclista com os dados atualizados.
     */
    @PutMapping("/ciclista/{idCiclista}")
    public ResponseEntity<CiclistaResponseDTO> atualizarCiclista(@PathVariable Integer idCiclista, @Valid @RequestBody CiclistaRequestDTO ciclistaRequestDTO) {
        CiclistaResponseDTO ciclistaAtualizado = service.atualizarCiclista(idCiclista, ciclistaRequestDTO);
        return ResponseEntity.ok(ciclistaAtualizado);
    }

    /**
     * Ativa um ciclista inativo.
     * @param idCiclista O ID do ciclista a ser ativado.
     * @return Status 204 No Content em caso de sucesso.
     */
    @PostMapping("/ciclista/{idCiclista}/ativar")
    public ResponseEntity<Void> ativarCiclista(@PathVariable Integer idCiclista) {
        service.ativarCiclista(idCiclista);
        return ResponseEntity.noContent().build();
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

    /**
     * Atualiza o cartão de crédito de um ciclista.
     * @param idCiclista O ID do ciclista.
     * @param cartaoDeCreditoDto DTO com os novos dados do cartão de crédito.
     * @return Status 204 No Content em caso de sucesso.
     */
    @PutMapping("/cartao-de-credito/{idCiclista}")
    public ResponseEntity<Void> atualizarCartaoDeCredito(@PathVariable Integer idCiclista, @Valid @RequestBody CiclistaRequestDTO.CartaoDeCreditoDto cartaoDeCreditoDto) {
        service.atualizarCartaoDeCredito(idCiclista, cartaoDeCreditoDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retorna o ID da bicicleta atualmente alugada por um ciclista.
     * @param idCiclista O ID do ciclista.
     * @return O ID da bicicleta alugada (200 OK) ou 404 Not Found se não houver aluguel ativo.
     */
    @GetMapping("/ciclista/{idCiclista}/bicicletaalugada")
    public ResponseEntity<Integer> getBicicletaAlugada(@PathVariable Integer idCiclista) {
        try {
            Integer idBicicleta = service.getBicicletaAlugada(idCiclista);
            return ResponseEntity.ok(idBicicleta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
     * Atualiza os dados de um funcionário.
     * @param matricula A matrícula do funcionário a ser atualizado.
     * @param funcionarioRequestDTO DTO com os novos dados do funcionário.
     * @return O funcionário com os dados atualizados.
     */
    @PutMapping("/funcionarios/{matricula}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(@PathVariable String matricula, @Valid @RequestBody FuncionarioRequestDTO funcionarioRequestDTO) {
        FuncionarioResponseDTO funcionarioAtualizado = service.atualizarFuncionario(matricula, funcionarioRequestDTO);
        return ResponseEntity.ok(funcionarioAtualizado);
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
     * Verifica se um ciclista pode realizar um novo aluguel.
     * @param idCiclista O ID do ciclista.
     * @return true se o ciclista pode alugar, false caso contrário.
     */
    @GetMapping("/ciclista/{idCiclista}/permitiraluguel")
    public ResponseEntity<Boolean> permitirAluguel(@PathVariable Integer idCiclista) {
        try {
            boolean podeAlugar = service.permiteAluguel(idCiclista);

            return ResponseEntity.ok(podeAlugar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); 
        }
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
