package br.com.trabalho.corretordeprovas.model;

import java.util.List;

/**
 * Boletim geral de um aluno: suas notas em todas as disciplinas,
 * a média geral e a situação final.
 */
public class Boletim {
    public static final double MEDIA_APROVACAO = 7.0;

    private final String nomeAluno;
    private final List<Nota> notas;

    public Boletim(String nomeAluno, List<Nota> notas) {
        this.nomeAluno = nomeAluno;
        this.notas = List.copyOf(notas);
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public double calcularMedia() {
        if (notas.isEmpty()) {
            return 0.0;
        }

        int soma = 0;
        for (Nota nota : notas) {
            soma += nota.valor();
        }
        return (double) soma / notas.size();
    }

    public boolean isAprovado() {
    	if(calcularMedia() >= MEDIA_APROVACAO) {    		
    		return true;
    	}
    	return false;
    }

    public String getSituacao() {
        if (isAprovado()) {
            return "APROVADO";
        }
        return "REPROVADO";
    }
}
