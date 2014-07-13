/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kyle
 */
public class ThreadCommand extends Thread
{

    private volatile boolean isRunning = true;

    private ArmOperations ao;

    @Override
    public void run()
    {
        ArmOperations armOps = ArmOperations.getInstance();        
        armOps.init();                
//        armOps.write("");
//        System.out.println(armOps.read());
//        armOps.write("ROBOFORTH");
//        System.out.println("grip");
//        System.out.println(armOps.read());        
//        armOps.write("START");
//        System.out.println("ungrip");
//        System.out.println(armOps.read());
//        armOps.write("HOME");
//        System.out.println("ungrip");
//        System.out.println(armOps.read());
//        for (int i = 0; i < 20; i++)
//        {
//            armOps.write("HOME");
//            System.out.println(armOps.read());
//            armOps.write("READY");
//            System.out.println(armOps.read());
//            armOps.write("READY2");
//            System.out.println(armOps.read());
//        }
//        System.out.println(o.read());
//        i.write("START");
//        System.out.println("start");
//        try
//        {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException ex)
//        {
//            Logger.getLogger(ThreadCommand.class.getName()).log(Level.SEVERE, null, ex);
//        }        
//        i.write("GRIP");
//        System.out.println("grip");
//        try
//        {
//            Thread.sleep(3000);
//        }
//        catch (InterruptedException ex)
//        {
//            Logger.getLogger(ThreadCommand.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        System.out.println(i.read());
//        System.out.println(i.read());
//        System.out.println("written");
//        init();
//        while (isRunning)
//        {
//            System.out.println("waiting");
//        }

    }

    private void init()
    {
        ao = ArmOperations.getInstance();
        boolean success = ao.init();
        if (success)
        {
            System.out.println("All Inits successful");
        }
    }

    /**
     * Stops the thread gracefully
     */
    public void kill()
    {
        this.isRunning = false;
    }
}
