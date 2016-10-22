package shenaiting.map;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add extends AppCompatActivity {

    private NotesDB notesDB;
    private SQLiteDatabase dbwriter;
    private EditText ed10;
    private Button bt2;
    private Button bt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ed10=(EditText)findViewById(R.id.ed10);
        bt2=(Button)findViewById(R.id.bt2);
        bt3=(Button)findViewById(R.id.bt3);
        bt2OnClickListener  bt2OnClick=new bt2OnClickListener();
        bt3OnClickListener  bt3OnClick=new bt3OnClickListener();
        bt2.setOnClickListener(bt2OnClick);
        bt3.setOnClickListener(bt3OnClick);

        notesDB=new NotesDB(this);
        dbwriter=notesDB.getWritableDatabase();

    }

    public void addDB(){
        ContentValues cv=new ContentValues();
        cv.put(NotesDB.CONTENT,ed10.getText().toString());
        cv.put(NotesDB.TIME,getTime());
        dbwriter.insert(NotesDB.TABLE_NAME,null,cv);
    }

    public String getTime(){
        /*SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        String str=format.format(curDate);*/
        String str="1";
        return str;
    }
    class bt2OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            addDB();
            finish();
        }
    }

    class bt3OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
