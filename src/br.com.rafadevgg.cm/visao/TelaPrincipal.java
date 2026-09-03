package br.com.rafadevgg.cm.visao;

import br.com.rafadevgg.cm.modelo.Tabuleiro;

import javax.swing.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
        PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);

        add(painelTabuleiro);
        setTitle("Campo Minado");
        setSize(690, 438);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {

        new TelaPrincipal();

    }

}
