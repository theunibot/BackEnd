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
public class ObjectRouteContainer
{

    ArrayList<ObjectRoute> d1l = new ArrayList<>();
    ArrayList<ObjectRoute> d1r = new ArrayList<>();
    ArrayList<ObjectRoute> d2l = new ArrayList<>();
    ArrayList<ObjectRoute> d2r = new ArrayList<>();

    private static ObjectRouteContainer orc = null;

    public static ObjectRouteContainer getInstance()
    {
        if (orc == null)
        {
            orc = new ObjectRouteContainer();
        }
        return orc;
    }

    /**
     * Adds items to the specified list
     * @param r route to select
     * @param o RouteObject to add 
     */
    public void add(RouteInfo r, ObjectRoute o)
    {
        if (r == RouteInfo.D1L)
        {
            d1l.add(o);
        }
        else if(r==RouteInfo.D1R)
        {
            d1r.add(o);
        }
        else if (r == RouteInfo.D2L)
        {
            d2l.add(o);
        }
        else if (r == RouteInfo.D2R)
        {
            d2r.add(o);
        }
    }
    
    /**
     * Get the list of routes for a specific position.
     * @param r which route section to return
     * @return ArrayList of ObjectRoute
     */
    public ArrayList<ObjectRoute> get(RouteInfo r)
    {
        if (r == RouteInfo.D1L)
        {
            return d1l;
        }
        else if(r==RouteInfo.D1R)
        {
            return d1r;
        }
        else if (r == RouteInfo.D2L)
        {
            return d2l;
        }
        else if (r == RouteInfo.D2R)
        {
            return d2r;
        }
        else
        {
            return null;
        }
    }
}

enum RouteInfo
{

    D1L, D1R, D2L, D2R, S
}
