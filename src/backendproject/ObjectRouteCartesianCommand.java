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
public class ObjectRouteCartesianCommand implements ObjectRouteInterface
{

    private int x;
    private int y;
    private int z;
    private int pitch;
    private int yaw;
    private int roll;
    private String pitchStr;
    private String yawStr;
    private String rollStr;

    private String routeName;
    private int line;

    public ObjectRouteCartesianCommand(String x, String y, String z, String pitch, String yaw, String roll, String routeName, int line)
    {
        //turn 300.0 to 3000
        x = x.replace(".", "");
        y = y.replace(".", "");
        z = z.replace(".", "");
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        this.z = Integer.parseInt(z);
        this.pitchStr = pitch;
        this.yawStr = yaw;
        this.rollStr = roll;
//        this.pitch = Integer.parseInt(pitch);
//        this.yaw = Integer.parseInt(yaw);
//        this.roll = Integer.parseInt(roll);
        this.routeName = routeName;
        this.line = line;
        parsePYR();
    }

    private void parsePYR()
    {
        
    }
    
    @Override
    public String toString()
    {
        return "LEARN\n"
                + "DECIMAL " + roll + " " + yaw + " " + pitch + " " + z + " " + y + " " + x + " " + routeName + " " + line + " DLD";
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public int getPitch()
    {
        return pitch;
    }

    public int getYaw()
    {
        return yaw;
    }

    public int getRoll()
    {
        return roll;
    }

}
