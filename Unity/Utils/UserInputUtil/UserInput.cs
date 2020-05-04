
using System;
using System.Collections.Generic;
using UnityEngine;
using Lean.Touch;

/// <summary>
/// UserInput no longer DontDestroy Sington.
/// UserInput object must be in object hierachy.
/// Can use touches via RegistTouchRecognizer.
/// 
/// 1. UserInput Prefab 을 Hierachy 에 끌어놓는다.
/// 2. 끌어놓은 UserInput 에 Main Camera 를 설정한다.
/// 3. 사용하고자 하는 곳에서 UserInput.Instance.RegistTouchRecognizer() 한다.
/// </summary>

/*
 * Touch event 만 필요할때 - RegisterTouchRecognizer
 * LeanFinger event 필요할때 - RegisterFingerRecognizer
 * 
 *     UserInput 이 Hierachy 상에서 다른 오브젝트보다 먼저 생성되어야 하므로 Script execute order 를 설정해 줘야 한다.
 *     Script execute order 설정방법
 * 1 - go to Edit → Project Settings → Script Execution Order. 
 * 2 - Below the ‘Default Time’ box, click the ‘+’ button, and select the ‘UserInput’ script. 
 * 3 - Change the execution order value (default 100) to something below 0 (-110), or drag it above ‘Default Time’ and any other scripts. 
 * 4 - Click Apply, and enjoy!
 */

public class UserInput : MonoBehaviour
{
    public Camera currentMainCamera = null;

    protected static UserInput s_Instance = null;

    public static UserInput Instance
    {
        get
        {
            return s_Instance;
        }
    }

    private void OnEnable()
    {
        s_Instance = this;
        s_Instance.Init();
    }

    private void OnDisable()
    {
        s_Instance = null;
    }

    static void Create()
    {
        var obj = new GameObject("UserInput");
        s_Instance = obj.AddComponent<UserInput>();
    }

    LeanTouch CreateLeanTouch()
    {
        if (GameObject.Find("LeanTouch") == null)
        {
            Debug.Log("Create LeanTouch object");
            return new GameObject("LeanTouch").AddComponent<LeanTouch>();
        }

        return null;
    }

    void Init()
    {
        Debug.Log("[UserInput Init]");
        if(currentMainCamera == null)
        {
            Debug.LogWarning("[UserInput] currentMainCamera is null");
            currentMainCamera = Camera.main ?? Camera.allCameras[0];
            if (currentMainCamera == null) Debug.LogError("[UserInput] could not find Camera");
            //Debug.Log("[UserInput] camera : " + currentMainCamera.name);
        }

        // Regist listener
        LeanTouch.OnFingerDown = (LeanFinger finger) =>
        {
            if (shouldAutoUpdateTouches)
            {
                // Touch recognizer
                var position = finger.GetWorldPosition(10f, currentMainCamera);
                foreach (var tr in m_TouchRecognizer)
                {
                    tr.OnInputBegin(GetBeginEventData(position));
                }

                // Finger recognizer
                foreach (var tr in m_FingerRecognizer)
                {
                    tr.OnInputBegin(finger);
                }
            }
        };

        LeanTouch.OnFingerSet = (LeanFinger finger) =>
        {
            if (shouldAutoUpdateTouches)
            {
                // Touch recognizer
                var position = finger.GetWorldPosition(10f, currentMainCamera);
                foreach (var tr in m_TouchRecognizer)
                {
                    tr.OnInputDrag(GetDraggingEventData(position));
                }

                // Finger recognizer
                foreach (var tr in m_FingerRecognizer)
                {
                    tr.OnInputDrag(finger);
                }
            }
        };

        LeanTouch.OnFingerUp = (LeanFinger finger) =>
        {
            if (shouldAutoUpdateTouches)
            {
                // Touch recognizer
                var position = finger.GetWorldPosition(10f, currentMainCamera);
                foreach (var tr in m_TouchRecognizer)
                {
                    tr.OnInputEnd(GetEndEventData(position));
                }

                // Finger recognizer
                foreach (var tr in m_FingerRecognizer)
                {
                    tr.OnInputEnd(finger);
                }
            }
        };
    }
    
    /// <summary>
	/// if false, TouchKit will not do anything in Update. You will need to call updateTouches yourself.
	/// </summary>
	public bool shouldAutoUpdateTouches = true;

    Vector2 inputBeginPosition, inputEndPosition;

    void Update()
    {
        if (schedule != null)
        {
            schedule();
            schedule = null;
        }
    }

    private bool _shouldCheckForLostTouches = false; // used internally to ensure we dont check for lost touches too often

