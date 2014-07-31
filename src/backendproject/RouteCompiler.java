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
    private ObjectRouteContainer orc = ObjectRouteContainer.getInstance();

    //String consts
    public static final String ROUTE_PREFIX = "route ";
    public static final String ROUTE_FILE_BASENAME = "routes";
    public static final String ROUTE_LEFT = "#left";
    public static final String ROUTE_RIGHT = "#right";

    public static final String[] ROUTE_FILE_PREFIXS =
    {
        "D1_", "D2_", "S_"
    };

    public static final KVPair[] PITCH_ORIENTATION =
    {
        new KVPair("N", "0"), new KVPair("E", "10000"), new KVPair("S", "20000"), new KVPair("W", "30000")
    };

    public static final KVPair[] YAW_ORIENTATION =
    {
        new KVPair("N", "0"), new KVPair("E", "10000"), new KVPair("S", "20000"), new KVPair("W", "30000")
    };

    public static final KVPair[] ROLL_ORIENTATION =
    {
        new KVPair("N", "0"), new KVPair("E", "10000"), new KVPair("S", "20000"), new KVPair("W", "30000")
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
            ArrayList<RouteObjectList> routes = parseLines2(lines, prefix);

            for (RouteObjectList route : routes)
            {
                
                ArrayList<String> commands = route.getCommands();
                for (String commandString : commands)
                {
                    System.out.println(commandString);
//                    if(!runCommand(commandString))//if command not successul
//                    {
//                        success = false;
//                    }
                }
            }
        }
        return success;
    }

    private boolean runCommand(String commandString)
    {
        r12o.write(commandString);
        ResponseObject response = r12o.getResponse(commandString);

        if (!response.isSuccessful())
        {
            System.err.println("Command Failed! Cmd: " + commandString + " Response Msg: " + response.getMsg());
            return false;
        }
        return true;
    }

    private ArrayList<RouteObjectList> parseLines2(ArrayList<String> lines, String prefix)
    {
        RouteState routeState = new RouteState(null);
        ArrayList<RouteObjectList> routeObjList = new ArrayList<RouteObjectList>();
        RouteObjectList currentRouteObjectList = null;//current rol, used as a pointer to add tp its arraylist
        //set route type, D1, D2, etc
        routeState.setRouteType(getRouteType(prefix));
        int lineCount = 1;//used in error tracking
        for (String line : lines)
        {
            if (line.equals(ROUTE_LEFT))
            {
                routeState.setRouteSide(RouteSide.LEFT);
            }
            else if (line.equals(ROUTE_RIGHT))
            {
                routeState.setRouteSide(RouteSide.RIGHT);
            }
            else if (line.startsWith(ROUTE_PREFIX))//new Route
            {
                String routeName = prefix + line.replace(ROUTE_PREFIX, "").trim();
                routeState.setRouteName(routeName);//sets the name of the route for use
                currentRouteObjectList = new RouteObjectList(routeName, routeState);
                routeObjList.add(currentRouteObjectList);
            }
            else if (currentRouteObjectList != null)//not defining a new command, so a cartesian command
            {

                String[] pieces = line.split(" ");//splits the line to pieces
                if (pieces.length == 3)//x,y,z only
                {
                    currentRouteObjectList.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], "0", "0", "0", routeState.getRouteName(), currentRouteObjectList.size() + 1));
                }
                else if (pieces.length == 6)//x,y,z,pitch,yaw,roll
                {
                    String pitch = pieces[3];
                    String yaw = pieces[4];
                    String roll = pieces[5];

                    for (KVPair porint : PITCH_ORIENTATION)
                    {
                        if (pitch.equals(porint.key))
                        {
                            pitch = porint.value;
                        }
                    }
                    for (KVPair yorint : YAW_ORIENTATION)
                    {
                        if (yaw.equals(yorint.key))
                        {
                            yaw = yorint.value;
                        }
                    }
                    for (KVPair worint : ROLL_ORIENTATION)
                    {
                        if (roll.equals(worint.key))
                        {
                            roll = worint.value;
                        }
                    }
                    currentRouteObjectList.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], pitch, yaw, roll, routeState.getRouteName(), currentRouteObjectList.size() + 1));
                }
                else//error in format of info
                {
                    System.err.println("Format of line " + lineCount + " wrong: " + line);
                    //ignore line
                }
            }
            lineCount++;
        }

        return routeObjList;
    }

