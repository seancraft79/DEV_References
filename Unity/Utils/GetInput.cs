using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GetInput : MonoBehaviour
{

    public static bool IfInput()
    {
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetMouseButtonDown(0))
        {
            return true;
        }
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began)
        {
            return true;
        }
#endif
        return false;
    }

    public static bool InputDown()
    {
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetMouseButtonDown(0))
        {
            return true;
        }
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began)
        {
            return true;
        }
#endif
        return false;
    }

    public static bool InputUp()
    {
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetMouseButtonUp(0))
        {
            return true;
        }
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Ended)
        {
            return true;
        }
#endif
        return false;
    }

    public static Vector3 GetInputBeganPosition()
    {
        Vector3 inputPos = Vector3.zero;
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetMouseButtonDown(0))
        {
            inputPos = Input.mousePosition;
        }
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Began)
        {
            inputPos = Input.GetTouch(0).position;
        }
#endif
        return inputPos;
    }

    public static Vector3 GetInputEndPosition()
    {
        Vector3 inputPos = Vector3.zero;
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetMouseButtonUp(0))
        {
            inputPos = Input.mousePosition;
        }
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Ended)
        {
            inputPos = Input.GetTouch(0).position;
        }
#endif
        return inputPos;
    }

    public static Vector3 GetInputDraggingPosition()
    {
        Vector3 inputPos = Vector3.zero;
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        inputPos = Input.mousePosition;
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Moved)
        {
            inputPos = Input.GetTouch(0).position;
        }
#endif
        return inputPos;
    }

    public static bool IsDragging()
    {
#if UNITY_EDITOR || UNITY_STANDALONE || UNITY_WEBGL
        if (Input.GetAxis("Mouse X") != 0 || Input.GetAxis("Mouse Y") != 0) return true;
#elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8 || UNITY_IPHONE
        if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Moved) return true;
