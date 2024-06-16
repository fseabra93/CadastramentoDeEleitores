/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.List;

/**
 *
 * @author flaviorgs
 */
public class Eleitor extends Entity {
    private Pessoa pessoa;
    private int zonaEleitoral;
    private int secaoEleitoral;
    private String tituloEleitoral;
    private float multas;
    private boolean situacao;
    private List<Integer>anosSemVotar;


    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getZonaEleitoral() {
        return zonaEleitoral;
    }

    public void setZonaEleitoral(int zonaEleitoral) {
        this.zonaEleitoral = zonaEleitoral;
    }

    public int getSecaoEleitoral() {
        return secaoEleitoral;
    }

    public void setSecaoEleitoral(int secaoEleitoral) {
        this.secaoEleitoral = secaoEleitoral;
    }

    public String getTituloEleitoral() {
        return tituloEleitoral;
    }

    public void setTituloEleitoral(String tituloEleitoral) {
        this.tituloEleitoral = tituloEleitoral;
    }

    public float getMultas() {
        return multas;
    }

    public void setMultas(float multas) {
        this.multas = multas;
    }

    public boolean isSituacao() {
        return situacao;
    }

    public void setSituacao(boolean situacao) {
        this.situacao = situacao;
    }
    
    public List<Integer> getAnosSemVotar() {
        return this.anosSemVotar;
    }

    // Setter para anosSemVotar
    public void setAnosSemVotar(List<Integer> anosSemVotar) {
        this.anosSemVotar = anosSemVotar;
    }
    
    // Método para limpar a lista de anosSemVotar
    public void limparAnosSemVotar() {
        this.anosSemVotar.clear();
    }
    
    public void adicionarAnosNaoVotados(int ano) {
        this.anosSemVotar.add(ano);
    }
    
    
    
    
}
