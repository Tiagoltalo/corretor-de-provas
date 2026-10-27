package br.com.trabalho.corretordeprovas.model;

import java.util.List;

public class Boletim {
    private final String nomeAluno;
    private final List<Nota> notas;
    public Boletim(String nomeAluno, List<Nota> notas) {
		this.nomeAluno = nomeAluno;
		this.notas = notas;
    }
    
    public double calcularMedia() {
    	return 0;
    }
    public boolean isAprovado() {
    	return false;
    }
    
    public double getCalcularMedia() {
    	return 0;
    }
}
