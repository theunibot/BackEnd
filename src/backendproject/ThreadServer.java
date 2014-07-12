/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robotics1
 */
public class ThreadServer extends Thread
{
    private volatile boolean isRunning = true;

    @Override
    public void run()
    {
        System.out.println("Server Thread Started");
        try
        {
            HttpServer server = HttpServer.create(new InetSocketAddress(1600), 0);
            System.out.println("bound");
            HttpContext context = server.createContext("/myapp", new MyHttpHandler());
            context.getFilters().add(new ServerParameterFilter());
            server.start();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Stops the thread gracefully
     */
    public void kill()
    {
        this.isRunning = false;
    }

    static class MyHttpHandler implements HttpHandler
    {

        /**
         * All function calls begin here
         *
         * @param exchange
         * @throws IOException
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException
        {            
            Map<String, Object> params = (Map<String, Object>) exchange.getAttribute("parameters");            
            String input = null;
            System.out.println("Beginning key search");            
            if (params.get("key1") != null)
            {
                input = params.get("key1").toString();
                System.out.println("found key");
            }       
            else
            {
                System.out.println("not found key");
            }
            System.out.println(input);
            String response = "HELLO WORLD";

            byte[] data = response.getBytes("ASCII");
            response = new String(data);
            response = response.replace("?", "");
            System.out.println(response);
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            System.out.println("Data Sent");
//            //now you can use the params
        }
    }

    class ServerParameterFilter extends Filter
    {

        @Override
        public String description()
        {
            return "Parses the requested URI for parameters";
        }

        @Override
        public void doFilter(HttpExchange exchange, Filter.Chain chain)
                throws IOException
        {
            parseGetParameters(exchange);
            parsePostParameters(exchange);
            chain.doFilter(exchange);
        }

        private void parseGetParameters(HttpExchange exchange)
                throws UnsupportedEncodingException
        {

            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);
            exchange.setAttribute("parameters", parameters);
        }

        private void parsePostParameters(HttpExchange exchange)
                throws IOException
        {

            if ("post".equalsIgnoreCase(exchange.getRequestMethod()))
            {
                @SuppressWarnings("unchecked")
                Map<String, Object> parameters
                        = (Map<String, Object>) exchange.getAttribute("parameters");
                InputStreamReader isr
                        = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                parseQuery(query, parameters);
            }
        }

        @SuppressWarnings("unchecked")
        private void parseQuery(String query, Map<String, Object> parameters)
                throws UnsupportedEncodingException
        {

            if (query != null)
            {
                String pairs[] = query.split("[&]");

                for (String pair : pairs)
                {
                    String param[] = pair.split("[=]");

                    String key = null;
                    String value = null;
                    if (param.length > 0)
                    {
                        key = URLDecoder.decode(param[0],
                                System.getProperty("file.encoding"));
                    }

                    if (param.length > 1)
                    {
                        value = URLDecoder.decode(param[1],
                                System.getProperty("file.encoding"));
                    }

                    if (parameters.containsKey(key))
                    {
                        Object obj = parameters.get(key);
                        if (obj instanceof List<?>)
                        {
                            List<String> values = (List<String>) obj;
                            values.add(value);
                        }
                        else if (obj instanceof String)
                        {
                            List<String> values = new ArrayList<String>();
                            values.add((String) obj);
                            values.add(value);
                            parameters.put(key, values);
                        }
                    }
                    else
                    {
                        parameters.put(key, value);
                    }
                }
            }
        }
    }
}
