package com.example.yolijoli;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UmProfile extends AppCompatActivity {
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("COOKRCP01/row");
    private EditText idText;
    private View myMenu;
    private ImageButton signUpBtn;
    private ListView listview = null;
    private UmProfile.ListViewAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umprofile);
        idText = (EditText) findViewById(R.id.idtext);
        signUpBtn =(ImageButton) findViewById(R.id.signupbtn);
        myMenu = (View) findViewById(R.id.mymenu);
        listview = (ListView) findViewById(R.id.listView);
        idText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
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
                return false;
            }
        });
        conditionRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<Map<String, Object>> sortedDataList = new ArrayList<>();
                adapter = null;
                adapter = new UmProfile.ListViewAdapter();

                for (DataSnapshot data : task.getResult().getChildren()) {

                    Map<String, Object> dataMap = (Map<String, Object>) data.getValue();




                        sortedDataList.add(dataMap);



                }


                List<Map<String, Object>> distinctList = sortedDataList.stream()
                        .distinct()
                        .collect(Collectors.toList());
                Collections.shuffle(distinctList);
                int i=0;
                for (Map<String, Object> dataMap : distinctList) {
                    if(i==4){
                        break;
                    }
                    String rcp_nm = dataMap.get("RCP_NM").toString();
                    String att_file_no_main = dataMap.get("ATT_FILE_NO_MAIN").toString();
                    String rcp_parts_dtls = dataMap.get("RCP_PARTS_DTLS").toString();
                    //int Priority = (int) dataMap.get("Priority");

                    adapter.addItem(new ListItem(null, rcp_nm, rcp_parts_dtls, att_file_no_main));

                    i++;

                }
                listview.setAdapter(adapter);

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
    //ActivityResultLauncher 안드로이드 갤러리 카메라 관련기능
    protected void onStart() {
        super.onStart();
        myMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UmProfile.this,MainActivity.class));
                finish();

            }
        });


    }


    @Override//다음은 뒤로가기 버튼 이벤트로써 종료창을 보여줍니다다
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if(keycode ==KeyEvent.KEYCODE_BACK) {
            onClickShowAlert();
        }

        return false;
    }
    public void onClickShowAlert() {// 알림창
        AlertDialog.Builder myAlertBuilder =
                new AlertDialog.Builder(UmProfile.this);
        // alert의 title과 Messege 세팅
        myAlertBuilder.setTitle("Alert");
        myAlertBuilder.setMessage("Click OK to continue, or Cancel to stop:");
        // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
        myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                // OK 버튼을 눌렸을 경우

                moveTaskToBack(true);

                finish();

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        myAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancle 버튼을 눌렸을 경우
                Toast.makeText(getApplicationContext(),"Pressed Cancle",
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
        myAlertBuilder.show();
    }
}
