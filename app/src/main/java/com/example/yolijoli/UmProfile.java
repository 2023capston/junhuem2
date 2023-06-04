package com.example.yolijoli;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class UmProfile extends AppCompatActivity {
    private EditText idText;
    private View myRecipe;
    private ImageButton signUpBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umprofile);
        idText = (EditText) findViewById(R.id.idtext);
        signUpBtn =(ImageButton) findViewById(R.id.signupbtn);
        myRecipe = (View) findViewById(R.id.myrecipe);
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

    }
    //ActivityResultLauncher 안드로이드 갤러리 카메라 관련기능
    protected void onStart() {
        super.onStart();
        myRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("com.example.yolijoli.START_MAINACTIVITY");//화면 넘기기
                startActivity(intent);//화면 넘기기

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
