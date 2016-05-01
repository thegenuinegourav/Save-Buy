package thegenuinegourav.savebuy;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;


public class PurchasesActivty extends Activity {

    private EditText titleEditText,descriptionEditText,amountEditText;
    private ListView listView;
    private ArrayList<String> numbers;
    private String title,description,amount,TickerText,NotificationTitleText,NotificationDescriptionText;
    private int number,totalAmount,leftAmount;
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases_activty);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);

        listView = (ListView)findViewById(R.id.list);
        numbers = new ArrayList<String>();

        SharedPreferences sharedPreferences = getSharedPreferences("MyWishes", Context.MODE_PRIVATE);
        number = sharedPreferences.getInt("Number", 0);
        if(number!=0)
        {
            for(int i=1;i<=number;i++)
            {
                numbers.add(sharedPreferences.getString("Title"+String.valueOf(i),"NA") + "." +
                sharedPreferences.getString("Description"+String.valueOf(i),"NA") + "." +
                sharedPreferences.getString("Amount"+String.valueOf(i),"NA"));
            }
            listView.setAdapter(new CustomAdapter(PurchasesActivty.this, numbers));
        }


    }

    public void NewWish(View view) {
        LayoutInflater inflater = LayoutInflater.from(PurchasesActivty.this);
        View subView = inflater.inflate(R.layout.new_wish_alertdialogbox, null);
        titleEditText = (EditText)subView.findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)subView.findViewById(R.id.descriptionEditText);
        amountEditText = (EditText)subView.findViewById(R.id.amountEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                amount = amountEditText.getText().toString();
                title = titleEditText.getText().toString();
                description = descriptionEditText.getText().toString();

                //Putting data into SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyWishes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();  //to edit the sharedPreference

                number = sharedPreferences.getInt("Number", 0) + 1;

                //Adding values to sharedPreference
                editor.putInt("Number", number);

                editor.putString("Title" + String.valueOf(number), title);
                editor.putString("Description" + String.valueOf(number), description);
                editor.putString("Amount" + String.valueOf(number), amount);

                editor.commit();

                number = sharedPreferences.getInt("Number", 0);


                numbers.add(sharedPreferences.getString("Title" + String.valueOf(number), "NA") + "." +
                        sharedPreferences.getString("Description" + String.valueOf(number), "NA") + "." +
                        sharedPreferences.getString("Amount" + String.valueOf(number), "NA"));
                listView.setAdapter(new CustomAdapter(PurchasesActivty.this, numbers));


                SharedPreferences sharedPreferences2 = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                int totalAmountInDataFile = sharedPreferences2.getInt("TotalAmount", 0);
                int number = sharedPreferences.getInt("Number", 0);

                for(int i=1;i<=number;i++)
                {
                    if(totalAmountInDataFile>=Integer.parseInt(sharedPreferences.getString("Amount"+String.valueOf(i),"0")))
                    {
                        String newLine = System.getProperty("line.separator");//This will retrieve line separator dependent on OS.
                        TickerText = "Congratulation! Now you can buy "+sharedPreferences.getString("Title"+String.valueOf(i),"NA");
                        NotificationTitleText = "Buy "+ sharedPreferences.getString("Title"+String.valueOf(i),"NA");
                        NotificationDescriptionText = "Amount Required: " + sharedPreferences.getString("Amount"+String.valueOf(i),"NA")+"rupess"
                                + newLine+"You have: " + String.valueOf(totalAmountInDataFile);
                        leftAmount =totalAmountInDataFile- Integer.parseInt(sharedPreferences.getString("Amount"+String.valueOf(i),"0"));
                        BuildNewNotifiaction(i);

                    }
                }



            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
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
        notificationManager.notify(uniqueID, notification.build());
    }

}
