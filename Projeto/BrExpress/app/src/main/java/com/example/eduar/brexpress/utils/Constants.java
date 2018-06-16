package com.example.eduar.brexpress.utils;

/**
 * Created by eduar on 21/05/2018.
 */

public class Constants {

    //server ip and port address
    public static String SERVER_URL = "http://192.168.1.7:8000/";

    public static String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\"" +
            ")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?" +
            "[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    //
    public static final String CONVERT_IMAGE_TO_BASE64_RECEIVER = "convertImageToBase64";
    public static final String BASE64_IMAGE = "base64Image";

    public static final String PRODUCT_SAVED_SUCCESSFULLY = "productSavedSuccessfully";
    public static final String PRODUCT_SAVED_ERROR = "productSavedError";

    public static final String PRODUCT_UPDATED_SUCCESSFULLY = "productUpdatedSuccessfully";
    public static final String PRODUCT_UPDATED_ERROR = "productUpdatedError";

    public static final String CLIENT_SAVED_SUCCESSFULLY = "clientSavedSuccessfully";
    public static final String CLIENT_SAVED_ERROR = "clientSavedError";

    public static final String CLIENT_UPDATED_SUCCESSFULLY = "clientUpdatedSuccessfully";
    public static final String CLIENT_UPDATED_ERROR = "clientUpdatedSavedError";

    public static final String WORKER_SAVED_SUCCESSFULLY = "workerSavedSuccessfully";
    public static final String WORKER_SAVED_ERROR = "workerSavedError";

    public static final String WORKER_UPDATED_SUCCESSFULLY = "workerUpdatedSuccessfully";
    public static final String WORKER_UPDATED_ERROR = "workerUpdatedSavedError";

    public static final String WORKER_DELETED_SUCCESSFULLY = "workerDeletedSuccessfully";
    public static final String WORKER_DELETED_ERROR = "workerDeletedSavedError";

    public static final String SHIPPING_SAVED_SUCCESSFULLY = "shippingSavedSuccessfully";
    public static final String SHIPPING_SAVED_ERROR = "shippingSavedError";

    public static final String SHIPPING_UPDATED_SUCCESSFULLY = "shippingUpdatedSuccessfully";
    public static final String SHIPPING_UPDATED_ERROR = "shippingUpdatedSavedError";

    public static final String SHIPPING_DELETED_SUCCESSFULLY = "shippingDeletedSuccessfully";
    public static final String SHIPPING_DELETED_ERROR = "shippingDeletedSavedError";

    public static final String LOGIN_SUCCESS = "loginSuccess";
    public static final String LOGIN_ERROR = "loginError";

    public static final String SHARED_PREFERENCES = "myPref";

    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";
    public static final String USER_TYPE = "userType";
    public static final String PRODUCT_ID = "productId";
    public static final String WORKER_ID = "workerId";
    public static final String SHIPPING_ID = "shippingId";
    public static final String IS_EDITING = "isEditing";

    public static final String DIALOG_ACTION_CLICK = "dialogActionClick";
    public static final String CONFIRMED_ACTION = "confirmedAction";
    public static final String DENIED_ACTION = "deniedAction";

    public static final String PRODUCT_DELETED_SUCCESS = "productDeletedSuccess";
    public static final String PRODUCT_DELETED_ERROR = "productDeletedError";

    public static final String PRODUCT_BOUGHT_SUCCESS = "productBoughtSuccess";
    public static final String PRODUCT_BOUGHT_ERROR = "productBoughtError";

    public static final String PRODUCT_LOADED_SUCCESS = "productLoadedSuccess";
    public static final String ALL_PRODUCTS_LOADED = "allProductsLoaded";
    public static final String PRODUCT_LOADED_ERROR = "productLoadedError";

    public static final String IMAGE_DOWNLOADED = "imageDownloaded";
    public static final String INPUTSTREAM_IMAGE = "inputstreamImage";
    public static final String PRODUCT_IMAGE_DOWNLOADING = "prodcutImageDownloading";
}
