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
            for (String command : initCommands)//runs every command in the file
            {
                command = command.trim();//removes extra whitespace
                r12o.write(command);
                ResponseObject response = getResponse(command);

                if (!response.isSuccessful())
                {
                    System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
                }
            }
        }
        return success;
    }

    /**
     * Returns a wrapper object holding data from response.
     *
     * @param command command sent, used to filter out of response.
     * @return ResponseObject wrapper object for command sent
     */
    public ResponseObject getResponse(String command)
    {
        String responseStr = r12o.readNoEcho(command);

        //clean up string
        responseStr = responseStr.replace("\n>", "");//filters the ">" and the new line. Saves all other new lines
        responseStr = responseStr.replace(">", "");//removes any missed ">"
        responseStr = responseStr.trim();
        boolean succesful = false;
        if (responseStr.endsWith(RESPONSE_OK))
        {
            succesful = true;
        }
        return new ResponseObject(responseStr, succesful);

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