	float BeginEventTime;
	PointEventData GetBeginEventData(Vector3 inputPosition)
    {
        PointEventData eventData = new PointEventData();
        this.inputBeginPosition = inputPosition;
        eventData.InputBeginWorldPosition = inputPosition;
        eventData.InputBeginLocalPosition = inputPosition;
		BeginEventTime = Time.time;

        eventData.InputBeginSelctedDraggableObjects = null;
        RaycastHit2D[] hits = Physics2D.RaycastAll(eventData.InputBeginWorldPosition, Vector3.forward, 10f);
        if (hits.Length > 0)
        {
            eventData.InputBeginSelctedDraggableObjects = new GameObject[hits.Length];
            for (int i = 0; i < hits.Length; i++)
            {
                eventData.InputBeginSelctedDraggableObjects[i] = hits[i].collider.gameObject;
            }
            eventData.InputBeginDragPosition = inputPosition;
        }
        return eventData;
    }

    PointEventData GetEndEventData(Vector3 inputPosition)
    {
        eventData = new PointEventData();
        eventData.InputEndWorldPosition = inputPosition;

        // recognizing button click event
        if (UnityEngine.EventSystems.EventSystem.current.currentSelectedGameObject != null)
        {
            eventData.InputEndSelectedUIButtonName = UnityEngine.EventSystems.EventSystem.current.currentSelectedGameObject.name;
            eventData.InputEndSelectedUIButton = UnityEngine.EventSystems.EventSystem.current.currentSelectedGameObject;
        }

        eventData.InputEndSelctedDraggableObjects = null;
        RaycastHit2D[] hits = Physics2D.RaycastAll(eventData.InputEndWorldPosition, Vector3.forward, 10f);
        if (hits.Length > 0)
        {
            eventData.InputEndSelctedDraggableObjects = new GameObject[hits.Length];
            for (int i = 0; i < hits.Length; i++)
            {
                eventData.InputEndSelctedDraggableObjects[i] = hits[i].collider.gameObject;
            }
        }

        // Determine whether this touch ended on same position with begin position
        this.inputEndPosition = inputPosition;
        float dist = Vector2.Distance(inputBeginPosition, inputEndPosition);
        eventData.isClickedSamePosition = (dist < 0.2f && Time.time - BeginEventTime < 0.3f);
        return eventData;
    }

    PointEventData GetDraggingEventData(Vector3 inputPosition)
    {
        eventData = new PointEventData();
        eventData.InputDraggingWorldPosition = inputPosition;
        eventData.InputDraggingLocalPosition = inputPosition;

        eventData.InputDraggingWorldPosition = inputPosition;

        return eventData;
    }

    public void DebugPrintRecognizer()
    {
        Debug.Log("mTouchRecognizer count : " + m_TouchRecognizer.Count);
        foreach (var item in m_TouchRecognizer)
        {
            Debug.Log("   -- TR name : " + item.GetType().Name);
        }
        Debug.Log(">>>>>> DebugPrintRecognizer end ===");
    }

    protected HashSet<IKTouchRecognizer> m_TouchRecognizer = new HashSet<IKTouchRecognizer>();
    protected HashSet<IKFingerRecognizer> m_FingerRecognizer = new HashSet<IKFingerRecognizer>();
    event System.Action schedule = null;
    PointEventData eventData;

    public void RegistTouchRecognizer(IKTouchRecognizer tr)
    {
        if(UserInput.Instance == null)
        {
            Debug.LogError("UserInput instance is null");
            return;
        }
        schedule += () =>
        {
            m_TouchRecognizer.Add(tr);
            Debug.Log(">>>>>> RegisteredTouchRecognizer : " + tr.GetType().Name);
            //DebugPrintRecognizer();
        };
    }

    public void UnRegistTouchRecognizer(IKTouchRecognizer tr)
    {
        if (UserInput.Instance == null)
        {
            Debug.LogError("UserInput instance is null");
            return;
        }

        schedule += () =>
        {
            m_TouchRecognizer.Remove(tr);
        };
    }

    public void RegistFingerRecognizer(IKFingerRecognizer tr)
    {
        if (UserInput.Instance == null)
        {
            Debug.LogError("UserInput instance is null");
            return;
        }
        schedule += () =>
        {
            m_FingerRecognizer.Add(tr);
            Debug.Log(">>>>>> RegisteredFingerRecognizer : " + tr.GetType().Name);
            //DebugPrintRecognizer();
        };
    }

    public void UnRegistFingerRecognizer(IKFingerRecognizer tr)
    {
        if (UserInput.Instance == null)
        {
            Debug.LogError("UserInput instance is null");
            return;
        }

        schedule += () =>
        {
            m_FingerRecognizer.Remove(tr);
        };
    }

    public int GetRecognizerCount()
    {
        return m_TouchRecognizer.Count;
    }
}
