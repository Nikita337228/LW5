package com.example.lw1;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class hello extends AppCompatActivity{
    ArrayAdapter<String> mTextAdapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    MyDatabase db = new MyDatabase(this);
    String loginUser;
    String passUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloact);
        EditText mEditText = findViewById(R.id.TextEdit);
        Button AddButton = findViewById(R.id.AddButton);
        Button DelButton = findViewById(R.id.DeleteButton);
        //Button DelProfile = findViewById(R.id.DelUserButton);
        ListView listView = findViewById(R.id.ViewList);
        ArrayList<String> mArrList = new ArrayList<String>();
        ArrayList<String> RemList = new ArrayList<String>();
        mTextAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mArrList);
        listView.setAdapter(mTextAdapter);

        Bundle arg = getIntent().getExtras();
        if (arg != null) {
            String str = arg.get("hello").toString();
            loginUser = arg.get("login").toString();
            passUser = arg.get("pass").toString();
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
        }
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int i = 0;
        while (sharedPref.contains("row"+ Integer.toString(i))){
            mTextAdapter.add(sharedPref.getString("row"+ Integer.toString(i), ""));
            i++;
        }
        mTextAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tmp = mTextAdapter.getItem(i);
                if (listView.isItemChecked(i)){
                    view.setBackgroundColor(Color.YELLOW);
                    RemList.add(tmp);
                }
                else {
                    view.setBackgroundColor(0xFFFFFFF);
                    RemList.remove(tmp);
                }
            }
        });
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = String.valueOf(mEditText.getText());
                mTextAdapter.add(tmp);
                mEditText.setText("");
                mTextAdapter.notifyDataSetChanged();
            }
        });
        DelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < RemList.size(); i++){
                    mTextAdapter.remove(RemList.get(i));
                }
                RemList.clear();
                mTextAdapter.notifyDataSetChanged();
            }
        });
//        DelProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (db.deleteUser(new User(loginUser, passUser))){
//                    Toast.makeText(getApplicationContext(), "Профиль удален", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        editor = sharedPref.edit();
        editor.clear();
        for (int i = 0; i < mTextAdapter.getCount(); i++){
            editor.putString("row" + Integer.toString(i), mTextAdapter.getItem(i));
        }
        editor.apply();
    }
}
