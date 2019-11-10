
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


### Use Java8
```
android {
    ...

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    or

    compileOptions {
        targetCompatibility 1.8
        sourceCompatibility 1.8
    }
}
```


### Use Vector drawable
```
	defaultConfig {
		...

		vectorDrawables {
		    useSupportLibrary = true
		}
    }
```


### Data Binding
```
	dataBinding {
	   enabled = true
	}
```