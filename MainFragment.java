/**
 * Copyright 2015-present Amberfog
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

package com.seatgeek.placesautocompletedemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.hsalf.smilerating.SmileRating;
import com.seatgeek.placesautocompletedemo.activity.ColorActivity;
import com.seatgeek.placesautocompletedemo.activity.DetailActivity;
import com.seatgeek.placesautocompletedemo.adapter.HeaderAdapter;
import com.seatgeek.placesautocompletedemo.adapter.ImageAdapter;
import com.seatgeek.placesautocompletedemo.adapter.MessagesAdapter;
import com.seatgeek.placesautocompletedemo.helper.DBHandler;
import com.seatgeek.placesautocompletedemo.model.ParkingCar;
import com.seatgeek.placesautocompletedemo.model.Message;
import com.seatgeek.placesautocompletedemo.network.ApiClient;
import com.seatgeek.placesautocompletedemo.network.ApiInterface;
import com.seatgeek.placesautocompletedemo.util.DataProvider;
import com.seatgeek.placesautocompletedemo.util.DirectionJSONParser;
import com.seatgeek.placesautocompletedemo.util.EndPoints;
import com.seatgeek.placesautocompletedemo.util.TinyDB;
import com.seatgeek.placesautocompletedemo.util.TrackGPS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slidinguppanel.SlidingUpPanelLayout;

public class MainFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener, HeaderAdapter.ItemClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener
        , MessagesAdapter.MessageAdapterListener {

    private static final String ARG_LOCATION = "arg.location";
    private static final String TAG = "MainFragment";

    //     private RelativeLayout mListView;
//    private LockableRecyclerView mListView;
//    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private SlidingUpPanelLayout mLayout;

    // ListView stuff
    //private View mTransparentHeaderView;
    //private View mSpaceView;
//    private View mTransparentView;
//    private View mWhiteSpaceView;
//    private PlacesAutocompleteTextView edSearch;

    private HeaderAdapter mHeaderAdapter;

    private LatLng mLocation;
    private Marker mLocationMarker;

    private SupportMapFragment mMapFragment;

    private GoogleMap mMap;
    private boolean mIsNeedLocationUpdate = true;
    private boolean mIsMapReady = false;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Button btn_Rate;
    private Button btn_book;

    RelativeLayout rlContent;

    TextView txt_Adress;
    TextView txt_Scale, txt_Add, txt_Type, txt_Rate;

    private String lat_dest, lng_dest, latitude, longitude;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private Activity mapActivity;
    private String RADIUS;

    private Intent placePickerIntent;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private CardView searchLocation;

    private ArrayList<Integer> listOfItems;

    private LinearLayout dotsLayout;
    private int dotsCount;
    private TextView[] dots;

    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private NestedScrollView scrollView;
    private MessagesAdapter mAdapter;

    private SmileRating rating;

    private MaterialStyledDialog dialogReview;

    private TinyDB tinydb ;

    private ArrayList<String> arrayListBookmark = new ArrayList<String>();
    private String currentPos = "0";

    private String[] TypeStr = new String[]{"Nhỏ", "Vừa", "To"};
    private String[] ScaleStr = new String[]{"Lòng Đường", "Vỉa Hè", "Hầm Chung Cư", "Hầm TTTM - Văn Phòng", "Trường Học, Cơ Quan", "Khách Sạn", "Bệnh Viện", "Khác"};
//    private String[] PriceStr = new String[]{"Lòng Đường","Vỉa Hè","Hầm Chung Cư","Hầm TTTM - Văn Phòng","Trường Học, Cơ Quan","Khách Sạn","Bệnh Viện","Khác"};

    public MainFragment() {
        atmList = new ArrayList<ParkingCar>();
    }

    public static MainFragment newInstance(LatLng location) {
        MainFragment f = new MainFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(getContext(), data);

                LatLng placeLatLng = place.getLatLng();
                Double lat = placeLatLng.latitude;
                Double lng = placeLatLng.longitude;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LATITUDE", lat.toString());
                editor.putString("LONGITUDE", lng.toString());
                editor.commit();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15f), 50, null);

                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    View customView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mapActivity = getActivity();
        dataProvider = new DataProvider(mapActivity);
        placePickerGoogle(placePickerIntent);

        tinydb = new TinyDB(getContext());

        arrayListBookmark = tinydb.getListString("BOOKMARK");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_cmt);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);

        mAdapter = new MessagesAdapter(getContext(), messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        scrollView.scrollTo(0, 0);


//        mListView = (LockableRecyclerView) rootView.findViewById(android.R.id.list);
//        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        searchLocation = (CardView) rootView.findViewById(R.id.search_bar);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                startActivityForResult(placePickerIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        final LayoutInflater inflatera = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        customView = inflatera.inflate(R.layout.review, null);

        dialogReview = new MaterialStyledDialog.Builder(getActivity())
                .setHeaderDrawable(R.drawable.header_2)
                .setCustomView(customView, 20, 20, 20, 0)
                .build();


        btn_Rate = (Button) rootView.findViewById(R.id.btn_rate);
        btn_book = (Button) rootView.findViewById(R.id.btn_book);

        btn_Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (customView != null) {
//                    ViewGroup parent = (ViewGroup) customView.getParent();
//                    if (parent != null) {
//                        parent.removeView(customView);
//                    }
//                }
//                try {
//                    customView = inflatera.inflate(R.layout.review,null);
//                } catch (InflateException e) {
//
//                }
//                dialogHeader_4.getBuilder().setCustomView(customView,20,20,20,0);
                dialogReview.show();

            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayListBookmark.contains(currentPos)){
                    btn_book.setBackgroundResource(R.drawable.star_off);
                    arrayListBookmark.remove(currentPos);
                }else{
                    btn_book.setBackgroundResource(R.drawable.star_on);
                    arrayListBookmark.add(currentPos);
                }
                tinydb.putListString("BOOKMARK",arrayListBookmark);
//                btn_book.setBackgroundResource(R.drawable.star_on);
            }
        });

        rating = (SmileRating) customView.findViewById(R.id.ratingsBar);

        final EditText ed_review = (EditText) customView.findViewById(R.id.reviewED);
        final EditText ed_title = (EditText) customView.findViewById(R.id.titleED);
        Button cancelDialog = (Button) customView.findViewById(R.id.cancelD);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReview.dismiss();
            }
        });
        Button submitDialog = (Button) customView.findViewById(R.id.submitD);
        submitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            Toast.makeText(getActivity().getApplicationContext(),""+rating.getRating(),Toast.LENGTH_SHORT).show();
                sendComment(10,
                        ed_review.getText().toString().trim(),
                        ed_title.getText().toString().trim(),
                        rating.getRating(), new ParkingCar());
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mapActivity);
        latitude = sharedPreferences.getString("LATITUDE", null);
        longitude = sharedPreferences.getString("LONGITUDE", null);
        RADIUS = sharedPreferences.getString("RADIUS", "1000");

        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(new ImageAdapter(getActivity()));
        pager.setCurrentItem(getArguments().getInt(Constants.Extra.IMAGE_POSITION, 0));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setTextColor(getResources().getColor(android.R.color.black));
                }
                dots[position].setTextColor(Color.GREEN);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUiPageViewController(rootView);


        mLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.sliding_layout);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.setAnchorPoint(0.68f);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("", "onPanelStateChanged " + newState);
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        txt_Adress = (TextView) rootView.findViewById(R.id.name);
        txt_Scale = (TextView) rootView.findViewById(R.id.txt_Scale);
        txt_Add = (TextView) rootView.findViewById(R.id.textView3);
        txt_Type = (TextView) rootView.findViewById(R.id.textView4);
        txt_Rate = (TextView) rootView.findViewById(R.id.textView5);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "font/UTM Avo.ttf");
        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "font/UTM Dax.ttf");
        txt_Adress.setTypeface(tf, Typeface.BOLD);
        txt_Scale.setTypeface(tf1, Typeface.BOLD);
        txt_Add.setTypeface(tf1, Typeface.BOLD);
        txt_Type.setTypeface(tf1, Typeface.BOLD);
        txt_Rate.setTypeface(tf1, Typeface.BOLD);

        collapseMap();
        return rootView;
    }

    private void setUiPageViewController(View parent) {
        dotsLayout = (LinearLayout) parent.findViewById(R.id.viewPagerCountDots);
        dotsCount = 5;
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
            dotsLayout.addView(dots[i]);
        }

        dots[0].setTextColor(Color.GREEN);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLocation = getArguments().getParcelable(ARG_LOCATION);
        if (mLocation == null) {
//            mLocation = getLastKnownLocation(false);
            mLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        }

        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);

        try {
            MapsInitializer.initialize(mapActivity.getApplicationContext());
        } catch (Exception e) {
            Log.e("" + "MAP_INITILIZE", e.getMessage());
        }

        ArrayList<String> testData = new ArrayList<String>(100);
        for (int i = 0; i < 100; i++) {
            testData.add("Item " + i);
        }

        mHeaderAdapter = new HeaderAdapter(getActivity(), testData, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    }

    public boolean allowBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else {
            getActivity().finish();
            return true;
        }

        return false;
    }

    public boolean endAnimation() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        prepareATMlist();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(mapActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mapActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_json));

            mMap = googleMap;
            mMap.setOnMarkerClickListener(this);

            mGoogleApiClient = new GoogleApiClient
                    .Builder(getActivity())
                    .addApi(Places.GEO_DATA_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            setUpMapIfNeeded();

            mIsMapReady = true;

            prepareATMlist();

            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }
        // Position the map's camera near Sydney, Australia.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null)
        {
            // Try to obtain the map from the SupportMapFragment.
//            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                LatLng update = getLastKnownLocation();
                if ( mLocation != null ) {
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15f), 50, null);
                    mLocationMarker = mMap.addMarker(new MarkerOptions().position(mLocation).title(mapActivity.getResources().getString(R.string
                            .marker_you)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_me)));
                    mLocationMarker.showInfoWindow();

                }
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mIsNeedLocationUpdate = false;
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                        moveToLocation(latLng, false);
//                        if(mSlidingUpPanelLayout.isExpanded())
//                        mSlidingUpPanelLayout.collapsePane();
//                        else {
//                            rlContent.setVisibility(View.GONE);
////                            edSearch.setVisibility(View.VISIBLE);
//                        }
                    }
                });
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        if(mGoogleApiClient != null)
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        if(mGoogleApiClient != null)
        mGoogleApiClient.disconnect();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private LatLng getLastKnownLocation() {
        return getLastKnownLocation(true);
    }

    private LatLng getLastKnownLocation(boolean isMoveMarker) {
        LocationManager lm = (LocationManager) TheApp.getAppContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Activity activity = getActivity();
        if (activity == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
        }
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            if (isMoveMarker) {
                moveMarker(latLng);
            }
            return latLng;
        }
        return null;
    }

    private void moveMarker(LatLng latLng) {
        if (mLocationMarker != null) {
            mLocationMarker.remove();
        }
//        mLocationMarker = mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_my_location))
//                .position(latLng).anchor(0.5f, 0.5f));
//
//
//        mMap.setOnMarkerClickListener(this);
    }

    private void moveToLocation(Location location) {
        if (location == null) {
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        moveToLocation(latLng);
    }

    private void moveToLocation(LatLng latLng) {
        moveToLocation(latLng, true);
    }

    private void moveToLocation(LatLng latLng, final boolean moveCamera) {
        if (latLng == null) {
            return;
        }
    }

    private void collapseMap() {
        if (mHeaderAdapter != null) {
            mHeaderAdapter.showSpace();
        }
//        mTransparentView.setVisibility(View.GONE);
        if (mMap != null && mLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 13f), 1000, null);
        }
//        mListView.setScrollingEnabled(true);

    }

    private void expandMap() {
        if (mHeaderAdapter != null) {
            mHeaderAdapter.hideSpace();
        }
//        mTransparentView.setVisibility(View.INVISIBLE);
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
        }
//        mListView.setScrollingEnabled(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mIsNeedLocationUpdate && mIsMapReady) {
//            moveToLocation(location);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);

//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onItemClicked(int position) {
//        mSlidingUpPanelLayout.collapsePane();
    }

     @Override
    public boolean onMarkerClick(final Marker marker) {
         if(!marker.getTitle().equalsIgnoreCase("YOU ARE HERE")) {
             currentPos = (marker.getTitle());
             if(arrayListBookmark.contains(currentPos)){
                 btn_book.setBackgroundResource(R.drawable.star_on);
             }else{
                 btn_book.setBackgroundResource(R.drawable.star_off);
             }
             mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
             txt_Adress.setText(atmList.get(Integer.parseInt(marker.getTitle())).getAtmName());
             txt_Scale.setText("Quy Mô : " + ScaleStr[atmList.get(Integer.parseInt(marker.getTitle())).getLoc_Scale()]);
             txt_Add.setText("Địa Điểm : " + atmList.get(Integer.parseInt(marker.getTitle())).getLoc_Address());
             txt_Type.setText("Loại Hình : " + TypeStr[atmList.get(Integer.parseInt(marker.getTitle())).getLoc_Type()]);
             txt_Rate.setText("Đánh Giá : " + atmList.get(Integer.parseInt(marker.getTitle())).getLoc_Des());
             if (marker.equals(mLocationMarker)) {
                 mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 13f), 100, null);
             }

             if (!marker.getTitle().equalsIgnoreCase(mapActivity.getResources().getString(R.string.marker_you))) {
                 intent = new Intent(mapActivity, DetailActivity.class);
//             destLat = String.valueOf(marker.getTitle());
//             destLng =

                 latitude = sharedPreferences.getString("LATITUDE", null);
                 longitude = sharedPreferences.getString("LONGITUDE", null);
                 destLat = String.valueOf(marker.getPosition().latitude);
                 destLng = String.valueOf(marker.getPosition().longitude);

//                 new ATMRoute().execute(latitude, longitude, destLat, destLng);

                 getInbox(50);

             }
         }

        return true;
    }

    private ArrayList<ParkingCar> atmList;
    private ParkingCar atmObj;
    private DataProvider dataProvider;
    private LatLng marker;
    private int noOfATM;
    private String atmName;
    private String atmAddress;

    private void prepareATMlist() {

        RADIUS = sharedPreferences.getString("RADIUS", "1000");
        latitude = sharedPreferences.getString("LATITUDE", null);
        longitude = sharedPreferences.getString("LONGITUDE", null);

        new LocationData().execute(latitude, longitude, RADIUS);
//        seekBar.setProgress(Integer.parseInt(RADIUS) / 1000);
//        radiusOfArea.setText("ATMs within the range of " + Integer.parseInt(RADIUS) / 1000 + " kms.");
    }

    private class LocationData extends AsyncTask<String, Void, String> {
        ProgressDialog pd = null;

        public ArrayList<ParkingCar> databaseTest() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            TrackGPS trackGPS = new TrackGPS(getContext());
            float x = Float.parseFloat(sharedPreferences.getString("LATITUDE", String.valueOf(trackGPS.getLatitude())));
            float y = Float.parseFloat(sharedPreferences.getString("LONGITUDE", String.valueOf(trackGPS.getLongitude())));
            int r = Integer.parseInt(sharedPreferences.getString("RADIUS", "1000"));
            DBHandler db = new DBHandler(getContext());
            db.open();
        ArrayList<ParkingCar> data = db.getSample(x,y,r);
//            ArrayList<ParkingCar> data = db.getSample(21.017390f, 105.783582f,r);
            db.close();

            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mapActivity);
            pd.setMessage(getResources().getString(R.string.loader_message));
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String latitude = params[0];
            String longitude = params[1];
            String radius = params[2];

            /*Fetching ParkingCar List from Google API Server*/
            String atmData = "";//dataProvider.ATMData(latitude, longitude, radius);
            return atmData;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();
            //addToLocationList(result);

            atmList = databaseTest();

