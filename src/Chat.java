import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 * Clase que simula el funcionamiento de un Chat. 
 * Se encarga de alternar funciones de Servidor y Cliente para que desde la 
 * misma aplicación se puedan realizar y recibir peticiones simultáneas.
 *
 * @author Odei Riveiro Zafra
 * @version 08.06.2016
 */
public class Chat {
    /**
     * Variable de tipo cadena usada para almacenar el HOST de Conexión.
     */
    protected static String HOST = "localhost";
    /**
     * Variable entera usada para almacenar el Puerto de Conexión.
     */
    protected static int Puerto = 1800;
    /**
     * Buffer usado para capturar por teclado las cadenas introducidas.
     */
    protected static BufferedReader br = 
            new BufferedReader(new InputStreamReader(System.in));
    
    /**
     * Lanza una instancia de la aplicación donde validaremos en primer lugar
     * el usuario introducido por teclado y .
     * 
     * @param args String[]: argumentos de la línea de comandos,
     * en primer lugar obtenemos el HOST y en segundo el Puerto de Conexón,
     * si no son introducidos usamos los asignados por defecto
     * @throws java.io.IOException posible excepción de entrada
     */
    public static void main(String[] args) throws IOException {
        String usuario;
        boolean valido;
        
        if (args.length == 2) {                                                 // Comprobamos si recibimos como parámetros el HOST y Puerto de la línea de comandos
            HOST = args[0];                                                     // y sobrescribimos los valores por defecto de los mismos si se da el caso
            Puerto = Integer.parseInt(args[1]);
        }
        
        do {
            System.out.print("Introduce tu Usuario: ");
            usuario = br.readLine();                                            // Capturamos usuario 
            valido = comprobarUsuario(usuario);                                 // mientras que no sea valido
            if (!valido) {
                System.out.println("El Usuario no es Válido");
            }
        } while(!valido);
        
        lanzarCliente(usuario);                                                 // Una vez validado el usuario lanzamos el cliente
    }

}