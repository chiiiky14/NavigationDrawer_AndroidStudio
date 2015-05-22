package com.example.root.drawernavigation.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.root.drawernavigation.R;
import com.example.root.drawernavigation.fragment.Fragment1;
import com.example.root.drawernavigation.fragment.Fragment2;
import com.example.root.drawernavigation.fragment.Fragment3; 


public class MainActivity extends ActionBarActivity {

    int mPosition = -1;
    String mTitle = "";
    String idusuarioapp;
    // Array of strings storing country names
    String[] mCountries ;
    private SQLiteDatabase db;

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flagsactivo = new int[]{
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta,
            R.drawable.ic_alerta
    };


    // Array of strings to initial counts
    String[] mCount = new String[]{
            "", "", "", "", "","","", "", "","" };

    String[] mCount2 = new String[]{
            "", "", "", "", "","","", "", "" };

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawer ;
    private List<HashMap<String,String>> mList ;
    private SimpleAdapter mAdapter;
    final private String COUNTRY = "country";
    final private String FLAG = "flag";
    final private String COUNT = "count";
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting an array of country names


        // Title of the activity
        mTitle = (String)getTitle();

        // Getting a reference to the drawer listview
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        // Getting a reference to the sidebar drawer ( Title + ListView )
        mDrawer = ( LinearLayout) findViewById(R.id.drawer);

        // Each row in the list stores country name, count and flag
        mList = new ArrayList<HashMap<String,String>>();




        mCountries = getResources().getStringArray(R.array.drawer);
        for(int i=0;i<flagsactivo.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(COUNTRY, mCountries[i]);
            hm.put(COUNT, mCount[i]);
            hm.put(FLAG, Integer.toString(flagsactivo[i]));
            mList.add(hm);
        }




        // Keys used in Hashmap
        String[] from = { FLAG,COUNTRY,COUNT };

        // Ids of views in listview_layout
        int[] to = { R.id.flag , R.id.country };

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout, from, to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);



        // Creating a ToggleButton for NavigationDrawer with drawer event listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer , R.string.drawer_open,R.string.drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Select the option");
                supportInvalidateOptionsMenu();
            }
        };

        // Setting event listener for the drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        showFragment(0);
        // ItemClick event handler for the drawer items
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                // Increment hit count of the drawer list item
                incrementHitCount(position);


                showFragment(position);


                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawer);
            }
        });


        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting the adapter to the listView
        mDrawerList.setAdapter(mAdapter);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void incrementHitCount(int position){
        HashMap<String, String> item = mList.get(position);
        String count = item.get(COUNT);
        item.remove(COUNT);
        if(count.equals("")){
            count = "  1  ";
        }else{
            int cnt = Integer.parseInt(count.trim());
            cnt ++;
            count = "  " + cnt + "  ";
        }
        item.put(COUNT, count);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    public void showFragment(int position){

        //Currently selected country
        mTitle = mCountries[position];


        if (position==0) {
            Fragment fragment = new Fragment1();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if (position==1){
            Fragment fragment = new Fragment2();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }else if (position==2){
            Fragment fragment = new Fragment3();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }



    }

    // Highlight the selected country : 0 to 4
    public void highlightSelectedCountry(){
        int selectedItem = mDrawerList.getCheckedItemPosition();

        if(selectedItem > 8)
            mDrawerList.setItemChecked(mPosition, true);
        else
            mPosition = selectedItem;

        if(mPosition!=-1)
            getSupportActionBar().setTitle(mCountries[mPosition]);
    }



}
