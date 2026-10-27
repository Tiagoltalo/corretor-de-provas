package br.com.trabalho.corretordeprovas;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.io.CriadorDeArquivo;
import br.com.trabalho.corretordeprovas.io.GeradorDeResultado;
import br.com.trabalho.corretordeprovas.service.GeradorBoletim;
import br.com.trabalho.corretordeprovas.utils.Utils;

public class Main {
    private static final File DIRETORIO_RESPOSTAS = new File("dados/respostas");
    private static final File DIRETORIO_RESULTADOS = new File("dados/resultados");
    private static final File DIRETORIO_BOLETINS = new File("dados/boletins");

    public static void main(String[] args) {
        Utils utils = new Utils();
        CriadorDeArquivo criador = new CriadorDeArquivo(utils, DIRETORIO_RESPOSTAS);
        GeradorDeResultado geradorDeResultado =
                new GeradorDeResultado(utils, DIRETORIO_RESPOSTAS, DIRETORIO_RESULTADOS);
        GeradorBoletim geradorBoletim = new GeradorBoletim(DIRETORIO_RESULTADOS, DIRETORIO_BOLETINS);

        boolean executando = true;

        try {
            while (executando) {
                exibirMenu(utils);
                String opcao = utils.lerTexto();

                try {
                    switch (opcao) {
                        case "1":
                        	criador.criarArquivoDisciplina();
                        	break;
                        case "2":
                        	geradorDeResultado.gerarResultado();
                        	break;
                        case "3":
                        	gerarBoletins(utils, geradorBoletim);
                        	break;
                        case "0":
                        	executando = false;
                        	break;
                        default:
                        	utils.imprimirMensagem("Erro: opção inválida!\n");
                        	break;
                    }
                } catch (EntradaInvalidaException e) {
                    utils.imprimirMensagem(e.getMessage() + "\n");
                } catch (IOException e) {
                    utils.imprimirMensagem("Erro de arquivo: " + e.getMessage() + "\n");
                }
            }
        } catch (NoSuchElementException e) {
            // a entrada padrão foi encerrada (Ctrl+D / Ctrl+Z)
            utils.imprimirMensagem("\nEntrada encerrada.\n");
        }
    }

    private static void gerarBoletins(Utils utils, GeradorBoletim gerador)
            throws EntradaInvalidaException, IOException {
        List<File> boletins = gerador.gerarBoletins();

        utils.imprimirMensagem("\n" + boletins.size() + " boletim(ns) gerado(s) em "
                + DIRETORIO_BOLETINS.getPath() + ":\n");
        for (File boletim : boletins) {
            utils.imprimirMensagem("  " + boletim.getName() + "\n");
        }
        utils.imprimirMensagem("\n");
    }

    private static void exibirMenu(Utils utils) {
        utils.imprimirMensagem("==================== CORRETOR DE PROVAS ====================\n"
                + "1 - Criar arquivo de respostas de uma disciplina\n"
                + "2 - Gerar resultado de uma disciplina\n"
                + "3 - Gerar boletins\n"
                + "0 - Sair\n"
                + "Digite a opção desejada: ");
    }
}
