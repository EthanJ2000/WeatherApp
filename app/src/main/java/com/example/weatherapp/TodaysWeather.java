package com.example.weatherapp;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Common.Common;
import com.example.weatherapp.Model.Main;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.Model.WeatherResult;
import com.example.weatherapp.Retrofit.IOpenWeatherMap;
import com.example.weatherapp.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

public class TodaysWeather {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;




    public TodaysWeather() {

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

        getWeatherInformation();
    }

    public void getWeatherInformation(){
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<WeatherResult>() {
            @Override
            public void accept(WeatherResult weatherResult) throws Exception {
                String temp = new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString();
                String Location = weatherResult.getName();

                MainActivity.txtTemp.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                MainActivity.txtLocation.setText(weatherResult.getName());
                MainActivity.txtHumidity.setText(new StringBuilder("Humidity:"+String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                MainActivity.txtDesc.setText(String.valueOf(weatherResult.getWeather().get(0).getDescription()));



            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "accept: ",throwable);
            }
        }));
    }






}
