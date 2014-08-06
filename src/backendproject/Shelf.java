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
public class Shelf
{

    private EnumDiskType disk;

    public Shelf()
    {
    }

    public Shelf(EnumDiskType disk)
    {
        this.disk = disk;
    }

    public EnumDiskType getDisk()
    {
        return disk;
    }

    public void setDisk(EnumDiskType disk)
    {
        this.disk = disk;
    }

}