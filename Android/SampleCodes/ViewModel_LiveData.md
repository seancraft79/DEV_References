
# ViewModel + LiveData

[ViewModel overview](https://developer.android.com/topic/libraries/architecture/viewmodel#java)  

### Add dependency
```
def lifecycle_version = "2.1.0"

// ViewModel and LiveData
implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
```


### ViewModel
```
public class MyViewModel extends ViewModel {
    private MutableLiveData<List<User>> users;
    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<List<User>>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch users.
    }
}
```

### ViewModelFactory
```
public class MainViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(MainRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown viewModel class");
        }
    }
}

// Init ViewModel by ViewModelFactory
viewModel = ViewModelProviders.of(this, new MainViewModelFactory()).get(MainViewModel.class);
```

### Share data between fragments
```
// ViewModel
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Item> selected = new MutableLiveData<Item>();

    public void select(Item item) {
        selected.setValue(item);
    }

    public LiveData<Item> getSelected() {
        return selected;
    }
}

// Activity
public class DetailFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, { item ->
           // Update the UI.
        });
    }
}

// Fragment
public class MasterFragment extends Fragment {
    private SharedViewModel model;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
    }
}
```

