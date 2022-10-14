package com.reginald.briefcaseglobal.Aariyan.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.reginald.briefcaseglobal.Aariyan.Activity.DealsButtonActivity;
import com.reginald.briefcaseglobal.Aariyan.Activity.SignatureActivity;
import com.reginald.briefcaseglobal.Aariyan.Adapter.AlreadySelectedItemAdapter;
import com.reginald.briefcaseglobal.Aariyan.Adapter.ItemAdapter;
import com.reginald.briefcaseglobal.Aariyan.Database.DatabaseAdapter;
import com.reginald.briefcaseglobal.Aariyan.Interface.ClickProduct;
import com.reginald.briefcaseglobal.Aariyan.Interface.InsertHeader_N_Lines;
import com.reginald.briefcaseglobal.Aariyan.Interface.ItemSelectionInterface;
import com.reginald.briefcaseglobal.Aariyan.Interface.RestApis;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.Networking.Post_N_InsertIntoLocal;
import com.reginald.briefcaseglobal.Aariyan.ViewModel.ProductViewModel;
import com.reginald.briefcaseglobal.Network.APIs;
import com.reginald.briefcaseglobal.Network.ApiClient;
import com.reginald.briefcaseglobal.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateDealsFragment extends Fragment implements View.OnClickListener, ClickProduct {

    private DealsButtonActivity activity;

    private TextView transactionId, customerCode, dateFrom, dateTo;
    private Button saveBtn, finishBtn, addItemBtn;
    private RecyclerView itemRecyclerView;

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
    ItemAdapter itemAdapter;

    private ProgressBar progressBar;

    private static String BASE_URL = "http://102.37.0.48/NewBriefcaseTradePort/";
    //private String code = "SA005"; // Hard coded from Postman:

    String userID, code, ipURL;

    private DatabaseAdapter databaseAdapter;

    private ProductViewModel viewModel;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;
    String date = "";

    private String costForGP = "0.0";

    private String selectedDateFrom = "0", selectedDateTo = "0";

    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public HeadersModel headersModel;
    public LinesModel linesModel;

    private List<HeadersModel> listOfHeaders = new ArrayList<>();
    private List<LinesModel> listOfLines = new ArrayList<>();

    AlreadySelectedItemAdapter alreadySelectedItemAdapter;

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
        View root = inflater.inflate(R.layout.fragment_create_deals, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseAdapter = new DatabaseAdapter(activity);
        sharedPreferences = activity.getSharedPreferences("IP_FILE", Context.MODE_PRIVATE);
        ;
        editor = sharedPreferences.edit();

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

        itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        initializeBottomSheet();
    }

    private void initializeBottomSheet() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

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

        divider = bottomSheetView.findViewById(R.id.divider);
        dividerTwo = bottomSheetView.findViewById(R.id.dividerTwo);

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

        progressBar = bottomSheetView.findViewById(R.id.pBar);

        dialog.setContentView(bottomSheetView);

        //behavior:
        behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = String.valueOf(s);
                if (!search.equals("")) {
                    itemAdapter.getFilter().filter(search);
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ipURL = sharedPreferences.getString("IP", BASE_URL);
        userID = sharedPreferences.getString("ID", "SA005");
        code = sharedPreferences.getString("CODE", "SA005");
        customerCode.setText(String.valueOf("" + code));
        dateFrom.setText(sharedPreferences.getString("from", "Date From"));
        dateTo.setText(sharedPreferences.getString("to", "Date To"));

        if (!sharedPreferences.getString("from", "Date From").equals("Date From")
                && !sharedPreferences.getString("to", "Date To").equals("Date To")) {
            selectedDateFrom = sharedPreferences.getString("from", "Date From");
            selectedDateTo = sharedPreferences.getString("to", "Date To");
        }

        if (transactionId().isEmpty() || transactionId().equals("")) {
            Toast.makeText(activity, "Didn't find Transaction Id!", Toast.LENGTH_SHORT).show();
        } else {
            transactionId.setText(String.valueOf("" + transactionId()));
        }

        // loading related will be there:
        RestApis apiService = ApiClient.getClient(ipURL).create(RestApis.class);
        loadProduct(apiService);

    }

    private void loadProduct(RestApis apiService) {
        ClickProduct clickProduct = this;
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        //viewModel.init(apiService, code, databaseAdapter);
        //Testing:
        viewModel.init(apiService, "SA005", databaseAdapter);
        viewModel.getListOfProducts().observe(activity, new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                itemAdapter = new ItemAdapter(activity, productModels, clickProduct);
                itemToBeSelectedList.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);
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
                if (!selectedDateFrom.equals("0") && !selectedDateTo.equals("0")) {
                    progressBar.setVisibility(View.VISIBLE);
                    // create Bottom Sheet:
                    showBottomDialogWithData();
                } else {
                    Toast.makeText(activity, "Please select date first!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dateFromTextView:
                showDatePicker("from");
                break;

            case R.id.dateToTextView:
                showDatePicker("to");
                break;
            case R.id.saveDate:
                if (!selectedDateFrom.equals("0") && !selectedDateTo.equals("0")) {
                    editor.putString("from", selectedDateFrom);
                    editor.putString("to", selectedDateTo);
                    editor.commit();
                } else {
                    Toast.makeText(activity, "Select a date first!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.gpBtn:
                if (TextUtils.isEmpty(sellingPrice.getText().toString().trim()) || sellingPrice.getText().toString().equals("")) {
                    Toast.makeText(activity, "Please Enter Selling price first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(sellingPrice.getText().toString().trim()) || !costForGP.equals("0.0")) {
                    gpTextView.setText(String.valueOf("" + marginCalculator(costForGP, sellingPrice.getText().toString().toLowerCase(Locale.ROOT))));
                    workOnBottomTextView();
                    break;
                }
            case R.id.finishButton:
                fungForSelectedItem();
                headersModel.setProductPrice(sellingPrice.getText().toString().trim());
                linesModel.setPrice(sellingPrice.getText().toString().trim());
                break;

            case R.id.finishBtn:
                saveInLocalDatabase();
                startActivity(new Intent(activity, SignatureActivity.class)
                        .putExtra("url", ipURL)
                        .putExtra("tID",""+transactionId.getText().toString().trim()));
                break;

        }
    }

    private void saveInLocalDatabase() {
        InsertHeader_N_Lines performInsertionIntoLocal = new Post_N_InsertIntoLocal(databaseAdapter);
        performInsertionIntoLocal.insertHeaders(listOfHeaders);
        performInsertionIntoLocal.insertLines(listOfLines);
    }

    private void fungForSelectedItem() {
        if (headersModel != null && linesModel != null) {
            listOfLines.add(linesModel);
            listOfHeaders.add(headersModel);
        }

        //Showing on the recyclerView:
        alreadySelectedItemAdapter = new AlreadySelectedItemAdapter(activity, listOfHeaders);
        itemRecyclerView.setAdapter(alreadySelectedItemAdapter);
        alreadySelectedItemAdapter.notifyDataSetChanged();

        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private String transactionId() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String subscriberId = ts + "-" + android.provider.Settings.Secure.getString(activity.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        return subscriberId;
    }

    private void workOnBottomTextView() {
        bottomLayout.setVisibility(View.VISIBLE);
        finalPrice.setText(String.valueOf("" + sellingPrice.getText().toString().trim()));
        dateShowing.setText(String.valueOf("From: " + selectedDateFrom + "\nTO: " + selectedDateTo));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishBtnInBottomSheet.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    public double marginCalculator(String cost, String userInputPrice) {
        return (1 - (Double.parseDouble(cost) / Double.parseDouble(userInputPrice))) * 100;
    }

    private void showDatePicker(String identifier) {

        datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                //Month
                int j = i1 + 1;

                //date = i + "-" + j + "-" + i2;
                //date = i2 + "-" + j + "-" + i;
                date = i + "-" + j + "-" + i2;
                //2022-1-15
                if (identifier.equals("from")) {
                    selectedDateFrom = date;
                    dateFrom.setText(date);
                } else {
                    selectedDateTo = date;
                    dateTo.setText(date);
                }


            }
            //}, day, month, year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        new DatePickerDialog(AddTimeActivity.this, null, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

        datePickerDialog.show();
    }

    private void showBottomDialogWithData() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //set min height to parent:
        CoordinatorLayout layout = bottomSheetView.findViewById(R.id.bottomSheetLayout);
        //assert layout != null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        dialog.show();
    }

    @Override
    public void carryModel(ProductModel model) {
        selectedItemLayout.setVisibility(View.VISIBLE);
        dividerTwo.setVisibility(View.VISIBLE);
        costForGP = model.getCost();
        afterSelectedName.setText(String.valueOf("ITEM SELECTED, NAME: " + model.getStrDesc()));
        afterSelectedCost.setText(String.valueOf("ITEM COST: " + model.getCost()));

        String tID =  transactionId.getText().toString().trim();

        //Populating data for saving and posting:
        headersModel = new HeadersModel(
                "" + tID,
                "" + code,
                "" + selectedDateFrom,
                "" + selectedDateTo,
                "" + userID,
                0, //isCompleted
                0, //isUploaded
                "" + model.getStrDesc(),
                "" + sellingPrice.getText().toString().trim(),
                "" + model.getStrPartNumber()
        );



        linesModel = new LinesModel(
                "" + model.getStrPartNumber(),
                "" + sellingPrice.getText().toString().trim(),
                ""+tID
        );

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                bottomLayout.setVisibility(View.VISIBLE);
//            }
//        },2000);
    }

}