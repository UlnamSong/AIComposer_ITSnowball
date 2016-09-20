package com.itsnowball.aicomposer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

public class KakaoTalkSendActivity extends AppCompatActivity {

    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_talk_send);
        try {
            kakaoLink = KakaoLink.getKakaoLink(getApplicationContext());
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText("[인공지능 작곡가]\n인공지능이 만들어주는 노래!\n궁금하시지 않으신가요?\n지금 바로 앱을 설치해보세요!");
            kakaoTalkLinkMessageBuilder.addAppLink("앱으로 이동",
                    new AppActionBuilder()
                            .setAndroidExecuteURLParam("target=main")
                            .setIOSExecuteURLParam("target=main", AppActionBuilder.DEVICE_TYPE.PHONE).build());
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            finish();
        } catch (KakaoParameterException e) {
            Log.e("error",e.getMessage());
        }
    }
}
