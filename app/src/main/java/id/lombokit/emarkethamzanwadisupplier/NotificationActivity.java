package id.lombokit.emarkethamzanwadisupplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import id.lombokit.emarkethamzanwadisupplier.Adapters.NotifPagerAdapter;
import id.lombokit.emarkethamzanwadisupplier.Adapters.TimingsAdapter;

public class NotificationActivity extends AppCompatActivity {

    private NotifPagerAdapter notifPagerAdapter;
    private TabLayout customTab;
    private ViewPager pager;

    Typeface mTypeface;
    ImageView kembali, pemberitahuan;
    TextView judul;

    String TAG = "bendera";

    private Dialog bottomDialog;
    private TextView tvToday,tvTomorrow;
    private LinearLayout linearDialog;
    private FrameLayout frameOk;
    private RecyclerView rvTimings;
    private TimingsAdapter timingsAdapter;

    private String[] NAMES_TIMMING = {"9 TO 10 am","10 to 11 am","11 to 12 am","12 to 1 pm","1 to 2 pm","2 to 3 pm","3 to 4 pm"};
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ImageView btn;

    Animation slideUpAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });

        pemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });

        judul.setText((CharSequence) "Pemberitahuan");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]
        Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to weather topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("weather")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
                                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            }
                        });
                // [END subscribe_topics]
            }
        });

        Button logTokenButton = findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START retrieve_current_token]
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d(TAG, msg);
                                Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END retrieve_current_token]
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        slideUpAnimation= AnimationUtils.loadAnimation(NotificationActivity.this, R.anim.slide_up_linear);

        for (int i=0; i<NAMES_TIMMING.length ; i++){

            stringArrayList.add(NAMES_TIMMING[i]);
        }

        bottomDialog  = new Dialog(NotificationActivity.this,R.style.BottomDialog);
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        bottomDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(bottomDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        bottomDialog.getWindow().setAttributes(layoutParams);
        bottomDialog.setCancelable(true);
        bottomDialog.setContentView(R.layout.dilaog_bottom);
        tvToday = bottomDialog.findViewById(R.id.tvToday);
        tvTomorrow= bottomDialog.findViewById(R.id.tvTomorrow);
        frameOk= (FrameLayout) bottomDialog.findViewById(R.id.frameOk);
        linearDialog= (LinearLayout) bottomDialog.findViewById(R.id.linearDialog);
        rvTimings = (RecyclerView) bottomDialog.findViewById(R.id.rvTimings);
        RecyclerView.LayoutManager mLayoutManagerHappyHours = new LinearLayoutManager(NotificationActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        rvTimings.setLayoutManager(mLayoutManagerHappyHours);
        rvTimings.setNestedScrollingEnabled(false);
        rvTimings.setItemAnimator(new DefaultItemAnimator());
        rvTimings.setHasFixedSize(false);

        timingsAdapter = new TimingsAdapter(NotificationActivity.this, stringArrayList);
        rvTimings.setAdapter(timingsAdapter);

        frameOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
                Intent it = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(it);
            }
        });


        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToday.setBackgroundResource(R.drawable.round_button_dark);
                tvToday.setTextColor(Color.parseColor("#ffffff"));
                tvTomorrow.setBackgroundResource(R.drawable.round_button_light);
                tvTomorrow.setTextColor(Color.parseColor("#28a7fc"));
            }
        });


        tvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvTomorrow.setBackgroundResource(R.drawable.round_button_dark);
                tvTomorrow.setTextColor(Color.parseColor("#ffffff"));
                tvToday.setBackgroundResource(R.drawable.round_button_light);
                tvToday.setTextColor(Color.parseColor("#28a7fc"));

            }
        });



        pager = (ViewPager)findViewById(R.id.pager);
        customTab=  findViewById(R.id.customTab);


        customTab.addTab(customTab.newTab().setText("PEMBERITAHUAN"));

        mTypeface = Typeface.createFromAsset(this.getAssets(), "roboto_regular.ttf");
        ViewGroup vg = (ViewGroup) customTab.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }


        notifPagerAdapter = new NotifPagerAdapter
                (getSupportFragmentManager(), customTab.getTabCount());
        pager.setAdapter(notifPagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(customTab));
        pager.setOffscreenPageLimit(customTab.getTabCount());

        customTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


}
