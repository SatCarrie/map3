package shenaiting.map;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    //地图显示
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    //点击发送短信，回复短信，定位朋友位置
    private Button bt4;
    private Button bt5;
    private Button bt6;
    PendingIntent paIntent;
    SmsManager smsManager;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private String longitude;
    private String latitude;
    //跳转friends页面和enemy页面
    private ImageView im1;
    private ImageView im2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //地图显示
        initView();
        initLocation();

        //地图标记相关
        //定义Maker坐标点
        LatLng point = new LatLng(22.30,113.52);
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.biaoji3);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        baiduMap.addOverlay(option);

        //点击发送短信，回复短信，定位朋友位置
        bt4=(Button)findViewById(R.id.bt4);
        bt5=(Button)findViewById(R.id.bt5);
        bt6=(Button)findViewById(R.id.bt6);
        bt4OnClickListener bt4On=new bt4OnClickListener();
        bt5OnClickListener bt5On=new bt5OnClickListener();
        bt6OnClickListener bt6On=new bt6OnClickListener();
        bt4.setOnClickListener(bt4On);
        bt5.setOnClickListener(bt5On);
        bt6.setOnClickListener(bt6On);
        paIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
        smsManager = SmsManager.getDefault();
        notesDB=new NotesDB(this);
        dbReader=notesDB.getReadableDatabase();

        //跳转friends页面和enemy页面
        friendsonClickListener listen2=new friendsonClickListener();
        im2=(ImageView)findViewById(R.id.im2);
        im2.setOnClickListener(listen2);
    }

//activity生命周期函数
protected void onStart() {
    super.onStart();
    baiduMap.setMyLocationEnabled(true);
    if(!mLocationClient.isStarted()){
        mLocationClient.start();
    }
}
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        baiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

//地图标记相关
private void initLocation() {

    mLocationClient = new LocationClient(this);
    mLocationListener = new MyLocationListener();
    mLocationClient.registerLocationListener(mLocationListener);

    LocationClientOption option = new LocationClientOption();
    option.setCoorType("bd09ll");
    option.setIsNeedAddress(true);
    option.setOpenGps(true);
    option.setScanSpan(1000);
    mLocationClient.setLocOption(option);
}
    private void initView() {
        mapView= (MapView)findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {


            MyLocationData data = new MyLocationData.Builder()//
                    .accuracy(bdLocation.getRadius())//
                    .latitude(bdLocation.getLatitude())//
                    .longitude(bdLocation.getLongitude())//
                    .build();
            baiduMap.setMyLocationData(data);
            longitude= String.valueOf(bdLocation.getLongitude());
            latitude= String.valueOf(bdLocation.getLatitude());
            if(isFirstIn){
                LatLng latLng=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(msu);
                isFirstIn=false;

            }
        }
    }

//点击发送短信，回复短信，定位朋友位置
class bt4OnClickListener implements View.OnClickListener{

    @Override
    public void onClick(View view) {
        int i=0;
        String str1=null;
        for (i=0;i<2;i++) {
            Cursor cursor1 = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
            cursor1.moveToPosition(i);
            str1 = cursor1.getString(cursor1.getColumnIndex("content"));
            smsManager.sendTextMessage(str1, null, "这条短信是自动发送的", paIntent,
                    null);
        }
    }
}


    class bt5OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            sendSMS("");
        }
    }

    private void sendSMS(String number)
    {

        String smsBody=longitude+"/"+latitude;
        Uri smsToUri = Uri.parse("smsto:"+number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        startActivity(intent);
    }
    class bt6OnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {

        }
    }


//跳转friends页面和enemy页面
        class  friendsonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent2=new Intent();
            intent2.setClass(MainActivity.this,friends.class);
            startActivity(intent2);
        }
    }
}
