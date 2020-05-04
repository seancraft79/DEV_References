using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public static class Extensions 
{
	/// <summary>
	/// Returns vector with specified y.
	/// </summary>
	/// <returns>The y.</returns>
	/// <param name="a">Input vector.</param>
	/// <param name="y">The y coordinate.</param>
	public static Vector3 WithY(this Vector3 a, float y)
	{
		return new Vector3 (a.x, y, a.z);
	}

	public static Vector3 WithX(this Vector3 a, float x)
	{
		return new Vector3(x, a.y, a.z);
	}

	public static Vector3 WithZ(this Vector3 a, float z)
	{
		return new Vector3(a.x, a.y, z);
	}

	public static void DestroyAllChildren(this Transform transform)
	{
		for (int i = 0; i < transform.childCount; i++)
		{
			GameObject.Destroy(transform.GetChild(i).gameObject);
		}
	}
	
	/// <summary>
	/// Vector2Int.up, down, left, right중 하나를 리턴
	/// </summary>
	public static Vector2Int Normalize(this Vector2Int vector)
	{
		if (vector == Vector2Int.zero)
			return Vector2Int.zero;
		if(Mathf.Abs(vector.x) > Mathf.Abs(vector.y))
		{
			if (vector.x > 0) return Vector2Int.right;
			else return Vector2Int.left;
		}
		else
		{
			if (vector.y > 0) return Vector2Int.up;
			else return Vector2Int.down;
		}
	}

	public static Vector2 ToVector2(this Vector3 vector)
	{
		return new Vector2(vector.x, vector.y);
	}
}
