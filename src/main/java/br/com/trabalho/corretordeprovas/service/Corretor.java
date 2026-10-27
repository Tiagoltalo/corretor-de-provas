package br.com.trabalho.corretordeprovas.service;

import java.util.Objects;

import br.com.trabalho.corretordeprovas.model.Respostas;

public class Corretor {
	private final Respostas gabaritoOficial;
	
	public Corretor(Respostas gabaritoOficial){
		this.gabaritoOficial = Objects.requireNonNull(gabaritoOficial, "o gabarito não pode ser nulo!");
	}
	public int contarAcertos(Respostas respostasDoAluno) {
        Objects.requireNonNull(respostasDoAluno, "as respostas do aluno não podem ser nulas");
 
        if (respostasDoAluno.todasIguais()) {
            return 0;
        }
 
        int acertos = 0;
        for (int questao = 1; questao <= Respostas.NUM_QUESTOES; questao++) {
            if (respostasDoAluno.getRespostaDaQuestao(questao) == gabaritoOficial.getRespostaDaQuestao(questao)) {
                acertos++;
            }
        }
        return acertos;
    }
}
