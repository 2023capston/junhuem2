package com.example.yolijoli;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class coDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveCoBtn;
    TextView pageTitleTextView;
    String coTitle,coContent,docId;
    boolean isEditMode = false;
    TextView deleteCoTextViewBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_details);

        titleEditText = findViewById(R.id.co_title_text);
        contentEditText = findViewById(R.id.co_content_text);
        saveCoBtn = findViewById(R.id.save_co_btn);
        pageTitleTextView = findViewById(R.id.co_title);
        deleteCoTextViewBtn  = findViewById(R.id.delete_co_text_view_btn);

        //receive data
        coTitle = getIntent().getStringExtra("coTitle");
        coContent= getIntent().getStringExtra("coContent");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(coTitle);
        contentEditText.setText(coContent);
        if(isEditMode){
            pageTitleTextView.setText("Edit");
            deleteCoTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveCoBtn.setOnClickListener( (v)-> saveNote());

        deleteCoTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

    }


    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("empty");
            return;
        }
        coInfo coinfo = new coInfo();
        coinfo.setCoTitle(noteTitle);
        coinfo.setCoContent(noteContent);
        coinfo.setTimest(Timestamp.now());

        saveNoteToFirebase(coinfo);

    }

    void saveNoteToFirebase(coInfo note){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(coDetailsActivity.this,"회원정보 추가 완료");
                    finish();
                }else{
                    Utility.showToast(coDetailsActivity.this,"오류 : 정보 추가 실패");
                }
            }
        });

    }

    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(coDetailsActivity.this,"삭제 완료");
                    finish();
                }else{
                    Utility.showToast(coDetailsActivity.this,"오류 : 삭제 실패");
                }
            }
        });
    }
}