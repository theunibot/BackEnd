/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import java.util.ArrayList;

/**
 *
 * @author kyle
 */
public class R12Operations
{

    //Command Objects
    private R12Interface r12i = null;
    private static R12Operations r12Operations = null;

    /**
     * initializes object's dependencies
     * @return boolean of success
     */
    public boolean init()
    {
        r12i = R12Interface.getInstance();
        boolean success = r12i.init();

        return success;
    }
    
    
    /**
     * Returns the response without the echo
     * @param command command to filter out
     * @return response without the command
     */
    public String readNoEcho(String command)
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
     * Reads using the R12Interface, responds with a usable string.
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
