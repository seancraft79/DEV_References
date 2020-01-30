# ViewBinding

[ViewBinding](https://developer.android.com/topic/libraries/view-binding) 

### app build.gradle
```
    android {
        
        ...

        dataBinding {
            enabled = true
        }
    }
```

### Ignore generating binding class
```
<LinearLayout
        ...
        tools:viewBindingIgnore="true" >
    ...
</LinearLayout>
```


### Layout
```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"/>
   </LinearLayout>
</layout>
```

### Activity
```
    <ActivityPrefix>Binding viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_layout);
    viewBinding.setLifeCycleOwner(this);

    // For layout data binding
    viewBinding.setViewModel(viewModel);
```

### Fragment
```
    // OnViewCreated
    <FragmentPrefix>Binding viewBinding = DataBindingUtil.bind(view);
    viewBinding.setLifeCycleOwner(this);

    // For layout data binding
    viewBinding.setViewModel(viewModel);
```

