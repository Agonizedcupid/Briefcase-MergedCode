package com.reginald.briefcaseglobal.Interface;


import com.reginald.briefcaseglobal.Model.CustomerModel;

import java.util.List;

public interface CustomerListInterface {

    void customerList(List<CustomerModel> list);
    void error(String message);
}
