package br.com.trabalho.corretordeprovas.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.io.GravadorDeResultado;
import br.com.trabalho.corretordeprovas.model.Boletim;
import br.com.trabalho.corretordeprovas.model.Nota;
import br.com.trabalho.corretordeprovas.utils.Ordenacao;

/**
 * Item 3 do enunciado: gera um boletim (arquivo .txt) para cada aluno a partir
 * dos arquivos de resultado das disciplinas.
 *
 * Contrato com o item 2 (ver GravadorDeResultado):
 *  - lê apenas os arquivos cujo nome termina em "_alfabetico.txt";
 *  - o nome da disciplina é o trecho do nome do arquivo antes desse sufixo;
 *  - cada linha tem o formato NOME&lt;TAB&gt;ACERTOS (a nota é o número de acertos).
 *
 * A classe não lê do console: recebe os diretórios no construtor.
 */
public class GeradorBoletim {
    private static final String SUFIXO_RESULTADO = GravadorDeResultado.SUFIXO_ALFABETICO;
    private static final String SEPARADOR = "=".repeat(40);

    private final File diretorioResultados;
    private final File diretorioBoletins;

    public GeradorBoletim(File diretorioResultados, File diretorioBoletins) {
        this.diretorioResultados = Objects.requireNonNull(diretorioResultados,
                "o diretório de resultados não pode ser nulo");
        this.diretorioBoletins = Objects.requireNonNull(diretorioBoletins,
                "o diretório de boletins não pode ser nulo");
    }

    /**
     * Gera um boletim por aluno.
     *
     * @return os arquivos de boletim criados (um por aluno, em ordem alfabética)
     * @throws EntradaInvalidaException se um diretório for inválido ou um arquivo de resultado estiver malformado
     * @throws IOException              se houver falha de leitura ou escrita
     */
    public List<File> gerarBoletins() throws IOException, EntradaInvalidaException {
        validarDiretorios();

        File[] arquivos = listarArquivosDeResultado();
        Map<String, List<Nota>> notasPorAluno = lerNotas(arquivos);

        List<File> gerados = new ArrayList<>();
        for (Map.Entry<String, List<Nota>> entrada : notasPorAluno.entrySet()) {
            Boletim boletim = new Boletim(entrada.getKey(), entrada.getValue());
            gerados.add(gravarBoletim(boletim));
        }
        return gerados;
    }

    private void validarDiretorios() throws IOException, EntradaInvalidaException {
        if (!diretorioResultados.isDirectory()) {
            throw new EntradaInvalidaException("diretório de resultados",
                    "não existe ou não é um diretório (gere os resultados pela opção 2 primeiro)",
                    diretorioResultados.getPath());
        }

        if (!diretorioBoletins.isDirectory() && !diretorioBoletins.mkdirs()) {
            throw new IOException("Não foi possível criar o diretório de boletins: "
                    + diretorioBoletins.getPath());
        }
    }

    /**
     * Lista os arquivos de resultado (*_alfabetico.txt) em ordem alfabética crescente
     * pelo nome da disciplina. Essa é a ordem em que as disciplinas aparecem no boletim.
     */
    private File[] listarArquivosDeResultado() throws IOException, EntradaInvalidaException {
        File[] arquivos = diretorioResultados.listFiles(
                (diretorio, nome) -> nome.endsWith(SUFIXO_RESULTADO));

        // listFiles devolve null se o diretório não puder ser lido
        if (arquivos == null) {
            throw new IOException("Não foi possível ler o diretório: " + diretorioResultados.getPath());
        }

        if (arquivos.length == 0) {
            throw new EntradaInvalidaException("diretório de resultados",
                    "nenhum arquivo *" + SUFIXO_RESULTADO + " encontrado (gere os resultados pela opção 2 primeiro)",
                    diretorioResultados.getPath());
        }

        Arrays.sort(arquivos, Comparator.comparing(GeradorBoletim::nomeDaDisciplina, Ordenacao.ALFABETICA));
        return arquivos;
    }

    private Map<String, List<Nota>> lerNotas(File[] arquivos) throws IOException, EntradaInvalidaException {
        // TreeMap: alunos em ordem alfabética
        Map<String, List<Nota>> notasPorAluno = new TreeMap<>(Ordenacao.ALFABETICA);

        for (File arquivo : arquivos) {
            lerArquivo(arquivo, nomeDaDisciplina(arquivo), notasPorAluno);
        }
        return notasPorAluno;
    }

    private void lerArquivo(File arquivo, String disciplina, Map<String, List<Nota>> notasPorAluno)
            throws IOException, EntradaInvalidaException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo, StandardCharsets.UTF_8))) {
            String linha;
            int numeroLinha = 0;

            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;

                if (numeroLinha == 1 && linha.startsWith("\uFEFF")) {
                    linha = linha.substring(1); // remove BOM, comum em arquivos salvos no Windows
                }
                if (linha.isBlank()) {
                    continue;
                }

                String local = arquivo.getName() + ", linha " + numeroLinha;
                String[] partes = linha.split("\t");

                if (partes.length != 2) {
                    throw new EntradaInvalidaException(local, "esperado o formato NOME<TAB>NOTA", linha);
                }

                String aluno = partes[0].trim();
                int valor = converterNota(partes[1].trim(), local);

                if (aluno.isEmpty()) {
                    throw new EntradaInvalidaException(local, "nome do aluno vazio", linha);
                }

                notasPorAluno
                        .computeIfAbsent(aluno, chave -> new ArrayList<>())
                        .add(new Nota(disciplina, valor));
            }
        }
    }

    private int converterNota(String texto, String local) throws EntradaInvalidaException {
        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new EntradaInvalidaException(local, "a nota '" + texto + "' não é um número inteiro",
                    texto, e);
        }

        if (valor < 0 || valor > 10) {
            throw new EntradaInvalidaException(local, "a nota deve estar entre 0 e 10", texto);
        }
        return valor;
    }

    private File gravarBoletim(Boletim boletim) throws IOException {
        File destino = new File(diretorioBoletins, nomeSeguro(boletim.getNomeAluno()) + ".txt");

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(destino, StandardCharsets.UTF_8))) {
            escritor.write(SEPARADOR);
            escritor.newLine();
            escritor.write("BOLETIM GERAL DO ALUNO");
            escritor.newLine();
            escritor.write(SEPARADOR);
            escritor.newLine();
            escritor.newLine();
            escritor.write("Aluno: " + boletim.getNomeAluno());
            escritor.newLine();
            escritor.newLine();

            for (Nota nota : boletim.getNotas()) {
                escritor.write("Disciplina: " + nota.disciplina() + "\tNota: " + nota.valor());
                escritor.newLine();
            }

            escritor.newLine();
            escritor.write(String.format(Locale.US, "Média geral: %.1f", boletim.calcularMedia()));
            escritor.newLine();
            escritor.write("Situação final: " + boletim.getSituacao());
            escritor.newLine();
            escritor.write(SEPARADOR);
            escritor.newLine();
        }
        return destino;
    }

    private static String nomeDaDisciplina(File arquivo) {
        String nome = arquivo.getName();
        return nome.substring(0, nome.length() - SUFIXO_RESULTADO.length());
    }

    /** Troca por "_" os caracteres que não podem aparecer em nome de arquivo. */
    private static String nomeSeguro(String nome) {
        return nome.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
