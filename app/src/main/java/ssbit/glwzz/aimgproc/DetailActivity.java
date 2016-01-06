package ssbit.glwzz.aimgproc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar_detail;
    private FloatingActionButton fab_change;
    private AppCompatImageView imageView_big;
    private OnePic myPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        myFindView();
        mySetView();
        myGetIntent();


    }

    private void myFindView() {
        toolbar_detail = (Toolbar) findViewById(R.id.toolbar_detail);
        fab_change = (FloatingActionButton) findViewById(R.id.fab_change);
        imageView_big = (AppCompatImageView) findViewById(R.id.imageView_big);
    }

    private void mySetView() {
        setSupportActionBar(toolbar_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        fab_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //不修改对应的图片而是界面结束时回传信息
                String tip;
                if (myPic.isIdentified()) {
                    tip = getResources().getString(
                            myPic.getHighQuality() ?
                                    R.string.tip_make_low :
                                    R.string.tip_make_high);
                    myPic.toggleHighQuality();
                    fabChange_updateIcon();

                    Snackbar.make(view, tip,
                            Snackbar.LENGTH_LONG).setAction(
                            getResources().getString(R.string.undo),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myPic.toggleHighQuality();
                                    fabChange_updateIcon();
                                }
                            }).show();
                } else {
                    tip = getResources().getString(R.string.tip_no_make);
                    Snackbar.make(view, tip, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void myGetIntent() {
        myPic = (OnePic) getIntent().getSerializableExtra("pic");
        imageView_big.setImageBitmap(
                myPic.getBitmapBig()
        );
        if (myPic.isIdentified()) {
            fabChange_updateIcon();
        } else {
            fab_change.setImageResource(R.drawable.ic_no_make);
        }
    }

    private void fabChange_updateIcon() {
        fab_change.setImageResource(
                myPic.getHighQuality() ?
                        R.drawable.ic_make_low :
                        R.drawable.ic_make_high);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                myFinish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void myFinish() {
        Intent intent = new Intent();
        intent.putExtra(MyExtraName.pic, myPic);
        DetailActivity.this.setResult(Msg.detailResultCode, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myFinish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
