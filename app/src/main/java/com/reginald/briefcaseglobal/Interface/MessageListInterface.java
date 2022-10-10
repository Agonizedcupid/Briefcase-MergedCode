package com.reginald.briefcaseglobal.Interface;


import com.reginald.briefcaseglobal.Model.MessageModel;

import java.util.List;

public interface MessageListInterface {

    void messageList(List<MessageModel> list);
    void error(String error);
}
