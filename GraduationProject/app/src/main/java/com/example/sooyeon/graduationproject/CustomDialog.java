package com.example.sooyeon.graduationproject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sooyeon.graduationproject.tab.MemoFragement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomDialog extends Dialog implements View.OnClickListener{
    private static FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String selectedMemoKey;

    public String url;
    public Context context;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    static {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final TextView main_label) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.memo_add_dlg);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText edtUrl = (EditText) dlg.findViewById(R.id.edtUrl);
        final Button btnAccept = (Button) dlg.findViewById(R.id.btnAccept);
        final Button btnCancel = (Button) dlg.findViewById(R.id.btnCancel);

        //인스턴스를 얻어온다
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                url = edtUrl.getText().toString();
                Memo memo = new Memo(url);
                String mUrl = memo.title;
                mUrl = mUrl.replace("/", "");
                mUrl = mUrl.replace(".", "");
                mUrl = mUrl.replace(":", "");

                if(isUrl(url)){
//                    if(urlAdapter.hasDuplicate(memo)){
//                        Toast.makeText(context, "이미 등록된 주소입니다", Toast.LENGTH_SHORT).show();
//                    }
                    if(mFirebaseUser != null){
                        mFirebaseDatabase
                                .getReference()
                                .child(mFirebaseUser.getUid())
                                .child(mUrl)
                                .setValue(memo);

                        initMemo();
                        Toast.makeText(context, "등록되었습니다", Toast.LENGTH_SHORT).show();

                        dlg.dismiss();
                    }
                }else {
                    Toast.makeText(context, "올바른 url형식이 아닙니다", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

    public boolean isUrl(String url) {
        String urlRegex = "^(file|gopher|news|nntp|telnet|https?|ftps?|sftp)://([a-z0-9-]+.)+[a-z0-9]{2,4}.*$";
        return url.matches(urlRegex);
    }

    private void initMemo(){
        selectedMemoKey = null;
    }

    @Override
    public void onClick(View v) {

    }
}