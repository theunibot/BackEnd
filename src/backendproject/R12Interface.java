/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

/**
 *
 * @author kyle
 */
public class R12Interface
{

    private final boolean USE_CONNECT_TIMEOUT = true;

    private final int INITIAL_CONNECT_TIMEOUT = 5000;//milis
    private String address = "192.168.1.1";
    private int port = 1111;

    private final String INI_FILENAME = "R12ArmSetup.ini";
    private final String INI_FILE_SECTION_KEY = "vars";
    private final String INI_FILE_PORT_KEY = "portkey";
    private final String INI_FILE_ADDRESS_KEY = "addresskey";

    private Socket socket = null;
    private InputStream inputStream  = null;
    private BufferedReader inFromServer = null;   
    private DataOutputStream outToServer = null;

    private static R12Interface r12Interface = null;

    private volatile boolean isRunning = true;

    /**
     * Init the R12 interface. Sets up the socket, return success or failure.
     * Pulls its values from an INI file. If one does not exist, one is
     * generated automatically.
     *
     * @return boolean - true success, false failure
     */
    public boolean init()
    {
        boolean success = false;
        loadInfoFromFile();
        System.out.println("Opening Socket...");

        try
        {
            socket = new Socket();
            if (USE_CONNECT_TIMEOUT)
            {
                socket.connect(new InetSocketAddress(InetAddress.getByName(address), port), INITIAL_CONNECT_TIMEOUT);
            }
            else
            {
                socket.connect(new InetSocketAddress(InetAddress.getByName(address), port));
            }
            
            inputStream = socket.getInputStream();            
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            outToServer = new DataOutputStream(socket.getOutputStream());
            success = true;
            System.out.println("Socket Opened Succesfully");
        }
        catch (IOException ex)
        {
            Logger.getLogger(R12Interface.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Setup of socket to address " + address + " on port " + port + " failed.");
        }

        return success;
    }

    /**
     * Stops the interface gracefully
     */
    public void kill()
    {
        this.isRunning = false;
    }

    public static R12Interface getInstance()
    {
        if (r12Interface == null)
        {
            r12Interface = new R12Interface();
        }
        return r12Interface;
    }

    private void loadInfoFromFile()
    {
        try
        {
            String workingDir = System.getProperty("user.dir");
            System.out.println("Working Directory: " + workingDir);
            String pathToFile = workingDir + File.separator + INI_FILENAME;
            System.out.println("Parsing file: " + pathToFile);
            /*=====Parsing File===*/
            File file = new File(pathToFile);
            if (file.exists())
            {
                System.out.println("INI File found");
                Ini ini = new Ini(file);
                String tempPort = ini.get(INI_FILE_SECTION_KEY, INI_FILE_PORT_KEY);
                String tempAddress = ini.get(INI_FILE_SECTION_KEY, INI_FILE_ADDRESS_KEY);
                if (tempPort != null && !tempPort.equals(""))
                {
                    port = Integer.valueOf(tempPort);
                }
                if (tempAddress != null && !tempAddress.equals(""))
                {
                    address = tempAddress;
                }
                System.out.println("Info from INI file: addr: " + address + " port: " + port);
            }
            else
            {
                System.out.println("INI File not found. Creating one at " + pathToFile);
                if (file.createNewFile())//file succesfully created
                {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(";ini file for the setup of the R12 TCP Connection.\n"
                            + "[vars]\n"
                            + INI_FILE_ADDRESS_KEY + "=" + address + "\n"
                            + INI_FILE_PORT_KEY + "=" + port);
                    bw.close();
                }
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(R12Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Reads the info sent back from the TCP connection. <b>This IS
     * blocking.</b>
     *
     * @return <b>If successful:</b> the response String<br/>
     * <b>If failure:</b> null
     */
    public int read(byte[] buffer, int offset)
    {
        int readVal = -2;
        if (inputStream != null)
        {
            try
            {
//                System.out.println(offset);
                readVal = inputStream.read(buffer, offset, buffer.length - offset);
            }
            catch (IOException ex)
            {
                Logger.getLogger(R12Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return readVal;
    }

    /**
     * Writes out the TCP connection. <b>This is NOT blocking.</b>
     *
     * @param s String to write out the TCP connection.
     * @return boolean - if write was successful
     */
    public boolean write(String s)
    {

        try
        {
            outToServer.writeBytes(s);
//            outToServer.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(R12Interface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Breaks down the connection and quits, properly closing all objects.
     */
    public void quit()
    {
        try
        {
            inFromServer.close();
            outToServer.close();
            socket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(R12Interface.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Quit Function failed");
        }
    }
}
