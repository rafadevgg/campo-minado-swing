package br.com.rafadevgg.cm.modelo;

@FunctionalInterface
public interface CampoObserver {
    void eventoOcorreu(Campo campo, CampoEvent campoEvent);
}
