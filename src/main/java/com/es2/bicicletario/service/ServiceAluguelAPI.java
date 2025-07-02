package com.es2.bicicletario.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.es2.bicicletario.dto.AluguelRequestDTO;
import com.es2.bicicletario.dto.AluguelResponseDTO;
import com.es2.bicicletario.dto.CiclistaRequestDTO;
import com.es2.bicicletario.dto.CiclistaResponseDTO;
import com.es2.bicicletario.dto.DevolucaoRequestDTO;
import com.es2.bicicletario.dto.DevolucaoResponseDTO;
import com.es2.bicicletario.dto.FuncionarioResponseDTO;
import com.es2.bicicletario.entity.*;
import com.es2.bicicletario.repository.*;
import com.es2.bicicletario.validation.RegraDeNegocioException;

@Service
public class ServiceAluguelAPI {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DevolucaoRepository devolucaoRepository;

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CiclistaRepository ciclistaRepository;

    /**
     * Cria um novo ciclista no sistema, aplicando validações de negócio.
     * @param novoCiclista DTO com os dados para o novo ciclista.
     * @return A entidade Ciclista persistida no banco de dados.
     */
    @Transactional
    public CiclistaResponseDTO criarCiclista(CiclistaRequestDTO novoCiclista) {

        if (novoCiclista.getSenha() == null || !novoCiclista.getSenha().equals(novoCiclista.getConfirmacaoSenha())) {
            throw new RegraDeNegocioException("A senha e a confirmação de senha não coincidem.");
        }

        if (novoCiclista.getCartaoDeCredito() == null || !CartaoDeCredito.verificaCartao(/*integrar cartão aqui */)) {
            throw new RegraDeNegocioException("Dados do cartão de crédito são obrigatórios e o cartão deve ser válido.");
        }

        Ciclista ciclista = new Ciclista();
        ciclista.setNomeCiclista(novoCiclista.getNomeCiclista());
        ciclista.setDataNascimento(novoCiclista.getDataNascimento());
        ciclista.setCartao(novoCiclista.getCartaoDeCredito().toEntity());
        ciclista.setSenha(novoCiclista.getSenha());
        ciclista.setStatus(Status.AGUARDANDO_ATIVAMENTO);

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

        return CiclistaResponseDTO.fromEntity(ciclistaSalvo);
    }


    public Funcionario criarFuncionario(Funcionario novoFuncionario) {
        return funcionarioRepository.save(novoFuncionario);
    }

    public Optional<CiclistaResponseDTO> getCiclistaById(Integer idCiclista) {
    return ciclistaRepository.findById(idCiclista)
        .map(CiclistaResponseDTO::fromEntity);
    }

    public Optional<FuncionarioResponseDTO> getFuncionarioById(String matricula) {
    return funcionarioRepository.findById(matricula)
        .map(FuncionarioResponseDTO::fromEntity);
    }

    public Boolean permiteAluguel(Ciclista ciclista /*Entraria aqui a id da bicicleta */) {
       
        List<Status> statusDesejados = List.of(Status.EM_ANDAMENTO, Status.FINALIZADO_COM_COBRANCA_PENDENTE);
        List<Aluguel> alugueis = aluguelRepository.findAllByCiclistaIdAndStatusIn(ciclista.getId(), statusDesejados);

        if (alugueis.isEmpty() && ciclista.getStatus().equals(Status.ATIVO)) {
            return true;
        }

        return false;
        
    }

    public Integer getBicicletaAlugada(Integer idCiclista) { // Ainda falta integrar
        Optional<Aluguel> optionalAluguel = aluguelRepository.findByCiclistaIdAndStatus(idCiclista, Status.EM_ANDAMENTO);
        Aluguel aluguel = optionalAluguel.orElseThrow(() -> new RuntimeException("O ciclista não tem aluguéis no momento."));

        if (aluguel == null) {
            return null;
        }

        return aluguel.getIdBicicleta();
    }

    public boolean bicicletaEmReparo() { // A integrar
        return false;
    }


    public Boolean existeEmail(String email) {
        Optional<Ciclista> optionalCiclista = ciclistaRepository.findByEmailEndereco(email);
        Ciclista ciclista = optionalCiclista.orElseThrow(() -> new RuntimeException("Ciclista não encontrado."));

        return ciclista != null;
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

    public CartaoDeCredito getCartaoDeCredito(Integer idCiclista) {
        Optional<Ciclista> optionalCiclista = ciclistaRepository.findById(idCiclista);
        Ciclista ciclista = optionalCiclista.orElseThrow(() -> new RuntimeException("Ciclista não encontrado."));
        
        return ciclista.getCartao(); 
    }

    @Transactional
    public AluguelResponseDTO realizarAluguel(AluguelRequestDTO novoAluguel) { // Integrar com trancas e bikes futuramente
        
        Optional<Ciclista> optionalCiclista = ciclistaRepository.findById(novoAluguel.getIdCiclista());
        Ciclista ciclista = optionalCiclista.orElseThrow(() -> new RuntimeException("Ciclista não encontrado."));

        if (!permiteAluguel(ciclista)) {
            throw new RegraDeNegocioException("O aluguel não foi autorizado.");
        }       

        Aluguel aluguel = new Aluguel();

        aluguel.setTrancaInicial(novoAluguel.getIdTranca());
        aluguel.setCiclista(ciclista);
        aluguel.setHoraInicio(LocalDateTime.now());
        aluguel.setIdBicicleta(10001);
        aluguel.setStatus(Status.EM_ANDAMENTO);
        
        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);

        Cobranca.realizarCobrancaPadrao(); // A integrar, externo.

        Tranca.destravaTranca();

        return AluguelResponseDTO.fromEntity(aluguelSalvo);
    }

    public AluguelResponseDTO getAluguelByIdBicicleta(Integer idBicicleta) {
        Optional<Aluguel> optionalAluguel = aluguelRepository.findByIdBicicletaAndStatus(idBicicleta, Status.EM_ANDAMENTO);
        Aluguel aluguel = optionalAluguel.orElseThrow(() -> new RuntimeException("Nenhum aluguel ativo com esta bicicleta."));
        
        return AluguelResponseDTO.fromEntity(aluguel);
    }

    public Boolean trancaDisponivel() { // A integrar 
        System.out.println("Tranca disponível!");

        return true;
    }

    public DevolucaoResponseDTO realizarDevolucao(DevolucaoRequestDTO novaDevolucao, Aluguel aluguel) { // A integrar

        String valorTotal = Cobranca.realizarCobrancaExtra(); // A integrar

        Devolucao devolucao = new Devolucao();
        
        devolucao.setTrancaFinal(novaDevolucao.getIdTranca());
        devolucao.setHoraFim(LocalDateTime.now());
        aluguel.setStatus(Status.FINALIZADO);
        devolucao.setCobranca(new BigDecimal(valorTotal));

        Aluguel aluguelAtualizado = aluguelRepository.save(aluguel); 

        devolucao.setAluguel(aluguelAtualizado);
        
        Devolucao devolucaoCriado = devolucaoRepository.save(devolucao);

        Tranca.travarTranca(); // Integrar

        return DevolucaoResponseDTO.fromEntity(devolucaoCriado);
    }

    public Boolean validaTranca(/*Id da tranca */){
        return true;//Integrar
    }

    public Boolean validaBicicleta(){ // Integrar
        return true;
    }

}


