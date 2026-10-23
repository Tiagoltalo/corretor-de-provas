package br.com.trabalho.corretordeprovas;

import java.io.File;
import java.io.IOException;
import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.io.CriadorDeArquivo;
import br.com.trabalho.corretordeprovas.utils.Utils;

public class Main {
    public static void main(String[] args) {
		Utils utils = new Utils();

		boolean executando = true;
		while(executando) {
			exibirMenu(utils);
			String opcao = utils.lerTexto();
			try {
				switch (opcao) {
				case "1":
					CriadorDeArquivo criador = new CriadorDeArquivo(utils, new File("dados/respostas"));
					criador.criarArquivoDisciplina();
					break;
				case "0":
					executando = false;
					break;
				default:
					utils.imprimirMensagem("Erro: Opção inválida! Digite novamente:");
					break;
				}				
			} catch (EntradaInvalidaException | IOException e) {
				utils.imprimirMensagem("Erro: " + e.getMessage() + "\n");
			}
		}
	}

	private static void exibirMenu(Utils utils) {
		utils.imprimirMensagem("""
				==================== CORRETOR DE PROVAS ====================
				1 - Criar arquivo de respostas de uma disciplina
				2 - Gerar resultado de uma disciplina
                3 - Gerar boletins
                0 - Sair
				Digite a opção desejada:""");	
	}
}
