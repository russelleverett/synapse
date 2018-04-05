package byobgyn.com.synapse.managers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import byobgyn.com.synapse.utils.JsonDoc;

class SynapseRunnable implements Runnable {
    private SynapseTask mTask;
    private int mCompleteState;

    SynapseRunnable(SynapseTask task, int completeState) {
        mTask = task;
        mCompleteState = completeState;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection conn = mTask.configureConnection();

            if(mTask.getRequestBody() != null) {
                byte[] request = mTask.getRequestBody().getBytes();

                conn.setDoOutput(true);
                conn.addRequestProperty("Content-Length", String.valueOf(request.length));

                DataOutputStream requestStream = new DataOutputStream(conn.getOutputStream());
                requestStream.write(request);
                requestStream.flush();
                requestStream.close();
            }

            try {
                InputStream responseStream = conn.getInputStream();

                StringBuilder sb = new StringBuilder();
                Reader reader = new BufferedReader(new InputStreamReader(responseStream, Charset.forName("UTF-8")));
                int c;
                while ((c = reader.read()) != -1)
                    sb.append((char) c);

                String rawResponse = sb.toString();
                responseStream.close();

                mTask.setResponse(JsonDoc.parse(rawResponse));
                mTask.handleState(mCompleteState);
            }
            catch(Exception exc) {
                mTask.setErrorMessage(String.format("%s: %s (%s)", conn.getResponseCode(), conn.getResponseMessage(), exc.getMessage()));
                mTask.handleState(-1);
            }
        }
        catch(Exception exc) {
            mTask.setErrorMessage(exc.toString());
            mTask.handleState(-1);
        }
    }
}
