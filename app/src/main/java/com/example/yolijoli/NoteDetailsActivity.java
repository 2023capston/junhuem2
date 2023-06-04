package com.example.yolijoli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText,locEditText,pnumEditText,sauceEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId,loc,pnum,sauce;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        locEditText = findViewById(R.id.notes_loc_text);
        pnumEditText = findViewById(R.id.notes_pnum_text);
        sauceEditText = findViewById(R.id.notes_sauce_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn  = findViewById(R.id.delete_note_text_view_btn);

        //receive data
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        loc = getIntent().getStringExtra("email");
        pnum = getIntent().getStringExtra("pnum");
        sauce = getIntent().getStringExtra("sauce");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        locEditText.setText(loc); //++
        pnumEditText.setText(pnum); //++
        sauceEditText.setText(sauce);
        if(isEditMode){
            pageTitleTextView.setText("Edit");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener( (v)-> saveNote());

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

    }

    void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        String noteLoc = locEditText.getText().toString(); //++
        String notePnum = pnumEditText.getText().toString(); //++
        String noteSauce = sauceEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("empty");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setLoc(noteLoc);
        note.setPnum(notePnum);
        note.setSauce(noteSauce);

        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note){
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
                    Utility.showToast(NoteDetailsActivity.this,"회원정보 추가 완료");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"오류 : 정보 추가 실패");
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
                    Utility.showToast(NoteDetailsActivity.this,"삭제 완료");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"오류 : 삭제 실패");
                }
            }
        });
    }


}