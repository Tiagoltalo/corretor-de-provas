package br.com.trabalho.corretordeprovas.utils;

import java.util.Scanner;

public class Utils {
    Scanner scanner = new Scanner(System.in);

    public String lerTexto () {
        String texto = scanner.nextLine().trim();

        return texto;
    }

    public void imprimirMensagem (String mensagem) {
        System.out.print(mensagem);
    }

    public void pausar () {
        System.out.print("\nPressione Enter... ");
        scanner.nextLine();
    }

    public void limparTerminal () {
        System.out.print("\033c");
    }
}
