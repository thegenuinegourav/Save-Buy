package thegenuinegourav.savebuy;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyingOrNot extends Activity {

    private TextView title,leftAmount;
    private int uniqueID;
    private String Description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_or_not);

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();

        title  =(TextView)findViewById(R.id.textView6);
        leftAmount = (TextView)findViewById(R.id.textView8);

        uniqueID = getIntent().getExtras().getInt("UniqueID");

        title.setText(getIntent().getExtras().getString("Title"));
        leftAmount.setText(String.valueOf(getIntent().getExtras().getInt("LeftAmount")));
    }

    public void No(View view) {
        startActivity(new Intent(this,HomeActivity.class));
    }

    public void Yes(View view) {
        SharedPreferences sharedPreferences2 = getSharedPreferences("MyWishes", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        SharedPreferences.Editor editor2=sharedPreferences2.edit();
        int totalAmountInDataFile = sharedPreferences.getInt("TotalAmount", 0);

        editor.putInt("TotalAmount", Integer.parseInt(leftAmount.getText().toString()));
        editor.putInt("Amount", Integer.parseInt(sharedPreferences2.getString("Amount" + String.valueOf(uniqueID), "NA")));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd          HH:mm:ss");
        Date date = new Date();
        editor.putString("Date", dateFormat.format(date));
        Description = sharedPreferences.getString("Description"," ") + dateFormat.format(date)+"            -"+sharedPreferences2.getString("Amount"+String.valueOf(uniqueID),"NA")+"\n";
        editor.putString("Description",Description);
        editor.commit();
        int number = sharedPreferences2.getInt("Number", 0);

        for(int i=uniqueID;i<=number;i++)
        {
            //Adding values to sharedPreference
            editor2.putInt("Number", number-1);

            editor2.putString("Title" + String.valueOf(i),sharedPreferences2.getString("Title" + String.valueOf(i+1), "NA"));
            editor2.putString("Description" + String.valueOf(i), sharedPreferences2.getString("Description" + String.valueOf(i+1), "NA"));
            editor2.putString("Amount" + String.valueOf(i), sharedPreferences2.getString("Amount" + String.valueOf(i+1), "NA"));

            editor2.commit();
        }

        editor2.remove("Title" + String.valueOf(number)).commit();
        editor2.remove("Description" + String.valueOf(number)).commit();
        editor2.remove("Amount" + String.valueOf(number)).commit();

        editor2.apply();

        Toast.makeText(this,"GO Grab it now",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, HomeActivity.class));
    }
}
