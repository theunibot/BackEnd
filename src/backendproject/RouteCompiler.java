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
public class RouteCompiler
{

    private static RouteCompiler routeCompiler = null;
    private R12Operations r12o = R12Operations.getInstance();

    private final String ROUTE_COMPILER_FILE_CONTENTS = ""
            + "////This is the R12 Robot Route Compiler file. This file is where commands are placed in the RoboForth\n"
            + "//Language to generate sets of routes.\n"
            + "//Each command should be on its own line.\n"
            + "//All comments must be on their own line and start with \"//\"\n"
            + "//\n"
            + "//To Be Implemented: To declare a route to be avalible for use the syntax \"include: <name of route>\" to ensure\n"
            + "//the route is added to a usable list for the program"
            + "\n"
            + "\nROUTE TEST";
    private final String PATH_TO_FILE = FileUtils.getFilesFolderString() + "routeCompiler.txt";

    /**
     * Gets the uniform instance of RouteCompiler
     *
     * @return instance of RouteCompiler
     */
    public static RouteCompiler getInstance()
    {
        if (routeCompiler == null)
        {
            routeCompiler = new RouteCompiler();
        }
        return routeCompiler;
    }

    /**
     * Reads the files to generate the start routes.
     *
     * @return success
     */
    public boolean init()
    {
        boolean success = true;
        ArrayList<String> commands = FileUtils.readCommandFileOrGenEmpty(PATH_TO_FILE, ROUTE_COMPILER_FILE_CONTENTS);
        System.out.println("Read " + commands.size() + " command(s) from route compiler file.");
        for (String command : commands)//runs every command in the file
        {
            r12o.write(command);
            ResponseObject response = r12o.getResponse(command);

            if (!response.isSuccessful())
            {
                System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
            }
        }

        return success;
    }
}
