package com.reginald.briefcaseglobal.Aariyan.Networking;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Header;
import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.InsertHeader_N_Lines;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Post_N_InsertIntoLocal implements InsertHeader_N_Lines {

    private DatabaseAdapter databaseAdapter;
    public Post_N_InsertIntoLocal(DatabaseAdapter databaseAdapter) {
        this.databaseAdapter = databaseAdapter;
    }

    @Override
    public void insertHeaders(List<HeadersModel> listOfHeaders) {
        Observable observable = Observable.fromIterable(listOfHeaders)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                HeadersModel model = (HeadersModel) o;
                databaseAdapter.insertDealsHeaders(model);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("DATA_INSERTED", "HEADERS: ERROR: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("DATA_INSERTED", "Headers: Inserted");
            }
        };
        observable.subscribe(observer);
    }

    @Override
    public void insertLines(List<LinesModel> listOfLines) {
        Observable observable = Observable.fromIterable(listOfLines)
                .subscribeOn(Schedulers.io());

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                LinesModel model = (LinesModel) o;
                databaseAdapter.insertLines(model);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("DATA_INSERTED", "Lines: ERROR: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("DATA_INSERTED", "Lines: Inserted");
            }
        };
        observable.subscribe(observer);
    }

}
