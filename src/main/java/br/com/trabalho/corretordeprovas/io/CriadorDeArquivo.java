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
}
