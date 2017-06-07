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

    /**
     * Método usado para simular la lógica del Cliente.
     * 
     * @param usuario String: usuario validado
     */
    protected static void lanzarCliente(String usuario) {
        try{
            boolean sw = true;
            boolean sw2 = false;
            String entrada, salida, usuarioAux = "";
            
            while (sw) {
                try (Socket sCliente = new Socket(HOST, Puerto)) {              // Intentamos conectar al Servidor
                    if (!sw2) {                                                 // Si es la primera vez que entramos
                        sw2 = !sw2;
                        OutputStream os = sCliente.getOutputStream();           // Mandamos al Servidor el nombre del cliente
                        DataOutputStream flujo_salida = new DataOutputStream(os);
                        flujo_salida.writeUTF(usuario);
                        
                        InputStream is = sCliente.getInputStream();             // y recibimos el del servidor
                        DataInputStream flujo_entrada = new DataInputStream(is);
                        usuarioAux = flujo_entrada.readUTF();
                        System.out.println("Servidor conectado: " + usuarioAux);
                    } else {                                                    // En caso contrario
                        System.out.print(usuario + " >> ");
                        salida = br.readLine();                                 // Capturamos salida a enviar al servidor
                        OutputStream os = sCliente.getOutputStream();
                        DataOutputStream flujo_salida = new DataOutputStream(os);
                        flujo_salida.writeUTF(salida);                          // y se la mandamos
                        
                        if (salida.equals("EXIT")) {
                            sw = false;
                        } else {
                            InputStream is = sCliente.getInputStream();
                            DataInputStream flujo_entrada = new DataInputStream(is);
                            entrada = flujo_entrada.readUTF();                  // Obtenemos la entrada recibida del servidor
                            System.out.println(usuarioAux + " >> " + entrada);  // y la mostramos
                            if (entrada.equals("EXIT")) {
                                sw = false;                                     // Repetimos el proceso mientras las instrucciones no sean EXIT
                            }
                        }
                    }
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());                                 // Si se produce un error mostramos dicho error
            lanzarServidor(usuario);                                            // Y lanzamos el Servidor mandandole el nombre de usuario
        }           
    }
    
    /**
     * Método usado para simular la lógica del Servidor.
     * 
     * @param usuario String: usuario validado
     */
    public static void lanzarServidor(String usuario) {
        try {
            boolean sw = true;
            boolean sw2 = false;
            String entrada, salida, usuarioAux = "";
            
            ServerSocket skServidor = new ServerSocket(Puerto);                 // Publicamos Servidor por el puerto establecido
            System.out.println("Escuchando puerto " + Puerto);
            
            while (sw) {
                try (Socket sCliente = skServidor.accept()) {                   // Nos mantenemos a la espera de peticiones de clientes
                    if (!sw2) {                                                 // Si es la primera vez que entramos
                        sw2 = !sw2;
                        InputStream is = sCliente.getInputStream();
                        DataInputStream flujo_entrada = new DataInputStream(is);
                        usuarioAux = flujo_entrada.readUTF();                   // Recibimos el nombre del cliente
                        System.out.println("Cliente conectado: " + usuarioAux);
                        
                        OutputStream os = sCliente.getOutputStream();
                        DataOutputStream flujo_salida = new DataOutputStream(os);
                        flujo_salida.writeUTF(usuario);                         // y le mandamos el nuestro
                    } else {                                                    // En caso contrario
                        InputStream is = sCliente.getInputStream();
                        DataInputStream flujo_entrada = new DataInputStream(is);
                        entrada = flujo_entrada.readUTF();                      // Obtenemos ebtrada recibida del cliente
                        System.out.println(usuarioAux + " >> " + entrada);      // y la mostramos
                        
                        if (entrada.equals("EXIT")) {
                            sw = false;
                        } else {
                            System.out.print(usuario + " >> ");
                            salida = br.readLine();                             // Capturamos salida a enviar al cliete
                            OutputStream os = sCliente.getOutputStream();
                            DataOutputStream flujo_salida = new DataOutputStream(os);
                            flujo_salida.writeUTF(salida);                      // y se la mandamos
                            if (salida.equals("EXIT")) {
                                sw = false;                                     // Repetimos el proceso mientras las instrucciones no sean EXIT
                            } 
                        }
                    }
                }
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());                                 // Si se produce un error mostramos dicho error
        }
    }
    
    /**
     * Método usado para validar el usuario recibido como parámetro.
     * 
     * @param usuario String: usuario a validar
     * @return boolean: resultado de la validación del usuario
     */
    protected static boolean comprobarUsuario(String usuario) {
        boolean sw = false;
        if (Pattern.compile("^[a-z0-9_]{3,8}$").matcher(usuario).find()) {      // Si la expresión regular valida al usuario
            sw = true;                                                          // activamos la variable sw
        }
        return sw;
    }
}