package com.Lists_App.Listy;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Created by Osama Eldawebi
 * Last edited 2019-07-26
 */

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayList<String> items2;
    private ArrayList<String> items3;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayAdapter<String> itemsAdapter2;
    private ArrayAdapter<String> itemsAdapter3;
    private ListView lvItems;
    static int tabId = 1;
    private DatabaseHelper DB;
    private Button btnAdd;

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

        // Loop over items in database and bullet-add to the lists
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
                itemsAdapter = new CustomListAdapter(this, R.layout.custom_list, items);
                itemsAdapter2 = new CustomListAdapter(this, R.layout.custom_list, items2);
                itemsAdapter3 = new CustomListAdapter(this, R.layout.custom_list, items3);

            }
        }
        lvItems.setAdapter(itemsAdapter);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        btnAdd = (Button) findViewById(R.id.btnAddItem);

        AddDataFromItem();

        registerForContextMenu(lvItems);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_career:
                    tabId = 1;
                    lvItems.setAdapter(itemsAdapter);

                    return true;
                case R.id.navigation_personal:
                    tabId = 2;
                    lvItems.setAdapter(itemsAdapter2);

                    return true;
                case R.id.navigation_training:
                    tabId = 3;
                    lvItems.setAdapter(itemsAdapter3);

                    return true;
            }
            return false;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        getMenuInflater().inflate(R.menu.item_menu, menu);

    }

    /**
     * Shows menu with edit and delete options as well as the events they trigger when pressed.
     * Edit option: Edits stored items and registers the new text.
     * Bulleted items remain bulleted and non-bulleted remain non-bulleted.
     * @param item The item that has been pressed on by user
     * @return a boolean indicating that a menu item has been pressed
     */
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.item_edit:
                showEditItemDialog(this, info.position);
                return true;
            case R.id.item_delete:
                if (tabId == 1 && (info.position < items.size())) {
                    String hi = items.get(info.position).substring(0, 1);
                    if (items.get(info.position).substring(0, 2).equalsIgnoreCase("\u2022 ")) {
                        deleteData(items.remove(info.position).substring(2));
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                    } else {
                        deleteData(items.remove(info.position));
                        itemsAdapter.notifyDataSetChanged();
                    }
                } else if (tabId == 2 && (info.position < items2.size())) {
                    if (items2.get(info.position).substring(0, 2).equalsIgnoreCase("\u2022 ")) {
                        deleteData(items2.remove(info.position).substring(2));
                        itemsAdapter2.notifyDataSetChanged();
                    } else {
                        deleteData(items2.remove(info.position));
                        itemsAdapter2.notifyDataSetChanged();
                    }
                } else {
                    if (info.position < items3.size()) {
                        if (items3.get(info.position).substring(0, 2).equalsIgnoreCase("\u2022 ")) {
                            deleteData(items3.remove(info.position).substring(2));
                            itemsAdapter3.notifyDataSetChanged();
                        } else {
                            deleteData(items3.remove(info.position));
                            itemsAdapter3.notifyDataSetChanged();
                        }
                    }
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Shows an alert dialog with an EditText field containing the item to be edited. The user is
     * given an option to either cancel or apply changes to selected item. The changes are made
     * to the stored data in the database.
     * @param c The context of the view adapter
     * @param position The position of the selected item in the view adapter
     */
    private void showEditItemDialog(Context c, final int position) {
        final EditText editItem = new EditText(c);
        final ArrayList<String> current;
        if (tabId == 1) {
            current = items;
        } else if (tabId == 2) {
            current = items2;
        } else {
            current = items3;
        }
        if (current.get(position).substring(0, 2).equalsIgnoreCase("\u2022 ")) {
            editItem.setText(current.get(position).substring(2));
        } else {
            editItem.setText(current.get(position));
        }
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(c, R.style.AlertDialogStyle))
                .setTitle("Edit Item")
                //.setMessage("Make changes to the selected item below.")
                .setView(editItem)
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bullet = "\u2022 ";
                        String newText = String.valueOf(editItem.getText());
                        String oldText = current.get(position);

                        Cursor data;
                        if(oldText.substring(0,2).equalsIgnoreCase(bullet)) {
                            //get the id associated with that name
                            data = DB.getItemID(oldText.substring(2));
                        }
                        else {
                            data = DB.getItemID(oldText);
                        }
                        int itemID = -1;
                        while (data.moveToNext()) {
                            itemID = data.getInt(0);
                        }
                        if (itemID > -1) {
                            if (oldText.substring(0, 2).equalsIgnoreCase(bullet)) {
                                DB.updateItem(oldText.substring(2), itemID, newText);
                                current.set(position, "\u2022 " + newText);
                            } else {
                                DB.updateItem(oldText, itemID, newText);
                                current.set(position, newText);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    /**
     * Adds data from the text field on the bottom through by clicking a button.
     */
    public void AddDataFromItem() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
                String itemText = etNewItem.getText().toString();
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

    /**
     * Adds data to the database.
     * @param newEntry The added item to be stored in the database.
     */
    public void AddData(String newEntry) {
        boolean insertData = DB.addData(newEntry);
        if (insertData) {
            Toast.makeText(this, "Item Successfully Added!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error Adding Item", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Deletes item from the database.
     * @param newEntry The item to be deleted from the database.
     */
    public void deleteData(String newEntry) {
        boolean removeData = DB.removeData(newEntry);
        if (removeData) {
            Toast.makeText(this, "Item Successfully Deleted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Error Deleting Item", Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Creates a swiping feature
     * Incomplete and not in use!!!
     */
    private void setupOnSwipe(AdapterView lvItems) {
        lvItems.setOnTouchListener(new OnSwipe(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }
        });
    }


}
