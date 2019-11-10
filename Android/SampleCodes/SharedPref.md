
# SharedPreferences

### SharedPreferences
```
	public static final String PREF_FILE = "mypref";

	public static SharedPreferences.Editor getPrefEdit(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_FILE, MODE_PRIVATE);
		return pref.edit();
	    }

	SharedPreferences.Editor editor = getPrefEdit(context);
	editor.putBoolean(key, value);
	editor.commit();
```
```

