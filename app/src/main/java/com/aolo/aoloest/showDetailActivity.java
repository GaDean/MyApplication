package com.aolo.aoloest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

public class showDetailActivity extends AppCompatActivity {
    private EditText nameText;
    private ShareButton shareButton;
    private ShareOpenGraphContent content;
    private ShareOpenGraphObject object;
    private ShareOpenGraphAction action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        nameText=(EditText)findViewById(R.id.nameText);
        shareButton=(ShareButton)findViewById(R.id.share);
        facebookShare();
        shareButton.setShareContent(content);


        //法一: 分享連結(網址)->按鈕那邊設空值
 /*       ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://www.strava.com/activities/428503233?fb_action_ids=10206820301161134&fb_action_types=fitness.bikes&fb_source=other_multiline&action_object_map=%5B922007814514813%5D&action_type_map=%5B%22fitness.bikes%22%5D&action_ref_map=%5B%5D"))
                .build();
        shareButton=(ShareButton)findViewById(R.id.share);
        shareButton.setShareContent(content);  */



        //抓取FB好友(只會抓取有使用本app的朋友)
        AccessToken accessToken=AccessToken.getCurrentAccessToken();
        new GraphRequest(accessToken,
                "me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback(){
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // 處理回傳值
                        // 找朋友只會傳回有在使用此app的朋友
                        JSONObject object=response.getJSONObject();
                        Log.i("test", object.optString("data"));
                    }
                }).executeAsync();
    }


    void facebookShare(){
        //法二: 利用FB的格式
        //這邊設定要分享的資訊
        object=new ShareOpenGraphObject.Builder()
                .putString("og:type", "fitness.bikes")
                .putString("og:title", "Share")
                .putString("og:description", nameText.getText().toString())
                .build();
        Log.i("test" , "object build");

        //fitness.bikes
        action = new ShareOpenGraphAction.Builder()
                .setActionType("fitness.bikes")
                .putObject("fitness", object)
                .build();

        Log.i("test" , "action build");

        content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("fitness")
                .setAction(action)
                .build();

        Log.i("test" , "content build");

    }

    public void share(View v){
        ShareDialog.show(this, content);



    }



}
