package com.example.applunch;


import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean validator;
    // ArrayAdapter<String> adaptery;
    AppAdapter adaptery;
    private Intent intent;
    ArrayList<String> names;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = (AppInfo)parent.getItemAtPosition(position);
                try {
                    intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(appInfo.info.packageName);
                    startActivity(intent);
                } catch (ActivityNotFoundException | NullPointerException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.features,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

            }
        });
    }


    private void refresh() {
        Load load = new Load();
        load.execute(PackageManager.GET_META_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Load load = new Load();
        load.execute(PackageManager.GET_META_DATA);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
       // Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();


        if(text.equals("Sort By Name"))
        {
            Toast.makeText(this,"Sortation was done when displaying the apps",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class Load extends AsyncTask<Integer, Integer, List<AppInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);

        }

        @Override
        protected List<AppInfo> doInBackground(Integer... integers) {
            List<AppInfo> apps = new ArrayList<>();
            PackageManager packageManager = getPackageManager();

            List<ApplicationInfo> infos = packageManager.getInstalledApplications(integers[0]);

            for (ApplicationInfo info : infos){
                if(validator && (info.flags & ApplicationInfo.FLAG_SYSTEM) == 1){
                    continue;
                }

                AppInfo app = new AppInfo();
                app.info = info;
                app.label = (String) info.loadLabel(packageManager);
                apps.add(app);

            }


            Collections.sort(apps,new ElementsComparator());

            return apps;
        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            // AppAdapter adapter = new AppAdapter(MainActivity.this,appInfos);
            adaptery = new AppAdapter(MainActivity.this,appInfos);
            listView.setAdapter(adaptery);
            names = adaptery.getNames();
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(listView,appInfos.size() + "loaded",Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search name or category");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("FILTERING WITH " + newText);
                adaptery.getFilter().filter(newText); // `adaptery` era null aici.
                return true;
            }
        });

        return true;

    }

    private class ElementsComparator implements Comparator<AppInfo> {
        @Override
        public int compare(AppInfo o1, AppInfo o2) {
            CharSequence first = o1.label;
            CharSequence second = o2.label;

            if(first == null){
                first = o1.info.packageName;
            }
            if (second == null){
                second = o2.info.packageName;
            }
            return Collator.getInstance().compare(first.toString(),second.toString());

        }
    }
}