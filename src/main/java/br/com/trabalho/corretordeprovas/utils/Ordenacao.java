package br.com.trabalho.corretordeprovas.utils;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Ordenação alfabética em português: ignora maiúsculas/minúsculas e posiciona
 * corretamente as letras acentuadas (Álgebra junto do A, e não depois do Z).
 */
public final class Ordenacao {
    private static final Collator COLLATOR = Collator.getInstance(Locale.forLanguageTag("pt-BR"));

    static {
        COLLATOR.setStrength(Collator.SECONDARY);
    }

    /** Ordem alfabética crescente (A a Z). Textos que só diferem na caixa ficam em ordem estável. */
    public static final Comparator<String> ALFABETICA = (a, b) -> {
        int comparacao;
        synchronized (COLLATOR) {
            comparacao = COLLATOR.compare(a, b);
        }
        return comparacao != 0 ? comparacao : a.compareTo(b);
    };

    private Ordenacao() {}
}
