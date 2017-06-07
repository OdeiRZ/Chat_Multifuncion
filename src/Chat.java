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
    
}