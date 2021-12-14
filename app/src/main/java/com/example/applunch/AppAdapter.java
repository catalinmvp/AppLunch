package com.example.applunch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppAdapter extends ArrayAdapter {

    LayoutInflater inflater;
    PackageManager packageManager;
    List<AppInfo> apps;
    ArrayList<String> names = new ArrayList<String>();

    List<AppInfo> filteredApps;

    public AppAdapter(@NonNull Context context, List<AppInfo> apps) {
        super(context, R.layout.item_layout,apps);
        inflater = LayoutInflater.from(context);
        packageManager = context.getPackageManager();
        this.apps = apps;
        this.filteredApps = apps;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AppInfo currentInfo = filteredApps.get(position);
        View view = convertView;

        if(view == null){
            view = inflater.inflate(R.layout.item_layout,parent,false);

        }

        TextView Title = (TextView) view.findViewById(R.id.title1);
        Title.setText(currentInfo.label);
        names.add(currentInfo.label);
        // names.add(Title.getText().toString());
        // setNames(names);

        ImageView imageView = (ImageView) view.findViewById(R.id.iconimg);
        Drawable background = currentInfo.info.loadIcon(packageManager);
        imageView.setBackgroundDrawable(background);

        return view;
    }

    @Override
    public int getCount() {
        return filteredApps.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return filteredApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String wantedPrefix = charSequence.toString().toLowerCase(Locale.ROOT);

                List<AppInfo> goodApps = new ArrayList<>();
                for (AppInfo info : apps) {
                    if (info.label.toLowerCase(Locale.ROOT).startsWith(wantedPrefix)) {
                        goodApps.add(info);
                    }
                }

                FilterResults res = new FilterResults();
                res.count = goodApps.size();
                res.values = goodApps;
                return res;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredApps = (List<AppInfo>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
