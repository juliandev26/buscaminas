package view;

import java.util.Scanner;

public class BuscaminasView {
    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public int[] pedirCoordenadas() {
        int[] coordenadas = new int[2];
        System.out.print("Ingrese Fila (1-10): ");
        coordenadas[0] = scanner.nextInt() - 1;

        System.out.print("Ingrese Columna (1-10): ");
        coordenadas[1] = scanner.nextInt() - 1;

        return coordenadas;
    }

    public void mostrarResultado(boolean minaEncontrada, int minasCercanas, int intentosRestantes, int minasRestantes) {
        if (minaEncontrada) {
            System.out.println("\n¡Acertaste! Restan " + minasRestantes + " minas.");
        } else {
            System.out.println("\nMina no encontrada. Minas cercanas: " + minasCercanas);
        }
        System.out.println("Intentos restantes: " + intentosRestantes);
    }
}