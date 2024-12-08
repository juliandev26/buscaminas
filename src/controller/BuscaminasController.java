package controller;

import model.BuscaminasModel;
import view.BuscaminasView;

public class BuscaminasController {
    private final BuscaminasModel model;
    private final BuscaminasView view;

    public BuscaminasController(BuscaminasModel model, BuscaminasView view) {
        this.model = model;
        this.view = view;
    }

    public void iniciarJuego() {
        view.mostrarMensaje("\nBienvenido a Buscaminas\n");

        while (model.getIntentosRestantes() > 0 && model.getMinasRestantes() > 0) {
            int[] coordenadas = view.pedirCoordenadas();
            int x = coordenadas[0];
            int y = coordenadas[1];

            if (x < 0 || x >= 20 || y < 0 || y >= 20) {
                view.mostrarMensaje("Coordenadas fuera de rango. Intente de nuevo.");
                continue;
            }

            boolean minaEncontrada = model.esMina(x, y);
            int minasCercanas = model.contarMinasCercanas(x, y);
            model.marcarIntento(minaEncontrada);

            view.mostrarResultado(minaEncontrada, minasCercanas, model.getIntentosRestantes(), model.getMinasRestantes());
        }

        if (model.getMinasRestantes() == 0) {
            view.mostrarMensaje("¡Ganaste!");
        } else {
            view.mostrarMensaje("¡Perdiste!");
        }
    }
}