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
public class RouteObjectList
{

    ArrayList<ObjectRouteInterface> commands = new ArrayList<>();

    private String name;
    private RouteState routeState = new RouteState(null);
    private int numItems = 0;

    public RouteObjectList(String name, RouteState routeState)
    {
        this.name = name;
        this.routeState.setRouteSide(routeState.getRouteSide());
        this.routeState.setRouteType(routeState.getRouteType());
        this.routeState.setRouteName(routeState.getRouteName());
    }

    public int size()
    {
        return commands.size();
    }

    public void add(ObjectRouteInterface o)
    {
        commands.add(o);
        numItems = commands.size();
    }
    
    public ArrayList<String> getCommands()
    {
        ArrayList<String> commandStrings;
        commandStrings = new ArrayList<>();
        commandStrings.add("ROUTE " + this.name);
        commandStrings.add(numItems + " RESERVE");
        for (ObjectRouteInterface o : commands)
        {
            commandStrings.add(o.toString());
        }
        return commandStrings;
    }

}
