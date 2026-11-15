package br.com.fiap.zerofoodapi.model;

import java.time.LocalDateTime;

public class Transacao {

    private Long idTransacao;
    private Anuncio anuncio;
    private Lojista lojistaComprador;
    private LocalDateTime dataHora;
    private Integer quantidadeVendida;
    private Double valorTotal;
    private Double taxaTakeRate;
    private Character status; // 'P' (Pendente), 'C' (Concluída), 'X' (Cancelada)

    public Transacao() {}

    public Transacao(Long idTransacao, Anuncio anuncio, Lojista lojistaComprador, LocalDateTime dataHora, Integer quantidadeVendida, Double valorTotal, Double taxaTakeRate, Character status) {
        this.idTransacao = idTransacao;
        this.anuncio = anuncio;
        this.lojistaComprador = lojistaComprador;
        this.dataHora = dataHora;
        this.quantidadeVendida = quantidadeVendida;
        this.valorTotal = valorTotal;
        this.taxaTakeRate = taxaTakeRate;
        this.status = status;
    }

    // Getters e Setters
    public Long getIdTransacao() { return idTransacao; }
    public void setIdTransacao(Long idTransacao) { this.idTransacao = idTransacao; }

    public Anuncio getAnuncio() { return anuncio; }
    public void setAnuncio(Anuncio anuncio) { this.anuncio = anuncio; }

    public Lojista getLojistaComprador() { return lojistaComprador; }
    public void setLojistaComprador(Lojista lojistaComprador) { this.lojistaComprador = lojistaComprador; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getQuantidadeVendida() { return quantidadeVendida; }
    public void setQuantidadeVendida(Integer quantidadeVendida) { this.quantidadeVendida = quantidadeVendida; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }

    public Double getTaxaTakeRate() { return taxaTakeRate; }
    public void setTaxaTakeRate(Double taxaTakeRate) { this.taxaTakeRate = taxaTakeRate; }

    public Character getStatus() { return status; }
    public void setStatus(Character status) { this.status = status; }
}