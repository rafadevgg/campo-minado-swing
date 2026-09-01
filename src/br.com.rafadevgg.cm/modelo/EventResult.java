package br.com.rafadevgg.cm.modelo;

public class EventResult {

    private final boolean ganhou;

    public EventResult(boolean win) {
        this.ganhou = win;
    }

    public boolean isGanhou() {
        return ganhou;
    }
}
