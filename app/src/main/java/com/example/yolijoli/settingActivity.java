package com.example.yolijoli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class settingActivity extends AppCompatActivity {

    Switch takeInfo;
    TextView logout, yourActivity, signout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        logout = findViewById(R.id.logout_profile_setting);
        signout = findViewById(R.id.signout_setting);
        takeInfo = findViewById(R.id.takeYourInfo_setting);
        editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(settingActivity.this,LoginActivity.class));
                finish();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(settingActivity.this, LoginActivity.class));
                firebaseAuth.getCurrentUser().delete();
                Utility.showToast(settingActivity.this,"회원탈퇴 완료");
            }
        });

        takeInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    editor.putString("checked","yes");
                    editor.apply();
                    takeInfo.setChecked(true);
                    Utility.showToast(settingActivity.this,"개인정보 이용에 동의했습니다");
                }
                else{
                    editor.putString("checked","false");
                    editor.apply();
                    takeInfo.setChecked(false);
                    Utility.showToast(settingActivity.this,"개인정보 이용 동의를 취소했습니다");
                }
            }
        });

        if(sharedPreferences.getString("checked","no").equals("yes")){
            takeInfo.setChecked(true);
        }
        else{
            takeInfo.setChecked(false);
        }

    }
}