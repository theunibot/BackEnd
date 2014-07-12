/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robotics1
 */
public class Main
{

    static List<ObjectCommand> cmdQueue;
    static List<ObjectCommand> i1Queue;
    static List<ObjectCommand> i2Queue;
    static List<ObjectCommand> i3Queue;
        

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
////        R12Interface i = new R12Interface();
////        i.init();
//        i.write("Hello World");
//        System.out.println("Written");
//        System.out.print(i.read());
////        System.out.println("read 1");       
//        System.out.print(i.read());
////        System.out.println("read 3");
//        System.out.print(i.read());
//        System.out.println("done");
//        i.quit();

//        cmdQueue = Collections.synchronizedList(new ArrayList<ObjectCommand>());
//        i1Queue = Collections.synchronizedList(new ArrayList<ObjectCommand>());
//        i2Queue = Collections.synchronizedList(new ArrayList<ObjectCommand>());
//        i3Queue = Collections.synchronizedList(new ArrayList<ObjectCommand>());
//        ThreadServer threadServer = new ThreadServer();
//        threadServer.start();        
        ThreadCommand threadCommand = new ThreadCommand();
        threadCommand.start();
        
        

        
    }    
}
