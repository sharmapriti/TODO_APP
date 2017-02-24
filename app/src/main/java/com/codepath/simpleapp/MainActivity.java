package com.codepath.simpleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int RESULT_EDIT_TEXT = 100;
    public static final int RESULT_Add_TEXT = 200;
    //List view to hold Task and Date Value
    ArrayList<String> items;
    ArrayList<String> dateItems;

    ArrayAdapter<String> itemsAdapter;
    ArrayAdapter<String> dateitemsAdapter;
    ListView lvItems;
    ListView lvItems1;
    File filesDir = null;
    File todoFile = null;
    File todoFileDate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filesDir = getFilesDir();
        File fileList[] = filesDir.listFiles();
        if (fileList.length ==0) {
            todoFile = new File(filesDir, "Task.txt");
            todoFileDate = new File(filesDir, "date.txt");
        }


        lvItems = (ListView) findViewById(R.id.lvToDoList);
        lvItems1 = (ListView) findViewById(R.id.lvDueDate);

        dateItems = new ArrayList<String>();
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        dateitemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dateItems);
        lvItems1.setAdapter(dateitemsAdapter);
        lvItems.setAdapter(itemsAdapter);
        readItems();

        setupListViewListener();

        lvItems.setOnItemClickListener(onListClick);
        lvItems1.setOnItemClickListener(onListClick);



    }

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos1, long id) {
            Intent intent = new Intent(MainActivity.this,EditItemActivity.class);
            intent.putExtra ("ListItem",lvItems.getItemAtPosition(pos1).toString());
            intent.putExtra ("ListItemPos",pos1);
            intent.putExtra ("ItemDate",lvItems1.getItemAtPosition(pos1).toString());
            startActivityForResult(intent, RESULT_EDIT_TEXT);
        }

    };

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        dateItems.remove(pos);
                        dateitemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;

                    }

                }
        );

        lvItems1.setOnItemLongClickListener(
                new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        dateItems.remove(pos);
                        dateitemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;

                    }

                }
        );
    }

    public void onAddItem(View v){

        String quickText = ( (EditText) findViewById(R.id.quicktText)).getText().toString();
        if(!quickText.isEmpty()) {
            items.add(quickText);

            itemsAdapter.notifyDataSetChanged();
            dateItems.add("");
            dateitemsAdapter.notifyDataSetChanged();
            ((EditText) findViewById(R.id.quicktText)).setText("");
            writeItems();
        } else {

            Intent intent = new Intent(v.getContext(), AddTask.class);
            startActivityForResult(intent, RESULT_Add_TEXT);
        }
    }

    //Method to read data from file
    private void readItems() {

        try {
        BufferedReader buff = new BufferedReader(new FileReader(filesDir + "/Task.txt"));
        String line;
            while ((line = buff.readLine()) != null) {
                items.add(line);
                itemsAdapter.notifyDataSetChanged();
            }
        buff.close();
        buff = new BufferedReader(new FileReader(filesDir + "/date.txt"));
        while ((line = buff.readLine()) != null) {
            dateItems.add(line);
            dateitemsAdapter.notifyDataSetChanged();
        }
        buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to write data into file
    private void writeItems(){

        if (todoFile == null)
        {
            todoFile = new File(filesDir, "Task.txt");
        }
        if (todoFileDate == null)
        {
            todoFileDate = new File(filesDir, "date.txt");
        }
        try{
            FileUtils.writeLines(todoFile, items);
            FileUtils.writeLines(todoFileDate, dateItems);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == RESULT_Add_TEXT ){
            if(resultCode == RESULT_OK) {
                String edtext = data.getExtras().getString("Task");
                String dueDate = data.getExtras().getString("DueDate");
                items.add(edtext);
                itemsAdapter.notifyDataSetChanged();
                dateItems.add(dueDate);
                dateitemsAdapter.notifyDataSetChanged();
                writeItems();

            }

        }

        if (requestCode == RESULT_EDIT_TEXT ) {
            if (resultCode == RESULT_OK) {
                    String edtext = data.getExtras().getString("Task");
                    String dueDate = data.getExtras().getString("DueDate");
                    int pos11 = data.getExtras().getInt("EditPos");

                    items.remove(pos11);
                    itemsAdapter.notifyDataSetChanged();
                    items.add(pos11, edtext);
                    itemsAdapter.notifyDataSetChanged();
                    dateItems.remove(pos11);
                    dateitemsAdapter.notifyDataSetChanged();
                    dateItems.add(pos11, dueDate);
                    dateitemsAdapter.notifyDataSetChanged();
                    writeItems();

            }
        }

        super.onActivityResult(requestCode,resultCode,data);
    }
}
