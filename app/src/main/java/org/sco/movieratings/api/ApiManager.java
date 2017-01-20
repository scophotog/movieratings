package org.sco.movieratings.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.support.annotation.NonNull;

import org.sco.movieratings.BuildConfig;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by sargenzi on 1/18/17.
 */

public class ApiManager {

    private final Retrofit mRetrofit;
    private final Map<Class<?>, Object> mCachedServiceMap = new HashMap<>();
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    public ApiManager() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(addAPIKey())
                .addInterceptor(interceptor)
                .build();

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(BASE_URL);
        retrofitBuilder.client(client);
        retrofitBuilder.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()));
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
        mRetrofit = retrofitBuilder.build();
    }

    private Interceptor addAPIKey() {
        return new Interceptor() {
            @Override
            public Response intercept(final Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * Generates an appropriate service that can be used for network requests
     *
     * @param serviceClass the service class type
     * @return an instance of the service class, which can be used for the actual request
     */
    @NonNull
    @SuppressWarnings("unchecked")
    public synchronized <T> T getService(@NonNull Class<T> serviceClass) {
        if (mCachedServiceMap.containsKey(serviceClass)) {
            return (T) mCachedServiceMap.get(serviceClass);
        }

        final T service = mRetrofit.create(serviceClass);
        mCachedServiceMap.put(serviceClass, service);
        return service;
    }

}

