package com.example.one;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private ArrayList<String> items;
    private ArrayList<String> items2;
    private ArrayList<String> items3;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayAdapter<String> itemsAdapter2;
    private ArrayAdapter<String> itemsAdapter3;
    private ListView lvItems;
    static int tabId = 1;

    DatabaseHelper DB;
    SQLiteDatabase db;
    Button btnAdd;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_career:
                    tabId = 1;
                    //mTextMessage.setText(R.string.title_home);
                    lvItems.setAdapter(itemsAdapter);

                    return true;
                case R.id.navigation_personal:
                    tabId = 2;
                    // mTextMessage.setText(R.string.title_dashboard);
                    lvItems.setAdapter(itemsAdapter2);

                    return true;
                case R.id.navigation_training:
                    tabId = 3;
                    // mTextMessage.setText(R.string.title_notifications);
                    lvItems.setAdapter(itemsAdapter3);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_title_bar);

        lvItems = (ListView) findViewById(R.id.lvItems);

        items = new ArrayList<String>();
        items2 = new ArrayList<String>();
        items3 = new ArrayList<String>();

        DB = new DatabaseHelper(this);

        Cursor data = DB.getListContents();

        if (data.getCount() > 0) {
            while (data.moveToNext()) {
                if (data.getString(1) != null) {
                    items.add("\u2022 " + data.getString(1));
                }
                if (data.getString(2) != null) {
                    items2.add("\u2022 " + data.getString(2));
                }
                if (data.getString(3) != null) {
                    items3.add("\u2022 " + data.getString(3));
                }
                itemsAdapter = new CustomListAdapter(this , R.layout.custom_list , items);
                itemsAdapter2 = new CustomListAdapter(this , R.layout.custom_list , items2);
                itemsAdapter3 = new CustomListAdapter(this , R.layout.custom_list , items3);

            }
        }

        lvItems.setAdapter(itemsAdapter);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btnAdd = (Button) findViewById(R.id.btnAddItem);
        AddDataFromItem();

        setupListViewListener(lvItems);


    }

 /*   private void setupOnSwipe(AdapterView lvItems) {
        lvItems.setOnTouchListener(new OnSwipe(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }
        });
    }*/

    // Attaches a long click listener to the listview
    private void setupListViewListener(AdapterView lvItems) {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View item, int pos, long id) {

                        // Remove the item within array at position
                        if (tabId == 1 && (pos < items.size())) {
                            String hi = items.get(pos).substring(0,1);
                            if(items.get(pos).substring(0,2).equalsIgnoreCase("\u2022 ")) {
                                deleteData(items.remove(pos).substring(2));
                                // Refresh the adapter
                                itemsAdapter.notifyDataSetChanged();

                            }
                            else {
                                deleteData(items.remove(pos));
                                itemsAdapter.notifyDataSetChanged();
                            }
                        } else if (tabId == 2 && (pos < items2.size())) {
                            if(items2.get(pos).substring(0,2).equalsIgnoreCase("\u2022 ")) {
                                deleteData(items2.remove(pos).substring(2));
                                itemsAdapter2.notifyDataSetChanged();
                            }
                            else {
                                deleteData(items2.remove(pos));
                                itemsAdapter2.notifyDataSetChanged();
                            }
                        } else {
                            if(pos < items3.size()) {
                                if(items3.get(pos).substring(0,2).equalsIgnoreCase("\u2022 ")) {
                                    deleteData(items3.remove(pos).substring(2));
                                    itemsAdapter3.notifyDataSetChanged();
                                }
                                else {
                                    deleteData(items3.remove(pos));
                                    itemsAdapter3.notifyDataSetChanged();
                                }
                            }
                        }
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }

    public void AddDataFromItem() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                String itemText = etNewItem.getText().toString();
                // itemsAdapter.add(itemText);
                if (itemText.length() > 0) {
                    if (tabId == 1) {
                        items.add(itemText);
                        AddData(itemText);
                    } else if (tabId == 2) {
                        items2.add(itemText);
                        AddData(itemText);
                    } else {
                        items3.add(itemText);
                        AddData(itemText);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Cannot Add Empty Field", Toast.LENGTH_LONG).show();
                }
                etNewItem.setText("");
            }
        });

    }

    public void AddData(String newEntry) {

        boolean insertData = DB.addData(newEntry);

        if (insertData) {
            Toast.makeText(this, "Item Successfully Added!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error Adding Item", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteData(String newEntry) {

        boolean removeData = DB.removeData(newEntry);

        if (removeData) {
            Toast.makeText(this, "Item Successfully Deleted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error Deleting Item", Toast.LENGTH_LONG).show();
        }
    }


}
