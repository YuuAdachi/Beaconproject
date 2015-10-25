package com.example.k13006kk.beaconproject;

import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

/**
 * Created by k13006kk on 2015/10/08.
 */
public class BeaconApplication extends /*Application,*/ Service implements BootstrapNotifier /*,BeaconConsumer */{

    String room_uuid;

    //public static final String TAG = BeaconApplication.class.getSimpleName();
    public static final String TAG = org.altbeacon.beacon.service.BeaconService.class.getSimpleName();


    // iBeaconのデータを認識するためのParserフォーマット
    public static final String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private RegionBootstrap regionBootstrap;

    private BeaconManager beaconManager;

    // iBeacon領域
    private Region region;

    @Override
    public void onCreate() {
        super.onCreate();

        // iBeaconのデータを受信できるようにParserを設定
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_FORMAT));
        // Beaconをスキャンする間隔
        beaconManager.setBackgroundBetweenScanPeriod(1000);

        // UUID, major, minorの指定はしない
        /*Region*/ region = new Region("uuid-region-bootstrap-001", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);


        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                // 検出したビーコンの情報を全部Logに書き出す
                for(Beacon beacon : beacons) {

                    DataHolder holder = DataHolder.getInstance();
                    room_uuid = beacon.getId1().toString();
                    holder.setTestString(room_uuid);

                    /*
                    Bundle bundle = new Bundle();
                    room_uuid = beacon.getId1().toString();
                    bundle.putString("UUID", room_uuid);

                    Fragment fragment = new Fragment();
                    fragment.setArguments(bundle);
                    */

                    /*
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction t = fm.beginTransaction();
                    HogeFragment fragment = new HogeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putExtra("test", "これはテストです。");
                    // フラグメントに渡す値をセット
                    fragment.setArguments(bundle);
                    t.add(R.id.hoge_fragment, fragment, "hoge_fragment");
                    t.commit();
                    */

                    /*
                    Intent intent=new Intent();
                    room_uuid = beacon.getId1().toString();
                    intent.putExtra("UUID", room_uuid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClassName("com.example.k13006kk.beaconproject","com.example.k13006kk.beaconproject.MainActivity");
                    startActivity(intent);
                    */


                    Log.d(TAG, "UUID:" + beacon.getId1() + ", major:" + beacon.getId2() + ", minor:" + beacon.getId3() + ", Distance:" + beacon.getDistance() + ",RSSI" + beacon.getRssi() + ", TxPower" + beacon.getTxPower());
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {

                   Intent intent=new Intent();
                   room_uuid = beacon.getId1().toString();
                   intent.putExtra("UUID", room_uuid);
                   startActivity(intent);

                    //Log.d("MyActivity",room_uuid);
                    Log.d("MyActivity", "UUID:" + beacon.getId1() + ", major:" + beacon.getId2() + ", minor:" + beacon.getId3() + ", Distance:" + beacon.getDistance());
                }
            }
        });
    }
*/
    @Override
    public void didEnterRegion(Region region) {
        // 領域に入場した
        Log.d(TAG, "Enter Region");
        /*Intent intent = new Intent(this, MainActivity.class);
        ///intent.putExtra("DATA1", data1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        */
        try {
            // レンジング開始
            beaconManager.startRangingBeaconsInRegion(region);
        } catch(RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        // 領域から退場した
        Log.d(TAG, "Exit Region");

        try {
            // レンジング停止
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch(RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        // 入退場状態が変更された
        Log.d(TAG, "Determine State: " + i);
    }

}
