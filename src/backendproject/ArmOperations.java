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
public class ArmOperations
{

    private R12Operations r12o = null;
    private static ArmOperations armOprations = null;
    
    //Regular Objects
    private ArrayList<String> initCommands = null;

    //Strings
    private final String RESPONSE_OK = "OK";

    private final String INIT_COMMANDS_FILENAME = "initCommands.txt";
    private final String INIT_COMMANDS_FILEPATH = FileUtils.getWorkingDirectoryString() + INIT_COMMANDS_FILENAME;

    private final String INIT_FILE_HEADER = ""
            + "//This is the R12 Robot Init File. This file is where commands are placed in the RoboForth"
            + "\n//Language and are called upon startup by the R12Operations object. "
            + "\n//Each command should be seperated by a carriage return."
            + "\n//All comments must be on their own line and start with \"//\"";

    public ArmOperations()
    {
        
    }

    public boolean init()
    {
        r12o = R12Operations.getInstance();
        boolean success = r12o.init();
        //if init command file exists, read all the commands and write them out to the 
        initCommands = FileUtils.readCommandFileOrGenEmpty(INIT_COMMANDS_FILEPATH, INIT_FILE_HEADER);

        System.out.println("Read " + initCommands.size() + " command(s) from init commands file.");
        
        if (success)//if the socket was setup and read/write is OK to use
        {
            for (String command : initCommands)
            {
                r12o.write(command);
                String response = r12o.read();
                
                if (!response.equals(RESPONSE_OK))
                {
                    System.err.println("Command " + command + " got a response of " + response);
                    success = false;
                }
            }            
        }
        return success;
    }

    public static ArmOperations getInstance()
    {
        if (armOprations == null)
        {
            armOprations = new ArmOperations();
        }
        return armOprations;
    }
}
