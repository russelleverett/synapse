package byobgyn.com.synapse;

import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import byobgyn.com.synapse.managers.SynapseManager;
import byobgyn.com.synapse.managers.SynapseTaskListener;
import byobgyn.com.synapse.utils.JsonDoc;

public class SynapseApplication extends Application {
    final String SETTINGS_FILE = "com.byobgyn.edol.SETTINGS";
    final String USER_ID = "com.byobgyn.edol.CMDR_REF";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences settings = getApplicationContext().getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        String userId = settings.getString(USER_ID, null);
        boolean createUser = userId == null;
        if(createUser) {
            userId = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_ID, userId);
            editor.apply();
        }

        SynapseManager.init(userId);
        SynapseManager.login(new SynapseTaskListener() {
            @Override
            public void TaskComplete(int messageId, JsonDoc response) {
                if(messageId == SynapseManager.PLAYER_LOGIN) {
                    if(response.has("message"))
                        Toast.makeText(SynapseApplication.this, response.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
