package com.example.computer.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.BitSet;

public class MainActivity extends AppCompatActivity {
    View view;
    Button btnRed,btnyellow,btnchartreuse,btncyan;


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ChangeTheme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.versionArray[i],
                    MyData.id_[i],
                    MyData.drawableArray[i]
            ));
        }

        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        btnRed = (Button) findViewById(R.id.btnred);
        btnyellow = (Button) findViewById(R.id.btnyellow);
        btnchartreuse = (Button) findViewById(R.id.btnchartreuse);
        btncyan = (Button) findViewById(R.id.btncyan);

        //set màng hình
        view = this.getWindow().getDecorView();


//        view.setBackgroundResource(R.color.red);
//        btnRed.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        btnRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                view.setBackgroundResource(R.color.red);
                ChangeTheme.changeToTheme(MainActivity.this, 1);
            }
        });
        btnyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.setBackgroundResource(R.color.yellow);
                ChangeTheme.changeToTheme(MainActivity.this, 2);
            }
        });
        btnchartreuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.setBackgroundResource(R.color.chartreuse);
                ChangeTheme.changeToTheme(MainActivity.this, 3);
            }
        });
        btncyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.setBackgroundResource(R.color.cyan);
                ChangeTheme.changeToTheme(MainActivity.this, 4);
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            Bitmap bmp = null;
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < MyData.nameArray.length; i++) {
                if (selectedName.equals(MyData.nameArray[i])) {
                    selectedItemId = MyData.id_[i];
//                    MyData.selected_id = MyData.id_[i];
                }
            }
//            removedItems.add(selectedItemId);
//            data.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
           Intent name_detail = new Intent(MainActivity.this, detail_Rcy.class);
//
            name_detail.putExtra("name_detail",MyData.nameArray[selectedItemId]);
            name_detail.putExtra("price_detail",MyData.versionArray[selectedItemId]);
//            ByteArrayOutputStream bs = new ByteArrayOutputStream();
//
//            bmp.compress(Bitmap.CompressFormat.JPEG,50,bs);
//            name_detail.putExtra("img_detail",bs.toByteArray());


            Toast.makeText(MainActivity.this,"Bạn vừa chọn sản phẩm :" + MyData.nameArray[selectedItemId], Toast.LENGTH_SHORT).show();
            startActivity(name_detail);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_item) {
            //check if any items to add
            if (removedItems.size() != 0) {
                addRemovedItemToList();

            } else {
                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
    private void addRemovedItemToList() {
        int addItemAtListPosition = 3;
        data.add(addItemAtListPosition, new DataModel(
                MyData.nameArray[removedItems.get(0)],
                MyData.versionArray[removedItems.get(0)],
                MyData.id_[removedItems.get(0)],
                MyData.drawableArray[removedItems.get(0)]
        ));
        adapter.notifyItemInserted(addItemAtListPosition);
        removedItems.remove(0);
    }
}
