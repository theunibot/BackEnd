/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package backendproject;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kyle
 */
public class Latch
{

    private volatile CountDownLatch countLatch = new CountDownLatch(0);
    
    public Latch()
    {
        countLatch = new CountDownLatch(0);
    }
    
    /**
     * Locks the lock for all other threads.
     */
    public void lock()
    {
        countLatch = new CountDownLatch(1);        
    }
    
    /**
     * Unlocks the lock for all other threads.
     */
    public void unlock()
    {
        countLatch.countDown();
    }
    
    /**
     * Causes the current thread to wait until the latch has been unlocked, 
     * unless the thread is {@linkplain Thread#interrupt interrupted}.
     *
     * @throws InterruptedException if the current thread is interrupted
     *         while waiting
     */
    public void await()
    {
        try
        {
            countLatch.await();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Latch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
