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
@Table(name="comentarios")
public class Comentarios{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 700)
    private String texto;

    @Column(nullable = false)
    private Date data;

    @Column(nullable = false, length = 100)
    private String autor;

    public Comentarios (){}

    public Comentarios(String texto, Date data, String autor){
        this.texto = texto;
        this.data = data;
        this.autor = autor;
    }
    
    public Long getId() {
        return id;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
}