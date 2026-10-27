package br.com.trabalho.corretordeprovas.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.model.Aluno;
import br.com.trabalho.corretordeprovas.model.Respostas;

/**
 * Lê os arquivos de entrada do item 2: o gabarito oficial e o arquivo de
 * respostas dos alunos de uma disciplina.
 */
public class LeitorDeProva {

    /**
     * Lê o gabarito oficial: um arquivo com uma única linha de 10 respostas V/F.
     */
    public Respostas lerGabarito(File arquivo) throws IOException, EntradaInvalidaException {
        if (!arquivo.isFile()) {
            throw new EntradaInvalidaException("gabarito", "arquivo não encontrado", arquivo.getPath());
        }

        List<String> linhas = lerLinhasNaoVazias(arquivo);
        if (linhas.size() != 1) {
            throw new EntradaInvalidaException("gabarito",
                    "o arquivo deve ter exatamente uma linha com as respostas", arquivo.getPath());
        }
        return Respostas.validarECriar(linhas.get(0), "gabarito oficial");
    }

    /**
     * Lê o arquivo de respostas de uma disciplina. Cada linha tem o formato
     * RESPOSTAS&lt;TAB&gt;NOME. Se a linha não tiver tab, as 10 primeiras letras
     * são as respostas e o restante é o nome (como no exemplo do enunciado).
     */
    public List<Aluno> lerAlunos(File arquivo) throws IOException, EntradaInvalidaException {
        if (!arquivo.isFile()) {
            throw new EntradaInvalidaException("arquivo de respostas", "arquivo não encontrado", arquivo.getPath());
        }

        List<Aluno> alunos = new ArrayList<>();
        int numeroLinha = 0;

        for (String linha : lerLinhas(arquivo)) {
            numeroLinha++;
            if (linha.isBlank()) {
                continue;
            }

            String local = arquivo.getName() + ", linha " + numeroLinha;
            String textoRespostas;
            String nome;

            int posicaoTab = linha.indexOf('\t');
            if (posicaoTab >= 0) {
                textoRespostas = linha.substring(0, posicaoTab);
                nome = linha.substring(posicaoTab + 1).trim();
            } else if (linha.length() > Respostas.NUM_QUESTOES) {
                textoRespostas = linha.substring(0, Respostas.NUM_QUESTOES);
                nome = linha.substring(Respostas.NUM_QUESTOES).trim();
            } else {
                throw new EntradaInvalidaException(local, "esperado o formato RESPOSTAS<TAB>NOME", linha);
            }

            if (nome.isEmpty()) {
                throw new EntradaInvalidaException(local, "nome do aluno vazio", linha);
            }

            Respostas respostas = Respostas.validarECriar(textoRespostas, "respostas (" + local + ")");
            alunos.add(new Aluno(nome, respostas));
        }

        if (alunos.isEmpty()) {
            throw new EntradaInvalidaException("arquivo de respostas", "nenhum aluno encontrado", arquivo.getPath());
        }
        return alunos;
    }

    private List<String> lerLinhasNaoVazias(File arquivo) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (String linha : lerLinhas(arquivo)) {
            if (!linha.isBlank()) {
                linhas.add(linha);
            }
        }
        return linhas;
    }

    private List<String> lerLinhas(File arquivo) throws IOException {
        List<String> linhas = new ArrayList<>();

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linhas.isEmpty() && linha.startsWith("\uFEFF")) {
                    linha = linha.substring(1); // remove BOM, comum em arquivos salvos no Windows
                }
                linhas.add(linha);
            }
        }
        return linhas;
    }
}
