package shenaiting.map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class friends extends AppCompatActivity {

    private ListView listView;
    private friendsadapt afriendsadapter;
    private ImageView im7;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //布局相关
        listView=(ListView)findViewById(R.id.listView1);
        im7=(ImageView) findViewById(R.id.im7);
        btOnClickListener btOnClick=new btOnClickListener();
        im7.setOnClickListener(btOnClick);

        //数据库相关
        notesDB=new NotesDB(this);
        dbReader=notesDB.getReadableDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }

    class btOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent6=new Intent();
            intent6.setClass(friends.this,add.class);
            startActivity(intent6);
        }
    }

    public void selectDB(){
        Cursor cursor1=dbReader.query(NotesDB.TABLE_NAME,null,null,null,null,null,null);
        afriendsadapter=new friendsadapt(this,cursor1  );
        listView.setAdapter(afriendsadapter);
    }
}

