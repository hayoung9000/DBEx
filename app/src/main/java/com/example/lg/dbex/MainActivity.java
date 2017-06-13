package com.example.lg.dbex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText groupName,groupCnt,resultName,resultCnt;
    Button init,insert,select;
    MyDBHelper myhelper;
    SQLiteDatabase sqlDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        groupName=(EditText)findViewById(R.id.edit_group_name);
        groupCnt=(EditText)findViewById(R.id.edit_group_cnt);
        resultName=(EditText)findViewById(R.id.edit_result_name);
        resultCnt=(EditText)findViewById(R.id.edit_result_cnt);
        init=(Button)findViewById(R.id.but_init);
        insert=(Button)findViewById(R.id.but_insert);
        select=(Button)findViewById(R.id.but_select);

        myhelper=new MyDBHelper(this);
        //기존의 테이블이 존재하면 삭제학 테이블을 새로 생성한다
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getWritableDatabase();
                myhelper.onUpgrade(sqlDb,1,2);
                sqlDb.close();
            }
        });
    }
    class MyDBHelper extends SQLiteOpenHelper{
        // idolDB라는 이름의 데이터베이스가 생성된다
        public MyDBHelper(Context context) {
            super(context,"idolDB", null, 1);
        }
        // idolTable라는 이름의 테이블 생성
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql="create table idoltable(idolName text not null primary key, idolCount integer)";
                db.execSQL(sql);
        }
        //이미 idolTable이 존재한다면 기존의 테이블을 삭제하고 새로 테이블을 만들 때
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql="drop table f exist idolTable";
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
