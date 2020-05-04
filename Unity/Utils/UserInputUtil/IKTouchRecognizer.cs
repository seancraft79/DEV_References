using Lean.Touch;

public interface IKTouchRecognizer
{
    void OnInputBegin(PointEventData eventData);
    void OnInputEnd(PointEventData eventData);
    void OnInputDrag(PointEventData eventData);
}

public interface IKFingerRecognizer
{
    void OnInputBegin(LeanFinger finger);
    void OnInputEnd(LeanFinger finger);
    void OnInputDrag(LeanFinger finger);
}
