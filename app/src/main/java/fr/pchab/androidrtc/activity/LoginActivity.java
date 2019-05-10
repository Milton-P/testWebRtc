package fr.pchab.androidrtc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import fr.pchab.androidrtc.R;

public class LoginActivity extends Activity {
    private EditText mUserNameEditText;
    private EditText mPassWordEditText;
    private String  mUserName;
    private String  mPassWord;
    private HashMap<String, String> mHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button mButton = findViewById(R.id.login);
        mUserNameEditText = findViewById(R.id.user_name);
        mPassWordEditText = findViewById(R.id.passWord);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mUserNameEditText.getText().toString();
                mPassWord = mPassWordEditText.getText().toString();
                // todo 发起登录请求
                if (mUserName.equals("master")) {
                    Intent intent = new Intent(LoginActivity.this,MasterActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginActivity.this,TraineeActivity.class);
                    startActivity(intent);
                }
                //login();

            }
        });
    }

    public void login() {
        mHashMap.put("username", mUserName);
        mHashMap.put("password", mPassWord);

        new LoginTask().execute(mHashMap);
    }

    private String requestPost(HashMap<String, String> paramsMap) {
        String result = "";
        String baseUrl = "http:";
        try {
            String date = new JSONObject(paramsMap).toString();
            android.util.Log.d("milton"," date = " + date);
            URL url = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(5 * 1000);
            urlConn.setReadTimeout(5 * 1000);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.connect();
            PrintWriter dos = new PrintWriter(urlConn.getOutputStream());
            dos.write(date);
            dos.flush();
            dos.close();
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result  = streamToString(urlConn.getInputStream());
            }
            urlConn.disconnect();
        } catch (Exception e) {
        }
        return result;
    }

    public String streamToString(InputStream is) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }
            is.close();
            reader.close();
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    class LoginTask extends AsyncTask<HashMap, Integer, String> {

        @Override
        protected String doInBackground(HashMap... hashMaps) {
            return requestPost(hashMaps[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject loginState = new JSONObject(s);
                //JSONObject loginState = object.getJSONObject("LoginState");
                boolean isMaster = loginState.getBoolean("isMaster");
                boolean valid = loginState.getBoolean("valid");
                if (valid) {
                    if (isMaster) {
                        Intent intent = new Intent(LoginActivity.this,MasterActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this,TraineeActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "密码或用户名错误", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
