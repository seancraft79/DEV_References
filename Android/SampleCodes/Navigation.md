# Navigation  
  
[androidx.navigation](https://developer.android.com/jetpack/androidx/releases/navigation)  
  
### app build.gradle
```
        // Navigation
        def navigation_version = "2.2.0"
        implementation "androidx.navigation:navigation-fragment:$navigation_version"
        implementation "androidx.navigation:navigation-ui:$navigation_version"
```

### Resource
```
    // res 에 navigation 리소스 폴더 생성
    // navigation Resource File 생성 (예 : nav_graph 이름으로 생성함)

```

### MainActivity layout
```
    <fragment
            android:id="@+id/nav_host_fragment"
            android:name="androidx.navigation.fragment.NavHostFragment"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:defaultNavHost="true"
            app:navGraph="@navigation/nav_graph" />
```

### MainActivity
```
    public class MainActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // App bar 사용시
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // App bar 사용시
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // App bar 사용시
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.navigateUp(navController, appBarConfiguration);
    }
}
```

### Navigation + Bundle data
```
    // Send parcelable data
    Bundle bundle = new Bundle();
    bundle.putParcelable("item", item);
    // action_name : nav_graph 에서 생성한 action id
    Navigation.findNavController(mView).navigate(R.id.action_name, bundle);

    // Receive data
    final ParcelableItem item = getArguments().getParcelable("item");
```
