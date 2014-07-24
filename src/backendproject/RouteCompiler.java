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

    //String consts
    public static final String ROUTE_PREFIX = "ROUTE ";
    public static final String ROUTE_FILE_BASENAME = "routes";
    public static final String ROUTE_LEFT = "#left";
    public static final String ROUTE_RIGHT = "#right";
    
    public static final String[] ROUTE_FILE_PREFIXS =
    {
        "D1_", "D2_", "S_"
    };

    public static final ParseObj[] PITCH_ORIENTATION =
    {
        new ParseObj("N", "0"), new ParseObj("E", "10000"), new ParseObj("S", "20000"), new ParseObj("W", "30000")
    };

    public static final ParseObj[] YAW_ORIENTATION =
    {
        new ParseObj("N", "0"), new ParseObj("E", "10000"), new ParseObj("S", "20000"), new ParseObj("W", "30000")
    };

    public static final ParseObj[] ROLL_ORIENTATION =
    {
        new ParseObj("N", "0"), new ParseObj("E", "10000"), new ParseObj("S", "20000"), new ParseObj("W", "30000")
    };

    private final String ROUTE_COMPILER_FILE_CONTENTS = ""
            + "//This is the R12 Robot Route Compiler file.\n"
            + "\n"
            + ""
            + "//Each command should be on its own line.\n"
            + "//All comments must be on their own line and start with \"//\"\n"
            + "//\n"
            + "//To Be Implemented: To declare a route to be avalible for use the syntax \"include: <name of route>\" to ensure\n"
            + "//the route is added to a usable list for the program"
            + "\n"
            + "\n" + ROUTE_LEFT + ""
            + "\n"
            + "\n" + ROUTE_RIGHT;
    private String pathToFile = "";

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
        for (String prefix : ROUTE_FILE_PREFIXS)
        {
            pathToFile = FileUtils.getFilesFolderString() + prefix + ROUTE_FILE_BASENAME + ".txt";
            ArrayList<String> lines = FileUtils.readCommandFileOrGenEmpty(pathToFile, ROUTE_COMPILER_FILE_CONTENTS);
            System.out.println("Read " + lines.size() + " line(s) from route compiler file.");
            ArrayList<ObjectRouteInterface> commands = parseLines(lines, prefix);
            for (ObjectRouteInterface command : commands)//runs every command in the file
            {
                String commandString = command.toString();
//                System.out.println(commandString);
                r12o.write(commandString);
                ResponseObject response = r12o.getResponse(commandString);

                if (!response.isSuccessful())
                {
                    System.err.println("Command Failed! Cmd: " + command + " Response Msg: " + response.getMsg());
                }
            }
        }
        return success;
    }

    private ArrayList<ObjectRouteInterface> parseLines(ArrayList<String> lines, String prefix)
    {
        ArrayList<ObjectRouteInterface> commands = new ArrayList<ObjectRouteInterface>();
        String currentRoute = null;
        int currentLine = 1;
        for (String line : lines)
        {
            if (line.startsWith(ROUTE_PREFIX))//is route def
            {
                String routeName = line.replace(ROUTE_PREFIX, "").trim();
                commands.add(new ObjectRouteDefine(routeName));
                currentRoute = prefix + routeName;
                currentLine = 0;
            }
            else if (currentRoute != null)//not defining a new command, so a cartesian command
            {

                String[] pieces = line.split(" ");//splits the line to pieces
                if (pieces.length == 3)//x,y,z only
                {
                    commands.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], "0", "0", "0", currentRoute, currentLine));
                }
                else if (pieces.length == 6)//x,y,z,pitch,yaw,roll
                {
                    String roll = pieces[5];
                    String pitch = pieces[4];
                    String yaw = pieces[3];

                    for (ParseObj porint : PITCH_ORIENTATION)
                    {
                        if (pitch.equals(porint.key))
                        {
                            pitch = porint.value;
                        }
                    }
                    for (ParseObj yorint : YAW_ORIENTATION)
                    {
                        if (yaw.equals(yorint.key))
                        {
                            yaw = yorint.value;
                        }
                    }
                    for (ParseObj worint : ROLL_ORIENTATION)
                    {
                        if (roll.equals(worint.key))
                        {
                            roll = worint.value;
                        }
                    }
                    commands.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], pitch, yaw, roll, currentRoute, currentLine));
                }
                else//error in format of info
                {
                    System.err.println("Format of line wrong: " + line);
                    //ignore line
                }
                currentLine++;
            }
        }
        return commands;
    }
}

class ParseObj
{

    public String key;
    public String value;

    public ParseObj(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

}
