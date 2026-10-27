package br.com.trabalho.corretordeprovas.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.trabalho.corretordeprovas.utils.Ordenacao;

/**
 * Resultado de uma turma em uma disciplina. Oferece as duas visões pedidas
 * no enunciado: ordem alfabética e ordem decrescente de acertos, além da média.
 */
public class ResultadoDisciplina {
    private final Disciplina disciplina;
    private final List<ResultadoAluno> resultados;

    public ResultadoDisciplina(Disciplina disciplina, List<ResultadoAluno> resultados) {
        this.disciplina = disciplina;
        this.resultados = List.copyOf(resultados);
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    /** Alunos em ordem alfabética (A a Z). */
    public List<ResultadoAluno> ordenadosPorNome() {
        List<ResultadoAluno> lista = new ArrayList<>(resultados);
        lista.sort(Comparator.comparing(ResultadoAluno::nome, Ordenacao.ALFABETICA));
        return lista;
    }

    /** Alunos do maior para o menor número de acertos; empates em ordem alfabética. */
    public List<ResultadoAluno> ordenadosPorNotaDecrescente() {
        List<ResultadoAluno> lista = new ArrayList<>(resultados);
        lista.sort(Comparator.comparingInt(ResultadoAluno::acertos).reversed()
                .thenComparing(ResultadoAluno::nome, Ordenacao.ALFABETICA));
        return lista;
    }

    public double calcularMediaDaTurma() {
        if (resultados.isEmpty()) {
            return 0.0;
        }

        int soma = 0;
        for (ResultadoAluno resultado : resultados) {
            soma += resultado.acertos();
        }
        return (double) soma / resultados.size();
    }
}
