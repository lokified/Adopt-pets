package com.loki.yourpet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PetListAdapter extends ArrayAdapter<String> {

    private Activity mContext;
    private  String[] mName;
    private  String[] mLocation;
    private  Integer[] mImgId;

    public PetListAdapter(Activity context, String[] name, String[] location, Integer[] imgId) {
        super(context,R.layout.adopt_pet_list,name);
        this.mContext = context;
        this.mName = name;
        this.mLocation = location;
        this.mImgId = imgId;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater =  mContext.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.adopt_pet_list, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        titleText.setText(mName[position]);
        imageView.setImageResource(mImgId[position]);
        subtitleText.setText("location: "+ mLocation[position]);

        return rowView;

    };
}
