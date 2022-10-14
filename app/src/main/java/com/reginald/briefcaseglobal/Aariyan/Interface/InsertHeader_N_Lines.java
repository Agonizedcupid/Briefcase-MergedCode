package com.reginald.briefcaseglobal.Aariyan.Interface;

import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;

import java.util.List;

public interface InsertHeader_N_Lines {

    void insertHeaders(List<HeadersModel> listOfHeaders);
    void insertLines(List<LinesModel> listOfLines);
}
