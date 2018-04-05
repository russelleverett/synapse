package byobgyn.com.synapse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import byobgyn.com.synapse.entities.Player;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread welcomeThread = new Thread() {
            public void run() {
                do {
                    try {
                        super.run();
                        sleep(5000);
                    }
                    catch(Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                while(Player.getInstance() == null);

                Intent i = new Intent(SplashActivity.this, PlayerSummaryActivity.class);
                startActivity(i);
                finish();
            }
        };
        welcomeThread.start();
    }
}
