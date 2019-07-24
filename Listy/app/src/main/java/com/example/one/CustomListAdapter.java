package com.example.one;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter <String> {

    private Context mContext;
    private int id;
    private List<String> items ;

    public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }



        if (position % 2 == 1) {
            mView.setBackgroundColor(Color.WHITE);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mView.setBackgroundColor(Color.rgb(252, 252, 248));
        }


        TextView text = (TextView) mView.findViewById(R.id.textView);

        if((items.get(position) != null))
        {
            text.setText(items.get(position));

        }

        return mView;
    }

}
