/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import java.io.File;
import java.util.Map;

/**
 *
 * @author kyle
 */
public class R12Operations
{

    //Command Objects
    private R12Interface r12i = null;
    private static R12Operations r12Operations = null;
    
    //connect vars
    private String address = "192.168.1.1";
    private int port = 1111;
    
    //INI vars
    private final String INI_FILENAME = "R12ArmSetup.ini";
    private final String INI_FILE_SECTION_KEY = "vars";
    private final String INI_FILE_PORT_KEY = "portkey";
    private final String INI_FILE_ADDRESS_KEY = "addresskey";

    private final String INI_CONTENTS = ";ini file for the setup of the R12 TCP Connection.\n"
            + "[vars]\n"
            + INI_FILE_ADDRESS_KEY + "=" + address + "\n"
            + INI_FILE_PORT_KEY + "=" + port;

    /**
     * initializes object's dependencies
     *
     * @return boolean of success
     */
    public boolean init()
    {
        r12i = R12Interface.getInstance();
        loadInfoFromFile();
        boolean success = r12i.init(address, port);
        return success;
    }

    
    private void loadInfoFromFile()
    {

        String workingDir = System.getProperty("user.dir");
        System.out.println("Working Directory: " + workingDir);
        String pathToFile = workingDir + File.separator + INI_FILENAME;
        System.out.println("Parsing file: " + pathToFile);
        /*=====Parsing File===*/

        Map<String, String> map = FileUtils.readINIFileOrGenerate(
                pathToFile,
                INI_FILE_SECTION_KEY,
                new String[]
                {
                    INI_FILE_PORT_KEY, INI_FILE_ADDRESS_KEY
                },
                INI_CONTENTS);
        String portTemp = map.get(INI_FILE_PORT_KEY);
        String addressTemp = map.get(INI_FILE_ADDRESS_KEY);
        if (portTemp != null)
        {            
            port = Integer.parseInt(portTemp);
            
        }
        if (addressTemp != null)
        {
            address = addressTemp;
            
        }
        System.out.println("Address: " + address +" Port: " + port);
    }

    /**
     * Returns the response without the echo
     *
     * @param command command to filter out
     * @return response without the command
     */
    public String readNoEcho(String command)
    {
        return read().replaceFirst(command, "").trim();
    }

    /**
     * Reads using the R12Interface, responds with a usable string.
     *
     * @return String including echo of command
     */
    public String read()
    {
        byte[] buffer = new byte[65536];
        int offsetIterator = 0;
        int response = 0;
        do
        {
            response = r12i.read(buffer, offsetIterator);
            offsetIterator += response;
        }
        while (buffer[offsetIterator - 1] != '>');
        String s = new String(buffer, 0, offsetIterator);
        return s;
    }

    /**
     * Writes to the R12 the command. Automatically includes the needed return.
     *
     * @param s command to send, no return needed
     */
    public void write(String s)
    {
        r12i.write(s + "\r");
    }

    public static R12Operations getInstance()
    {
        if (r12Operations == null)
        {
            r12Operations = new R12Operations();
        }
        return r12Operations;
    }
}
