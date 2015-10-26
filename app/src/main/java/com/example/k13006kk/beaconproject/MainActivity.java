package com.example.k13006kk.beaconproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.BeaconService;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    String room_uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, BeaconApplication.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnClick(View view){

      /*
        ImageView img = (ImageView) findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.motion);
        animation.setDuration( 1000 );
        animation.setFillAfter(true);   //終了後を保持
        animation.setFillEnabled(true);
        img.startAnimation(animation);
        */

        DataHolder holder = DataHolder.getInstance();
        room_uuid = holder.getTestString();

        /*
        Bundle bundle = getArguments();
        room_uuid = bundle.getString("UUID");
        */

        /*
        Intent intent = getIntent();
        room_uuid = intent.getStringExtra("UUID");
        */

        TextView tv = (TextView) findViewById(R.id.uuid);//テスト用
        tv.setText(room_uuid);//テスト用

        AsyncHttpClient client = new AsyncHttpClient(); //通信準備

        final RequestParams params = new RequestParams(); //リクエストパラメータ
        params.put("uuid", room_uuid); //送るパラメータ1
        String url = "http://192.168.100.211:808/beacon_server.php";

        //final String TAG = MainActivity.class.getSimpleName();

        client.get(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(/*View view, */int i, Header[] headers, byte[] bytes) {
                //InputStream input;
                try {

                    String json = new String(bytes);
                    System.out.println(json);
                    JSONObject jsonObject = new JSONObject(json);


                    System.out.println(jsonObject.getString("room_number")); // "fuga"
                    System.out.println(jsonObject.getString("name"));
                    TextView tv = (TextView) findViewById(R.id.room_id);//？を指定
                    tv.setText(jsonObject.getString("room_number"));//？を変更
                    TextView tv2 = (TextView) findViewById(R.id.room_name);//？を指定
                    tv2.setText(jsonObject.getString("name"));//？を変更


                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
                TextView tv = (TextView) findViewById(R.id.myTextView);//テスト用
                tv.setText("成功");//テスト用
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                TextView tv = (TextView) findViewById(R.id.myTextView);//
                tv.setText("失敗");//テスト用
            }


        });


    }
}
