package model;

import java.util.Random;

public class BuscaminasModel {
    private final int[][] tablero;
    private int minasRestantes;
    private final int intentosTotales = 75;
    private int intentosRestantes;

    public BuscaminasModel(int tamaño, int numMinas) {
        tablero = new int[tamaño][tamaño];
        minasRestantes = numMinas;
        intentosRestantes = intentosTotales;
        colocarMinas(tamaño, numMinas);
    }

    private void colocarMinas(int tamaño, int numMinas) {
        Random random = new Random();
        int colocadas = 0;

        while (colocadas < numMinas) {
            int x = random.nextInt(tamaño);
            int y = random.nextInt(tamaño);

            if (tablero[x][y] == 0) {
                tablero[x][y] = 1; // Mina colocada
                colocadas++;
            }
        }
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }

    public int getMinasRestantes() {
        return minasRestantes;
    }

    public boolean esMina(int x, int y) {
        return tablero[x][y] == 1;
    }

    public int contarMinasCercanas(int x, int y) {
        int conteo = 0;
        int tamaño = tablero.length;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < tamaño && ny >= 0 && ny < tamaño) {
                    conteo += tablero[nx][ny];
                }
            }
        }
        return conteo;
    }

    public void marcarIntento(boolean minaEncontrada) {
        if (minaEncontrada) {
            minasRestantes--;
        }
        intentosRestantes--;
    }
}