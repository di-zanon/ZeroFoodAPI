package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.AnuncioDAO;
import br.com.fiap.zerofoodapi.dao.LojistaDAO;
import br.com.fiap.zerofoodapi.dao.TransacaoDAO;
import br.com.fiap.zerofoodapi.model.Anuncio;
import br.com.fiap.zerofoodapi.model.Transacao;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoDAO transacaoDAO;
    private final AnuncioDAO anuncioDAO;
    private final LojistaDAO lojistaDAO;

    // Taxa fixa do ZeroFood (10%)
    private static final double TAXA_PLATAFORMA = 0.10;

    public TransacaoService(TransacaoDAO transacaoDAO, AnuncioDAO anuncioDAO, LojistaDAO lojistaDAO) {
        this.transacaoDAO = transacaoDAO;
        this.anuncioDAO = anuncioDAO;
        this.lojistaDAO = lojistaDAO;
    }

    public Transacao salvar(Transacao transacao) throws SQLException {
        // 1. Verifica se o anúncio existe
        Optional<Anuncio> anuncioOpt = anuncioDAO.buscarPorId(transacao.getAnuncio().getIdAnuncio());
        if (anuncioOpt.isEmpty()) {
            throw new RuntimeException("Anúncio não encontrado para realizar a transação.");
        }
        Anuncio anuncio = anuncioOpt.get();

        // 2. Verifica se o comprador existe
        if (lojistaDAO.buscarPorId(transacao.getLojistaComprador().getIdLojista()).isEmpty()) {
            throw new RuntimeException("Lojista comprador não encontrado.");
        }

        // 3. Regra de Negócio: Verificar estoque
        if (transacao.getQuantidadeVendida() > anuncio.getQuantidadeEstoque()) {
            throw new RuntimeException("Quantidade solicitada maior que o estoque disponível (" + anuncio.getQuantidadeEstoque() + ").");
        }

        // 4. Regra de Negócio: Calcular o valor total e a taxa da plataforma (Take Rate)
        double valorTotal = anuncio.getValorUnitario() * transacao.getQuantidadeVendida();
        double taxa = valorTotal * TAXA_PLATAFORMA;

        transacao.setValorTotal(valorTotal);
        transacao.setTaxaTakeRate(taxa);
        transacao.setDataHora(LocalDateTime.now());

        // Define o status inicial como 'P' (Pendente) caso não venha
        // Simula integracao com gateway de pagamento para pagamento ainda nao processado
        if (transacao.getStatus() == null) {
            transacao.setStatus('P');
        }

        return transacaoDAO.salvar(transacao);
    }

    public List<Transacao> listarTodos() throws SQLException {
        return transacaoDAO.listarTodos();
    }

    public Optional<Transacao> buscarPorId(Long id) throws SQLException {
        return transacaoDAO.buscarPorId(id);
    }

    public boolean atualizarStatus(Long id, Character novoStatus) throws SQLException {
        Optional<Transacao> transacao = transacaoDAO.buscarPorId(id);
        if (transacao.isEmpty()) {
            throw new RuntimeException("Transação não encontrada.");
        }

        Transacao t = transacao.get();
        t.setStatus(novoStatus);
        return transacaoDAO.atualizar(t);
    }

    public boolean excluir(Long id) throws SQLException {
        if (transacaoDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Transação não encontrada para exclusão.");
        }
        return transacaoDAO.excluir(id);
    }
}