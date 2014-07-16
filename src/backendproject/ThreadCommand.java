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
        init();
        
        for (int i = 0; i < 3; i++)
        {
            ao.runCommand();
        }
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
