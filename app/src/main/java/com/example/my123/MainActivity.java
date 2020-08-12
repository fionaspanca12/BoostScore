package com.example.my123;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    String baseUrl;
    MenuItem menuItemcsgo,menuItemdota2,menuItemlol;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    ArrayList<String> pageTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDatabase = this.openOrCreateDatabase("pageTitle",MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles (title VARCHAR)");
        sqLiteDatabase.execSQL("INSERT INTO articles (title) VALUES ( 'All Games')");
        sqLiteDatabase.execSQL("INSERT INTO articles (title) VALUES ( 'Live')");
        sqLiteDatabase.execSQL("INSERT INTO articles (title) VALUES ( 'Finished')");
        Cursor c = sqLiteDatabase.rawQuery("SELECT title FROM articles", null);

        int titleIndex = c.getColumnIndex("title");
        c.moveToFirst();
        int count = c.getCount();

        while (count != 0){

            try{
                Log.i("title", c.getString(titleIndex));
                c.moveToNext();
                count--;
                pageTitle.add(c.getString(titleIndex));
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }


        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
}
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItemcsgo=menu.findItem(R.id.csgo_item);
        menuItemdota2=menu.findItem(R.id.dota2_item);
        menuItemlol=menu.findItem(R.id.lol_item);
        if (getFromSP("cec1")){
            menuItemcsgo.setChecked(true);
            baseUrl="https://api.pandascore.co/csgo/";
            viewpager();
        }

        if (getFromSP("cec2")){
            menuItemlol.setChecked(true);
            baseUrl="https://api.pandascore.co/lol/";
            viewpager();
        }

        if (getFromSP("cec3")){
            menuItemdota2.setChecked(true);
            baseUrl= "https://api.pandascore.co/dota2/";
            viewpager();
        }

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.csgo_item) {
            item.setChecked(true);
            baseUrl= "https://api.pandascore.co/csgo/";
            saveInSp("cec1","cec2","cec3",true,false,false);
            finish();

            startActivity(getIntent());
            return true;
        }
        if (id == R.id.lol_item) {
            item.setChecked(true);
            baseUrl="https://api.pandascore.co/lol/";
            saveInSp("cec1","cec2","cec3",false,true,false);
            finish();

            startActivity(getIntent());
            return true;
        }
        if (id == R.id.dota2_item) {
            item.setChecked(true);
            baseUrl="https://api.pandascore.co/dota2/";

            saveInSp("cec1","cec2","cec3",false,false,true);
            finish();

            startActivity(getIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    private void saveInSp(String key,String key1,String key2,boolean value,boolean value1,boolean value2){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.putBoolean(key1, value1);
        editor.putBoolean(key2, value2);
        editor.commit();
    }

    public String getBaseUrl(){
        return this.baseUrl;
    }

    public void viewpager(){
        tabLayout.removeAllTabs();
        for (int i = 0; i < 3; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle.get(i)));
        }
        //set viewpager adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //change ViewPager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
