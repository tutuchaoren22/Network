package com.duyuqian.network;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static String URL = "https://twc-android-bootcamp.github.io/fake-data/data/default.json";
    public static int DEFAULT_VALUE = 0;
    OkHttpClient okHttpClient = new OkHttpClient();
    Toast toast;
    Request request;
    Wrapper wrapper;
    Gson gson = new Gson();
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateNumberLaunch(getNumberLaunch() + 1);
    }


    @OnClick({R.id.get_data, R.id.get_number})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.get_data:
                getDataFromURL(URL);
                break;
            case R.id.get_number:
                showToast(toast, String.valueOf(getNumberLaunch()));
                break;
            default:
        }
    }


    public void getDataFromURL(String url) {
        request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        showToast(toast, "failed");
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String result = response.body().string();
                            wrapper = gson.fromJson(result, Wrapper.class);
                            updateDataBase();
                            runOnUiThread(() -> {
                                if (wrapper.getData().size() > 0) {
                                    showToast(toast, wrapper.getData().get(0).getName());
                                }

                            });
                        }
                    }
                }
        );
    }

    public void showToast(Toast toast, String content) {
        if (toast == null) {
            toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public void updateDataBase() {
        LocalDataSource database = new MyApplication().getLocalDataSource();
        for (Person person : wrapper.getData()) {
            database.personDao().insertAll(person);
        }
    }

    public int getNumberLaunch() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int counts = sharedPref.getInt("counts", DEFAULT_VALUE);
        return counts;
    }

    public void updateNumberLaunch(int numberOfLaunchApp) {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("counts", numberOfLaunchApp);
        editor.commit();
    }

}