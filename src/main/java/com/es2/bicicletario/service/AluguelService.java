package com.es2.bicicletario.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CartaoDeCreditoRequestDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.CobrancaRequestDTO;
import com.es2.bicicletario.dto.CobrancaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
import com.es2.bicicletario.dto.FuncionarioRequestDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.*;
import com.es2.bicicletario.repository.*;
import com.es2.bicicletario.validation.RegraDeNegocioException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AluguelService {

    private final FuncionarioRepository funcionarioRepository;
    private final DevolucaoRepository devolucaoRepository;
    private final AluguelRepository aluguelRepository;
    private final CiclistaRepository ciclistaRepository;
    private final EmailService emailService;
    private final WebClient webClient;

    @Transactional
    public CiclistaResponseDTO criarCiclista(CiclistaRequestDTO novoCiclista) {

        this.verificaDadosCiclista(novoCiclista);
        this.verificaDadosDuplicados(novoCiclista);

        Ciclista ciclista = new Ciclista();
        ciclista.setNomeCiclista(novoCiclista.getNomeCiclista());
        ciclista.setDataNascimento(novoCiclista.getDataNascimento());
        ciclista.setCartao(novoCiclista.getCartaoDeCredito().toEntity());
        ciclista.setSenha(novoCiclista.getSenha());
        ciclista.setFotoDocumento(novoCiclista.getFotoDocumento());
        ciclista.setStatus(Status.INATIVO);

        if (!Email.isValido(novoCiclista.getEmail())) {
            throw new RegraDeNegocioException("O e-mail está em um formato inválido.");
        }
        
        ciclista.setEmail(new Email(novoCiclista.getEmail()));

        Nacionalidade nacionalidade = novoCiclista.getNacionalidade();
        ciclista.setNacionalidade(nacionalidade);

        if (Nacionalidade.BRASILEIRO.equals(nacionalidade)) {
            // Validação específica para brasileiro
            if (novoCiclista.getCpf() == null || !Cpf.validarCpf(novoCiclista.getCpf())) {
                throw new RegraDeNegocioException("Para brasileiros, um CPF válido é obrigatório.");
            }
            ciclista.setCpf(new Cpf(novoCiclista.getCpf()));
            ciclista.setPassaporte(null);

        } else if (Nacionalidade.ESTRANGEIRO.equals(nacionalidade)) {
            // Validação específica para estrangeiro (com checagem de nulidade)
            if (novoCiclista.getPassaporte() == null || !Passaporte.validarPassaporte(novoCiclista.getPassaporte().toEntity())) {
                throw new RegraDeNegocioException("Para estrangeiros, um passaporte válido é obrigatório.");
            }
            ciclista.setPassaporte(novoCiclista.getPassaporte().toEntity());
            ciclista.setCpf(null);

        } else {
            throw new RegraDeNegocioException("A nacionalidade (BRASILEIRO ou ESTRANGEIRO) é obrigatória.");
        }

        Ciclista ciclistaSalvo = ciclistaRepository.save(ciclista);

        emailService.enviaEmail("joao.p.nv@edu.unirio.br", "Ciclista criado", "Seu ciclista: " + ciclista.toString() + "foi criado com sucesso!");

        return CiclistaResponseDTO.fromEntity(ciclistaSalvo);
    }

    @Transactional
    public CiclistaResponseDTO atualizarCiclista(Integer idCiclista, CiclistaRequestDTO ciclistaRequestDTO) {
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findById(idCiclista));

        this.verificaDadosCiclista(ciclistaRequestDTO);
        this.verificaDadosDuplicados(ciclistaRequestDTO);

        // Atualiza os campos do ciclista com os dados do DTO
        ciclista.setNomeCiclista(ciclistaRequestDTO.getNomeCiclista());
        ciclista.setDataNascimento(ciclistaRequestDTO.getDataNascimento());
        ciclista.setCartao(ciclistaRequestDTO.getCartaoDeCredito().toEntity());
        ciclista.setSenha(ciclistaRequestDTO.getSenha());
        ciclista.setEmail(new Email(ciclistaRequestDTO.getEmail()));

        Nacionalidade nacionalidade = ciclistaRequestDTO.getNacionalidade();
        ciclista.setNacionalidade(nacionalidade);

        if (Nacionalidade.BRASILEIRO.equals(nacionalidade)) {

            if (ciclistaRequestDTO.getCpf() == null || !Cpf.validarCpf(ciclistaRequestDTO.getCpf())) {
                throw new RegraDeNegocioException("Para brasileiros, um CPF válido é obrigatório.");
            }

            ciclista.setCpf(new Cpf(ciclistaRequestDTO.getCpf()));
            ciclista.setPassaporte(null);

        } else if (Nacionalidade.ESTRANGEIRO.equals(nacionalidade)) {

            if (ciclistaRequestDTO.getPassaporte() == null || !Passaporte.validarPassaporte(ciclistaRequestDTO.getPassaporte().toEntity())) {
                throw new RegraDeNegocioException("Para estrangeiros, um passaporte válido é obrigatório.");
            }

            ciclista.setPassaporte(ciclistaRequestDTO.getPassaporte().toEntity());
            ciclista.setCpf(null);

        }

        Ciclista ciclistaAtualizado = ciclistaRepository.save(ciclista);
        return CiclistaResponseDTO.fromEntity(ciclistaAtualizado);
    }

    public Optional<CiclistaResponseDTO> getCiclistaById(Integer idCiclista) {
        return ciclistaRepository.findById(idCiclista)
            .map(CiclistaResponseDTO::fromEntity);
    }

    public CartaoDeCredito getCartaoDeCredito(Integer idCiclista) {
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findById(idCiclista));
        
        return ciclista.getCartao(); 
    }

    @Transactional
    public void atualizarCartaoDeCredito(Integer idCiclista, CiclistaRequestDTO.CartaoDeCreditoDto cartaoDeCreditoDto) {
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findById(idCiclista));
        
        if (cartaoDeCreditoDto == null) {
            throw new RegraDeNegocioException("Os dados do cartão de crédito não podem ser nulos.");
        }

        CartaoDeCredito novoCartao = cartaoDeCreditoDto.toEntity();
        
        if (!CartaoDeCredito.verificaCartao()) {
            throw new RegraDeNegocioException("O cartão de crédito informado é inválido.");
        }

        ciclista.setCartao(novoCartao);
        ciclistaRepository.save(ciclista);
    }

    @Transactional
    public void ativarCiclista(Integer idCiclista) {
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findById(idCiclista));
        if (ciclista.getStatus() == Status.INATIVO) {
            ciclista.setStatus(Status.ATIVO);
            ciclistaRepository.save(ciclista);
        } else {
            throw new RegraDeNegocioException("O ciclista não está inativo.");
        }
    }

    public Boolean existeEmail(String email) {
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findByEmailEndereco(email));

        return ciclista != null;
    }


    // Métodos do funcionário


    public Funcionario criarFuncionario(Funcionario novoFuncionario) {
        return funcionarioRepository.save(novoFuncionario);
    }

    @Transactional
    public FuncionarioResponseDTO atualizarFuncionario(String matricula, FuncionarioRequestDTO funcionarioRequestDTO) {
        Funcionario funcionario = funcionarioRepository.findById(matricula)
                .orElseThrow(() -> new RegraDeNegocioException("Funcionário não encontrado com a matrícula: " + matricula));

        // Atualiza os campos do funcionário com os dados do DTO
        funcionario.setNome(funcionarioRequestDTO.getNome());
        funcionario.setIdade(funcionarioRequestDTO.getIdade());
        funcionario.setFuncao(funcionarioRequestDTO.getFuncao());
        funcionario.setEmail(new Email(funcionarioRequestDTO.getEmail()));
        
        // Opcionalmente, atualiza a senha se for fornecida
        if (funcionarioRequestDTO.getSenha() != null && !funcionarioRequestDTO.getSenha().isBlank()) {
            if (!funcionarioRequestDTO.getSenha().equals(funcionarioRequestDTO.getConfirmacaoSenha())) {
                throw new RegraDeNegocioException("A senha e a confirmação de senha não coincidem.");
            }
            funcionario.setSenha(funcionarioRequestDTO.getSenha());
        }

        Funcionario funcionarioAtualizado = funcionarioRepository.save(funcionario);
        return FuncionarioResponseDTO.fromEntity(funcionarioAtualizado);
    }

    public Optional<FuncionarioResponseDTO> getFuncionarioById(String matricula) {
    return funcionarioRepository.findById(matricula)
        .map(FuncionarioResponseDTO::fromEntity);
    }

    public List<FuncionarioResponseDTO> getFuncionarios() {
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        List<FuncionarioResponseDTO> funcDTO = new ArrayList<>();

        for (Funcionario func : funcionarios) {
            funcDTO.add(FuncionarioResponseDTO.fromEntity(func));
        }
        
        return funcDTO;
    }

    public Boolean deletarFuncionario(String matricula) {
        funcionarioRepository.deleteById(matricula);
        return true;
    }


    // Métodos de aluguel



    public boolean permiteAluguel(Integer idCiclista) {
       
        Ciclista ciclista = this.converteParaCiclista(ciclistaRepository.findById(idCiclista));
        
        List<Status> statusDesejados = List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE);
        List<Aluguel> alugueis = aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclista.getId(), statusDesejados);

        return alugueis.isEmpty() && ciclista.getStatus().equals(Status.ATIVO);
    }

    public Integer getBicicletaAlugada(Integer idCiclista) { // Ainda falta integrar
        Optional<Aluguel> optionalAluguel = aluguelRepository.findByCiclistaIdAndStatus(idCiclista, Status.EM_ANDAMENTO);
        Aluguel aluguel = optionalAluguel.orElseThrow(() -> new RuntimeException("O ciclista não tem aluguéis no momento."));

        if (aluguel == null) {
            return null;
        }

        return aluguel.getIdBicicleta();
    }

    
    @Transactional
    public AluguelResponseDTO realizarAluguel(AluguelRequestDTO novoAluguel) { // Integrar com trancas e bikes futuramente
        
        Ciclista ciclista = converteParaCiclista(ciclistaRepository.findById(novoAluguel.getIdCiclista()));

        if (!permiteAluguel(ciclista.getId())) {
            throw new RegraDeNegocioException("O aluguel não foi autorizado.");
        }       

        Aluguel aluguel = new Aluguel();

        aluguel.setTrancaInicial(novoAluguel.getIdTranca());
        aluguel.setCiclista(ciclista);
        aluguel.setHoraInicio(LocalDateTime.now());
        aluguel.setIdBicicleta(10001);
        aluguel.setStatus(Status.EM_ANDAMENTO);

        CobrancaRequestDTO cobrancaRequestDTO = new CobrancaRequestDTO();
        cobrancaRequestDTO.setValor(new BigDecimal("10.00"));
        cobrancaRequestDTO.setCiclista(ciclista.getId());

        try {
                webClient
                    .post()
                    .uri("/payment/cobranca")
                    .bodyValue(cobrancaRequestDTO)
                    .retrieve()
                    .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Falha na API externa. Status: " + response.statusCode() + ". Body: " + errorBody)))
                    )
                    .bodyToMono(CobrancaResponseDTO.class)
                    .block();

                emailService.enviaEmail(
                    "joao.p.nv@edu.unirio.br", "Pagamento efetuado.", "O pagamento padrão de 10,00 foi confirmado!");

            } catch (RuntimeException ex) {
                throw new RuntimeException("Não foi possível concluir pagamento.");
            }

        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);

        
        Tranca.destravaTranca();

        return AluguelResponseDTO.fromEntity(aluguelSalvo);
    }

    public AluguelResponseDTO getAluguelByIdBicicleta(Integer idBicicleta) {
        Optional<Aluguel> optionalAluguel = aluguelRepository.findByIdBicicletaAndStatus(idBicicleta, Status.EM_ANDAMENTO);
        Aluguel aluguel = optionalAluguel.orElseThrow(() -> new RuntimeException("Nenhum aluguel ativo com esta bicicleta."));
        
        return AluguelResponseDTO.fromEntity(aluguel);
    }

    @Transactional
    public DevolucaoResponseDTO realizarDevolucao(DevolucaoRequestDTO novaDevolucao) {

        Optional<Aluguel> optionalAluguel = aluguelRepository.findByIdBicicletaAndStatus(novaDevolucao.getIdBicicleta(), Status.EM_ANDAMENTO);
        Aluguel aluguel = optionalAluguel.orElseThrow(() -> new RuntimeException("Não encontrou alugueis ativos com essa bicicleta."));
        Ciclista ciclista = aluguel.getCiclista();

        LocalDateTime horaDevolucao = LocalDateTime.now();

        BigDecimal valorExtra = this.calcularValorExcedente(aluguel.getHoraInicio(), horaDevolucao);

        CobrancaRequestDTO cobrancaRequestDTO = new CobrancaRequestDTO();
        cobrancaRequestDTO.setValor(valorExtra);
        cobrancaRequestDTO.setCiclista(aluguel.getCiclista().getId());

        Devolucao devolucao = new Devolucao();

        if (valorExtra == BigDecimal.ZERO) {
            devolucao.setHoraCobranca(null);
            devolucao.setCobrancaExtra(null);
            devolucao.setNumCartao(null);
        }else{
            try {
                CobrancaResponseDTO respostaApi = webClient
                    .post()
                    .uri("/payment/cobranca")
                    .bodyValue(cobrancaRequestDTO)
                    .retrieve()
                    .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Falha na API externa. Status: " + response.statusCode() + ". Body: " + errorBody)))
                    )
                    .bodyToMono(CobrancaResponseDTO.class)
                    .block();

                devolucao.setCobrancaExtra(respostaApi.getValor());
                devolucao.setNumCartao(ciclista.getCartao().getNumeroCartao());
                devolucao.setHoraCobranca(respostaApi.getHoraFinalizacao());

                emailService.enviaEmail(
                    "joao.p.nv@edu.unirio.br", "Pagamento efetuado.", "O pagamento de " + valorExtra + "foi confirmado!");

            } catch (RuntimeException ex) {
                throw new RuntimeException("Não foi possível concluir pagamento.");
            }
            
        }

        devolucao.setTrancaFinal(novaDevolucao.getIdTranca());
        devolucao.setIdBicicleta(novaDevolucao.getIdBicicleta());
        devolucao.setHoraFim(horaDevolucao);
        aluguel.setStatus(Status.FINALIZADO);
        
        Aluguel aluguelAtualizado = aluguelRepository.save(aluguel); 

        devolucao.setAluguel(aluguelAtualizado);
        
        Devolucao devolucaoCriado = devolucaoRepository.save(devolucao);

        Tranca.travarTranca(); // Integrar

        return DevolucaoResponseDTO.fromEntity(devolucaoCriado);
    }


    // Métodos de utilidade

    private Ciclista converteParaCiclista(Optional<Ciclista> optionalCiclista){
        return optionalCiclista.orElseThrow(() -> new RuntimeException("Ciclista não encontrado."));
    }

    private void verificaDadosCiclista(CiclistaRequestDTO novoCiclista) {

        if (novoCiclista.getSenha() == null || !novoCiclista.getSenha().equals(novoCiclista.getConfirmacaoSenha())) {
            throw new RegraDeNegocioException("A senha e a confirmação de senha não coincidem.");
        }

        if (!Email.isValido(novoCiclista.getEmail())) {
            throw new RegraDeNegocioException("O e-mail está em um formato inválido.");
        }

        if (novoCiclista.getCartaoDeCredito() == null) {
            throw new RegraDeNegocioException("Dados do cartão de crédito são obrigatórios.");
        }

        CartaoDeCreditoRequestDTO cartaoDeCreditoRequestDTO = new CartaoDeCreditoRequestDTO();
        cartaoDeCreditoRequestDTO.setCvv(novoCiclista.getCartaoDeCredito().getCvv());
        cartaoDeCreditoRequestDTO.setNomeTitular(novoCiclista.getCartaoDeCredito().getNomeTitular());
        cartaoDeCreditoRequestDTO.setNumero(novoCiclista.getCartaoDeCredito().getNumero());
        cartaoDeCreditoRequestDTO.setValidade(novoCiclista.getCartaoDeCredito().getValidade());

        ResponseEntity<Void> responseValida;

        try {
            responseValida = webClient
                .post()
                .uri("/verifyCard/validaCartaoDeCredito")
                .bodyValue(cartaoDeCreditoRequestDTO)
                .retrieve()
                .onStatus(
                    status -> status.isError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RegraDeNegocioException("API de validação retornou erro: " + errorBody)))
                )
                .toBodilessEntity()
                .block();

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível se comunicar com o serviço de validação de cartão.", ex);
        }

        if (responseValida == null || responseValida.getStatusCode() != HttpStatus.OK) {
            throw new RegraDeNegocioException("O cartão de crédito fornecido é inválido ou não pôde ser verificado.");
        }
        
    }

    private void verificaDadosDuplicados(CiclistaRequestDTO novoCiclista){
        if (ciclistaRepository.findByEmailEndereco(novoCiclista.getEmail()).isPresent()) {
            throw new RegraDeNegocioException("Este e-mail já está em uso.");
        }

        if (novoCiclista.getNacionalidade() == Nacionalidade.BRASILEIRO) {

            if (ciclistaRepository.findByCpfNumero(novoCiclista.getCpf()).isPresent()) {
                throw new RegraDeNegocioException("Este CPF já está em uso.");
            }

        } else if (novoCiclista.getNacionalidade() == Nacionalidade.ESTRANGEIRO && novoCiclista.getPassaporte() != null &&
                ciclistaRepository.findByPassaporteNumeroPassaporte(novoCiclista.getPassaporte().getNumero()).isPresent()) {

                throw new RegraDeNegocioException("Este Passaporte já está em uso.");
        }
    }

    private BigDecimal calcularValorExcedente(LocalDateTime inicio, LocalDateTime fim) {

        Duration duracaoTotal = Duration.between(inicio, fim);

        long minutosTotais = duracaoTotal.toMinutes();

        final long LIMITE_MINUTOS_SEM_COBRANCA = 120;

        if (minutosTotais <= LIMITE_MINUTOS_SEM_COBRANCA) {
            return BigDecimal.ZERO;
        }

        long minutosExcedentes = minutosTotais - LIMITE_MINUTOS_SEM_COBRANCA;

        long blocosDe30Min = minutosExcedentes / 30;

        BigDecimal valorPorBloco = new BigDecimal("5.00");

        return valorPorBloco.multiply(new BigDecimal(blocosDe30Min));
    }
}
