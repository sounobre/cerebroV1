/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.cerebro.cerebro.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author souno
 */
@Entity
@Table(name = "entradadupla")
public class EntradaDupla {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer chamado;
    
    private String data;
    
    private String executante;

    public EntradaDupla() {
    }

    public EntradaDupla(Integer id, Integer chamado, String data, String executante) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final EntradaDupla other = (EntradaDupla) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    
    
    
}
