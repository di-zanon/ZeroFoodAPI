package br.com.fiap.zerofoodapi.service;

import br.com.fiap.zerofoodapi.dao.LojistaDAO;
import br.com.fiap.zerofoodapi.model.Lojista;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class LojistaService {

    private final LojistaDAO lojistaDAO;

    // Injeção de dependência via construtor (Padrão Spring)
    public LojistaService(LojistaDAO lojistaDAO) {
        this.lojistaDAO = lojistaDAO;
    }

    public Lojista salvar(Lojista lojista) throws SQLException {
        // REGRA DE NEGÓCIO: Verificar se o CNPJ já está cadastrado
        Optional<Lojista> lojistaExistente = lojistaDAO.buscarPorCnpj(lojista.getCnpj());

        if (lojistaExistente.isPresent()) {
            // Se encontrou, lança uma exceção que será tratada na Controller (retornando 400 ou 409)
            throw new RuntimeException("Já existe um lojista cadastrado com este CNPJ.");
        }

        // Se não existe, prossegue com o salvamento
        return lojistaDAO.salvar(lojista);
    }

    public List<Lojista> listarTodos() throws SQLException {
        return lojistaDAO.listarTodos();
    }

    public Optional<Lojista> buscarPorId(Long id) throws SQLException {
        return lojistaDAO.buscarPorId(id);
    }

    public boolean atualizar(Lojista lojista) throws SQLException {
        // Verifica se o lojista existe
        Optional<Lojista> lojistaExistente = lojistaDAO.buscarPorId(lojista.getIdLojista());
        if (lojistaExistente.isEmpty()) {
            throw new RuntimeException("Lojista não encontrado para atualização.");
        }

        // Verifica se o novo CNPJ já está em uso por OUTRO lojista
        Optional<Lojista> lojistaComCnpj = lojistaDAO.buscarPorCnpj(lojista.getCnpj());
        if (lojistaComCnpj.isPresent() && !lojistaComCnpj.get().getIdLojista().equals(lojista.getIdLojista())) {
            throw new RuntimeException("O CNPJ informado já pertence a outro lojista.");
        }

        return lojistaDAO.atualizar(lojista);
    }

    public boolean excluir(Long id) throws SQLException {
        // Verifica se o lojista existe antes de tentar excluir
        if (lojistaDAO.buscarPorId(id).isEmpty()) {
            throw new RuntimeException("Lojista não encontrado para exclusão.");
        }
        return lojistaDAO.excluir(id);
    }
}