//            Log.v("" + "ATMLIST", atmList.toString());
            int noOfATM = atmList.size();
            mMap.clear();

            final List<Marker> markers = new ArrayList<Marker>();

            LatLng currentPos = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

            List<LatLng> points = new ArrayList<LatLng>();
            points.add(new LatLng(currentPos.latitude-2, currentPos.longitude-2));
            points.add(new LatLng(currentPos.latitude-2, currentPos.longitude+2));
            points.add(new LatLng(currentPos.latitude+2, currentPos.longitude+2));
            points.add(new LatLng(currentPos.latitude+2, currentPos.longitude-2));


            List<LatLng> hole = new ArrayList<LatLng>();
            float p = 360/360;
            float d =0;
            for(int i=0; i < 360; ++i, d+=p){
                hole.add(SphericalUtil.computeOffset(currentPos, Double.parseDouble(RADIUS), d));
            }

            mMap.addPolygon(new PolygonOptions()
                    .addAll(points)
                    .addHole(hole)
                    .strokeWidth(0)
                    .fillColor(Color.argb(80, 0, 0, 0)));
//            CircleOptions circleOptions = new CircleOptions().center(currentPos).radius(Double.parseDouble(RADIUS)).strokeWidth(2)
//                    .strokeColor(Color.BLUE);
//            mMap.addCircle(circleOptions);

            mMap.addMarker(new MarkerOptions().position(currentPos).title(mapActivity.getResources().getString(R.string.marker_you)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_me))).showInfoWindow();

            markers.add(mMap.addMarker(new MarkerOptions().position(currentPos).title(mapActivity.getResources().getString(R.string.marker_you)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_me))));


            for (int i = 0; i < noOfATM; i++) {

                marker = new LatLng(atmList.get(i).getLatitude(), atmList.get(i).getLongitude());

                String title = String.valueOf(i);
                String address = atmList.get(i).getAtmAddress();
                markers.add(mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_atm_marker)).position(marker).title(title).snippet(address)));

            }


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15f), 50, null);

