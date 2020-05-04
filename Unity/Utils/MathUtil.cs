using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MathUtil
{ 
    // 세개의 입력값 중에서 가운데 값을 찾아준다
    public static float GetMiddleValueIn3(float v1, float v2, float v3)
    {
        float middle = (v1 + v2 + v3)
                - Mathf.Max(Mathf.Max(v1, v2), v3)
                - Mathf.Min(Mathf.Min(v1, v2), v3);
        //Debug.Log("GetMiddleValueAmong3 : " + middle);
        return middle;
    }
}
