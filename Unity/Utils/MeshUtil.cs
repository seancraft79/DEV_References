using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MeshUtil
{
    public static GameObject CreateTriagle2D(Vector2[] vertices, Material ma, Color color, int sortingOrder)
    {
        if(vertices.Length!=3)
        {
            Debug.LogWarning("CreateTriangle2D inappropriate position data");
            return null;
        }

        GameObject go = new GameObject("TriangleMesh");
        MeshFilter mf = go.AddComponent<MeshFilter>();
        MeshRenderer mr = go.AddComponent<MeshRenderer>();
        mr.material = ma;
        ma.color = color;
        mr.sortingOrder = sortingOrder;

        Mesh m = new Mesh();

        m.vertices = new Vector3[] {
            new Vector3(vertices[0].x, vertices[0].y, 0)
            ,new Vector3(vertices[1].x, vertices[1].y, 0)
            ,new Vector3(vertices[2].x, vertices[2].y, 0)
        };

        m.uv = vertices;

        m.triangles = new int[] { 0, 1, 2 } ;

        mf.mesh = m;
        m.RecalculateBounds();
        m.RecalculateNormals();

        return go;
    }

    public static GameObject CreatePlane2D(Vector2[] vertices, Material ma, Color color, int sortingOrder)
    {
        if (vertices.Length != 3)
        {
            Debug.LogWarning("CreatePlane2D inappropriate position data");
            return null;
        }

        GameObject go = new GameObject("PlaneMesh");
        MeshFilter mf = go.AddComponent<MeshFilter>();
        MeshRenderer mr = go.AddComponent<MeshRenderer>();
        mr.material = ma;
        ma.color = color;
        mr.sortingOrder = sortingOrder;

        Mesh m = new Mesh();

        m.vertices = new Vector3[] {
            new Vector3(vertices[0].x, vertices[0].y, 0)
            ,new Vector3(vertices[1].x, vertices[1].y, 0)
            ,new Vector3(vertices[2].x, vertices[2].y, 0)
            ,new Vector3(vertices[0].x, vertices[2].y, 0)
        };

        m.uv = new Vector2[] {
            new Vector2(0,0)
            ,new Vector2(0,1)
            ,new Vector2(1,1)
            ,new Vector2(1,0)
        };

        m.triangles = new int[] { 0, 1, 2, 0, 2, 3 };

        mf.mesh = m;
        m.RecalculateBounds();
        m.RecalculateNormals();

        return go;
    }

    public static void UpdateMesh2DPosition(GameObject go, Vector2[] vertices)
    {
        MeshFilter mf = go.GetComponent<MeshFilter>();
        
        mf.mesh.vertices = new Vector3[] {
             new Vector3(vertices[0].x, vertices[0].y, 0)
            ,new Vector3(vertices[1].x, vertices[1].y, 0)
            ,new Vector3(vertices[2].x, vertices[2].y, 0)
            ,new Vector3(vertices[0].x, vertices[2].y, 0)
        };
    }
}
