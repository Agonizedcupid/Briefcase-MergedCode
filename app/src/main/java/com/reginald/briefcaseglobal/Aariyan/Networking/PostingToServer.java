package com.reginald.briefcaseglobal.Aariyan.Networking;

import android.util.Log;

import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Interface.SuccessInterface;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.Network.ApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class PostingToServer {

    private static final String TAG = "PostingToServer";

    private DatabaseAdapter databaseAdapter;
    RestApis apis;
    private ExecutorService executorService;

    private CompositeDisposable postDisposable;

    String finalLinesModelInXML;
    String finalHeadersModelInXml = "";
    String transactionId = "";

    double lat, lng;

    public PostingToServer(DatabaseAdapter databaseAdapter, RestApis apis, double lat, double lng) {
        this.databaseAdapter = databaseAdapter;
        this.apis = apis;
        executorService = Executors.newSingleThreadExecutor();
        postDisposable = new CompositeDisposable();
        this.lat = lat;
        this.lng = lng;
    }

    public void tryDirectPostingToServer(SuccessInterface successInterface) {
        // Querying Data:
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                //Headers
                mappingOuterModelToXmlFromDatabase(successInterface);
            }
        });

    }


    private void mappingOuterModelToXmlFromDatabase(SuccessInterface successInterface) {
        List<HeadersModel> listOfHeaders = databaseAdapter.getHeadersByUploaded();
        StringBuilder finalModelBuilder = new StringBuilder();
        HeadersModel model = listOfHeaders.get(0);
        transactionId = model.getTransactionId();
        finalHeadersModelInXml = "<Headers>" +
                "<transactionId>" + model.getTransactionId() + "</transactionId>" +
                "<CustomerCode>" + model.getCustomerCode() + "</CustomerCode>" +
                "<DealDateFrom>" + model.getDateFrom() + "</DealDateFrom>" +
                "<DealDateTo>" + model.getDateTo() + "</DealDateTo>" +
                "<CreatedByID>" + model.getUserId() + "</CreatedByID>" +
                //"<Coordinates>" + String.valueOf("-34.033013600000004,23.04171") + "</Coordinates>";
                "<Coordinates>" + String.valueOf(lat + "," + lng) + "</Coordinates>";

        finalModelBuilder.append(finalHeadersModelInXml)
                .append(mappingInnerModelToXmlFromDatabase(transactionId))
                .append("</Headers>");

        postItToTheServer(finalModelBuilder.toString(), transactionId, successInterface);

    }

    //Looping
//    private void mappingOuterModelToXmlFromDatabase(SuccessInterface successInterface) {
//        List<HeadersModel> listOfHeaders = databaseAdapter.getHeadersByUploaded();
//        StringBuilder finalModelBuilder = new StringBuilder();
//        for (HeadersModel model : listOfHeaders) {
//            transactionId = model.getTransactionId();
//            finalHeadersModelInXml = "<Headers>" +
//                    "<transactionId>" + model.getTransactionId() + "</transactionId>" +
//                    "<CustomerCode>" + model.getCustomerCode() + "</CustomerCode>" +
//                    "<DealDateFrom>" + model.getDateFrom() + "</DealDateFrom>" +
//                    "<DealDateTo>" + model.getDateTo() + "</DealDateTo>" +
//                    "<CreatedByID>" + model.getUserId() + "</CreatedByID>" +
//                    //"<Coordinates>" + String.valueOf("-34.033013600000004,23.04171") + "</Coordinates>";
//                    "<Coordinates>" + String.valueOf(lat + "," + lng) + "</Coordinates>";
//
//            finalModelBuilder.append(finalHeadersModelInXml)
//                    .append(mappingInnerModelToXmlFromDatabase(transactionId))
//                    .append("</Headers>");
//
//            postItToTheServer(finalModelBuilder.toString(), transactionId, successInterface);
////            postItToTheServer(finalHeadersModelInXml +
////                    mappingInnerModelToXmlFromDatabase(transactionId) +
////                    "</Headers>", transactionId, successInterface);
//
//        }
//
//
//    }


    private void postItToTheServer(String xmlData, String transactionId, SuccessInterface successInterface) {
        Log.d("XML_DATA", xmlData);
        postDisposable.add(apis.postToServer(xmlData)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        JSONArray status = new JSONArray(responseBody.string());
                        Log.d(TAG, "accept: "+status);
                        JSONObject single = status.getJSONObject(0);
                        String result = single.getString("result");

                        // Lets update the Headers isUploaded to 1:
                        if (databaseAdapter.updateDealsHeadersIsUploaded(transactionId) > 0) {
                            successInterface.onSuccess(result + " & Updated");
                        } else {
                            successInterface.onSuccess(result + " But Not Updated");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, "Error: "+throwable.getMessage());
                        successInterface.onError("" + throwable.getMessage());
                    }
                }));
    }

    /**
     * if (cursor2.moveToFirst()) {
     * do {
     * xml += XmlTemplate().toString()
     * .replace("unik",cursor2.getString(cursor2.getColumnIndex("strPartNumber")))
     * .replace("moola",""+ cursor2.getString(cursor2.getColumnIndex("Price")))
     * .replace("palo",""+cursor2.getString(cursor2.getColumnIndex("Quantity")));
     * } while (cursor2.moveToNext());
     * }
     */

    private String mappingInnerModelToXmlFromDatabase(String transactionId) {
        StringBuilder builder = new StringBuilder();
        List<LinesModel> listOfLines = databaseAdapter.getLinesByTransactionId(transactionId);
        if (listOfLines.size() > 0) {
            for (LinesModel model : listOfLines) {
//                finalLinesModelInXML += linesModelFormatLikeXML()
//                        .replace("pCode", model.getProductCode())
//                        .replace("price", model.getPrice());
                builder.append(linesModelFormatLikeXML()
                        .replace("pCode", model.getProductCode())
                        .replace("price", model.getPrice()
                        ));
            }
        }
        Log.d("LINES_FORMAT", "mappingDataToXmlFromDatabase: " + finalLinesModelInXML);
        //return finalLinesModelInXML;
        return builder.toString();
    }


    private String linesModelFormatLikeXML() {
        String Xml = "<Lines>" +
                "<ProductCode>pCode</ProductCode>" +
                "<Price>price</Price>" +
                "</Lines>";
        return Xml;

    }

}
