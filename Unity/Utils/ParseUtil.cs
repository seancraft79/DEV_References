using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ParseUtil
{
    public static int ParseStringToInt(string value)
    {
        int result = -1;
        return int.TryParse(value, out result) ? result : -1;
    }

    public static int ParseStringToInt(string value, int defaultValue)
    {
        int result = -1;
        return int.TryParse(value, out result) ? result : defaultValue;
    }
}
