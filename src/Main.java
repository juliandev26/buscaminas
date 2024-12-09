// Main.java
import controller.BuscaminasController;
import model.BuscaminasModel;
import view.BuscaminasView;

public class Main {
    public static void main(String[] args) {
        BuscaminasModel model = new BuscaminasModel(10, 10); // Tablero 10x10 con 10 minas
        BuscaminasView view = new BuscaminasView();
        BuscaminasController controller = new BuscaminasController(model, view);

        controller.iniciarJuego();
    }
}