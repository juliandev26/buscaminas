package model;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
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
        // Aseguramos que el tamaño sea 10x10 si no se proporciona otro tamaño
        if (tamaño != 10 || numMinas < 10 || numMinas > tamaño * tamaño) {
            throw new IllegalArgumentException("El tamaño del tablero debe ser 10x10 y el número de minas debe ser válido.");
        }

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
        if (fila < 0 || fila >= tamaño || columna < 0 || columna >= tamaño) {
            throw new IllegalArgumentException("Coordenadas fuera de los límites del tablero.");
        }
        if (revelado[fila][columna]) {
            throw new CasillaYaDescubiertaException("La casilla ya está descubierta.");
        }
        if (marcado[fila][columna]) return false;

        // Usar una cola para manejar la revelación de casillas
        Queue<int[]> porRevelar = new LinkedList<>();
        porRevelar.add(new int[]{fila, columna});

        while (!porRevelar.isEmpty()) {
            int[] actual = porRevelar.poll();
            int f = actual[0];
            int c = actual[1];

            if (revelado[f][c]) continue; // Si ya se reveló, pasar a la siguiente

            revelado[f][c] = true;

            if (tablero[f][c] == -1) {
                juegoTerminado = true;
                return true; // Encontrada una mina
            }

            if (tablero[f][c] == 0) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int nf = f + i, nc = c + j;
                        if (nf >= 0 && nf < tamaño && nc >= 0 && nc < tamaño && !revelado[nf][nc]) {
                            porRevelar.add(new int[]{nf, nc});
                        }
                    }
                }
            }
        }

        return false; // No es mina, ni juego terminado
    }

    public void marcarCasilla(int fila, int columna) {
        if (!revelado[fila][columna]) marcado[fila][columna] = !marcado[fila][columna];
    }

    public boolean esJuegoTerminado() {
        return juegoTerminado;
    }

    public boolean esVictoria() {
        // Recorrer todas las casillas del tablero
        for (int fila = 0; fila < tamaño; fila++) {
            for (int columna = 0; columna < tamaño; columna++) {
                // Si hay una casilla no revelada que no sea mina, no hay victoria
                if (!revelado[fila][columna] && tablero[fila][columna] != -1) {
                    return false;
                }
            }
        }
        // Si todas las casillas no minadas están reveladas, se considera victoria
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

    // Método solo para pruebas (puedes eliminarlo en producción)
    public int[][] obtenerEstadoTablero() {
        return tablero;
    }

    // Método para verificar si una casilla está revelada
    public boolean[][] obtenerEstadoRevelado() {
        return revelado;
    }

}
