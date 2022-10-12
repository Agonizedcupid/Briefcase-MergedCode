package com.reginald.briefcaseglobal.Aariyan.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.reginald.briefcaseglobal.Aariyan.Activity.DealsButtonActivity;
import com.reginald.briefcaseglobal.Aariyan.Adapter.ItemAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.ViewModel.ProductViewModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;

import java.util.List;

public class CreateDealsFragment extends Fragment implements View.OnClickListener {

    private DealsButtonActivity activity;

    private TextView transactionId, customerCode, dateFrom, dateTo;
    private Button saveBtn, finishBtn, addItemBtn;
    private RecyclerView itemRecyclerViewBtn;

    //Bottom Sheet:
    private ConstraintLayout bottomLayout, selectedItemLayout;
    private Button finishBtnInBottomSheet, gpBtn;
    private EditText searchBar, sellingPrice;
    private RecyclerView itemToBeSelectedList;
    private TextView afterSelectedName, afterSelectedCost, gpTextView, finalPrice, dateShowing;
    private ImageView divider, dividerTwo;
    BottomSheetBehavior<View> behavior;
    BottomSheetDialog dialog;
    View bottomSheetView;

    private static String BASE_URL = "http://102.37.0.48/NewBriefcaseTradePort/";
    private String code = "SA005"; // Hard coded from Postman:

    private ProductViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (DealsButtonActivity) context;
        }
    }

    public CreateDealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_create_deals, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        transactionId = view.findViewById(R.id.transactionId);
        customerCode = view.findViewById(R.id.customerCode);
        dateFrom = view.findViewById(R.id.dateFromTextView);
        dateFrom.setOnClickListener(this);
        dateTo = view.findViewById(R.id.dateToTextView);
        dateTo.setOnClickListener(this);
        addItemBtn = view.findViewById(R.id.addItemsBtn);
        addItemBtn.setOnClickListener(this);
        saveBtn = view.findViewById(R.id.saveDate);
        saveBtn.setOnClickListener(this);
        finishBtn = view.findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(this);

        itemRecyclerViewBtn = view.findViewById(R.id.itemRecyclerView);
        itemRecyclerViewBtn.setLayoutManager(new LinearLayoutManager(activity));

        initializeBottomSheet();
    }

    private void initializeBottomSheet() {
        dialog = new BottomSheetDialog(activity);
        bottomSheetView = LayoutInflater.from(activity).inflate(R.layout.available_items_layout, null);

        finalPrice = bottomSheetView.findViewById(R.id.finalPriceShowing);
        dateShowing = bottomSheetView.findViewById(R.id.dateShowing);

        finishBtnInBottomSheet = bottomSheetView.findViewById(R.id.finishButton);
        assert finishBtnInBottomSheet != null;
        finishBtnInBottomSheet.setOnClickListener(this);

        bottomLayout = bottomSheetView.findViewById(R.id.bottomLayout);
        selectedItemLayout = bottomSheetView.findViewById(R.id.afterItemSelectedLayout);

        itemToBeSelectedList = bottomSheetView.findViewById(R.id.itemToBeSelectedRecyclerView);
        assert itemToBeSelectedList != null;
        itemToBeSelectedList.setLayoutManager(new LinearLayoutManager(activity));

        searchBar = bottomSheetView.findViewById(R.id.searchHere);
        afterSelectedName = bottomSheetView.findViewById(R.id.selectedItemName);
        afterSelectedCost = bottomSheetView.findViewById(R.id.selectedItemCost);
        sellingPrice = bottomSheetView.findViewById(R.id.enterSellingPrice);

        gpBtn = bottomSheetView.findViewById(R.id.gpBtn);
        assert gpBtn != null;
        gpBtn.setOnClickListener(this);
        gpTextView = bottomSheetView.findViewById(R.id.gpTextView);

        bottomSheetView.findViewById(R.id.backBtnInBottomSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        dialog.setContentView(bottomSheetView);

        //behavior:
        behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
    }

    @Override
    public void onResume() {
        super.onResume();
        // loading related will be there:
        RestApis apiService = ApiClient.getClient(BASE_URL).create(RestApis.class);
        loadProduct(apiService);

    }

    private void loadProduct(RestApis apiService) {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        viewModel.init(apiService, code);
        viewModel.getListOfProducts().observe(activity, new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                ItemAdapter adapter = new ItemAdapter(activity, productModels);
                itemToBeSelectedList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addItemsBtn:
                // create Bottom Sheet:
                showBottomDialogWithData();
                break;
        }
    }

    private void showBottomDialogWithData() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //set min height to parent:
        CoordinatorLayout layout = bottomSheetView.findViewById(R.id.bottomSheetLayout);
        //assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        dialog.show();
    }
}