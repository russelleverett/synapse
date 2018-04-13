package byobgyn.com.synapse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import byobgyn.com.synapse.entities.Player;

public class PlayerSummaryActivity extends AppCompatActivity {
    private TextView txtCombatRank;
    private TextView txtCombatProgress;
    private ProgressBar pbCombatProgress;
    private TextView txtCommerceRank;
    private TextView txtCommerceProgress;
    private ProgressBar pbCommerceProgress;
    private TextView txtNotorietyRank;
    private TextView txtNotorietyProgress;
    private ProgressBar pbNotorietyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_summary);

        txtCombatRank = (TextView)findViewById(R.id.combat_rank);
        txtCombatProgress = (TextView)findViewById(R.id.combat_progress_text);
        pbCombatProgress = (ProgressBar)findViewById(R.id.combat_progress);
        txtCommerceRank = (TextView)findViewById(R.id.commerce_rank);
        txtCommerceProgress = (TextView)findViewById(R.id.commerce_progress_text);
        pbCommerceProgress = (ProgressBar)findViewById(R.id.commerce_progress);
        txtNotorietyRank = (TextView)findViewById(R.id.notoriety_rank);
        txtNotorietyProgress = (TextView)findViewById(R.id.notoriety_progress_text);
        pbNotorietyProgress = (ProgressBar)findViewById(R.id.notoriety_progress);

        Button btnShowBots = (Button)findViewById(R.id.btnShowBots);
        btnShowBots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerSummaryActivity.this, BotSelectActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Player player = Player.getInstance();
        txtCombatRank.setText(player.getCombatRank());
        txtCombatProgress.setText("Progress: " + player.getCombatProgress() + "%");
        pbCombatProgress.setProgress(player.getCombatProgress());

        txtCommerceRank.setText(player.getCommerceRank());
        txtCommerceProgress.setText("Progress: " + player.getCommerceProgress() + "%");
        pbCommerceProgress.setProgress(player.getCommerceProgress());

        txtNotorietyRank.setText(player.getNotorietyRank());
        txtNotorietyProgress.setText("Progress: " + player.getNotorietyProgress() + "%");
        pbNotorietyProgress.setProgress(player.getNotorietyProgress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ship_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent m_activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