//    
    private RouteType getRouteType(String prefix)
    {
        if (prefix.equals(ROUTE_FILE_PREFIXS[0]))//D1
        {
            return RouteType.D1;
        }
        else if (prefix.equals(ROUTE_FILE_PREFIXS[1]))//D2
        {
            return RouteType.D2;
        }
        else if (prefix.equals(ROUTE_FILE_PREFIXS[2]))//S
        {
            return RouteType.S;
        }
        else
        {
            return null;
        }
    }

//
}

enum RouteType
{

    D1, D2, S
}

enum RouteSide
{

    LEFT, RIGHT
}

class KVPair
{

    public String key;
    public String value;

    public KVPair(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

}
//    private RouteInfo getRouteInfo(RouteType type, RouteSide side)
//    {
//        if (type == RouteType.D1)
//        {
//            if (side == RouteSide.LEFT)
//            {
//                return RouteInfo.D1L;
//            }
//            else if (side == RouteSide.RIGHT)
//            {
//                return RouteInfo.D1R;
//            }
//        }
//        else if (type == RouteType.D2)
//        {
//            if (side == RouteSide.LEFT)
//            {
//                return RouteInfo.D2L;
//            }
//            else if (side == RouteSide.RIGHT)
//            {
//                return RouteInfo.D2R;
//            }
//        }
//        else if (type == RouteType.S)
//        {
//            return RouteInfo.S;
//        }
//
//        return null;
//    }
//private ArrayList<ObjectRouteInterface> parseLines(ArrayList<String> lines, String prefix)
//    {
//        ArrayList<ObjectRouteInterface> commands = new ArrayList<ObjectRouteInterface>();
//
//        RouteType routeDesktop = getRouteType(prefix);
//
//        String currentRoute = null;
//        int currentLine = 1;
//        RouteSide routeSide = null;
//        int i = 0;
//        for (String line : lines)
//        {
//            if (line.startsWith(ROUTE_PREFIX) && routeSide != null && routeDesktop != null)//is route def
//            {
//                String routeName = line.replace(ROUTE_PREFIX, "").trim();
//                currentRoute = prefix + routeName;
//                commands.add(new ObjectRouteDefine(currentRoute));
//                orc.add(getRouteInfo(routeDesktop, routeSide), new ObjectRoute(routeName, getRouteInfo(routeDesktop, routeSide)));
//
//                currentLine = 0;
//            }
//            else if (line.equals(ROUTE_LEFT))
//            {
//                routeSide = RouteSide.LEFT;
//            }
//            else if (line.equals(ROUTE_RIGHT))
//            {
//                routeSide = RouteSide.RIGHT;
//            }
//            else if (currentRoute != null)//not defining a new command, so a cartesian command
//            {
//
//                String[] pieces = line.split(" ");//splits the line to pieces
//                if (pieces.length == 3)//x,y,z only
//                {
//                    commands.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], "0", "0", "0", currentRoute, currentLine));
//                }
//                else if (pieces.length == 6)//x,y,z,pitch,yaw,roll
//                {
//                    String roll = pieces[5];
//                    String pitch = pieces[4];
//                    String yaw = pieces[3];
//
//                    for (ParseObj porint : PITCH_ORIENTATION)
//                    {
//                        if (pitch.equals(porint.key))
//                        {
//                            pitch = porint.value;
//                        }
//                    }
//                    for (ParseObj yorint : YAW_ORIENTATION)
//                    {
//                        if (yaw.equals(yorint.key))
//                        {
//                            yaw = yorint.value;
//                        }
//                    }
//                    for (ParseObj worint : ROLL_ORIENTATION)
//                    {
//                        if (roll.equals(worint.key))
//                        {
//                            roll = worint.value;
//                        }
//                    }
//                    commands.add(new ObjectRouteCartesianCommand(pieces[0], pieces[1], pieces[2], pitch, yaw, roll, currentRoute, currentLine));
//                }
//                else//error in format of info
//                {
//                    System.err.println("Format of line " + i + " wrong: " + line);
//                    //ignore line
//                }
//                currentLine++;
//                i++;
//            }
//        }
//        return commands;
//    }

