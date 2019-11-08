
# ViewModel + LiveData

### gradle
```
	defaultConfig {
		...

		vectorDrawables {
		    useSupportLibrary = true
		}
    }
```

### Layout
```
<layout xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">

    <data>
        <variable
            name="isLoading"
            type="boolean" />
    </data>

    ...

	<TextView
            ...
            app:visibleGone="@{isLoading}" />

</layout>
```
