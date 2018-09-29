Libraries Utilized

Retrofit (Retrofit-RxJava2Adapter)
    - Created ImgurService interface, with gallery search endpoint that takes path
    and query arguments.  Header annotation is attached to the call.
    - ServiceGenerator creates Retrofit.Builder object that sets base url, converterfactory,
    calladapterFactory, and OkHttp3 client.
    - API requests return Observable response via service interface using the retrofit-rx adapter.
    - ServiceGenerator instantiates ImgurService, which proceeds to make calls with
    Observable<ImgurGalleryList> return type.

RxJava2 (RxAndroid and RxBinding)
    - Created Observable from network calls via retrofit-rxjava adapter.
    - Subscribed to ImgurService observable with DisposableSingleObserver that adds the
    response body via onNext(), asynchronously, observers are subscribed on separate
    IO thread with callbacks executed on MainThread provided via RxAndroid library.
    - ImgurFragment search button observable is subscribed to for onClick events via
    RxBinding library.
    - Observables are added to CompositeDisposable object for easy garbage cleanup
    when fragment is destroyed.

LiveData
    - Sync ViewModel fields to Room table values.
    - Links views to ViewModel fields for updating data.

ViewModel
    - Retrieves data from repository to be observed by the view.

Gson (Retrofit-GsonConverter)
    - GsonConverter added to Retrofit client to map response body JSON to typed
    object models.

Timber
    - Timber DebugTree planted in Application class when starting in Debug mode.
    - Application log statements are made using Timber static object.

OkHttp3 (OkHttp3LoggingInterceptor)
    - LoggingIntercepter added to OkHttp3 client which is then added to Retrofit
    client.  Logs all data from resulting network requests and responses.

Butterknife
    - Binds layout views to their respective Activities, Fragments and Viewholders,
    for cleaner code.

Glide
    - Handles image bitmap rendering asynchronously.


Support Libraries
Dagger
LeakCanary
Stetho
