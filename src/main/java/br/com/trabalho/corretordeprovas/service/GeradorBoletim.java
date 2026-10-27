package br.com.trabalho.corretordeprovas.service;

import br.com.trabalho.corretordeprovas.model.Boletim;
import br.com.trabalho.corretordeprovas.utils.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class GeradorBoletim {	
	private final File diretorioResultados;
    private final File diretorioBoletins;

    public GeradorBoletim(File diretorioResultados, File diretorioBoletins) {
        this.diretorioResultados = diretorioResultados;
        this.diretorioBoletins = diretorioBoletins;
    }
    
    public void gerarBoletins() throws IOException{
    }
    
    private void validarDiretorios() throws IOException {
    	if(!diretorioResultados.isDirectory()) {
    		diretorioResultados.mkdirs();
    		if(!diretorioResultados.isDirectory()) {
    			throw new IOException("Não foi possível criar o diretório: " + diretorioResultados.getPath());
    		}
    	}
    	if(!diretorioBoletins.isDirectory()) {
    		diretorioBoletins.mkdirs();
    		if(!diretorioBoletins.isDirectory()) {
    			throw new IOException("Não foi possível criar o diretório: " + diretorioBoletins.getPath());
    		}
    	}
    	
    }
    private File[] listarArquivosDeResultado() throws IOException {
    	File[] arquivos = diretorioResultados.listFiles();
    	
    	if(arquivos == null) {
    		throw new IOException("Erro: O diretório"+ diretorioResultados.getPath() +" não pôde ser lido!");
    	}
    	Arrays.sort(arquivos);
    	return arquivos;
    }
    private Map<String, List<Nota>> lerNotas(File[] arquivos) throws IOException {
    	
    }
    private void gravarBoletim(Boletim boletim) throws IOException {
    	
    }
}
