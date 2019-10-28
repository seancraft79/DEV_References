# SAMPLE CODES


### Dialog transparent background 
```
final Dialog dialog = new Dialog(this);
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
dialog.setContentView(R.layout.splash);
dialog.show();
```


### Rounded button 
```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="rectangle">
    <solid android:color="#FFFFFF"/>
    <corners android:radius="10dp"/>
</shape>
```
