package com.vht.sample;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.vht.activity.CusActionbarActivity;
import com.vht.fragment.VStackChatGroupFragment;

/**
 * Created by VuVan on 27/03/2017.
 */

public class ChatGroupFragmentActivity extends CusActionbarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.azstack_activity_contain);

        getSupportActionBar().setTitle("Chat Group Fragment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        VStackChatGroupFragment azConversationFragment = new VStackChatGroupFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.v_contain, azConversationFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
