package br.com.rafadevgg.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int linha;
    private final int coluna;
    private boolean minado;
    private boolean aberto;
    private boolean marcado;
    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObserver> observers = new ArrayList<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registerObserver(CampoObserver observer) {
        observers.add(observer);
    }

    private void notify(CampoEvent event) {
        observers.stream().forEach(o -> o.eventoOcorreu(this, event));
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = linha != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaLinha + deltaColuna;

        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;
            if (marcado) {
                notify(CampoEvent.MARCAR);
            } else {
                notify(CampoEvent.DESMARCAR);
            }
        }
    }

    boolean abrir() {
        if (!aberto && !marcado) {
            if (minado) {
                notify(CampoEvent.EXPLODIR);
                return true;
            }
            setAberto(true);
            notify(CampoEvent.ABRIR);
            if (vizinhancaSegura()) {
                vizinhos.forEach(Campo::abrir);
            }
            return true;
        } else {
            return false;
        }
    }

    boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    void minar() {
        minado = true;
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isAberto() {
        return aberto;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
        if (aberto) {
            notify(CampoEvent.ABRIR);
        }
    }

    public boolean isFechado() {
        return !aberto;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    long minasNaVizinhanca() {
        return vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
    }

}
