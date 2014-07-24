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
    private RouteCompiler rc = null;
    private static ArmOperations armOprations = null;

    //Regular Objects
    private ArrayList<String> initCommands = null;

    //Strings
    public static final String RESPONSE_OK = "OK";

    private final String INIT_COMMANDS_FILENAME = "initCommands.txt";
    private final String INIT_COMMANDS_FILEPATH = FileUtils.getFilesFolderString() + INIT_COMMANDS_FILENAME;

    private final String INIT_FILE_HEADER = ""
            + "//This is the R12 Robot Init File. This file is where commands are placed in the RoboForth"
            + "\n//Language and are called upon startup by the ThreadCommand thread. "
            + "\n//Each command should be seperated by a carriage return."
            + "\n//All comments must be on their own line and start with \"//\"";

    public ArmOperations()
    {

    }

    public boolean init()
    {
        r12o = R12Operations.getInstance();
        rc = RouteCompiler.getInstance();
        boolean success = false;
        rc.init();
//        if (r12o.init() && runInitCommands() && rc.init())
//        {
//            success = true;
//        }
        return success;
    }

    private boolean runInitCommands()
    {

        //if init command file exists, read all the commands and write them out to the 
        initCommands = FileUtils.readCommandFileOrGenEmpty(INIT_COMMANDS_FILEPATH, INIT_FILE_HEADER);

        System.out.println("Read " + initCommands.size() + " command(s) from init commands file.");

        for (String command : initCommands)//runs every command in the file
        {
            r12o.write(command);
            ResponseObject response = r12o.getResponse(command);

            System.out.println(response.getMsg());
            if (!response.isSuccessful())
            {
                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
//                    return false;
            }
        }
        return true;
    }

    public static ArmOperations getInstance()
    {
        if (armOprations == null)
        {
            armOprations = new ArmOperations();
        }
        return armOprations;
    }

    public boolean runCommand()
    {

        return true;
    }
}
/*
System.out.println("running");
        String command;
        ResponseObject response;

        command = "HOME";
        r12o.write(command);
        response = r12o.getResponse(command);

        if (!response.isSuccessful())
        {
            System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
//                    return false;
        }

        for (int i = 0; i < 1000; i++)
        {
            System.out.println("looped");

            command = "3000 3000 3000 MOVETO";
            r12o.write(command);
            response = r12o.getResponse(command);

            System.out.println(response.getMsg());
            if (!response.isSuccessful())
            {
                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
//                    return false;
            }

            command = "HOME";
            r12o.write(command);
            response = r12o.getResponse(command);

            if (!response.isSuccessful())
            {
                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
//                    return false;
            }

//            command = "HOME";
//            r12o.write(command);
//            response = r12o.getResponse(command);
//
//            if (!response.isSuccessful())
//            {
//                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
////                    return false;
//            }
//
//            command = "TEST RUN";
//            r12o.write(command);
//            response = r12o.getResponse(command);
//
//            if (!response.isSuccessful())
//            {
//                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
////                    return false;
//            }
//            command = "GRIP";
//            r12o.write(command);
//            response = r12o.getResponse(command);
//
//            if (!response.isSuccessful())
//            {
//                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
////                    return false;
//            }
*/
