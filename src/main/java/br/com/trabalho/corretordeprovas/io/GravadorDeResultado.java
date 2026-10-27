package br.com.trabalho.corretordeprovas.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import br.com.trabalho.corretordeprovas.model.ResultadoAluno;
import br.com.trabalho.corretordeprovas.model.ResultadoDisciplina;

/**
 * Grava os dois arquivos de resultado de uma disciplina (item 2):
 *  - NOME_alfabetico.txt: alunos em ordem alfabética, uma linha NOME&lt;TAB&gt;ACERTOS;
 *  - NOME_notas.txt: os mesmos dados em ordem decrescente de acertos,
 *    com a média da turma na última linha.
 *
 * O GeradorBoletim (item 3) lê os arquivos *_alfabetico.txt.
 */
public class GravadorDeResultado {
    public static final String SUFIXO_ALFABETICO = "_alfabetico.txt";
    public static final String SUFIXO_NOTAS = "_notas.txt";

    private final File diretorio;

    public GravadorDeResultado(File diretorio) {
        this.diretorio = diretorio;
    }

    /**
     * @return os dois arquivos gerados: [0] alfabético, [1] por nota
     */
    public File[] gravar(ResultadoDisciplina resultado) throws IOException {
        garantirDiretorio();

        String nome = resultado.getDisciplina().nome();
        File alfabetico = new File(diretorio, nome + SUFIXO_ALFABETICO);
        File porNota = new File(diretorio, nome + SUFIXO_NOTAS);

        escrever(alfabetico, resultado.ordenadosPorNome(), null);
        escrever(porNota, resultado.ordenadosPorNotaDecrescente(),
                String.format(Locale.US, "Média da turma: %.2f", resultado.calcularMediaDaTurma()));

        return new File[] { alfabetico, porNota };
    }

    private void garantirDiretorio() throws IOException {
        if (!diretorio.isDirectory() && !diretorio.mkdirs()) {
            throw new IOException("Não foi possível criar o diretório: " + diretorio.getPath());
        }
    }

    private void escrever(File arquivo, List<ResultadoAluno> resultados, String linhaFinal) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo, StandardCharsets.UTF_8))) {
            for (ResultadoAluno resultado : resultados) {
                escritor.write(resultado.nome() + "\t" + resultado.acertos());
                escritor.newLine();
            }
            if (linhaFinal != null) {
                escritor.write(linhaFinal);
                escritor.newLine();
            }
        }
    }
}
