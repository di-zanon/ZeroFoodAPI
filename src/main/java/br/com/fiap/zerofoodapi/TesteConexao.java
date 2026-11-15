package br.com.fiap.zerofoodapi;

import br.com.fiap.zerofoodapi.config.ConnectionFactory;
import br.com.fiap.zerofoodapi.dao.LojistaDAO;
import br.com.fiap.zerofoodapi.model.Lojista;
import br.com.fiap.zerofoodapi.service.LojistaService;

public class TesteConexao {
    public static void main(String[] args) {
        // 1. Instanciamos a ConnectionFactory primeiro
        ConnectionFactory factory = new ConnectionFactory();

        // 2. Passamos a factory para o DAO pelo construtor
        LojistaDAO dao = new LojistaDAO(factory);
        LojistaService service = new LojistaService(dao);

        try {
            // 1. Tentar salvar um lojista NOVO
            Lojista novo = new Lojista();
            novo.setRazaoSocial("Teste Regra LTDA");
            novo.setNomeFantasia("Teste Regra");
            novo.setCnpj("88.888.888/0001-88"); // CNPJ novo
            novo.setEmail("teste@regra.com");
            novo.setTelefone("11955554444");
            novo.setEndereco("Rua da Regra, 10");

            Lojista salvo = service.salvar(novo);
            System.out.println("Lojista salvo! ID: " + salvo.getIdLojista());

            // 2. Tentar salvar um lojista com CNPJ DUPLICADO (Regra de Negócio)
            System.out.println("\nTentando salvar CNPJ duplicado...");
            Lojista duplicado = new Lojista();
            duplicado.setRazaoSocial("Outro Lojista");
            duplicado.setCnpj("88.888.888/0001-88"); // CNPJ já existente
            service.salvar(duplicado); // Isso deve lançar exceção!

        } catch (Exception e) {
            System.err.println("Regra de negócio ativada: " + e.getMessage());
        }
    }
}