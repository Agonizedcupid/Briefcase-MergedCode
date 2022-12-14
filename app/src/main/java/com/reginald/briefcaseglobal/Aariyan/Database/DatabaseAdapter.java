package com.reginald.briefcaseglobal.Aariyan.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.reginald.briefcaseglobal.Aariyan.Model.DealHeaderModel;
import com.reginald.briefcaseglobal.Aariyan.Model.HeadersModel;
import com.reginald.briefcaseglobal.Aariyan.Model.LinesModel;
import com.reginald.briefcaseglobal.Aariyan.Model.ProductModel;
import com.reginald.briefcaseglobal.Aariyan.Model.SignatureModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {


    DatabaseHelper helper;
    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }
    /**
     * INSERTION
     */
    //Insert insertProductHeaders:
    public long insertProduct(ProductModel model) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.strPartNumber, model.getStrPartNumber());
        contentValues.put(DatabaseHelper.strDesc, model.getStrDesc());
        contentValues.put(DatabaseHelper.strCategory, model.getStrCategory());
        contentValues.put(DatabaseHelper.Vat, model.getVat());
        contentValues.put(DatabaseHelper.ProductID, model.getProductID());
        contentValues.put(DatabaseHelper.strCompanyName, model.getStrCompanyName());
        contentValues.put(DatabaseHelper.Cost, model.getCost());
        contentValues.put(DatabaseHelper.selectedCustomerCode, model.getSelectedCustomerCode());

        long id = database.insert(DatabaseHelper.PRODUCT_TABLE_NAME, null, contentValues);
        return id;
    }


    public long insertDealsHeaders(HeadersModel model) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.transactionID, model.getTransactionId());
        contentValues.put(DatabaseHelper.customerCode, model.getCustomerCode());
        contentValues.put(DatabaseHelper.dateFrom, model.getDateFrom());
        contentValues.put(DatabaseHelper.dateTo, model.getDateTo());
        contentValues.put(DatabaseHelper.userId, model.getUserId());
        contentValues.put(DatabaseHelper.isCompleted, model.getIsCompleted());
        contentValues.put(DatabaseHelper.isUploaded, model.getIsUploaded());
        contentValues.put(DatabaseHelper.productName, model.getProductName());
        contentValues.put(DatabaseHelper.productPrice, model.getProductPrice());
        contentValues.put(DatabaseHelper.productCode, model.getProductCode());

        long id = database.insert(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertLines(LinesModel model) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.itemCode, model.getProductCode());
        contentValues.put(DatabaseHelper.itemPrice, model.getPrice());
        contentValues.put(DatabaseHelper.transactionIdInLines, model.getTransactionId());

        long id = database.insert(DatabaseHelper.DEALS_LINES_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertSignature(SignatureModel model) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SIGNATURE, model.getSignature());
        contentValues.put(DatabaseHelper.transactionIdInSignature, model.getTransactionId());

        long id = database.insert(DatabaseHelper.SIGNATURE_TABLE_NAME, null, contentValues);
        return id;
    }

    /**
     * GET DATA
     */
