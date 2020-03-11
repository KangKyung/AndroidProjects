package com.example.momsee;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.momsee.Retrofit.INodeJS;
import com.example.momsee.Retrofit.RetrofitClient;

import java.io.ByteArrayInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CheckImage extends AppCompatActivity {
    String ss;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView stringText;
    String email;
    ImageView passedImage;
    Button btnSetImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_load);
        Retrofit retrofit1 = RetrofitClient.getInstance();
        email = getIntent().getStringExtra("email");
        myAPI = retrofit1.create(INodeJS.class);
        image_load(email);//파라미터에 이메일을 넣어야됨;
        btnSetImage = findViewById(R.id.btnSetImage);
        stringText = findViewById(R.id.stringText);

        btnSetImage.setOnClickListener(v -> {
            String data = ss;
            //데이터 base64 형식으로 Decode
            byte[] bytePlainOrg = Base64.decode(data, 0);

            //byte[] 데이터  stream 데이터로 변환 후 bitmapFactory로 이미지 생성
            ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
            Bitmap bm = BitmapFactory.decodeStream(inStream) ;
            passedImage.setImageBitmap(bm);
        });
    }

    private void image_load(String email){
        try {
            compositeDisposable.add(myAPI.image_load(email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        passedImage = findViewById(R.id.passedImage);
                        ss = s; // 스트링 값 받아서 ss에 저장함
                        ss = ss.replaceAll("\\\\n", "");
                        stringText.setText(ss);
                    })
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