#endif
        return false;
    }

    public static Vector3 GetInputBeganWorldPosition()
    {
        return Camera.main.ScreenToWorldPoint(GetInputBeganPosition());
    }

    public static Vector3 GetInputEndWorldPosition()
    {
        return Camera.main.ScreenToWorldPoint(GetInputEndPosition());
    }

    public static Vector3 GetInputDraggingWorldPosition()
    {
        Vector3 pos = GetInputDraggingPosition();
        pos = Camera.main.ScreenToWorldPoint(pos);
        pos.z = 0;
        return pos;
    }

    public static Vector2 GetWorldPosition(Vector3 pos)
    {
        pos = Camera.main.ScreenToWorldPoint(pos);
        return new Vector2(pos.x, pos.y);
    }

    public static Vector3 GetWorldPositionV3(Vector3 pos)
    {
        pos = Camera.main.ScreenToWorldPoint(pos);
        return new Vector3(pos.x, pos.y, 0);
    }

    /// <summary>
    /// Get World screen position for ui canvas from input position
    /// 전달된 포지션에서 스크린 월드 좌표를 구하는 함수
    /// </summary>
    public static Vector2 GetWorldToScreenPosition(Vector3 pos, RectTransform canvasRect)
    {
        if (canvasRect == null) return Vector2.zero;
        pos = Camera.main.ScreenToWorldPoint(pos);
        
        Vector2 ViewportPosition = Camera.main.WorldToViewportPoint(pos);
        Vector2 WorldObject_ScreenPosition = new Vector2(
        ((ViewportPosition.x * canvasRect.sizeDelta.x) - (canvasRect.sizeDelta.x * 0.5f)),
        ((ViewportPosition.y * canvasRect.sizeDelta.y) - (canvasRect.sizeDelta.y * 0.5f)));
        return WorldObject_ScreenPosition;
    }

    public static string GetRaycasthit2DObjectName()
    {
        if (IfInput())
        {
            string name = string.Empty;
            Vector3 pos = GetInput.GetInputBeganWorldPosition();
            RaycastHit2D hit = Physics2D.Raycast(pos, Vector2.zero);
            if (hit) name = hit.collider.name;
            return name;
        }
        else return string.Empty;
    }

    public static GameObject GetRaycasthit2DObject()
    {
        Vector3 pos = GetInput.GetInputBeganWorldPosition();
        RaycastHit2D hit = Physics2D.Raycast(pos, Vector2.zero);
        if (hit) return hit.collider.gameObject;
        else return null;
    }

    public static GameObject GetRaycasthit2DObject(Vector3 pos)
    {
        RaycastHit2D hit = Physics2D.Raycast(pos, Vector2.zero);
        if (hit) return hit.collider.gameObject;
        else return null;
    }

    public static string[] GetRaycasthit2DAllObjectName()
    {
        string[] hitsNames = null;
        if (IfInput())
        {
            Vector3 touchPos = GetInput.GetInputBeganWorldPosition();
            RaycastHit2D[] hits = Physics2D.RaycastAll(touchPos, Vector3.forward, 10f);
            if (hits.Length>0)
            {
                hitsNames = new string[hits.Length];
                for (int i = 0; i < hits.Length; i++)
                {
                    //Debug.Log("hitsName : " + hits[i].collider.name);
                    hitsNames[i] = hits[i].collider.name;
                }
            }
            
        }
        return hitsNames;
    }

    public static string[] GetRaycasthit2DAllObjectName(Vector3 touchPos)
    {
        string[] hitsNames = null;
        if (IfInput())
        {
            RaycastHit2D[] hits = Physics2D.RaycastAll(touchPos, Vector3.forward, 10f);
            if (hits.Length > 0)
            {
                hitsNames = new string[hits.Length];
                for (int i = 0; i < hits.Length; i++)
                {
                    //Debug.Log("hitsName : " + hits[i].collider.name);
                    hitsNames[i] = hits[i].collider.name;
                }
            }

        }
        return hitsNames;
    }

    public static GameObject[] GetRaycasthit2DAllObject(Vector3 touchPos)
    {
        GameObject[] hitsObjects = null;
        if (IfInput())
        {
            RaycastHit2D[] hits = Physics2D.RaycastAll(touchPos, Vector3.forward, 10f);
            if (hits.Length > 0)
            {
                hitsObjects = new GameObject[hits.Length];
                for (int i = 0; i < hits.Length; i++)
                {
                    hitsObjects[i] = hits[i].collider.gameObject;
                }
            }
        }
        return hitsObjects;
    }

    /// <summary>
    /// Raycast 된 GameObject 배열안에 원하는 이름의 오브젝트가 있는지 검사하는 함수
    /// </summary>
    /// <param name="array">Raycast 된 게임오브젝트 배열</param>
    /// <param name="objName">찾는 오브젝트 이름</param>
    /// <returns></returns>
    public static bool IsGameObjectInArray(GameObject[] array, string objName)
    {
        if (array == null || array.Length < 1 || string.IsNullOrEmpty(objName)) return false;
        for (int i = 0; i < array.Length; i++)
        {
            if (array[i].name.Equals(objName)) return true;
        }
        return false;
    }

    public static bool IsGameObjectInArrayTag(GameObject[] array, string objTag)
    {
        if (array == null || array.Length < 1 || string.IsNullOrEmpty(objTag)) return false;
        for (int i = 0; i < array.Length; i++)
        {
            if (array[i].tag.Equals(objTag)) return true;
        }
        return false;
    }

    public static bool IsGameObjectInArray(GameObject[] array, GameObject obj)
	{
		for (int i = 0; i < array.Length; i++)
		{
			if (array[i] == obj) return true;
		}
		return false;
	}

	public static GameObject GetGameObjectFromRaycastedArray(GameObject[] array, string objName)
    {
        if (array == null || array.Length < 1 || string.IsNullOrEmpty(objName)) return null;
        for (int i = 0; i < array.Length; i++)
        {
            if (array[i].name.Equals(objName)) return array[i];
        }
        return null;
    }

    public static GameObject GetGameObjectFromRaycastedArrayTag(GameObject[] array, string objTag)
    {
        if (array == null || array.Length < 1 || string.IsNullOrEmpty(objTag)) return null;
        for (int i = 0; i < array.Length; i++)
        {
            if (array[i].tag.Equals(objTag)) return array[i];
        }
        return null;
    }

    public static GameObject GetContainedGameObjectFromRaycastedArray(GameObject[] array, string objName)
    {
        if (array == null || array.Length < 1 || string.IsNullOrEmpty(objName)) return null;
        for (int i = 0; i < array.Length; i++)
        {
            if (array[i].name.Contains(objName)) return array[i];
        }
        return null;
    }
}