//            if (markers.size() > 0) {
//                LatLngBounds bounds = builder.build();
////                int width = getResources().getDisplayMetrics().widthPixels;
////                int height = getResources().getDisplayMetrics().heightPixels;
////                int padding = (int) (width * 0.10);
//
//                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,15);
//                mMap.animateCamera(cu,1000,null);
//            }
        }
    }

    public void OnActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(getContext(), data);

                LatLng placeLatLng = place.getLatLng();
                Double lat = placeLatLng.latitude;
                Double lng = placeLatLng.longitude;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LATITUDE", lat.toString());
                editor.putString("LONGITUDE", lng.toString());
                editor.commit();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 15f), 50, null);

                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Log.i("", status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void placePickerGoogle(Intent intent) {
        try {
            placePickerIntent = intent;
            placePickerIntent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addToLocationList(String result) {

        if (atmList.size() > 0) {
            atmList.clear();
        }
        if (result != null) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jsonArray = jsonObj.getJSONArray("results");
                noOfATM = jsonArray.length();

                /*Adding ParkingCar details in List*/
                for (int i = 0; i < noOfATM; i++) {
                    atmName = jsonArray.getJSONObject(i).getString("name");
                    atmAddress = jsonArray.getJSONObject(i).getString("vicinity");
                    lat_dest = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                    lng_dest = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    atmObj = new ParkingCar();
                    atmObj.setAtmName(atmName);
                    atmObj.setAtmAddress(atmAddress);
                    atmObj.setLatitude(Double.parseDouble(lat_dest));
                    atmObj.setLongitude(Double.parseDouble(lng_dest));

                    if (!atmList.contains(atmObj))
                        atmList.add(atmObj);
                }
                Log.v("" + "MAPVIEW_ARRAY", atmList.toString());

            } catch (JSONException e) {
                Log.e("", e.getMessage());
            } catch (Exception e) {
                Log.e("", e.getMessage());
            }
        }
    }


    //=============================================================================
    @Override
    public void onIconClicked(int position) {
    }

    @Override
    public void onIconImportantClicked(int position) {
        Message message = messages.get(position);
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {

    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
//        enableActionMode(position);
    }

    /**
     * Fetches mail messages by making HTTP request
     * url: http://api.androidhive.info/json/inbox.json
     */
    private void getInbox(int pos) {
//        swipeRefreshLayout.setRefreshing(true);

//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//
//        Call<List<Message>> call = apiService.getInbox();
//        call.enqueue(new Callback<List<Message>>() {
//            @Override
//            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
//                // clear the inbox
//                messages.clear();
//
//                // add all the messages
//                // messages.addAll(response.body());
//
//                // TODO - avoid looping
//                // the loop was performed to add colors to each message
//                for (Message message : response.body()) {
//                    // generate a random color
//                    message.setColor(getRandomMaterialColor("400"));
//                    messages.add(message);
//                }
//
//                mAdapter.notifyDataSetChanged();
////                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<List<Message>> call, Throwable t) {
////                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
////                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

        String endPoint = "";
        endPoint = EndPoints.GET_COMMENTS.replace("_ID_",pos+"");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {

//                        Toast.makeText(TheApp.getAppContext(), response, Toast.LENGTH_LONG).show();

                        JSONArray commentsObj = obj.getJSONArray("items");

                        messages.clear();
                        for (int i = 0; i < commentsObj.length(); i++) {

                            JSONObject commentObj = (JSONObject) commentsObj.get(i);

                            String id = commentObj.getString("id");
                            String idUser = commentObj.getString("idUser");
                            String idLocation = commentObj.getString("idLocation");
                            String txtTitle = commentObj.getString("txtTitle");
                            String txtCmt = commentObj.getString("txtCmt");
                            String rate = commentObj.getString("rate");
                            String datatime = commentObj.getString("datatime");

                            Message messageData = new Message(Integer.parseInt(id),idUser,idLocation,txtTitle,txtCmt,datatime,rate,getRandomMaterialColor("400"));

                            messages.add(messageData);
                        }

                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TheApp.getAppContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error:ádasd " + e.getMessage());
                    Toast.makeText(TheApp.getAppContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + response);
                Toast.makeText(TheApp.getAppContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                // As of f605da3 the following should work
//                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        });

        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        TheApp.getInstance().addToRequestQueue(strReq);

    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    //=============================================== Draw Direct ==========================================================

    private List<List<HashMap<String, String>>> routes = null;
    private LatLng currentPosition, destPosition;
    private String distance;
    private String currentLat, currentLng, destLat, destLng;

    private class ATMRoute extends AsyncTask<String, Void, String> {
        ProgressDialog pd = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getResources().getString(R.string.loader_message));
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String lat_origin = params[0];
            String lng_origin = params[1];
            String lat_dest = params[2];
            String lng_dest = params[3];
            String mapRoute = dataProvider.mapRouteData(lat_origin, lng_origin, lat_dest, lng_dest);
            return mapRoute;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);

                DirectionJSONParser parser = new DirectionJSONParser();
                // Starts parsing data
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
//                Log.e(TAG + " ROUTE_PARSER", e.getMessage());
            }
            drawRouteonMap(routes);
            pd.dismiss();
        }
    }

    Polyline polyline;

    private void drawRouteonMap(List<List<HashMap<String, String>>> routes) {
        if(polyline != null) polyline.remove();
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        // Traversing through all the routes
        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();
            // Fetching i-th route
            List<HashMap<String, String>> path = routes.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.RED);
        }
        polyline = mMap.addPolyline(lineOptions);
    }

    private void sendComment(int pos, final String title, final String cmt,final int rate, final ParkingCar location) {

        String endPoint = "";
//        endPoint = EndPoints.SEND_COMMENT.replace("_ID_",pos+"");
        endPoint = EndPoints.FEEDS_COUNT;

        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                endPoint, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {

                        if(dialogReview.isShowing())
                                dialogReview.dismiss();
                        Toast.makeText(TheApp.getAppContext(), "Review OK!!", Toast.LENGTH_LONG).show();

//                        JSONArray commentsObj = obj.getJSONArray("messages");
//
//                        for (int i = 0; i < commentsObj.length(); i++) {
//                            JSONObject commentObj = (JSONObject) commentsObj.get(i);
//
//                        }


                    } else {
                        Toast.makeText(TheApp.getAppContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error:ádasd " + e.getMessage());
                    Toast.makeText(TheApp.getAppContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + response);
                Toast.makeText(TheApp.getAppContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                // As of f605da3 the following should work
//                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", "100");
                params.put("location_id","50");// location.getIdLocation()+"");
                params.put("title", title);
                params.put("message", cmt);
                params.put("rate", rate+"");

                Log.e(TAG, "Params: " + params.toString());

                return params;
            };
        };

        int socketTimeout = 0;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        strReq.setRetryPolicy(policy);

        //Adding request to request queue
        TheApp.getInstance().addToRequestQueue(strReq);
    }

}
