package com.reginald.briefcaseglobal.Aariyan.Networking;

import android.util.Log;

import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Interface.SuccessInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.SignatureModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PostSignature {
    private RestApis restApis;
    private CompositeDisposable compositeDisposable;
    public PostSignature (RestApis restApis) {
        this.restApis = restApis;
        compositeDisposable = new CompositeDisposable();
    }

    public void postSignatureToServer(SuccessInterface successInterface, List<SignatureModel> list) {
        compositeDisposable.add(restApis.postSignature(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        //JSONObject root = new JSONObject(responseBody.string());
                        //String single = root.getString("results");
                        Log.d("SIGNATURE_POSTING", ""+responseBody.string());
                        successInterface.onSuccess(""+responseBody.string());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        successInterface.onError(""+throwable.getMessage());
                        Log.d("SIGNATURE_POSTING", "ERROR: "+throwable.getMessage());
                    }
                }));
    }
}
