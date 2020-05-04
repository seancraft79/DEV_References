using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ColorUtil
{
    public static Color White()
    {
        return new Color(255, 255, 255, 255);
    }

    public static Color Red()
    {
        return new Color(255, 0, 0, 255); 
    }

    public static Color Green()
    {
        return new Color(0, 255, 0, 255); 
    }

    public static Color Yellow()
    {
        return new Color(255, 255, 0, 255);
    }

    public static Color Blue()
    {
        return new Color(0, 0, 255, 255);
    }

    public static Color LadderRedBlur()
    {
        return new Color(255, 186, 186, 255);
    }

    public static Color LadderGreenBlur()
    {
        return new Color(186, 255, 186, 255);
    }

    public static Color LadderYellowBlur()
    {
        return new Color(255, 255, 186, 255);
    }
}
