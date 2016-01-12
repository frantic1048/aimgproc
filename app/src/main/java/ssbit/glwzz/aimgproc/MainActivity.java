package ssbit.glwzz.aimgproc;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView_main;
    private AdapterOfOnePicRecView adapterOfOnePicRecView;
    private Toolbar toolbar_main;
    private DrawerLayout drawerLayout_main;
    private CollapsingToolbarLayout collapsingToolbarLayout_main;
    private NavigationView navigationView_main;
    private NavigationView.OnNavigationItemSelectedListener navigationView_main_L;
    private PicScanner picScanner;
    private Identifier identifier;
    private ProgressDialog progressDialog_scan;
    private ProgressDialog progressDialog_identify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFindView();
        myCreate();
        mySetView();
        myIni();

    }

    private void myFindView() {
        recyclerView_main = (RecyclerView) findViewById(R.id.recyclerView_main);
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        drawerLayout_main = (DrawerLayout) findViewById(R.id.drawerLayout_main);
        navigationView_main = (NavigationView) findViewById(R.id.navigationView_main);
        collapsingToolbarLayout_main = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout_main);
    }

    private void myCreate() {

        adapterOfOnePicRecView = new AdapterOfOnePicRecView(null
                , MainActivity.this);
        navigationView_main_L = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                String toolbar_title = null;
                if (identifier.diffed()) {
                    switch (item.getItemId()) {
                        case R.id.nav_viewAll: {
                            adapterOfOnePicRecView.setAllOnePic(identifier.getPics());
                            toolbar_title = getResources().getString(R.string.toolbar_title_all);
                            break;
                        }
                        case R.id.nav_viewHigh: {
                            adapterOfOnePicRecView.setAllOnePic(identifier.getHighPic());
                            toolbar_title = getResources().getString(R.string.toolbar_title_high);
                            break;
                        }
                        case R.id.nav_viewLow: {
                            adapterOfOnePicRecView.setAllOnePic(identifier.getLowPic());
                            toolbar_title = getResources().getString(R.string.toolbar_title_low);
                            break;
                        }
                        case R.id.nav_about: {
                            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    if (toolbar_title != null) {

                        collapsingToolbarLayout_main.setTitle(toolbar_title);
                    }
                } else {
                    if (progressDialog_identify == null) {
                        String title = getResources().
                                getString(R.string.progressDialogIdentify_title);
                        String msg = getResources()
                                .getString(R.string.progressDialogIdentify_msg);
                        progressDialog_identify =
                                ProgressDialog.show(MainActivity.this, title, msg,
                                        false, true,
                                        new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                finish();
                                            }
                                        });
                    }
                }
                drawerLayout_main.closeDrawers();
                return true;
            }
        };
        picScanner = new PicScanner(MainActivity.this, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Msg.ScanDone: {
                        progressDialog_scan.dismiss();
                        adapterOfOnePicRecView.setAllOnePic(picScanner.getPics());
                        identifier = new Identifier(MainActivity.this,
                                picScanner.getPics(), new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                switch (msg.what) {
                                    case Msg.IdentifyDone: {
                                        identifier.diff();
                                        if (progressDialog_identify != null) {
                                            progressDialog_identify.dismiss();
                                        }
                                        break;
                                    }
                                }
                            }
                        });
                        new Thread(identifier).start();
                        break;
                    }
                }
            }
        });
    }

    private void mySetView() {
        setSupportActionBar(toolbar_main);
        ActionBarDrawerToggle actionBarDrawerToggle_main = new
                ActionBarDrawerToggle(this,
                drawerLayout_main, toolbar_main,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout_main.setDrawerListener(actionBarDrawerToggle_main);
        actionBarDrawerToggle_main.syncState();

        recyclerView_main.setLayoutManager(
                new StaggeredGridLayoutManager(
                        getResources().getConfiguration().
                                orientation == 1 ? 2 : 4
                        , LinearLayoutManager.VERTICAL));
        recyclerView_main.setItemAnimator(new DefaultItemAnimator());
        recyclerView_main.setAdapter(adapterOfOnePicRecView);
        navigationView_main.setNavigationItemSelectedListener(navigationView_main_L);
        collapsingToolbarLayout_main.setTitle(getResources().getString(R.string.toolbar_title_all));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Msg.detailRequestCode: {
                OnePic pic = (OnePic) data.getSerializableExtra(MyExtraName.pic);
                if (identifier.deal(pic)) {
                    adapterOfOnePicRecView.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    private void myIni() {
        ArrayList<OnePic> pics = new ArrayList<OnePic>() {
        };
        adapterOfOnePicRecView.setAllOnePic(pics);
        new Thread(picScanner).start();
        String title = getResources().getString(R.string.progressDialogScan_title);
        String msg = getResources().getString(R.string.progressDialogScan_msg);
        progressDialog_scan = ProgressDialog.show(MainActivity.this,
                title, msg, false, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}