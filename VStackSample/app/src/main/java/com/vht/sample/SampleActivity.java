package com.vht.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vht.VStackClient;
import com.vht.VStackContact;
import com.vht.VStackOptions;
import com.vht.exception.VStackException;
import com.vht.listener.VStackConnectListener;
import com.vht.listener.VStackMakeChatGroupListener;
import com.vht.listener.VStackUserListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tantn on 12/25/2015.
 */
public class SampleActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spFriend;
    private Button btnChat11, btnCall, btnChatHistory, btnChatGroup, btnVideoCall;
    private Button btnCreateGroupFragment, btnChatGroupFragment, btnCallOut;
    private View vConnect;
    private EditText edtFriendId, edtPhoneNumber;
    private TextView tvConnect, tvYourFriend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


        //
        final TextView textViewToChange = (TextView) findViewById(R.id.txt_userid);
        textViewToChange.setText("Your VStack User: " + Config.my_name);

        //init view
        spFriend = (Spinner) findViewById(R.id.sp_user);
        edtFriendId = (EditText) findViewById(R.id.edtFriendId);
        edtPhoneNumber = (EditText) findViewById(R.id.edtPhone);
        tvYourFriend = (TextView) findViewById(R.id.tvYourFriend);
        btnChat11 = (Button) findViewById(R.id.btn_chat);
        btnCall = (Button) findViewById(R.id.btn_call);
        btnChatHistory = (Button) findViewById(R.id.btn_chat_history);
        btnChatGroup = (Button) findViewById(R.id.btn_create_group);
        btnVideoCall = (Button) findViewById(R.id.btn_video_call);
        btnCreateGroupFragment = (Button) findViewById(R.id.btn_create_group_fragment);
        btnChatGroupFragment = (Button) findViewById(R.id.btn_start_chat_group_fragment);
        btnCallOut = (Button) findViewById(R.id.btn_callout);

        vConnect = findViewById(R.id.v_connect);
        tvConnect = (TextView) findViewById(R.id.tv_no_connection);
        vConnect.setVisibility(View.GONE);

        btnChat11.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnChatHistory.setOnClickListener(this);
        btnChatGroup.setOnClickListener(this);
        btnVideoCall.setOnClickListener(this);
        btnCreateGroupFragment.setOnClickListener(this);
        btnChatGroupFragment.setOnClickListener(this);
        btnCallOut.setOnClickListener(this);

        //init vstack
        initVStack();

        //init listener
        initVStackListener();

        //connect to vstack server
        //if you change my_vstack_userid, you must disconnect from VStack server and clear all cached data on client
        //VStackClient.getInstance().logout();
        vConnect.setVisibility(View.VISIBLE);
        tvConnect.setText(R.string.connecting);
        VStackClient.getInstance().connect(Config.my_vstack_userid, Config.user_credentials, Config.my_name);
        tvYourFriend.setText(Config.listFriendVStackUserId[0]);

        spFriend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tvYourFriend.setText(Config.listFriendVStackUserId[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edtFriendId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtFriendId.length() > 0) {
                    spFriend.setEnabled(false);
                    tvYourFriend.setText(edtFriendId.getText().toString());
                } else {
                    spFriend.setEnabled(true);
                    int pos = spFriend.getSelectedItemPosition();
                    tvYourFriend.setText(Config.listFriendVStackUserId[pos]);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int pos = spFriend.getSelectedItemPosition();
        String friendId = edtFriendId.length() > 0 ? edtFriendId.getText().toString() : Config.listFriendVStackUserId[pos];
        String friendName = edtFriendId.length() > 0 ? edtFriendId.getText().toString() : Config.listFriendName[pos];
        if (id == R.id.btn_chat) {
//            int pos = spFriend.getSelectedItemPosition();
            VStackClient.getInstance().startChat(SampleActivity.this, friendId, friendName, null);
        } else if (id == R.id.btn_call) {
//            int pos = spFriend.getSelectedItemPosition();
            VStackClient.getInstance().startCall(SampleActivity.this, friendId, friendName, null, 0);
        } else if (id == R.id.btn_chat_history) {
            VStackClient.getInstance().viewChatHistory(SampleActivity.this);
        } else if (id == R.id.btn_create_group) {
            VStackClient.getInstance().createGroup(SampleActivity.this);
        } else if (id == R.id.btn_video_call) {
//            int pos = spFriend.getSelectedItemPosition();
            VStackClient.getInstance().startVideoCall(SampleActivity.this, friendId, friendName, null, 0);
        } else if (id == R.id.btn_create_group_fragment) {
            VStackClient.getInstance().createPublicGroup(SampleActivity.this, "name_group", new ArrayList<Integer>());
        } else if (id == R.id.btn_start_chat_group_fragment) {
            VStackClient.getInstance().startWithChatPublicFragment(SampleActivity.this, Config.groupId, ChatGroupFragmentActivity.class);
        } else if (id == R.id.btn_callout) {
            if (edtPhoneNumber.getText().toString().length() > 5)
                VStackClient.getInstance().startCallOut(SampleActivity.this, friendId, friendName, null, edtPhoneNumber.getText().toString());
        }
    }

    private void initVStack() {
        VStackOptions vStackOptions = new VStackOptions();
        vStackOptions.setGoogleCloudMessagingId(Config.SENDER_ID);
        VStackClient.newInstance(getBaseContext(), Config.app_id, Config.public_key, vStackOptions);
    }

    private void initVStackListener() {
        //register connect to VStack Server
        VStackClient.getInstance().registerConnectionListenter(new VStackConnectListener() {
            @Override
            public void onConnectionConnected(VStackClient client) {
                vConnect.post(new Runnable() {
                    @Override
                    public void run() {
                        tvConnect.setText(R.string.connected);
                        vConnect.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                vConnect.setVisibility(View.GONE);
                                spFriend.setEnabled(true);
                                btnChat11.setEnabled(true);
                                btnCall.setEnabled(true);
                                btnChatHistory.setEnabled(true);
                                btnChatGroup.setEnabled(true);
                                btnVideoCall.setEnabled(true);
                                btnChatGroupFragment.setEnabled(true);
                                btnCreateGroupFragment.setEnabled(true);
                                btnCallOut.setEnabled(true);
                            }
                        }, 800);
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(VStackClient client) {
            }

            @Override
            public void onConnectionError(VStackClient client, VStackException e) {
                System.out.println("================ error: " + e.toString());
            }
        });

        //register user
        VStackClient.getInstance().registerUserListener(new VStackUserListener() {
            @Override
            public void getUserInfo(List<String> vStackUserIds, int purpose) {
                try {
                    JSONArray arrayContact = new JSONArray();
                    for (String vStackUserId : vStackUserIds) {
                        JSONObject ob = new JSONObject();
                        ob.put("vStackUserId", vStackUserId);
                        ob.put("name", "name_" + vStackUserId);
                        arrayContact.put(ob);
                    }
                    VStackClient.getInstance().getUserInfoComplete(arrayContact, purpose);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void viewUserInfo(String vStackUserId) {
            }

            @Override
            public JSONArray getListFriend() {
                JSONArray arrayContact = new JSONArray();
                try {
                    for (int i = 0; i < Config.listFriendVStackUserId.length; i++) {
                        JSONObject obContact = new JSONObject();
                        obContact.put("vStackUserId", Config.listFriendVStackUserId[i]);
                        obContact.put("name", Config.listFriendName[i]);
                        arrayContact.put(obContact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return arrayContact;
            }
        });

        VStackClient.getInstance().setVStackMakeChatGroupListener(new VStackMakeChatGroupListener() {
            @Override
            public void onMakeChatGroupComplete(int r, int groupId, String groupName, List<VStackContact> list, String tag) {
                Log.e("groupId", groupId + "");
                if (r == 1 && groupId > 0) {
                    Toast.makeText(SampleActivity.this, "Create public group chat successfully", Toast.LENGTH_SHORT).show();
                } else if (r == 2 && groupId > 0) {
                    Toast.makeText(SampleActivity.this, "Group already exists", Toast.LENGTH_SHORT).show();
                }
                Config.groupId = groupId;
            }
        });
    }
}
