/**
 * Copyright 2014-present Amberfog
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seatgeek.placesautocompletedemo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.seatgeek.placesautocompletedemo.MainFragment;
import com.seatgeek.placesautocompletedemo.R;
import com.seatgeek.placesautocompletedemo.util.ExpandableListDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.fragment, MainFragment.newInstance(null));
            trans.replace(R.id.fragment,  MainFragment.newInstance(null), TAG_FRAGMENT);
            trans.addToBackStack(null);
            trans.commit();
        }

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

//        placePickerGoogle(placePickerIntent);
//        startActivity();

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

    @Override
    public void onBackPressed() {
        final MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if(fragment != null) {
            fragment.allowBackPressed();
//            if (fragment.allowBackPressed()) {
//                super.onBackPressed();
//            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if (fragment != null) {
            fragment.OnActivityResult(requestCode, resultCode, data);
        }
    }

    private Intent placePickerIntent;

    public void startActivity(){
        startActivityForResult(placePickerIntent, 1);
    }

    public void placePickerGoogle(Intent intent) {
        try {
            placePickerIntent = intent;
            placePickerIntent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Created by tranminhtue on 22/04/2017.
     */

    public static class SplashActivity extends Activity {

        protected boolean shouldAskPermissions() {
            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }

        @TargetApi(23)
        protected void askPermissions() {
            String[] permissions = {
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE",
                    "android.permission.READ_PHONE_STATE",
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.READ_PHONE_STATE",
            };
            int requestCode = 200;
            requestPermissions(permissions, requestCode);
        }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash);
            if (shouldAskPermissions()) {
                askPermissions();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }
                }).start();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }finally{
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        }
                    }
                }).start();
            }
        }
    }

    public static class PlacesAutocompleteActivity extends Activity {

        @InjectView(R.id.autocomplete)
        PlacesAutocompleteTextView mAutocomplete;

        @InjectView(R.id.street)
        TextView mStreet;

        @InjectView(R.id.city)
        TextView mCity;

        @InjectView(R.id.state)
        TextView mState;

        @InjectView(R.id.zip)
        TextView mZip;

        @Override
        protected void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_places_autocomplete);
            ButterKnife.inject(this);

            mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
                @Override
                public void onPlaceSelected(final Place place) {
                    mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                        @Override
                        public void onSuccess(final PlaceDetails details) {
                            Log.d("test", "details " + details);
                            mStreet.setText(details.name);
    //                        mStreet.setText(getLocationFromAddress(getApplicationContext(),details.name).toString());
                            for (AddressComponent component : details.address_components) {
                                for (AddressComponentType type : component.types) {
                                    switch (type) {
                                        case STREET_NUMBER:
                                            break;
                                        case ROUTE:
                                            break;
                                        case NEIGHBORHOOD:
                                            break;
                                        case SUBLOCALITY_LEVEL_1:
                                            break;
                                        case SUBLOCALITY:
                                            break;
                                        case LOCALITY:
                                            mCity.setText(component.long_name);
                                            break;
                                        case ADMINISTRATIVE_AREA_LEVEL_1:
                                            mState.setText(component.short_name);
                                            break;
                                        case ADMINISTRATIVE_AREA_LEVEL_2:
                                            break;
                                        case COUNTRY:
                                            break;
                                        case POSTAL_CODE:
    //                                        mZip.setText(type.);
                                            break;
                                        case POLITICAL:
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(final Throwable failure) {
                            Log.d("test", "failure " + failure);
                        }
                    });
                }
            });
        }

        public LatLng getLocationFromAddress(Context context, String strAddress) {

            Geocoder coder = new Geocoder(context);
            List<Address> address;
            LatLng p1 = null;

            try {
                // May throw an IOException
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                p1 = new LatLng(location.getLatitude(), location.getLongitude());

            } catch (IOException ex) {

                ex.printStackTrace();
            }

            return p1;
        }
    }
}
