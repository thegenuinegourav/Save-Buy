package thegenuinegourav.savebuy;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {

    private String[] Title={};
    //private String[] HeadingNames={"Dog1","Dog2","Dog3","Dog4","Dog5","Dog6","Dog7","Dog8"};
    private String[] Description={"Active","Not Active","Active","Active","Active","Not Active","Not Active","Not Active"};
    CustomAdapter(Context context,ArrayList<String> Head)
    {
        super(context,R.layout.custom_list_view,Head);
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.custom_list_view, parent, false);
        TextView title;
        TextView description;
        Button amount;
        title = (TextView)view.findViewById(R.id.title);
        description = (TextView)view.findViewById(R.id.description);
        amount = (Button)view.findViewById(R.id.money);

        String[] splited = getItem(position).split("\\.");
        title.setText(splited[0]);
        description.setText(splited[1]);
        amount.setText(splited[2]);

        return view;
    }
}