# ViewBinding

### app build.gradle
```
    dataBinding {
            enabled = true
        }
```

### Activity
```
    <ActivityPrefix>Binding viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_layout);
```

### Fragment
```
    // OnViewCreated
    <FragmentPrefix>Binding viewBinding = DataBindingUtil.bind(view);
```