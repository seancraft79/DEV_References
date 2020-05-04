using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

[CustomEditor(typeof(NameSetter))]  // The component which gotta be the Target. The button below would be attached to the target component
public class NameSetterEditor : Editor
{
    string namePrefix = string.Empty;
    int startIndex = 0;
    NameSetter setter;
    Transform[] children;

    public override void OnInspectorGUI()
    {
        base.OnInspectorGUI();
        if(GUILayout.Button("Set Name"))
        {
            GetTarget();

            if(children.Length>0)
            {
                int idx = startIndex;
                for (int i = 1; i < children.Length; i++)
                {
                    //Debug.Log("Set Name : " + (namePrefix + idx));
                    children[i].gameObject.name = namePrefix + idx;
                    idx++;
                }
            }
        }

        if (GUILayout.Button("Set Same Name with Parent"))
        {
            GetTarget();
            string pName = children[0].name;
            if (children.Length > 0)
            {
                int idx = startIndex;
                for (int i = 1; i < children.Length; i++)
                {
                    children[i].gameObject.name = pName;
                }
            }
        }
    }

    void GetTarget()
    {
        setter = target as NameSetter;
        namePrefix = setter.GetNamePrefix();
        startIndex = setter.GetStartIndex();

        //children = setter.GetComponentsInChildren<Transform>();
        children = setter.GetChildren();
        
        //Debug.Log("GetTarget : " + children.Length);
    }
}
