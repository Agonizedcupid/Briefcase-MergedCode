package com.reginald.briefcaseglobal.Network;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.reginald.briefcaseglobal.AppApplication;
import com.reginald.briefcaseglobal.Constant.Constant;
import com.reginald.briefcaseglobal.Interface.CustomerListInterface;
import com.reginald.briefcaseglobal.Interface.MessageListInterface;
import com.reginald.briefcaseglobal.Model.CustomerModel;
import com.reginald.briefcaseglobal.Model.MessageModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Networking {
    APIs apiService;
    private Context context;
    List<MessageModel> list = new ArrayList<>();
    List<CustomerModel> customerList = new ArrayList<>();
    String roles,userID,searchText,IP,DimsIp;
    String Result = "",OrderId="";
    private SQLiteDatabase db,dbOrders;

    public Networking(Context context, String Ip) {
        this.context = context;

        apiService = ApiClient.getClient(Ip).create(APIs.class);

    }

    public void getMessageList(MessageListInterface messageListInterface) {

        Call<ResponseBody> call = apiService.getMessageList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    Constant.listOfMessage.clear();
                    JSONArray finalResponse = new JSONArray(response.body().string());
                    for (int i=0; i<finalResponse.length(); i++) {
                        JSONObject object = finalResponse.getJSONObject(i);
                        String intAutoId = object.getString("intAutoId");
                        int intDepId = object.getInt("intDepId");
                        String Department = object.getString("Department");
                        String messagestofollow = object.getString("messagestofollow");
                        int intCreatedBy = object.getInt("intCreatedBy");
                        int intAssignedTo = object.getInt("intAssignedTo");
                        JSONObject o = object.getJSONObject("dteTimeCreate");
                        String date = o.getString("date");
                        int intStatusId = object.getInt("intStatusId");
                        String strSubject = object.getString("strSubject");
                        String FromUser = object.getString("FromUser");

                        MessageModel model = new MessageModel(
                                intAutoId,intDepId,Department,messagestofollow,intCreatedBy,intAssignedTo,
                                date,intStatusId,strSubject,FromUser
                        );

                        list.add(model);
                    }

                    messageListInterface.messageList(list);

                }catch (Exception e) {
                    messageListInterface.error(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                messageListInterface.error(e.getMessage());
            }
        });

    }

    public void getCustomerList(CustomerListInterface customerListInterface,String UserId) {

        Call<ResponseBody> call = apiService.getCustomerList(UserId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONArray finalResponse = new JSONArray(response.body().string());
                    for (int i=0; i<finalResponse.length(); i++) {
                        JSONObject object = finalResponse.getJSONObject(i);
                        String CustomerPastelCode = object.getString("CustomerPastelCode");
                        String StoreName = object.getString("StoreName");
                        String lastNotes = object.getString("lastNotes");
                        String lastDate = object.getString("lastDate");
                        CustomerModel model = new CustomerModel(CustomerPastelCode,StoreName,lastNotes,lastDate);
                        customerList.add(model);
                    }

                    customerListInterface.customerList(customerList);

                }catch (Exception e) {
                    customerListInterface.error(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable e) {
                customerListInterface.error(e.getMessage());
            }
        });

    }
}
