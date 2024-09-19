package edu.escuelaing.arem.ASE.app;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcreflexBEServer {
    public static void main(String[] args) throws IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        boolean running = true;
        while(running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isfirstLine = true;
            String firstLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isfirstLine) {
                    firstLine = inputLine;
                    isfirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI requri = getReqURI(firstLine);

            if (requri.getPath().startsWith("/compreflex")) {
                String result = computeMathCommand(requri.getQuery());
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\"result\":\"" + result + "\"}\n";

            } else {
                outputLine = getDefaultResponse();
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String getDefaultResponse() {
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Title of the document</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>Method Not Found</h1>\n"
                + "</body>\n"
                + "</html>\n";
        return response;
    }

    public static URI getReqURI(String firstLine) throws URISyntaxException {
        String rurl = firstLine.split(" ")[1];
        return new URI(rurl);
    }

    public static String computeMathCommand(String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Pattern pattern = Pattern.compile("comando=(\\w+)\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(command);

        if (matcher.find()) {
            String funcion = matcher.group(1);
            String[] numeros = matcher.group(2).split(",");

            System.out.println("Función: " + funcion);
            System.out.println("Números:");
            Class[] parameterTypes = new Class[numeros.length];
            int i = 0;
            Object[] params = new Object[numeros.length];
            for (String num : numeros) {
                System.out.println(num);
                parameterTypes[i] = double.class;
                params[i] = (double) Double.parseDouble(num);
                i++;
            }

            Class c = Math.class;

            Method m = c.getDeclaredMethod(funcion, parameterTypes);

            String resp = m.invoke(null, params).toString();

            return resp;
        }



        return "";
    }
}
