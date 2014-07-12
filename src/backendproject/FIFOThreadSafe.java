/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendproject;

import java.util.LinkedList;

/**
 *
 * @author kyle
 */
public class FIFOThreadSafe
{

    private Latch latch;
    private LinkedList<String> list;

    public FIFOThreadSafe()
    {
        latch = new Latch();//create a mutex for the LinkedList
        latch.unlock(); //unlock Latch, ready for first use
        list = new LinkedList<String>();                
    }    
    
    /**
     * Clears the list of all items.
     */
    public void clear()
    {
        latch.await();
        latch.lock();
        list.clear();
        latch.unlock();
    }
    
    /**
     * Adds a string to the list.
     */
    public void add(String s)
    {        
        latch.await();
        latch.lock();
        list.add(s);
        latch.unlock();
    }
    
    /**
     * Gets and removes the first String on the list.
     * @return first String on the list
     */
    public String pollFirst()
    {
        String s;
        latch.await();
        latch.lock();
        s = list.pollFirst();
        latch.unlock();
        return s;
    }
    
    /**
     * Gets and removes the last String on the list.
     * @return last String on the list
     */
    public String pollLast()
    {
        String s;
        latch.await();
        latch.lock();
        s = list.pollFirst();
        latch.unlock();
        return s;
    }

}
