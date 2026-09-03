package br.com.rafadevgg.cm.visao;

import br.com.rafadevgg.cm.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {
        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
        tabuleiro.forOne(c -> add(new BotaoCampo(c)));
        tabuleiro.registerObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Ganhou 😊");
                } else {
                    JOptionPane.showMessageDialog(this, "Perdeu 😒");
                }

                tabuleiro.reiniciar();
            });
        });
    }

}
