package SeolHui.leitnerbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

import SeolHui.leitnerbox.Data.ThemeCheck;
import SeolHui.leitnerbox.Data.Word;
import SeolHui.leitnerbox.WordList.WordListViewActivity;

public class MainActivity extends AppCompatActivity {
    public static DBManager dbManager;
    ArrayList<Word> words = new ArrayList<>();
    private static ThemeCheck isThemeCheck = new ThemeCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dbManager = new DBManager(this);
        isThemeCheck = dbManager.getResultThemeCheck();
        setTheme(this);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_ll);
        TextView main_text = (TextView) findViewById(R.id.main_text);

        getSupportActionBar().hide();

        MyDialog myDialog = new MyDialog(this, R.layout.dialog_notice);
        myDialog.noticeA();


        words = dbManager.getResultWords(-1);
        main_text.setText(String.valueOf(words.size()));

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WordListViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void setTheme(Activity activity) {

        if (isThemeCheck.getIsTheme() == 0)
            activity.setTheme(R.style.Theme_Black);
        if (isThemeCheck.getIsTheme() == 1)
            activity.setTheme(R.style.Theme_LeitnerBox);
        if (isThemeCheck.getIsTheme() == 2)
            activity.setTheme(R.style.Theme_Yellow);
        if (isThemeCheck.getIsTheme() == 3)
            activity.setTheme(R.style.Theme_Brown);
        if (isThemeCheck.getIsTheme() == 4)
            activity.setTheme(R.style.Theme_Pink);
        if (isThemeCheck.getIsTheme() == 5)
            activity.setTheme(R.style.Theme_Red);
        if (isThemeCheck.getIsTheme() == 6)
            activity.setTheme(R.style.Theme_Sky);
        if (isThemeCheck.getIsTheme() == 7)
            activity.setTheme(R.style.Theme_Green);
        if (isThemeCheck.getIsTheme() == 8)
            activity.setTheme(R.style.Theme_Blue);
    }

    public static ThemeCheck getThemeCheck() {

        return isThemeCheck;
    }

    public static void setThemeCheck(ThemeCheck themeCheck) {
        isThemeCheck = themeCheck;
    }

    public static void setAds(Activity activity) {
        AdView mAdView;
        //-- admob setting
        MobileAds.initialize(activity, activity.getString(R.string.admob_app_id));
        mAdView = activity.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드입니다.
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

}


