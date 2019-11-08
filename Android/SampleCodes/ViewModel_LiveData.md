
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

### Fragment
```
package com.example.android.persistence.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.ListFragmentBinding;
import com.example.android.persistence.db.entity.ProductEntity;
import com.example.android.persistence.model.Product;
import com.example.android.persistence.viewmodel.ProductListViewModel;

import java.util.List;

public class ProductListFragment extends Fragment {

    public static final String TAG = "ProductListFragment";

    private ProductAdapter mProductAdapter;

    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);

        mProductAdapter = new ProductAdapter(mProductClickCallback);
        mBinding.productsList.setAdapter(mProductAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProductListViewModel viewModel =
                new ViewModelProvider(this).get(ProductListViewModel.class);

        mBinding.productsSearchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable query = mBinding.productsSearchBox.getText();
                if (query == null || query.toString().isEmpty()) {
                    subscribeUi(viewModel.getProducts());
                } else {
                    subscribeUi(viewModel.searchProducts("*" + query + "*"));
                }
            }
        });

        subscribeUi(viewModel.getProducts());
    }

    private void subscribeUi(LiveData<List<ProductEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(this, new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(@Nullable List<ProductEntity> myProducts) {
                if (myProducts != null) {
                    mBinding.setIsLoading(false);
                    mProductAdapter.setProductList(myProducts);
                } else {
                    mBinding.setIsLoading(true);
                }
                // espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding.executePendingBindings();
            }
        });
    }

    private final ProductClickCallback mProductClickCallback = new ProductClickCallback() {
        @Override
        public void onClick(Product product) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(product);
            }
        }
    };
}
```

### VieweModel
```
package com.example.android.persistence.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.example.android.persistence.BasicApp;
import com.example.android.persistence.DataRepository;
import com.example.android.persistence.db.entity.ProductEntity;
import java.util.List;

public class ProductListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ProductEntity>> mObservableProducts;

    public ProductListViewModel(Application application) {
        super(application);

        mObservableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableProducts.setValue(null);

        mRepository = ((BasicApp) application).getRepository();
        LiveData<List<ProductEntity>> products = mRepository.getProducts();

        // observe the changes of the products from the database and forward them
        mObservableProducts.addSource(products, mObservableProducts::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return mObservableProducts;
    }

    public LiveData<List<ProductEntity>> searchProducts(String query) {
        return mRepository.searchProducts(query);
    }
}

```
