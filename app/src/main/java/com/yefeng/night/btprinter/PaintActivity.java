package com.yefeng.night.btprinter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yefeng.night.btprinter.print.PrintMsgEvent;
import com.yefeng.night.btprinter.print.PrintPic;
import com.yefeng.night.btprinter.print.PrintService;
import com.yefeng.night.btprinter.print.PrintUtil;
import com.yefeng.night.btprinter.print.PrinterMsgType;
import com.yefeng.night.btprinter.util.ImageUtils;
import com.yefeng.night.btprinter.view.DrawingView;

import de.greenrobot.event.EventBus;

/**
 * Created by yefeng on 7/9/15.
 * github:yefengfreedom
 * <p/>
 * Print machine model:WP-T630
 */
public class PaintActivity extends Activity{

    // application class
    private MyApplication myApp;

    DrawingView mPaintView;
    Button mBtnClear;
    Button mBtnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        // Application class
        myApp = (MyApplication) getApplicationContext();

        mPaintView = (DrawingView) findViewById(R.id.dv_paint);
        mBtnClear = (Button) findViewById(R.id.btn_paint_clear);
        mBtnFinish = (Button) findViewById(R.id.btn_paint_finish);

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPainting();
            }
        });

        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndPrint();
            }
        });
    }

    void clearPainting() {
        if (null != mPaintView) {
            mPaintView.clearPainting();
        }
    }

    void finishAndPrint() {
        Bitmap bitmap = mPaintView.getBitmap();
        if (null == bitmap) {
            myApp.showToast("Error: Drawing graphics failed to fetch");
            return;
        }
        Log.e("PaintActivity", "before bitmap :" + bitmap.getWidth() + "--" + bitmap.getHeight());
        bitmap = ImageUtils.zoomBitmapByWidth(bitmap, 200);
        Log.e("PaintActivity", "zoom bitmap :" + bitmap.getWidth() + "--" + bitmap.getHeight());
        PrintPic.getInstance().init(bitmap);
        if (bitmap.isRecycled()) {
            bitmap = null;
        } else {
            bitmap.recycle();
            bitmap = null;
        }
        myApp.showToast("Image in print...");
        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        intent.setAction(PrintUtil.ACTION_PRINT_PAINTING);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /**
     * handle printer message
     *
     * @param event print msg event
     */
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            myApp.showToast(event.msg);
        }
    }
}
