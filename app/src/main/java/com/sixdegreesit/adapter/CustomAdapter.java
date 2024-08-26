package com.sixdegreesit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.db.DesignationData;
import com.sixdegreesit.securityapp.R;

import java.util.List;



public class CustomAdapter extends BaseAdapter {
    Context context;
   /* int flags[];
    String[] countryNames;*/
    LayoutInflater inflter;

    List<String> DesignationName;
   

    public CustomAdapter(Context applicationContext, List<String> DesignationName) {
        this.context = applicationContext;
        this.DesignationName = DesignationName;
       
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return DesignationName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
    
        TextView names = (TextView) view.findViewById(R.id.textView);



        //icon.setImageResource(flags[i]);
        names.setText(DesignationName.get(i));


     

        return view;
    }
}
