package com.seatgeek.placesautocompletedemo.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.seatgeek.placesautocompletedemo.MainFragment;
import com.seatgeek.placesautocompletedemo.R;
import com.seatgeek.placesautocompletedemo.helper.DBHandler;
import com.seatgeek.placesautocompletedemo.model.ParkingCar;
import com.seatgeek.placesautocompletedemo.util.ExpandableListDataSource;
import com.seatgeek.placesautocompletedemo.util.GUIUtils;
import com.seatgeek.placesautocompletedemo.util.TrackGPS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ColorActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private View revealView;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    String TAG_FRAGMENT = "MainFragment";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;

    private Map<String, List<String>> mExpandableListData;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Set the saved theme
        sharedpreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        setTheme(sharedpreferences.getInt("theme", R.style.AppTheme));

        setContentView(R.layout.activity_color);


        if (savedInstanceState == null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.fragment, MainFragment.newInstance(null));
            trans.replace(R.id.fragment,  MainFragment.newInstance(null), TAG_FRAGMENT);
            trans.addToBackStack(null);
            trans.commit();
        }

        revealView = findViewById(R.id.reveal_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
//        mNavigationManager = FragmentNavigationManager.obtain(this);

        initItems();

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header_main, null, false);
        mExpandableListView.addHeaderView(listHeaderView);

        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();
        setupDrawer();
    }

    private void initItems() {
        items = getResources().getStringArray(R.array.film_genre);
    }

    private void addDrawerItems() {
//        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle("Film genres");
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                getSupportActionBar().setTitle(selectedItem);

                if (items[0].equals(mExpandableListTitle.get(groupPosition))) {
//                    mNavigationManager.showFragmentAction(selectedItem);
                } else if (items[1].equals(mExpandableListTitle.get(groupPosition))) {
//                    mNavigationManager.showFragmentComedy(selectedItem);
                } else if (items[2].equals(mExpandableListTitle.get(groupPosition))) {
//                    mNavigationManager.showFragmentDrama(selectedItem);
                } else if (items[3].equals(mExpandableListTitle.get(groupPosition))) {
//                    mNavigationManager.showFragmentMusical(selectedItem);
                } else if (items[4].equals(mExpandableListTitle.get(groupPosition))) {
//                    mNavigationManager.showFragmentThriller(selectedItem);
                } else {
                    throw new IllegalArgumentException("Not supported fragment type");
                }

                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle(R.string.film_genres);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences sharedPreferences;



    private void startHideRevealEffect(final int cx, final int cy) {

        if (cx != 0 && cy != 0) {
            // Show the unReveal effect when the view is attached to the window
            revealView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                    // Get the accent color
                    TypedValue outValue = new TypedValue();
                    getTheme().resolveAttribute(android.R.attr.colorPrimary, outValue, true);
                    revealView.setBackgroundColor(outValue.data);

                    GUIUtils.hideRevealEffect(revealView, cx, cy, 1920);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {}
            });
        }
    }

    @Override
    public void onBackPressed() {
        final MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if(fragment != null) {
//            if(fragment.allowBackPressed();
            if (fragment.allowBackPressed()) {
                super.onBackPressed();
            }
        }
    }


    private void hideNavigationStatus() {

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }


    Animator.AnimatorListener revealAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {
//            Intent i = new Intent(ColorActivity.this, MainActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//            finish();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            final MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
            if(fragment != null) {
               fragment.endAnimation();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    };


    public void view(View view) {

        int selectedTheme = 0;
        int primaryColor = 0;

        switch (view.getId()) {
            case R.id.circle1:
                selectedTheme = R.style.theme1;
                primaryColor = getResources().getColor(R.color.color_set_1_primary);
                break;
        }

        int [] location = new int[2];
        revealView.setBackgroundColor(Color.WHITE);
        view.getLocationOnScreen(location);

        int cx = (location[0] + (view.getWidth() / 2));
        int cy = location[1] + (GUIUtils.getStatusBarHeight(this) / 2);

        SharedPreferences.Editor ed = sharedpreferences.edit();
        ed.putInt("x", cx);
        ed.putInt("y", cy);
        ed.putInt("theme", selectedTheme);
        ed.apply();


        revealView .setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        hideNavigationStatus();
        GUIUtils.showRevealEffect(revealView, cx, cy, revealAnimationListener);
    }
}
