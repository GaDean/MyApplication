package com.aolo.aoloest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    protected AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化FB SDK(一定要放最前面)
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i("test", "onCreate");

        callbackManager=CallbackManager.Factory.create();
        LoginButton loginButton=(LoginButton)findViewById(R.id.login_button);  //初始化login btn
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override //登入成功
            public void onSuccess(LoginResult loginResult) {
                Log.i("test", "登入成功");
                accessToken=loginResult.getAccessToken();
                Log.d("test", "access token get");
                //Graph api
                GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override //Grapf api response 回傳的資料
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("test", "Login name:"+object.optString("name"));
                        Log.i("test", "Login id:"+object.optString("id"));
                        Log.i("test", "Login link:"+object.optString("link"));
                    }
                });

                //
                Bundle paramater=new Bundle();
                paramater.putString("fields", "id,name,link");
                request.setParameters(paramater);
                request.executeAsync();

            }

            @Override //取消登入
            public void onCancel() {
                Log.i("test", "Cancel");
            }

            @Override //登入失敗
            public void onError(FacebookException error) {
                Log.e("test", error.toString());
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    public void nextPage(View v){
        Log.i("test", "HAHA");
        Intent intent=new Intent(this, showDetailActivity.class);
        startActivity(intent);
    }
}
