package thegenuinegourav.savebuy;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BalanceActivity extends Activity {

    private TextView CurrentBalance;
    private int totalAmount,leftAmount;
    private String Description,amount,TickerText,NotificationTitleText,NotificationDescriptionText;
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        CurrentBalance = (TextView)findViewById(R.id.currentBalance);
        Description = "       Date                    Time            Amount\n";

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        totalAmount = sharedPreferences.getInt("TotalAmount",0);
        CurrentBalance.setText(String.valueOf(totalAmount));

    }

    public void AddMoney(View view) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("How much you saved today?");

        // Setting Dialog Message
        alertDialog.setMessage("Enter the amount");

        // Setting Icon to Dialog


        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        alertDialog.setView(input);


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                amount = input.getText().toString();

                //Putting data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();  //to edit the sharedPreference

                totalAmount = sharedPreferences.getInt("TotalAmount", 0) + Integer.parseInt(amount);

                //Adding values to sharedPreference
                editor.putInt("Amount", Integer.parseInt(amount));

                editor.putInt("TotalAmount", totalAmount);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd          HH:mm:ss");
                Date date = new Date();
                editor.putString("Date", dateFormat.format(date));

                if (sharedPreferences.getString("Description", " ").equals(" ")) {
                    Description = Description + dateFormat.format(date) + "            " + amount + "\n";
                } else {
                    Description = sharedPreferences.getString("Description", " ") + dateFormat.format(date) + "            " + amount + "\n";
                }
                editor.putString("Description", Description);

                //Commiting the changes in the sharedPreference
                editor.commit();

                // Write your code here to invoke YES event
                Toast.makeText(getApplicationContext(), "Added Sucessfully", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences2 = getSharedPreferences("MyWishes", Context.MODE_PRIVATE);
                int totalAmountInDataFile = sharedPreferences.getInt("TotalAmount", 0);
                int number = sharedPreferences2.getInt("Number", 0);

                for(int i=1;i<=number;i++)
                {
                    if(totalAmountInDataFile>=Integer.parseInt(sharedPreferences2.getString("Amount"+String.valueOf(i),"0")))
                    {
                        String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
                        TickerText = "Congratulation! Now you can buy "+sharedPreferences2.getString("Title"+String.valueOf(i),"NA");
                        NotificationTitleText = "Buy "+ sharedPreferences2.getString("Title"+String.valueOf(i),"NA");
                        NotificationDescriptionText = "Amount Required: " + sharedPreferences2.getString("Amount"+String.valueOf(i),"NA")+"rupess"
                                + newLine+"You have: " + String.valueOf(totalAmountInDataFile);
                        leftAmount =totalAmountInDataFile- Integer.parseInt(sharedPreferences2.getString("Amount"+String.valueOf(i),"0"));
                        BuildNewNotifiaction(i);

                    }
                }

                totalAmount = sharedPreferences.getInt("TotalAmount", 0);
                CurrentBalance.setText(String.valueOf(totalAmount));


            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void ViewHistory(View view) {
        startActivity(new Intent(this,ViewHistory.class));
    }

    public void BuildNewNotifiaction(int uniqueID)
    {
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker(TickerText);
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(NotificationTitleText);
        notification.setContentText(NotificationDescriptionText);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(alarmSound);
        //Vibration
        notification.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        //LED
        notification.setLights(Color.BLACK, 3000, 3000);

        Intent i = new Intent(this,BuyingOrNot.class);
        i.putExtra("Title", NotificationTitleText);
        i.putExtra("UniqueID", uniqueID);
        i.putExtra("LeftAmount", leftAmount);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager  =(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueID,notification.build());
    }

}
