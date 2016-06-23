package com.techtask.xps.pinsapp.Activities;

/**
 * Created by XPS on 6/11/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.techtask.xps.pinsapp.Adapters.FragmentViewPagerAdapter;
import com.techtask.xps.pinsapp.Helper.MapHelper;
import com.techtask.xps.pinsapp.Models.MarkerModel;
import com.techtask.xps.pinsapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String CURRENT_USER_ID = AccessToken.getCurrentAccessToken().getUserId();

    private AppBarLayout appBarLayout;
    private static ImageLoader imageLoader;

    public static List<MarkerModel> markers;
    public static ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        setupActionBar(pagerAdapter);
        markers = MarkerModel.find(MarkerModel.class,"owner_Id = ?",CURRENT_USER_ID);

        MapHelper helper = MapHelper.getInstance(this);
        helper.loadMapIfNeeded(pagerAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logoutItem){
            LoginManager.getInstance().logOut();
        finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0);
        finish();
    }

    private void setupActionBar(FragmentViewPagerAdapter pagerAdapter){

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.app_name));
        getProfileName(getSupportActionBar());

        getProfileCover();

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        appBarLayout.setExpanded(false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        appBarLayout.setExpanded(false);
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void getProfileName(final ActionBar actionBar){

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+AccessToken.getCurrentAccessToken().getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject obj = response.getJSONObject();
                        try {
                            String userName = obj.getString("name");
                            actionBar.setTitle(userName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e){
                            e.printStackTrace();
                            actionBar.setTitle(getString(R.string.app_name));
                        }
                    }
                }
        ).executeAsync();
    }
    private void getProfileCover(){
        Bundle params = new Bundle();
        params.putString("fields", "cover");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me", params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject obj = response.getJSONObject();
                            JSONObject JOSource = obj.optJSONObject("cover");
                            String coverPhoto = JOSource.getString("source");
                            ImageView imageView = (ImageView) findViewById(R.id.htab_header);
                            imageLoader.displayImage(coverPhoto,imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (NullPointerException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
