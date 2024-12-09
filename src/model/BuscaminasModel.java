package model;

import java.io.*;
import java.util.Random;
import exceptions.CasillaYaDescubiertaException;

public class BuscaminasModel implements Serializable {
    private final int[][] tablero;
    private final boolean[][] revelado;
    private final boolean[][] marcado;
    private final int tamaño;
    private int minasRestantes;
    private boolean juegoTerminado;
    private static final long serialVersionUID = 1L;

    public BuscaminasModel(int tamaño, int numMinas) {
        this.tamaño = tamaño;
        this.tablero = new int[tamaño][tamaño];
        this.revelado = new boolean[tamaño][tamaño];
        this.marcado = new boolean[tamaño][tamaño];
        this.minasRestantes = numMinas;
        this.juegoTerminado = false;

        colocarMinas(numMinas);
        calcularNumeros();
    }

    private void colocarMinas(int numMinas) {
        Random random = new Random();
        int colocadas = 0;

        while (colocadas < numMinas) {
            int fila = random.nextInt(tamaño);
            int columna = random.nextInt(tamaño);
            if (tablero[fila][columna] == -1) continue; // Ya hay una mina

            tablero[fila][columna] = -1; // Mina
            colocadas++;
        }
    }

    private void calcularNumeros() {
        for (int fila = 0; fila < tamaño; fila++) {
            for (int columna = 0; columna < tamaño; columna++) {
                if (tablero[fila][columna] == -1) continue;

                int contador = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nfila = fila + i, ncolumna = columna + j;
                        if (nfila >= 0 && nfila < tamaño && ncolumna >= 0 && ncolumna < tamaño) {
                            if (tablero[nfila][ncolumna] == -1) contador++;
                        }
                    }
                }
                tablero[fila][columna] = contador;
            }
        }
    }

    public boolean revelarCasilla(int fila, int columna) throws CasillaYaDescubiertaException {
        if (revelado[fila][columna]) throw new CasillaYaDescubiertaException("La casilla ya está descubierta.");
        if (marcado[fila][columna]) return false;

        revelado[fila][columna] = true;

        if (tablero[fila][columna] == -1) {
            juegoTerminado = true;
            return true; // Mina encontrada
        }

        if (tablero[fila][columna] == 0) {
            // Revelar adyacentes
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int nfila = fila + i, ncolumna = columna + j;
                    if (nfila >= 0 && nfila < tamaño && ncolumna >= 0 && ncolumna < tamaño) {
                        revelarCasilla(nfila, ncolumna);
                    }
                }
            }
        }
        return false;
    }

    public void marcarCasilla(int fila, int columna) {
        if (!revelado[fila][columna]) marcado[fila][columna] = !marcado[fila][columna];
    }

    public boolean esJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean esVictoria() {
        for (int fila = 0; fila < tamaño; fila++) {
            for (int columna = 0; columna < tamaño; columna++) {
                if (!revelado[fila][columna] && tablero[fila][columna] != -1) return false;
            }
        }
        return true;
    }

    public String obtenerTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 0; i < tamaño; i++) sb.append((char) ('A' + i)).append(" ");
        sb.append("\n");

        for (int fila = 0; fila < tamaño; fila++) {
            sb.append(fila + 1).append(" ");
            if (fila + 1 < 10) sb.append(" ");
            for (int columna = 0; columna < tamaño; columna++) {
                if (marcado[fila][columna]) sb.append("M ");
                else if (!revelado[fila][columna]) sb.append("■ ");
                else if (tablero[fila][columna] == -1) sb.append("* ");
                else sb.append(tablero[fila][columna]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Método para guardar el estado del juego
    public void guardarEstado(String nombreArchivo) throws IOException {
        File directorio = new File("saved_games");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saved_games/" + nombreArchivo))) {
            oos.writeObject(this);
        }
    }

    // Método para cargar el estado del juego
    public static BuscaminasModel cargarEstado(String nombreArchivo) throws IOException, ClassNotFoundException {
        File archivo = new File("saved_games/" + nombreArchivo);
        if (!archivo.exists()) {
            throw new FileNotFoundException("El archivo no existe: " + nombreArchivo);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (BuscaminasModel) ois.readObject();
        }
    }
}