//    public List<QueueModel> getQueue() {
//        List<QueueModel> list = new ArrayList<>();
//        SQLiteDatabase database = helper.getWritableDatabase();
//
//        String[] columns = {DatabaseHelper.UID, DatabaseHelper.Type, DatabaseHelper.Instructions};
//
//        Cursor cursor = database.query(DatabaseHelper.QUEUE_TABLE_NAME, columns, null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            QueueModel model = new QueueModel(
//                    cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getString(2)
//            );
//            list.add(model);
//        }
//        return list;
//    }

    /**
     * Get By Something (FILTERING)
     */
    //get Product by customer code:
    public List<ProductModel> getProductByCustomerCode(String customerCode) {

        List<ProductModel> listOfProducts = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.selectedCustomerCode + "=?";


        String[] args = {"" + customerCode};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.strPartNumber, DatabaseHelper.strDesc,
                DatabaseHelper.strCategory,
                DatabaseHelper.Vat, DatabaseHelper.ProductID,
                DatabaseHelper.strCompanyName, DatabaseHelper.Cost,
                DatabaseHelper.selectedCustomerCode};

        Cursor cursor = database.query(DatabaseHelper.PRODUCT_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            ProductModel model = new ProductModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            listOfProducts.add(model);
        }
        return listOfProducts;

    }

    public List<SignatureModel> getSignatureByTransactionId(String transactionId) {

        List<SignatureModel> listOfProducts = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.transactionIdInSignature + "=?";


        String[] args = {"" + transactionId};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.SIGNATURE, DatabaseHelper.transactionIdInSignature};

        Cursor cursor = database.query(DatabaseHelper.SIGNATURE_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            SignatureModel model = new SignatureModel(
                    cursor.getString(1),
                    cursor.getString(2)
            );
            listOfProducts.add(model);
        }
        return listOfProducts;

    }

    public List<LinesModel> getLines() {
        List<LinesModel> listOfLines = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.UID, DatabaseHelper.itemCode, DatabaseHelper.itemPrice};

        Cursor cursor = database.query(DatabaseHelper.DEALS_LINES_TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            LinesModel model = new LinesModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            listOfLines.add(model);
        }
        return listOfLines;

    }

    public List<LinesModel> getLinesByTransactionId(String transactionId) {

        List<LinesModel> listOfLines = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.transactionIdInLines + "=?";
        String[] args = {"" + transactionId};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.itemCode, DatabaseHelper.itemPrice, DatabaseHelper.transactionIdInLines};

        Cursor cursor = database.query(DatabaseHelper.DEALS_LINES_TABLE_NAME, columns, selection, args, null, null, null);
        while (cursor.moveToNext()) {
            LinesModel model = new LinesModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            listOfLines.add(model);
        }
        return listOfLines;

    }

    public List<HeadersModel> getHeadersByUploaded() {

        List<HeadersModel> listOfHeaders = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from tableName where name = ? and customerName = ?:
        // String selection = DatabaseHelper.USER_NAME+" where ? AND "+DatabaseHelper.CUSTOMER_NAME+" LIKE ?";
        String selection = DatabaseHelper.isUploaded + "=?";
        String[] args = {"" + 0};
        String[] columns = {DatabaseHelper.UID, DatabaseHelper.transactionID,
                DatabaseHelper.customerCode,
                DatabaseHelper.dateFrom,
                DatabaseHelper.dateTo,
                DatabaseHelper.userId,
                DatabaseHelper.isCompleted,
                DatabaseHelper.isUploaded,
                DatabaseHelper.productName,
                DatabaseHelper.productPrice,
                DatabaseHelper.productCode
        };

        Cursor cursor = database.query(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, columns, selection, args, null, null, null);

        while (cursor.moveToNext()) {
            HeadersModel model = new HeadersModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
            listOfHeaders.add(model);
        }
        return listOfHeaders;

    }

    public List<HeadersModel> getHeaders() {
        List<HeadersModel> listOfHeaders = new ArrayList<>();
        SQLiteDatabase database = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.UID, DatabaseHelper.transactionID,
                DatabaseHelper.customerCode,
                DatabaseHelper.dateFrom,
                DatabaseHelper.dateTo,
                DatabaseHelper.userId,
                DatabaseHelper.isCompleted,
                DatabaseHelper.isUploaded,
                DatabaseHelper.productName,
                DatabaseHelper.productPrice,
                DatabaseHelper.productCode
        };

        Cursor cursor = database.query(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            HeadersModel model = new HeadersModel(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
             listOfHeaders.add(model);
        }
        return listOfHeaders;

    }


    /**
     * Update
     */
    //Update isCompleted:
    public long updateDealsHeadersIsCompleted(int flag) {
        SQLiteDatabase database = helper.getWritableDatabase();
//        String selection = DatabaseHelper.isCompleted + " LIKE ? AND " + DatabaseHelper.OrderDetailId + " LIKE ? ";
//        String[] args = {"" + orderId, "" + orderDetailsId};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.isCompleted, flag);

        long ids = database.update(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, contentValues, null, null);

        return ids;
    }

    public long updateDealsHeadersIsUploaded(String transactionId) {
        SQLiteDatabase database = helper.getWritableDatabase();
        String selection = DatabaseHelper.transactionID + " LIKE ? ";
        String[] args = {"" + transactionId};

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.isUploaded, 1);

        long ids = database.update(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, contentValues, selection, args);

        return ids;
    }

    /**
     *  Delete
     */

    public long deleteSignature(String transactionId) {
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from table_name where id = id
        String selection = DatabaseHelper.transactionIdInSignature + " LIKE ?";

        String[] args = {"" + transactionId};
        long ids = database.delete(DatabaseHelper.SIGNATURE_TABLE_NAME, selection, args);

        return ids;
    }

    public long deleteLines(String transactionId) {
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from table_name where id = id
        String selection = DatabaseHelper.transactionIdInSignature + " LIKE ?";
        String[] args = {"" + transactionId};
        long ids = database.delete(DatabaseHelper.DEALS_LINES_TABLE_NAME, selection, args);

        return ids;
    }

    public long deleteDeals(String transactionId) {
        SQLiteDatabase database = helper.getWritableDatabase();
        //select * from table_name where id = id
        String selection = DatabaseHelper.transactionIdInSignature + " LIKE ?";
        String[] args = {"" + transactionId};
        long ids = database.delete(DatabaseHelper.DEALS_HEADERS_TABLE_NAME, selection, args);

        return ids;
    }

    /**
     * Drop Header table
     */

    public void dropProductTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_PRODUCT_TABLE);
        database.execSQL(DatabaseHelper.CREATE_PRODUCT_TABLE);
    }

    public void dropDealsHeadersTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_DEALS_HEADERS_TABLE);
        database.execSQL(DatabaseHelper.CREATE_DEALS_HEADERS_TABLE);
    }

    public void dropDealsLinesTable() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(DatabaseHelper.DROP_DEALS_LINES_TABLE);
        database.execSQL(DatabaseHelper.CREATE_DEALS_LINES_TABLE);
    }


    class DatabaseHelper extends SQLiteOpenHelper {
        private Context context;

        private static final String DATABASE_NAME = "BRIEFCASE_DB.db";
        private static final int VERSION_NUMBER = 25;

        /**
         *  Product table
         */
        private static final String PRODUCT_TABLE_NAME = "PRODUCT";
        private static final String UID = "_id";
        private static final String strPartNumber = "strPartNumber";
        private static final String strDesc = "strDesc";
        private static final String strCategory = "strCategory";
        private static final String Vat = "Vat";
        private static final String ProductID = "ProductID";
        private static final String strCompanyName = "strCompanyName";
        private static final String Cost = "Cost";
        private static final String selectedCustomerCode = "customerCode";

        //Creating the table:
        private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE " + PRODUCT_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + strPartNumber + " VARCHAR(255),"
                + strDesc + " VARCHAR(255),"
                + strCategory + " VARCHAR(255),"
                + Vat + " VARCHAR(255),"
                + ProductID + " VARCHAR(255),"
                + strCompanyName + " VARCHAR(255),"
                + Cost + " VARCHAR(255),"
                + selectedCustomerCode + " VARCHAR(255));";
        private static final String DROP_PRODUCT_TABLE = "DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME;

        /**
         *  Deals Header table
         */
        private static final String DEALS_HEADERS_TABLE_NAME = "DEALS_HEADERS";
        private static final String transactionID = "transactionId";
        private static final String customerCode = "customerCode";
        private static final String dateFrom = "dateFrom";
        private static final String dateTo = "dateTo";
        private static final String userId = "userId";
        private static final String isCompleted = "isCompleted";
        private static final String isUploaded = "isUploaded";
        private static final String productName = "productName";
        private static final String productPrice = "productPrice";
        private static final String productCode = "productCode";

        //Creating the table:
        private static final String CREATE_DEALS_HEADERS_TABLE = "CREATE TABLE " + DEALS_HEADERS_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + transactionID + " VARCHAR(255),"
                + customerCode + " VARCHAR(255),"
                + dateFrom + " VARCHAR(255),"
                + dateTo + " VARCHAR(255),"
                + userId + " VARCHAR(255),"
                + isCompleted + " INTEGER,"
                + isUploaded + " INTEGER,"
                + productName + " VARCHAR(255),"
                + productPrice + " VARCHAR(255),"
                + productCode + " VARCHAR(255));";

        private static final String DROP_DEALS_HEADERS_TABLE = "DROP TABLE IF EXISTS " + DEALS_HEADERS_TABLE_NAME;


        private static final String DEALS_LINES_TABLE_NAME = "DEALS_LINES";
        private static final String itemCode = "itemCode";
        private static final String itemPrice = "itemPrice";
        private static final String transactionIdInLines = "transactionId";

        //Creating the table:
        private static final String CREATE_DEALS_LINES_TABLE = "CREATE TABLE " + DEALS_LINES_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + itemCode + " VARCHAR(255),"
                + itemPrice + " VARCHAR(255),"
                + transactionIdInLines + " VARCHAR(255));";

        private static final String DROP_DEALS_LINES_TABLE = "DROP TABLE IF EXISTS " + DEALS_LINES_TABLE_NAME;


        private static final String SIGNATURE_TABLE_NAME = "SIGNATURE_TABLE";
        private static final String SIGNATURE = "signature";
        private static final String transactionIdInSignature = "transactionId";

        //Creating the table:
        private static final String CREATE_SIGNATURE_TABLE = "CREATE TABLE " + SIGNATURE_TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SIGNATURE + " VARCHAR(255),"
                + transactionIdInSignature + " VARCHAR(255));";

        private static final String DROP_SIGNATURE_TABLE = "DROP TABLE IF EXISTS " + SIGNATURE_TABLE_NAME;



        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUMBER);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table:
            try {
                db.execSQL(CREATE_PRODUCT_TABLE);
                db.execSQL(CREATE_DEALS_HEADERS_TABLE);
                db.execSQL(CREATE_DEALS_LINES_TABLE);
                db.execSQL(CREATE_SIGNATURE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_PRODUCT_TABLE);
                db.execSQL(DROP_DEALS_HEADERS_TABLE);
                db.execSQL(DROP_DEALS_LINES_TABLE);
                db.execSQL(DROP_SIGNATURE_TABLE);
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
