package br.com.rafadevgg.cm.visao;

import br.com.rafadevgg.cm.modelo.Campo;
import br.com.rafadevgg.cm.modelo.CampoEvent;
import br.com.rafadevgg.cm.modelo.CampoObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObserver, MouseListener {

    private Campo campo;
    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCAR = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXT_VERDE = new Color(0, 100, 0);

    public BotaoCampo(Campo campo) {
        this.campo = campo;
        setBorder(BorderFactory.createBevelBorder(0));
        setBackground(BG_PADRAO);
        setOpaque(true);

        addMouseListener(this);
        campo.registerObserver(this);
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvent campoEvent) {
        switch (campoEvent) {
            case ABRIR:
                aplicarEstiloAbrir();
                break;
            case MARCAR:
                aplicarEstiloMarcar();
                break;
            case EXPLODIR:
                aplicarEstiloExplodir();
                break;
            default:
                aplicarEstiloPadrao();
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void aplicarEstiloAbrir() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (campo.isMinado()) {
            setBackground(BG_EXPLODIR);
            setForeground(Color.WHITE);
            setText("X");
            return;
        }

        setBackground(BG_PADRAO);
        switch (campo.minasNaVizinhanca()) {
            case 1:
                setForeground(TEXT_VERDE);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        String value = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(value);
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCAR);
        setForeground(Color.BLACK);
        setText("N");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.WHITE);
        setText("X");
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setText("");
        setBorder(BorderFactory.createBevelBorder(0));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            campo.abrir();
        } else {
            campo.alternarMarcacao();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
