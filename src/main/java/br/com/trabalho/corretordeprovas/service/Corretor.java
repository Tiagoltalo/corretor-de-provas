package br.com.trabalho.corretordeprovas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.trabalho.corretordeprovas.model.Aluno;
import br.com.trabalho.corretordeprovas.model.Disciplina;
import br.com.trabalho.corretordeprovas.model.Respostas;
import br.com.trabalho.corretordeprovas.model.ResultadoAluno;
import br.com.trabalho.corretordeprovas.model.ResultadoDisciplina;

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
	public ResultadoDisciplina corrigir(String nomeDisciplina, List<Aluno> alunos) {
		Objects.requireNonNull(alunos, "a lista de alunos não pode ser nula");

		List<ResultadoAluno> resultados = new ArrayList<>();
		for (Aluno aluno : alunos) {
			resultados.add(new ResultadoAluno(aluno.nome(), contarAcertos(aluno.respostas())));
		}
		return new ResultadoDisciplina(new Disciplina(nomeDisciplina, gabaritoOficial), resultados);
	}
}
