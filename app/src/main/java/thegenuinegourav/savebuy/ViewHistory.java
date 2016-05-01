package thegenuinegourav.savebuy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHistory extends Activity {

    private TextView historyTextView;
    private String history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        historyTextView = (TextView)findViewById(R.id.history);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        history = sharedPreferences.getString("Description","ERROR");
        historyTextView.setText(history);
    }
}
