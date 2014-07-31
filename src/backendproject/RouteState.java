/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

/**
 *
 * @author kyle
 */
public class RouteState
{

    private String routeName;
    private RouteType routeType = null;
    private RouteSide routeSide = null;

    public RouteState(String routeName)
    {
        this.routeName = routeName;
    }

    public String getRouteName()
    {
        return routeName;
    }

    public void setRouteName(String routeName)
    {
        this.routeName = routeName;
    }    
    
    public RouteSide getRouteSide()
    {
        return routeSide;
    }

    public RouteType getRouteType()
    {
        return routeType;
    }

    public void setRouteSide(RouteSide routeSide)
    {
        this.routeSide = routeSide;
    }

    public void setRouteType(RouteType routeType)
    {
        this.routeType = routeType;
    }
}
