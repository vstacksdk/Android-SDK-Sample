package com.vht.sample;

import android.content.Context;
import android.util.Log;

import com.vht.VStackClient;
import com.vht.exception.VStackException;
import com.vht.listener.VStackConnectListener;
import com.vht.listener.VStackUserListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Utils {
    public static void connectVStackForGCM(final VStackClient VStackClient,
                                           final Context context) {
        VStackClient.getInstance().registerConnectionListenter(new VStackConnectListener() {
            @Override
            public void onConnectionConnected(VStackClient client) {
                Log.i("VStack", "onConnectionConnected");
            }

            @Override
            public void onConnectionDisconnected(VStackClient client) {
                Log.i("VStack", "onConnectionDisconnected");
            }

            @Override
            public void onConnectionError(VStackClient client, VStackException e) {
                Log.i("VStack", e.toString());
            }
        });
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
                JSONArray array = new JSONArray();
                try {
                    for (int i = 0; i < Config.listFriendVStackUserId.length; i++) {
                        JSONObject obContact = new JSONObject();
                        obContact.put("vStackUserId", Config.listFriendVStackUserId[i]);
                        obContact.put("name", Config.listFriendName[i]);
                        array.put(obContact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return array;
            }
        });

        VStackClient.connect(Config.my_vstack_userid, Config.user_credentials, Config.my_name);
    }
}
