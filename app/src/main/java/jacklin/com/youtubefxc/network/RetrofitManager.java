package jacklin.com.youtubefxc.network;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.concurrent.TimeUnit;

import jacklin.com.youtubefxc.api.GoogleService;
import jacklin.com.youtubefxc.api.YoutubeService;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RetrofitManager {
    private YoutubeService mService;
    private GoogleService mGoogleService;
    private static RetrofitManager instance = new RetrofitManager();

    String youtube_url = "https://www.googleapis.com/youtube/v3/";
    String google_url = "http://google.com/complete/";
    private RetrofitManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(youtube_url)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mService = retrofit.create(YoutubeService.class);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();

        mGoogleService = new Retrofit.Builder()
                .baseUrl(google_url)
                .client(okHttpClient)
                .addConverterFactory(
                        SimpleXmlConverterFactory.createNonStrict(
                                new Persister(new AnnotationStrategy()) // important part!
                        ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(GoogleService.class);
    }

    public static RetrofitManager getInstance(){
        return instance;
    }

    public YoutubeService getAPI(){
        return instance.mService;
    }

    public GoogleService getGoogleAPI(){
        return instance.mGoogleService;
    }

}
