package com.example.lg.dbex;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText groupName,groupCnt,resultName,resultCnt;
    Button init,insert,select,update,delete;
    MyDBHelper myhelper;
    SQLiteDatabase sqlDb;
    public void list(){
        sqlDb=myhelper.getReadableDatabase();
        String sql="select * from idolTable";
        Cursor cursor =sqlDb.rawQuery(sql,null);
        String names="Idol 이름"+"\r\n"+"================="+"\r\n";
        String counts="Idol 인원수"+"\r\n"+"================="+"\r\n";
        while(cursor.moveToNext()){ //데이터 행의 갯수만큼 반복
            names += cursor.getString(0)+"\r\n";
            counts += cursor.getInt(1)+"\r\n";; //getString도 가능하지만 나중에 연산이 필요한 경우가 있을 수 있기 때문에 getInt 사용
        }
        resultName.setText(names);
        resultCnt.setText(counts);
        cursor.close();
    }
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
        update=(Button)findViewById(R.id.but_update);
        delete=(Button)findViewById(R.id.but_delete);

        myhelper=new MyDBHelper(this);
        list();
        //기존의 테이블이 존재하면 삭제한 테이블을 새로 생성한다
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getWritableDatabase();
                myhelper.onUpgrade(sqlDb,1,2);
                sqlDb.close();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getWritableDatabase();
                String sql="insert into idolTable values('"+groupName.getText()+"',"+groupCnt.getText()+")";
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this,"저장됨",Toast.LENGTH_LONG).show();
                list();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getReadableDatabase();
                String sql="select * from idolTable";
                sqlDb.close();
                list();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getWritableDatabase();
                String sql="update idolTable set idolCount="+groupCnt.getText()+" where idolName='"+groupName.getText()+"'";
                sqlDb=myhelper.getReadableDatabase();
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this,"인원수가 수정됨",Toast.LENGTH_LONG).show();
                list();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqlDb=myhelper.getWritableDatabase();
                String sql="delete from idolTable where idolName='"+groupName.getText()+"'";
                sqlDb=myhelper.getReadableDatabase();
                sqlDb.execSQL(sql);
                sqlDb.close();
                Toast.makeText(MainActivity.this,"삭제됨",Toast.LENGTH_LONG).show();
                list();
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
            String sql="drop table if exists idolTable";
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
