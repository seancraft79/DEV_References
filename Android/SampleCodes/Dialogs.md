
# Dialogs

### 
```
public void showDialog(DialogInterface.OnDismissListener dismissListener) {
        final Activity activity = activityWeakReference.get();
        if(activity == null) return null;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
	
	// Do your code ...

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setOnDismissListener(dismissListener);

        dialog.show();
    }
```

