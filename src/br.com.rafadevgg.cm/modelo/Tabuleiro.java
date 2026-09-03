package br.com.rafadevgg.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObserver {

    private final int linhas;
    private final int colunas;
    private final int minas;
    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<EventResult>> observers = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarOsVizinhos();
        sortearMinas();
    }

    public void forOne(Consumer<Campo> funcao) {
        campos.forEach(funcao);
    }

    public void registerObserver(Consumer<EventResult> observer) {
        observers.add(observer);
    }

    private void notify(boolean result) {
        observers.stream().forEach(o -> o.accept(new EventResult(result)));
    }

    public void abrir(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
    }

    private void showMinas() {
        campos.stream()
                .filter(Campo::isMinado)
                .filter(c -> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }

    public void alternarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.alternarMarcacao());
    }

    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                Campo campo = new Campo(linha, coluna);
                campo.registerObserver(this);
                campos.add(campo);
            }
        }
    }

    private void associarOsVizinhos() {
        for (Campo c1 : campos) {
            for (Campo c2 : campos) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = Campo::isMinado;
        do {
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        } while(minasArmadas < minas);
    }

    public boolean objetivoAlcancado() {
        Predicate<Campo> objetivoAlcancado = Campo::objetivoAlcancado;
        return campos.stream().allMatch(objetivoAlcancado);
    }

    public void reiniciar() {
        campos.stream().forEach(Campo::reiniciar);
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvent campoEvent) {
        if (campoEvent == CampoEvent.EXPLODIR) {
            showMinas();
            notify(false);
        } else if (objetivoAlcancado()) {
            System.out.println("Ganhou...");
            notify(true);
        }
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }

}
