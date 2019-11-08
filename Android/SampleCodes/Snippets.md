
# Snippets

### Round stroke transparent background
```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">

    <solid
        android:color="#00000000" />

    <stroke
        android:width="1dp"
        android:color="#ffffff"/>

    <corners
        android:radius="10dp" />
</shape>
```

### Rounded button 
```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="#FFFFFF"/>
    <corners android:radius="10dp"/>
</shape>
```

### Dialog transparent background 
```
final Dialog dialog = new Dialog(this);
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
dialog.setContentView(R.layout.splash);
dialog.show();
```

### Use Java8
```
android {
    ...

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

### SharedPrefe
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
