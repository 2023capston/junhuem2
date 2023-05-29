package com.example.yolijoli;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Search extends AppCompatActivity {
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("COOKRCP01/row");

    private TextView searchMenuView;
    private TextView infoCar;
    private TextView infoEng;
    private TextView infoFat;
    private TextView infoNa;
    private TextView infoPro;
    private TextView Manual01;
    private TextView Manual02;
    private TextView Manual03;
    private ImageView menuImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchMenuView = (TextView) findViewById(R.id.searchmenuview);
        menuImage = (ImageView) findViewById(R.id.menuimage);
        infoCar= (TextView) findViewById(R.id.infocar);
        infoEng= (TextView) findViewById(R.id.infoeng);
        infoFat= (TextView) findViewById(R.id.infofat);
        infoNa= (TextView) findViewById(R.id.infona);
        infoPro= (TextView) findViewById(R.id.infopro);
        Manual01= (TextView) findViewById(R.id.menual01);
        Manual02= (TextView) findViewById(R.id.menual02);
        Manual03= (TextView) findViewById(R.id.menual03);



    }
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String menu_text = intent.getStringExtra("메뉴");
        //searchMenuView.setText(menu_text);
        conditionRef.orderByChild("RCP_NM").equalTo(menu_text).addValueEventListener(new ValueEventListener() {//조건문 찾기완료 조건은 재료 2 3개뽑아서 만들어야 할것 같음
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // searchMenuView.setText(((Map) snapshot.getValue()).get("rcp_nm").toString());
                for (DataSnapshot data : snapshot.getChildren()) {//조건에 맞게 불러온 데이터는 리스트 형태이기 때문에 바로 접근이 불가능
                    Map<String, Object> dataMap = (Map<String, Object>) data.getValue();//map형으로 키값 으로 받아줌
                    String rcp_nm = dataMap.get("RCP_NM").toString();
                    String info_car = dataMap.get("INFO_CAR").toString();
                    String info_eng = dataMap.get("INFO_ENG").toString();
                    String info_fat = dataMap.get("INFO_FAT").toString();
                    String info_na = dataMap.get("INFO_NA").toString();
                    String info_pro = dataMap.get("INFO_PRO").toString();
                    String manual01 = dataMap.get("MANUAL01").toString();
                    String manual02 = dataMap.get("MANUAL02").toString();
                    String manual03 = dataMap.get("MANUAL03").toString();
                    String imageUrl = dataMap.get("ATT_FILE_NO_MAIN").toString();
                    Log.w(TAG, "The read failed: " + imageUrl);
                    Glide.with(getApplicationContext()).load(imageUrl).into(menuImage);
                    searchMenuView.setText(rcp_nm);
                    infoCar.setText("탄수화물: "+info_car);
                    infoEng.setText("열량: "+info_eng);
                    infoFat.setText("지방: "+info_fat);
                    infoNa.setText("나트륨: "+info_na);
                    infoPro.setText("단백질: "+info_pro);
                    Manual01.setText(manual01);
                    Manual02.setText(manual02);
                    Manual03.setText(manual03);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "The read failed: " + error.getCode());
                Toast.makeText(getApplicationContext(), "그런 메뉴는 존재하지 않습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
