package com.example.photo.demo_photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectSizeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mSelect1;
    private LinearLayout mSelect2;
    private LinearLayout mSelect3;
    private LinearLayout mSelect4;
    private LinearLayout mSelect5;
    private ImageView back_iv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);

        initView();
    }

    private void initView(){
        mSelect1 = (LinearLayout) findViewById(R.id.select_size_item1);
        mSelect2 = (LinearLayout) findViewById(R.id.select_size_item2);
        mSelect3 = (LinearLayout) findViewById(R.id.select_size_item3);
        mSelect4 = (LinearLayout) findViewById(R.id.select_size_item4);
        mSelect5 = (LinearLayout) findViewById(R.id.select_size_item5);
        back_iv = (ImageView) findViewById(R.id.back_iv);

        mSelect1.setOnClickListener(this);
        mSelect2.setOnClickListener(this);
        mSelect3.setOnClickListener(this);
        mSelect4.setOnClickListener(this);
        mSelect5.setOnClickListener(this);
        back_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_size_item1:

                Intent intent1 = new Intent(SelectSizeActivity.this,EditImageActivity.class);
                intent1.putExtra("type",1);
                startActivity(intent1);
                SelectSizeActivity.this.finish();
                break;

            case R.id.select_size_item2:
                Intent intent2 = new Intent(SelectSizeActivity.this,EditImageActivity.class);
                intent2.putExtra("type",2);
                startActivity(intent2);
                SelectSizeActivity.this.finish();
                break;

            case R.id.select_size_item3:
                Intent intent3 = new Intent(SelectSizeActivity.this,EditImageActivity.class);
                intent3.putExtra("type",3);
                startActivity(intent3);
                SelectSizeActivity.this.finish();
                break;

            case R.id.select_size_item4:
                Intent intent4 = new Intent(SelectSizeActivity.this,EditImageActivity.class);
                intent4.putExtra("type",4);
                startActivity(intent4);
                SelectSizeActivity.this.finish();
                break;

            case R.id.select_size_item5:
                Intent intent5 = new Intent(SelectSizeActivity.this,EditImageActivity.class);
                intent5.putExtra("type",5);
                startActivity(intent5);
                SelectSizeActivity.this.finish();
                break;

            case R.id.back_iv:
                SelectSizeActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
