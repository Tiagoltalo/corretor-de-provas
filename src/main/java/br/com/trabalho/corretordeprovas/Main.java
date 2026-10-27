package br.com.trabalho.corretordeprovas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.io.CriadorDeArquivo;
import br.com.trabalho.corretordeprovas.model.Respostas;
import br.com.trabalho.corretordeprovas.service.Corretor;
import br.com.trabalho.corretordeprovas.utils.Utils;

public class Main {
    public static void main(String[] args) {
		Utils utils = new Utils();
		CriadorDeArquivo criador = new CriadorDeArquivo(utils, new File("dados/respostas"));

		exibirMenu(utils);
		boolean executando = true;
		while(executando) {
			String opcao = utils.lerTexto();
			try {
				switch (opcao) {
				case "1":
					criador.criarArquivoDisciplina();
					break;
				case "2":
					String caminho = utils.lerTexto();
					File arquivoGabarito = new File(caminho);
					
					if (!arquivoGabarito.isFile()) {
					    throw new FileNotFoundException("arquivo não encontrado: " + caminho);
					}
					
					List<String> todasAsLinhas = Files.readAllLines(arquivoGabarito.toPath());
					List<String> linhas = new ArrayList<>();

					for (String linha : todasAsLinhas) {
					    if (!linha.isBlank()) {
					        linhas.add(linha);
					    }
					}
					
					if (linhas.size() != 1) {
					    throw new EntradaInvalidaException("gabarito",
					            "o arquivo deve ter exatamente uma linha com as respostas", caminho);
					}

					Respostas gabarito = Respostas.validarECriar(linhas.get(0), "gabarito oficial");
					Corretor corretor = new Corretor(gabarito);
					break;
				case "0":
					executando = false;
					break;
				default:
					utils.imprimirMensagem("Erro: Opção inválida! Digite novamente: ");
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
