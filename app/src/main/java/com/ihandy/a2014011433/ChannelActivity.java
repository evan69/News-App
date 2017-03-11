package com.ihandy.a2014011433;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Evan Hou on 2016/9/6.
 */

public class ChannelActivity extends Activity implements View.OnClickListener {

    //private RelativeLayout mLayoutTitleBar;
    private TextView mTitleTextView;
    private Button mBackwardbButton;//backward
    private Button mForwardButton;//apply
    private TableLayout mContentLayout;

    private CheckBox sports;
    private CheckBox top_stories;
    private CheckBox technology;
    private CheckBox health;
    private CheckBox world;
    private CheckBox national;
    private CheckBox entertainment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();   //加载 activity_title 布局 ，并获取标题及两侧按钮
    }


    private void setupViews() {
        super.setContentView(R.layout.activity_channel);
        mTitleTextView = (TextView) findViewById(R.id.channel_text_title);
        mContentLayout = (TableLayout) findViewById(R.id.channel_layout_content);
        mBackwardbButton = (Button) findViewById(R.id.channel_button_backward);
        mForwardButton = (Button) findViewById(R.id.channel_button_forward);

        sports = (CheckBox)findViewById(R.id.box_sports); sports.setChecked(MainActivity.preferences.getBoolean("sports",true));
        top_stories = (CheckBox)findViewById(R.id.box_top_stories); top_stories.setChecked(MainActivity.preferences.getBoolean("top_stories",true));
        technology = (CheckBox)findViewById(R.id.box_technology); technology.setChecked(MainActivity.preferences.getBoolean("technology",true));
        health = (CheckBox)findViewById(R.id.box_health); health.setChecked(MainActivity.preferences.getBoolean("health",true));
        world = (CheckBox)findViewById(R.id.box_world); world.setChecked(MainActivity.preferences.getBoolean("world",true));
        national = (CheckBox)findViewById(R.id.box_national); national.setChecked(MainActivity.preferences.getBoolean("national",true));
        entertainment = (CheckBox)findViewById(R.id.box_entertainment); entertainment.setChecked(MainActivity.preferences.getBoolean("entertainment",true));
    }

    protected void showBackwardView(int backwardResid, boolean show) {
        if (mBackwardbButton != null) {
            if (show) {
                mBackwardbButton.setText(backwardResid);
                mBackwardbButton.setVisibility(View.VISIBLE);
            } else {
                mBackwardbButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    protected void showForwardView(int forwardResId, boolean show) {
        if (mForwardButton != null) {
            if (show) {
                mForwardButton.setVisibility(View.VISIBLE);
                mForwardButton.setText(forwardResId);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    protected void onBackward(View backwardView) {
        finish();
    }

    protected void onForward(View forwardView) {
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        //editor.putString("true");
        editor.putBoolean("sports",sports.isChecked());
        editor.putBoolean("top_stories",top_stories.isChecked());
        editor.putBoolean("technology",technology.isChecked());
        editor.putBoolean("health",health.isChecked());
        editor.putBoolean("world",world.isChecked());
        editor.putBoolean("national",national.isChecked());
        editor.putBoolean("entertainment",entertainment.isChecked());
        editor.commit();
        Toast.makeText(this, "successfully apply changes!", Toast.LENGTH_LONG).show();
        Intent intent=new Intent();
        intent.putExtra("changed", true);
        setResult(0, intent);
        finish();
    }


    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }

    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setTextColor(textColor);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.channel_button_backward:
                onBackward(v);
                break;

            case R.id.channel_button_forward:
                onForward(v);
                break;

            default:
                break;
        }
    }
}

