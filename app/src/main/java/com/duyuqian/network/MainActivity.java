package com.duyuqian.network;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static int DEFAULT_VALUE = 0;
    Wrapper wrapper;
    SharedPreferences sharedPref;
    LocalDataSource database = new MyApplication().getLocalDataSource();
    OkHttpClient okHttpClient = new OkHttpClient();
    Gson gson = new Gson();

    @BindString(R.string.url)
    String URL;
    @BindString(R.string.counts)
    String countsOfLaunch;

    @OnClick({R.id.get_data, R.id.get_number})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.get_data:
                getDataFromURL(URL);
                break;
            case R.id.get_number:
                showToast(String.valueOf(getNumberLaunch()));
                break;
            default:
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateNumberLaunch(getNumberLaunch() + 1);
    }

    public void getDataFromURL(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String result = Objects.requireNonNull(response.body()).string();
                            wrapper = gson.fromJson(result, Wrapper.class);
                            updateDataBase();
                            runOnUiThread(() -> {
                                if (wrapper.getData().size() > 0) {
                                    showToast(wrapper.getData().get(0).getName());
                                }
                            });
                        }
                    }
                }
        );
    }

    public void showToast(String content) {
        Toast toast = Toast.makeText(this, content, Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public void updateDataBase() {
        List<Person> personList = database.personDao().getAll();
        if (personList.size() != wrapper.getData().size()) {
            for (Person person : wrapper.getData()) {
                database.personDao().insertAll(person);
            }
        }
    }

    public int getNumberLaunch() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(countsOfLaunch, DEFAULT_VALUE);
    }

    public void updateNumberLaunch(int numberOfLaunchApp) {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(countsOfLaunch, numberOfLaunchApp);
        editor.apply();
    }

}