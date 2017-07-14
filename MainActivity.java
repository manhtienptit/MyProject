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

package com.seatgeek.placesautocompletedemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String TAG_FRAGMENT = "MainFragment";

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


}
