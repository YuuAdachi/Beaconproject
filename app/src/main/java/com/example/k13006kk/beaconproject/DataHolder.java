package com.example.k13006kk.beaconproject;

/**
 * Created by k13006kk on 2015/10/25.
 */
public class DataHolder {

    static DataHolder _instance = null;

    /**
     * 常にこのメソッドから呼び出すようにする
     *
     *
     * @return
     */

    static public DataHolder getInstance(){
        if(_instance==null){
            _instance = new DataHolder();
        }
        return _instance;
    }

    String test ="A";

    public String getTestString(){
        return test;
    }

    public void setTestString(String txt){
        this.test = txt;
    }
}
