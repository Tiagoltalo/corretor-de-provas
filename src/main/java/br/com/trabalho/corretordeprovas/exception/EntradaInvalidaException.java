package br.com.trabalho.corretordeprovas.exception;

public class EntradaInvalidaException extends Exception {
    private final String valor;
    private final String campo;

    public EntradaInvalidaException (String campo, String motivo, String valor) {
        super(String.format("Erro: Valor inválido para %s: %s.", campo, motivo));
        this.valor = valor;
        this.campo = campo;
    }

    public EntradaInvalidaException (String campo, String motivo, String valor, Throwable causa) {
        super(String.format("Erro: Valor inválido para %s: %s.", campo, motivo), causa);
        this.valor = valor;
        this.campo = campo;
    }

    public String getValor () { return valor; }
    public String getCampo () { return campo; }
}
