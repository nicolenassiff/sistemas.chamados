package br.com.sistemas.chamados.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

/** ENTITY: representa a tabela do banco. Cada objeto = uma linha */
@Entity 
@Table(name="chamados")
public class Chamados{

    public enum Prioridade { BAIXA, MEDIA, ALTA, CRITICA }

    public enum StatusChamado { ABERTO, EM_ATENDIMENTO, AGUARDANDO_CLIENTE, FECHADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    @Column
    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusChamado status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categorias categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Tecnicos tecnico;

    public Chamados (){}

    public Chamados(String titulo, String descricao, LocalDateTime dataAbertura, LocalDateTime dataFechamento, Prioridade prioridade, StatusChamado status, Cliente cliente, Categorias categoria, Tecnicos tecnico){
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.prioridade = prioridade;
        this.status = status;
        this.cliente = cliente;
        this.categoria = categoria;
        this.tecnico = tecnico;
    }
    
    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }
    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }
    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
    public Prioridade getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }
    public StatusChamado getStatus() {
        return status;
    }
    public void setStatus(StatusChamado status) {
        this.status = status;
    }
    public Cliente getCliente() { 
        return cliente; 
    }
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; 
    }
    public Categorias getCategoria() { 
        return categoria; 
    }
    public void setCategoria(Categorias categoria) { 
        this.categoria = categoria; 
    }
    public Tecnicos getTecnico() { 
        return tecnico; 
    }
    public void setTecnico(Tecnicos tecnico) { 
        this.tecnico = tecnico; 
    }
}