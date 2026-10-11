import vista.PantallaPrincipal;
import modelo.ConexionBD;

public class Main {
    public static void main(String[] args) {

        // 1. Verificar conexión a MySQL por consola
        System.out.println("Iniciando prueba de conexión...");
        ConexionBD.conectar();

        // 2. Ejecutar la interfaz gráfica de Swing
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PantallaPrincipal pantalla = new PantallaPrincipal();
                pantalla.setVisible(true);
            }
        });
    }
}