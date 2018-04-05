package byobgyn.com.synapse.managers;

import android.util.Base64;

import java.net.HttpURLConnection;
import java.net.URL;

import byobgyn.com.synapse.utils.JsonDoc;

class SynapseTask {
    private static SynapseManager sSynapseManager;

    private Runnable mSynapseRunnable;
    private String mMethod;
    private String mUrl;
    private String mRequestBody;
    private String mErrorMessage;
    private JsonDoc mResponse;
    private SynapseTaskListener mListener;

    Runnable getSynapseRunnable() { return mSynapseRunnable; }

    JsonDoc getResponse() {
        return mResponse;
    }

    void setResponse(JsonDoc mResponse) {
        this.mResponse = mResponse;
    }

    String getErrorMessage() { return mErrorMessage; }

    void setErrorMessage(String errorMessage) { this.mErrorMessage = errorMessage; }

    String getRequestBody() { return mRequestBody; }

    void setRequestBody(String request) { mRequestBody = request; }

    SynapseTaskListener getTaskListener() {
        return mListener;
    }

    HttpURLConnection configureConnection() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(mUrl).openConnection();
            conn.setConnectTimeout(30000);
            conn.setDoInput(true);
            conn.setRequestMethod(mMethod);
            conn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String token = sSynapseManager.getUserId() + ":";
            byte[] encoded = Base64.encode(token.getBytes(), Base64.NO_WRAP);
            String auth = String.format("Basic %s", new String(encoded));
            conn.addRequestProperty("Authorization", auth);

            return conn;
        }
        catch(Exception exc){
            return null;
        }
    }

    SynapseTask(SynapseTaskListener listener, String method, String url, int completeState) {
        mListener = listener;
        mMethod = method;
        mUrl = url;

        mSynapseRunnable = new SynapseRunnable(this, completeState);
        sSynapseManager = SynapseManager.getInstance();
    }

    void handleState(int state)  {
        sSynapseManager.handleState(this, state);
    }
}
