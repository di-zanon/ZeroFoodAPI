package br.com.fiap.zerofoodapi.model;

public class Avaliacao {

    private Long idAvaliacao;
    private Lojista lojistaAvaliador;
    private Lojista lojistaAvaliado;
    private Transacao transacao;
    private Integer quantidadeEstrelas;
    private String comentario;

    public Avaliacao() {}

    public Avaliacao(Long idAvaliacao, Lojista lojistaAvaliador, Lojista lojistaAvaliado, Transacao transacao, Integer quantidadeEstrelas, String comentario) {
        this.idAvaliacao = idAvaliacao;
        this.lojistaAvaliador = lojistaAvaliador;
        this.lojistaAvaliado = lojistaAvaliado;
        this.transacao = transacao;
        this.quantidadeEstrelas = quantidadeEstrelas;
        this.comentario = comentario;
    }

    // Getters e Setters
    public Long getIdAvaliacao() { return idAvaliacao; }
    public void setIdAvaliacao(Long idAvaliacao) { this.idAvaliacao = idAvaliacao; }

    public Lojista getLojistaAvaliador() { return lojistaAvaliador; }
    public void setLojistaAvaliador(Lojista lojistaAvaliador) { this.lojistaAvaliador = lojistaAvaliador; }

    public Lojista getLojistaAvaliado() { return lojistaAvaliado; }
    public void setLojistaAvaliado(Lojista lojistaAvaliado) { this.lojistaAvaliado = lojistaAvaliado; }

    public Transacao getTransacao() { return transacao; }
    public void setTransacao(Transacao transacao) { this.transacao = transacao; }

    public Integer getQuantidadeEstrelas() { return quantidadeEstrelas; }
    public void setQuantidadeEstrelas(Integer quantidadeEstrelas) { this.quantidadeEstrelas = quantidadeEstrelas; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}