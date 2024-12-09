package view;

import java.util.Scanner;

public class BuscaminasView {
    private final Scanner scanner = new Scanner(System.in);

    public String solicitarAccion() {
        System.out.println("Introduce tu acción (ej. 'A5' para descubrir, 'M A5' para marcar, 'G' para guardar, 'C' para cargar):");
        return scanner.nextLine().trim();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarTablero(String tablero) {
        System.out.println(tablero);
    }
}
