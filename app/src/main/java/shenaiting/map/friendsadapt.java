package shenaiting.map;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/12.
 */
public class friendsadapt extends BaseAdapter {

    private Context context;
    private Cursor cursor1;
    public friendsadapt(Context context, Cursor cursor1){
        this.context=context;
        this.cursor1=cursor1;
    }
    @Override
    public int getCount() {
        return cursor1.getCount();
    }

    @Override
    public Object getItem(int i) {
        return cursor1.getPosition();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list,null);
        }
        TextView textView1=(TextView)view.findViewById(R.id.tv1);
        cursor1.moveToPosition(i);
        String content=cursor1.getString(cursor1.getColumnIndex("content"));
        textView1.setText(content);
        return view;
    }
}
