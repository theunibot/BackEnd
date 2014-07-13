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
public class GeneralUtils
{
    public static void sleep(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
