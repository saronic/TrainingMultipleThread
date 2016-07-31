package fei.trainingmultiplethread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Button mLoadImageButton;
    private Button mShowToastButton;
    private ProgressBar mProgressBar;
    private static final int mSleepTime = 500;
    private static final int SET_PROGRESS_BAR_VISIBLE = 0;
    private static final int UPDATE_PROGRESS_BAR = 1;
    private static final int UPDATE_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.activity_main_image_view);
        mLoadImageButton = (Button) findViewById(R.id.activity_main_load_image_button);
        mShowToastButton = (Button) findViewById(R.id.activity_main_show_toast_button);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SET_PROGRESS_BAR_VISIBLE:
                        mProgressBar.setVisibility((Integer)msg.obj);
                        break;
                    case UPDATE_PROGRESS_BAR:
                        mProgressBar.setProgress((Integer) msg.obj);
                        break;
                    case UPDATE_IMAGE:
                        mImageView.setImageBitmap((Bitmap) msg.obj);
                }
            }
        };
        mLoadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new LoadImageTask(handler)).start();
            }
        });

        mShowToastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "xxxxxxxxxxxxxxxxx", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class LoadImageTask implements Runnable {
        private Handler mHandler;

        public LoadImageTask(Handler handler) {
            mHandler = handler;
        }

        @Override
        public void run() {
            Message msg = null;
            msg = mHandler.obtainMessage(SET_PROGRESS_BAR_VISIBLE, ProgressBar.VISIBLE);
            mHandler.sendMessage(msg);
            for (int i = 1; i < 11; i++) {
                sleep(500);
                msg = mHandler.obtainMessage(UPDATE_PROGRESS_BAR, i * 10);
                mHandler.sendMessage(msg);
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            msg = mHandler.obtainMessage(UPDATE_IMAGE, bitmap);
            mHandler.sendMessage(msg);
            msg = mHandler.obtainMessage(SET_PROGRESS_BAR_VISIBLE, ProgressBar.INVISIBLE);
            mHandler.sendMessage(msg);

        }
    }


}
