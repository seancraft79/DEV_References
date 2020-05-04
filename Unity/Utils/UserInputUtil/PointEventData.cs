using UnityEngine;

public class PointEventData
{
    /// <summary>
    /// Whether this touch clicked in same position
    /// </summary>
    public bool isClickedSamePosition = false;
    // Positions
    public Vector3 InputBeginWorldPosition { get; set; }
    public Vector3 InputBeginLocalPosition { get; set; }
    public Vector3 InputBeginDragPosition { get; set; }

    public Vector3 InputEndWorldPosition { get; set; }
    
    public Vector3 InputDraggingWorldPosition { get; set; }
    public Vector3 InputDraggingLocalPosition { get; set; }

    // for recognizing touched obj directly
    public GameObject[] InputBeginSelctedDraggableObjects { get; set; }
    public GameObject[] InputEndSelctedDraggableObjects { get; set; }

    // for recognizing clicked UI button when input ends
    string clickedBtnName = string.Empty;
    public string InputEndSelectedUIButtonName
    {
        get { return clickedBtnName; }
        set { clickedBtnName = value; }
    }
    public GameObject InputEndSelectedUIButton { get; set; }
    
    public PointEventData()
    {
        InputBeginWorldPosition = Vector3.zero;
        InputBeginLocalPosition = Vector3.zero;
        InputBeginSelctedDraggableObjects = null;
        InputEndSelctedDraggableObjects = null;

        InputEndWorldPosition = Vector3.zero;

        InputDraggingWorldPosition = Vector3.zero;
        InputDraggingLocalPosition = Vector3.zero;
        
    }
}