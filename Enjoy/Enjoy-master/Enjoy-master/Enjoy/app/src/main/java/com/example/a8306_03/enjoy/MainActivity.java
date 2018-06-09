package com.example.a8306_03.enjoy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터 준비
        ArrayList<Url> data = new ArrayList<>();
        data.add(new Url("사진","오늘은","https://www.google.com"));
        data.add(new Url("사진","내일은","https://www.naver.com"));
        data.add(new Url("사진","모래는","https://www.daum.net"));
        data.add(new Url("사진","글피는","https://www.swu.ac.kr"));
        data.add(new Url("사진","어제는","https://www.youtube.com"));

        //어댑터
        AdapterUrl adapter = new AdapterUrl(data);

        //뷰
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //클릭
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,i+" 번째 아이템 선택",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void moveMypage(View view){
        startActivity(new Intent(this, EnjoyMypage.class));
    }

    //버튼
    /*public void mOnPopupClick(View v) {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, EnjoyMemo.class);
        startActivity(intent);
        intent.putExtra("data", "Test");
        startActivityForResult(intent, 1);
    }*/

    public void mOnPlusClick(View v) {
        //메모 클래스 호출
        Intent intent = new Intent(this, EnjoyMemo.class);
        startActivity(intent);
    }

    public void mOnSearchClick(View v) {
        //검색 페이지 호출
        Intent intent = new Intent(this, EnjoySearch.class);
        startActivity(intent);
    }

    /**@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // 현재 사용중인 menu 에 우리가 작성한 xml 파일을 등록하기

        return true;
    }
}
