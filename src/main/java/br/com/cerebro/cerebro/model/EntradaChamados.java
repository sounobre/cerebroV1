/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerebro.cerebro.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author souno
 */
@Entity
@Table(name = "entradachamado")
public class EntradaChamados {
    
    @Id
    private Integer chamado;
    
    private String data;
    
    private String executante;

    public EntradaChamados() {
    }

    public EntradaChamados(Integer chamado, String data, String executante) {
        this.chamado = chamado;
        this.data = data;
        this.executante = executante;
    }

    public Integer getChamado() {
        return chamado;
    }

    public void setChamado(Integer chamado) {
        this.chamado = chamado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExecutante() {
        return executante;
    }

    public void setExecutante(String executante) {
        this.executante = executante;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.chamado);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntradaChamados other = (EntradaChamados) obj;
        if (!Objects.equals(this.chamado, other.chamado)) {
            return false;
        }
        return true;
    }
    
    
    
}
