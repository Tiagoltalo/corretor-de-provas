package br.com.trabalho.corretordeprovas.io;

import br.com.trabalho.corretordeprovas.utils.Utils;
import br.com.trabalho.corretordeprovas.exception.EntradaInvalidaException;

public class LeitorDeDisciplina {
    Utils utils = new Utils();

    public String lerDisciplina () throws EntradaInvalidaException {
        String texto;
        String disciplina;

        System.out.print("Informe o nome da disciplina: ");
        texto = utils.lerTexto();

        try {
            disciplina = texto.toLowerCase();
        } catch (Exception e) {
            throw new EntradaInvalidaException("disciplina", "impossível transformar em minúscula", texto);
        }

        return disciplina;
    }

    public String gabaritoDisciplina () throws EntradaInvalidaException {
        String texto;
        String gabarito;

        System.out.print("Informe o gabarito da disciplina (ex: vfvfffv): ");
        texto = utils.lerTexto();

        try {
            gabarito = texto.toLowerCase();
        } catch (Exception e) {
            throw new EntradaInvalidaException("disciplina", "impossível transformar em minúscula", texto);
        }

        for (char resposta : gabarito.toCharArray()) {
            if (resposta == 'v') {
                continue;
            } else if (resposta == 'f') {
                continue;
            } else {
                throw new EntradaInvalidaException("gabarito", "resposta diferente do esperado", gabarito);
            }
        }

        if (gabarito.length() > 10) {
            throw new EntradaInvalidaException("gabarito", "resposta maior que o esperado", gabarito);
        }

        return gabarito;
    }
}
