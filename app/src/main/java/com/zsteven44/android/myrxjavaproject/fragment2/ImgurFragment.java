package com.zsteven44.android.myrxjavaproject.fragment2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.zsteven44.android.myrxjavaproject.R;
import com.zsteven44.android.myrxjavaproject.imgur.ImgurAdapter;
import com.zsteven44.android.myrxjavaproject.imgur.ImgurGallery;
import com.zsteven44.android.myrxjavaproject.imgur.ImgurGalleryList;
import com.zsteven44.android.myrxjavaproject.services.ServiceGenerator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.zsteven44.android.myrxjavaproject.AppConstants.IMGUR_API_BASE_URL;

public class ImgurFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.search_text)
    Button searchButton;

    private Unbinder unbinder;
    private ImgurAdapter<ImgurGallery> adapter;
    private CompositeDisposable disposables;
    public ImgurFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_imgur, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.tag(ImgurFragment.class.getName());
        Timber.d("ImgurFragment view created.");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                2,
                GridLayoutManager.VERTICAL,
                false));
        adapter = new ImgurAdapter<ImgurGallery>(new ArrayList<ImgurGallery>(),
                R.layout.row_layout_imgur);
        recyclerView.setAdapter(adapter);
        disposables= new CompositeDisposable();
        disposables
                .add(RxView
                        .clicks(searchButton)
                        .subscribe(aVoid ->
                                Timber.d("SearchButton 'RxView.clicks' registered.")));
        loadGalleries();

    }

    private void loadGalleries() {
        Timber.d("Running loadGalleries...");
        Observable<ImgurGalleryList> imgurObservable = doSearchGalleries("top","day","cat");
        disposables.add(imgurObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ImgurGalleryList>() {
                    @Override
                    public void onNext(ImgurGalleryList imgurGalleryList) {
                        Timber.d("Running imgurObservable 'onNext': %s",
                                imgurGalleryList.toString());
                        adapter.addItemList(imgurGalleryList.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Running imgurObservable 'onComplete'.");
                    }
                }));
    }

    public Observable<ImgurGalleryList> doSearchGalleries(@NonNull final String sort,
                                                        @NonNull final String window,
                                                        @Nullable final String search) {

        Timber.d("Running doSearchGalleries with arguments:\nsort='%s' \nwindow='%s'\nsearch='%s'", sort, window, search);
        /*
            Option 1: Using Service Generator to house retrofit calls, recommended
            method from https://futurestud.io
        */
        ServiceGenerator.changeApiBaseUrl(IMGUR_API_BASE_URL);
        ImgurService service = ServiceGenerator.createService(ImgurService.class);
        Observable<ImgurGalleryList> call = service.getSearchGallery(sort, window, 1, search);
        return call;
    }

    /*
        Need to load imgur images via search and display them in adapter using
        rxjava observables.  Images need to be displayed with picass/glide.

     */

    @Override
    public void onDestroy() {
        unbinder.unbind();
        if (this.disposables != null && !this.disposables.isDisposed()) {
            this.disposables.dispose();
        }
        super.onDestroy();
    }
}
