package br.com.trabalho.corretordeprovas.model;

import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;

public final class Respostas {
	public static final int NUM_QUESTOES = 10;
	private final String valor;

	private Respostas(String valor) {
		this.valor = valor;
	}

	public static Respostas validarECriar(String texto, String campo) throws EntradaInvalidaException {
		if (texto == null || texto.isBlank()) {
			throw new EntradaInvalidaException(campo, "o campo está vazio!", texto);
		}

		String textoNormalizado = texto.trim().toUpperCase();

		if (textoNormalizado.length() != NUM_QUESTOES) {
			throw new EntradaInvalidaException(campo, "quantidade de respostas diferente do esperado!", texto);
		}

		for (char resposta : textoNormalizado.toCharArray()) {
			if (resposta != 'V' && resposta != 'F') {
				throw new EntradaInvalidaException(campo, "resposta diferente do esperado!", texto);
			}
		}
		return new Respostas(textoNormalizado);
	}

	public char getRespostaDaQuestao(int numero){
		if (numero < 1 || numero > NUM_QUESTOES) {
			throw new IllegalArgumentException("questão " + numero + " fora do intervalo de 1 a " + NUM_QUESTOES);
		}
		// Retorna o caractere presente na posição (numero - 1)
		return valor.charAt(numero - 1);
	}

	public boolean todasIguais() {
		char v0 = valor.charAt(0);
		for (int i = 1; i < valor.length(); i++) {
			if (valor.charAt(i) != v0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return valor;
	}
}
