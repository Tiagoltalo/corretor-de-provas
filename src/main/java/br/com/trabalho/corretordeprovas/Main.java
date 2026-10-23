package br.com.trabalho.corretordeprovas;

import java.io.File;
import java.io.IOException;
import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.io.CriadorDeArquivo;
import br.com.trabalho.corretordeprovas.utils.Utils;

public class Main {
    public static void main(String[] args) {
		Utils utils = new Utils();
		CriadorDeArquivo criador = new CriadorDeArquivo(utils, new File("dados/respostas"));
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
