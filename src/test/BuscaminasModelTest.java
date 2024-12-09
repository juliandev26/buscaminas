package test;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.CasillaYaDescubiertaException;
import model.BuscaminasModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BuscaminasModelTest {

    private BuscaminasModel modelo;

    @BeforeEach
    void setUp() {
        modelo = new BuscaminasModel(10, 10); // Crear un tablero de 10x10 con 10 minas
    }

    @Test
    void testInicializacionModelo() {
        int[][] tablero = modelo.obtenerEstadoTablero();
        boolean[][] revelado = modelo.obtenerEstadoRevelado();

        // Verificar que el tablero está inicializado correctamente
        assertNotNull(tablero, "El tablero no debe ser nulo.");
        assertNotNull(revelado, "El estado de revelado no debe ser nulo.");
        assertEquals(10, tablero.length, "El tamaño del tablero debe ser 10.");
        assertEquals(10, tablero[0].length, "El tamaño del tablero debe ser 10.");
    }

    @Test
    void testRevelarCasilla() {
        // Revelar una casilla aleatoria no mina (no se puede asegurar qué casilla es)
        try {
            for (int fila = 0; fila < 10; fila++) {
                for (int columna = 0; columna < 10; columna++) {
                    if (modelo.obtenerEstadoTablero()[fila][columna] != -1) {
                        boolean resultado = modelo.revelarCasilla(fila, columna);
                        assertFalse(resultado, "Revelar una casilla no mina no debe terminar el juego.");
                        assertTrue(modelo.obtenerEstadoRevelado()[fila][columna], "La casilla debe estar marcada como revelada.");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción al revelar una casilla válida: " + e.getMessage());
        }
    }

    @Test
    void testRevelarCasillaMina() {
        try {
            for (int fila = 0; fila < 10; fila++) {
                for (int columna = 0; columna < 10; columna++) {
                    if (modelo.obtenerEstadoTablero()[fila][columna] == -1) {
                        boolean resultado = modelo.revelarCasilla(fila, columna);
                        assertTrue(resultado, "Revelar una mina debe terminar el juego.");
                        assertTrue(modelo.esJuegoTerminado(), "El juego debe estar terminado.");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción al revelar una casilla válida: " + e.getMessage());
        }
    }

    @Test
    void testMarcarCasilla() {
        modelo.marcarCasilla(0, 0);
        boolean[][] marcado = modelo.obtenerEstadoRevelado();
        assertFalse(marcado[0][0], "La casilla marcada no debe estar revelada.");
    }

    @Test
    void testVictoria() {
        try {
            int[][] tablero = modelo.obtenerEstadoTablero();
            boolean[][] revelado = modelo.obtenerEstadoRevelado();
            for (int fila = 0; fila < 10; fila++) {
                for (int columna = 0; columna < 10; columna++) {
                    if (tablero[fila][columna] != -1 && !revelado[fila][columna]) {
                        // Solo revelar si la casilla no está revelada
                        modelo.revelarCasilla(fila, columna);
                    }
                }
            }
            // Verificar que el estado del juego es victoria
            assertTrue(modelo.esVictoria(), "Todas las casillas no mina están reveladas, debería ser una victoria.");
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción al revelar las casillas no mina: " + e.getMessage());
        }
    }


    @Test
    void testGuardarYCargarEstado() {
        String archivo = "test_game.ser";
        try {
            modelo.guardarEstado(archivo);
            BuscaminasModel cargado = BuscaminasModel.cargarEstado(archivo);
            assertNotNull(cargado, "El modelo cargado no debe ser nulo.");
            assertArrayEquals(modelo.obtenerEstadoTablero(), cargado.obtenerEstadoTablero(), "El estado del tablero debe coincidir.");
        } catch (IOException | ClassNotFoundException e) {
            fail("No se esperaba una excepción al guardar/cargar el estado: " + e.getMessage());
        }
    }

    @Test
    void testExcepcionCasillaYaDescubierta() {
        try {
            modelo.revelarCasilla(0, 0);
            assertThrows(CasillaYaDescubiertaException.class, () -> modelo.revelarCasilla(0, 0), "Revelar una casilla ya descubierta debe lanzar una excepción.");
        } catch (Exception e) {
            fail("No se esperaba ninguna excepción en el primer intento de revelar una casilla: " + e.getMessage());
        }
    }
}
