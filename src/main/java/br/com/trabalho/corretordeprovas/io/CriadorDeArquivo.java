package br.com.trabalho.corretordeprovas.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;
import br.com.trabalho.corretordeprovas.utils.Utils;

public class CriadorDeArquivo {
	private final Utils utils;
	private final File diretorio;
	
	public CriadorDeArquivo(Utils utils, File diretorio) {
		this.utils = utils;
		this.diretorio = diretorio;
	}
	public void criarArquivoDisciplina() throws EntradaInvalidaException, IOException{
		String disciplina = lerDisciplina();
		
		if(arquivoDaDisciplina(disciplina).exists()) {
			throw new EntradaInvalidaException("disciplina", "já existe um arquivo para essa disciplina", disciplina);
		}
		
		int quantidadeAlunos = lerQuantidadeAlunos();
		List<String> linhas = new ArrayList<>();
		
		for(int i = 0; i < quantidadeAlunos; i++) {
			utils.imprimirMensagem("Aluno " + (i + 1) + "/" + quantidadeAlunos + "\n");
			String aluno = lerNomeAluno();
			String gabarito = lerGabaritoAluno();
			linhas.add(montarLinha(gabarito, aluno));
		}
		
		File arquivo = gravar(disciplina, linhas);
		utils.imprimirMensagem("\nArquivo criado com sucesso: " + arquivo.getPath() + "\n\n");
	}
	private int lerQuantidadeAlunos() throws EntradaInvalidaException{
		while (true) {
			utils.imprimirMensagem("Insira a quantidade de alunos: ");
			String texto = utils.lerTexto();
			try {
				int quantidade = Integer.parseInt(texto);
				if(quantidade >= 1) {
					return quantidade;
				}
				utils.imprimirMensagem("a quantidade deve ser pelo menos 1");
			} catch (NumberFormatException e) {
				throw new EntradaInvalidaException("quantidade de alunos", "quantidade de alunos inválida!", texto);
			}
		}
	}
	private String lerDisciplina() throws EntradaInvalidaException {
		String texto;
        String disciplina;

        utils.imprimirMensagem("Insira o nome da disciplina: ");
        texto = utils.lerTexto();

        if (texto.length() == 0) {
        	throw new EntradaInvalidaException("disciplina", "nome da disciplina vazio!", texto);
        } else {
        	disciplina = texto.toLowerCase();
        }
        return disciplina;
	}
	private String lerNomeAluno() throws EntradaInvalidaException {
		String texto;
		String nomeAluno;
		
		utils.imprimirMensagem("Insira o nome do aluno: ");
		texto = utils.lerTexto();
		
		if(texto.length() == 0) {
			throw new EntradaInvalidaException("nome do aluno", "nome do aluno vazio!", texto);
		} else {
			nomeAluno = texto.toUpperCase();
		}
		return nomeAluno;
	}
	private String lerGabaritoAluno() throws EntradaInvalidaException {
		String texto;
		String gabaritoAluno;
		
		utils.imprimirMensagem("Insira o gabarito do aluno (ex: VFFVFVFFFV): ");
		texto = utils.lerTexto();
		
		try {
			gabaritoAluno = texto.toUpperCase();
		} catch (Exception e) {
			throw new EntradaInvalidaException("gabarito aluno", "impossível transformar em minúsculo", texto);
		}
		
		if(texto.length() != 10) {
			throw new EntradaInvalidaException("gabarito do aluno", "quantidade de repostas diferente de 10!", texto);
		} else {
			for(char resposta : gabaritoAluno.toCharArray()) {
				if(resposta == 'V' || resposta == 'F') {
					continue;
				} else {
					throw new EntradaInvalidaException("gabarito aluno", "respostas diferentes do esperado", gabaritoAluno);
				}
			}
		}
		return gabaritoAluno;
	}
	private String montarLinha(String gabarito, String nome) {
		List<String> linha = new ArrayList<>();
		linha.add(gabarito);
		linha.add(nome);
		
		String resultado = String.join("\t", linha);
		
		return resultado;
	}
	private File arquivoDaDisciplina(String nomeDisciplina) {
		return new File(diretorio, nomeDisciplina + ".txt");
	}
	private void garantirDiretorio() throws IOException {
		if(!diretorio.isDirectory()) {
			diretorio.mkdirs();
			if(!diretorio.isDirectory()) {
				throw new IOException("Não foi possível criar o diretório: " + diretorio.getPath());
			}
		}
	}
	private File gravar(String nomeDisciplina, List<String> linhas) throws IOException{
		garantirDiretorio();
		File arquivo = arquivoDaDisciplina(nomeDisciplina);
		
		if(arquivo.exists()) {
			throw new IOException("Já existe um arquivo para a disciplina: " + arquivo.getPath());
		}

		try (BufferedWriter escritor = new BufferedWriter(
				new FileWriter(arquivo, StandardCharsets.UTF_8))) {
			for (String linha : linhas) {
				escritor.write(linha);
				escritor.newLine();
			}
		}
		return arquivo;
	}
}
