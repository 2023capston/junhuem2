package com.example.yolijoli;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SearchList extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("COOKRCP01/row");

    private EditText idText;
    private ImageButton recipeClick;

    private ListView listview = null;
    private ListViewAdapter adapter = null;



// 데이터 로딩 시작 전에 ProgressDialog를 설정

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);

// 데이터 로딩 시작 시에 ProgressDialog를 표시


        listview = (ListView) findViewById(R.id.listView);
        idText = (EditText) findViewById(R.id.idtext);
        recipeClick =(ImageButton) findViewById(R.id.recipeclick);
        idText.setText("로딩중...");
        idText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:

                }
                return false;
            }
        });
        recipeClick.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v){
                String[] items = idText.getText().toString().split(",");
                Intent intent = new Intent(getApplicationContext(), SearchList.class);
                List<String> itemList = Arrays.asList(items);
                for (int i = 0; i < 7; i++) {
                    String ingredient = "";
                    if (i < itemList.size()) {
                        ingredient = itemList.get(i).trim();
                    }
                    intent.putExtra("재료" + (i + 1), ingredient);
                }

                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String ingred1 =  intent.getStringExtra("재료1");
        String ingred2 = intent.getStringExtra("재료2");
        String ingred3 = intent.getStringExtra("재료3");
        String ingred4 = intent.getStringExtra("재료4");
        String ingred5 = intent.getStringExtra("재료5");
        String ingred6 = intent.getStringExtra("재료6");
        String ingred7 = intent.getStringExtra("재료7");


        conditionRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Map<String, Object>> sortedDataList = new ArrayList<>();
                adapter = null;
                adapter = new ListViewAdapter();

                for (DataSnapshot data : task.getResult().getChildren()) {

                    Map<String, Object> dataMap = (Map<String, Object>) data.getValue();
                    String rcp_parts_dtls = dataMap.get("RCP_PARTS_DTLS").toString();
                    if (rcp_parts_dtls.contains(ingred1) && rcp_parts_dtls.contains(ingred2) && rcp_parts_dtls.contains(ingred3) && rcp_parts_dtls.contains(ingred4) && rcp_parts_dtls.contains(ingred5) && rcp_parts_dtls.contains(ingred6) && rcp_parts_dtls.contains(ingred7)) {



                        String rcp_nm = dataMap.get("RCP_NM").toString();

                        int Pri = 0;
                        if (!ingred1.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred1, index)) != -1) {
                                count++;
                                index += ingred1.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred1)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred2.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred2, index)) != -1) {
                                count++;
                                index += ingred2.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred2)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred3.isEmpty()) {//재료가 있다면?
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred3, index)) != -1) {
                                count++;
                                index += ingred3.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred3)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred4.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred4, index)) != -1) {
                                count++;
                                index += ingred4.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred4)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred5.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred5, index)) != -1) {
                                count++;
                                index += ingred5.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred5)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred6.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred6, index)) != -1) {
                                count++;
                                index += ingred6.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred6)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        if (!ingred7.isEmpty()) {
                            int count = 0;
                            int index = 0;
                            while ((index = rcp_parts_dtls.indexOf(ingred7, index)) != -1) {
                                count++;
                                index += ingred7.length();
                            }
                            Pri = Pri + count;//재료가 레시피에 등장하는 만큼 우선순위점수++
                            if (rcp_nm.contains(ingred7)) {//제목에 재료가 포함된다면 우선순위 점수++
                                Pri = Pri + 2;
                            }
                        }
                        dataMap.put("Priority", Pri);
                        sortedDataList.add(dataMap);


                }


                }
                List<Map<String, Object>> sortedData = sortedDataList.stream()
                        .sorted(Comparator.comparingInt(data -> (int) ((Map<String, Object>) data).get("Priority")).reversed())
                        .collect(Collectors.toList());

                List<Map<String, Object>> distinctList = sortedData.stream()
                        .distinct()
                        .collect(Collectors.toList());
                for (Map<String, Object> dataMap : distinctList) {
                    String rcp_nm = dataMap.get("RCP_NM").toString();
                    String att_file_no_main = dataMap.get("ATT_FILE_NO_MAIN").toString();
                    String rcp_parts_dtls = dataMap.get("RCP_PARTS_DTLS").toString();
                    int Priority = (int) dataMap.get("Priority");
                    adapter.addItem(new ListItem(null, rcp_nm, rcp_parts_dtls, att_file_no_main));
                    listview.setAdapter(adapter);


                }

            }
        });

    }
    public class ListViewAdapter extends BaseAdapter {
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        @Override
        public int getCount() {
            return items.size();
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }
        public void addItem(ListItem item) {
            items.add(item);
        }
        public void getCot(ListItem item) {
            items.add(item);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            final Context context = viewGroup.getContext();
            final ListItem listItem = items.get(position);
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_list_item, viewGroup, false);
            } else {
                View view = new View(context);
                view = (View) convertView;
            }
            ImageView menuImage = (ImageView) convertView.findViewById(R.id.menuimage);
            TextView nm = (TextView) convertView.findViewById(R.id.rcp_nm);
            Glide.with(getApplicationContext()).load(listItem.getAtt_file_no_main()).into(menuImage);
            nm.setText(listItem.getRcp_nm());
            if (position == getCount() - 1) {
                // 리스트뷰의 마지막 아이템인 경우
                idText.setText("로딩끝!");
            }
            //각 아이템 선택 이벤트
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Search.class);
                    String menu = nm.getText().toString();
                    intent.putExtra("메뉴", menu); //'메뉴'라는 이름으로 menu전달
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    protected void onStart() {
        super.onStart();

    }
}
