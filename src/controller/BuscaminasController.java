package controller;

import model.BuscaminasModel;
import view.BuscaminasView;
import exceptions.CasillaYaDescubiertaException;

import java.io.IOException;

public class BuscaminasController {
    private BuscaminasModel model;
    private final BuscaminasView view;

    public BuscaminasController(BuscaminasModel model, BuscaminasView view) {
        this.model = model;
        this.view = view;
    }

    public void iniciarJuego() {
        while (!model.esJuegoTerminado()) {
            view.mostrarTablero(model.obtenerTablero());
            String accion = view.solicitarAccion();

            try {
                if (accion.equalsIgnoreCase("G")) {
                    view.mostrarMensaje("Ingresa el nombre del archivo para guardar:");
                    String nombreArchivo = view.solicitarAccion();
                    model.guardarEstado(nombreArchivo);
                    view.mostrarMensaje("¡Juego guardado exitosamente!");
                } else if (accion.equalsIgnoreCase("C")) {
                    view.mostrarMensaje("Ingresa el nombre del archivo a cargar:");
                    String nombreArchivo = view.solicitarAccion();
                    model = BuscaminasModel.cargarEstado(nombreArchivo);
                    view.mostrarMensaje("¡Juego cargado exitosamente!");
                } else {
                    procesarAccion(accion);
                }

                if (model.esVictoria()) {
                    view.mostrarTablero(model.obtenerTablero());
                    view.mostrarMensaje("¡Ganaste! Has descubierto todas las casillas.");
                    break;
                }
            } catch (IOException e) {
                view.mostrarMensaje("Error al manejar el archivo: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                view.mostrarMensaje("El archivo no contiene un juego válido: " + e.getMessage());
            } catch (Exception e) {
                view.mostrarMensaje("Entrada inválida. Intenta de nuevo.");
            }
        }
    }

    private void procesarAccion(String accion) throws Exception {
        if (accion.startsWith("M")) {
            int fila = Integer.parseInt(accion.substring(2)) - 1;
            int columna = accion.charAt(1) - 'A';
            model.marcarCasilla(fila, columna);
        } else {
            int fila = Integer.parseInt(accion.substring(1)) - 1;
            int columna = accion.charAt(0) - 'A';
            if (model.revelarCasilla(fila, columna)) {
                view.mostrarMensaje("¡Perdiste! Encontraste una mina.");
                model.guardarEstado("ultima_partida.dat"); // Guarda la última partida automáticamente
            }
        }
    }
}