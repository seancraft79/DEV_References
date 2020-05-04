using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AngleUtil
{
    // return : -180 ~ 180 degree (for unity)
    public static float GetAngle(Vector3 vStart, Vector3 vEnd)
    {
        Vector3 v = vEnd - vStart;

        return Mathf.Atan2(v.y, v.x) * Mathf.Rad2Deg;
    }

    // 시계 반대방향으로 0도부터 360도까지 제공
    public static float GetCounterclockwiseAngle(GameObject center, Vector3 touchWorldPosition)
    {
        float angle = GetAngle(center.transform.position, touchWorldPosition);
        if (angle < 0) angle = 360 + angle;

        if (angle >= 90) angle -= 90;
        else if (angle >= 0 && angle < 90) angle += 270;
        
        return angle;
    }
}
