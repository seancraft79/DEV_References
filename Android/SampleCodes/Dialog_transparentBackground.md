
### Dialog transparent background 
```
final Dialog dialog = new Dialog(this);
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
dialog.setContentView(R.layout.splash);
dialog.show();
```
