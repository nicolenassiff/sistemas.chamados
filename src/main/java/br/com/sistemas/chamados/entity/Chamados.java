package br.com.sistemas.chamados.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** ENTITY: representa a tabela do banco. Cada objeto = uma linha */
@Entity 
@Table(name="chamados")
public class Chamados{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String titulo;

    @Column(nullable = false, length = 700)
    private String descricao;

    @Column(nullable = false)
    private Date data_abertura;

    @Column(nullable = false)
    private Date data_fechamento;

    @Column(nullable = false, length = 50)
    private String prioridade;

    @Column(nullable = false, length = 100)
    private String status;

    public Chamados (){}

    public Chamados(String titulo, String descricao, Date data_abertura){
        this.titulo = titulo;
        this.descricao = descricao;
        this.data_abertura = data_abertura;
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
    public Date getData_abertura() {
        return data_abertura;
    }
    public void setData_abertura(Date data_abertura) {
        this.data_abertura = data_abertura;
    }
    public Date getData_fechamento() {
        return data_fechamento;
    }
    public void setData_fechamento(Date data_fechamento) {
        this.data_fechamento = data_fechamento;
    }
    public String getPrioridade() {
        return prioridade;
    }
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}