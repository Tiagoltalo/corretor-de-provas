package br.com.trabalho.corretordeprovas.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.model.Aluno;
import br.com.trabalho.corretordeprovas.model.Respostas;
import br.com.trabalho.corretordeprovas.model.ResultadoAluno;
import br.com.trabalho.corretordeprovas.model.ResultadoDisciplina;
import br.com.trabalho.corretordeprovas.service.Corretor;
import br.com.trabalho.corretordeprovas.utils.Ordenacao;
import br.com.trabalho.corretordeprovas.utils.Utils;

/**
 * Item 2 do enunciado: o usuário escolhe a disciplina, informa o arquivo do
 * gabarito oficial, e o programa grava os dois arquivos de resultado e
 * (opcionalmente) mostra os dados na tela.
 */
public class GeradorDeResultado {
    private static final String EXTENSAO = ".txt";

    private final Utils utils;
    private final File diretorioRespostas;
    private final LeitorDeProva leitor = new LeitorDeProva();
    private final GravadorDeResultado gravador;

    public GeradorDeResultado(Utils utils, File diretorioRespostas, File diretorioResultados) {
        this.utils = utils;
        this.diretorioRespostas = diretorioRespostas;
        this.gravador = new GravadorDeResultado(diretorioResultados);
    }

    public void gerarResultado() throws EntradaInvalidaException, IOException {
        File arquivoDisciplina = escolherDisciplina();
        String nomeDisciplina = nomeDaDisciplina(arquivoDisciplina);
        List<Aluno> alunos = leitor.lerAlunos(arquivoDisciplina);

        File arquivoGabarito = lerCaminhoDoGabarito();
        Respostas gabarito = leitor.lerGabarito(arquivoGabarito);

        ResultadoDisciplina resultado = new Corretor(gabarito).corrigir(nomeDisciplina, alunos);
        File[] gerados = gravador.gravar(resultado);

        utils.imprimirMensagem("\nArquivos gerados:\n");
        for (File arquivo : gerados) {
            utils.imprimirMensagem("  " + arquivo.getPath() + "\n");
        }

        if (desejaVisualizar()) {
            exibir(resultado);
        }
    }

    private File escolherDisciplina() throws EntradaInvalidaException, IOException {
        File[] arquivos = diretorioRespostas.listFiles((diretorio, nome) -> nome.endsWith(EXTENSAO));

        if (arquivos == null || arquivos.length == 0) {
            throw new EntradaInvalidaException("disciplina",
                    "nenhuma disciplina cadastrada (use a opção 1 primeiro)", diretorioRespostas.getPath());
        }

        Arrays.sort(arquivos, Comparator.comparing(GeradorDeResultado::nomeDaDisciplina, Ordenacao.ALFABETICA));

        utils.imprimirMensagem("\nDisciplinas cadastradas:\n");
        for (int i = 0; i < arquivos.length; i++) {
            utils.imprimirMensagem("  " + (i + 1) + " - " + nomeDaDisciplina(arquivos[i]) + "\n");
        }
        utils.imprimirMensagem("Escolha a disciplina (número): ");
        String texto = utils.lerTexto();

        try {
            int escolha = Integer.parseInt(texto);
            if (escolha >= 1 && escolha <= arquivos.length) {
                return arquivos[escolha - 1];
            }
        } catch (NumberFormatException e) {
            // cai na exceção abaixo
        }
        throw new EntradaInvalidaException("disciplina",
                "escolha um número entre 1 e " + arquivos.length, texto);
    }

    private File lerCaminhoDoGabarito() throws EntradaInvalidaException {
        utils.imprimirMensagem("Informe o caminho do arquivo com o gabarito oficial: ");
        String caminho = utils.lerTexto();

        // ao arrastar um arquivo para o terminal, alguns sistemas colocam aspas ao redor do caminho
        if (caminho.length() >= 2 && caminho.startsWith("\"") && caminho.endsWith("\"")) {
            caminho = caminho.substring(1, caminho.length() - 1);
        }

        if (caminho.isEmpty()) {
            throw new EntradaInvalidaException("gabarito", "caminho vazio", caminho);
        }
        return new File(caminho);
    }

    private boolean desejaVisualizar() {
        utils.imprimirMensagem("\nDeseja visualizar os resultados na tela? (S/N): ");
        return utils.lerTexto().equalsIgnoreCase("S");
    }

    private void exibir(ResultadoDisciplina resultado) {
        utils.imprimirMensagem("\nDisciplina: " + resultado.getDisciplina().nome() + "\n");
        utils.imprimirMensagem("Gabarito oficial: " + resultado.getDisciplina().gabarito() + "\n");

        utils.imprimirMensagem("\n--- Ordem alfabética ---\n");
        for (ResultadoAluno aluno : resultado.ordenadosPorNome()) {
            utils.imprimirMensagem(String.format("%-40s %2d\n", aluno.nome(), aluno.acertos()));
        }

        utils.imprimirMensagem("\n--- Ordem decrescente de acertos ---\n");
        for (ResultadoAluno aluno : resultado.ordenadosPorNotaDecrescente()) {
            utils.imprimirMensagem(String.format("%-40s %2d\n", aluno.nome(), aluno.acertos()));
        }
        utils.imprimirMensagem(String.format(Locale.US, "\nMédia da turma: %.2f\n\n",
                resultado.calcularMediaDaTurma()));
    }

    private static String nomeDaDisciplina(File arquivo) {
        String nome = arquivo.getName();
        return nome.substring(0, nome.length() - EXTENSAO.length());
    }
}
