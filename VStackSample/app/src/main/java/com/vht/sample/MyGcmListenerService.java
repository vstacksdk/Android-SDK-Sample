/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vht.sample;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.vht.VStackClient;
import com.vht.exception.VStackException;
import com.vht.listener.VStackConnectListener;
import com.vht.listener.VStackUserListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * IntentService responsible for handling GCM messages.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // Implement connect VStack service here

        final VStackClient vStackClient = VStackClient.newInstance(this,
                Config.app_id, Config.public_key);

        VStackClient.getInstance().registerConnectionListenter(new VStackConnectListener() {
            @Override
            public void onConnectionConnected(VStackClient client) {
            }

            @Override
            public void onConnectionDisconnected(VStackClient client) {
            }

            @Override
            public void onConnectionError(VStackClient client, VStackException e) {
            }
        });

        //register user
        VStackClient.getInstance().registerUserListener(new VStackUserListener() {
            @Override
            public void getUserInfo(List<String> VStackUserIds, int purpose) {
                try {
                    JSONArray arrayContact = new JSONArray();
                    for (String VStackUserId : VStackUserIds) {
                        JSONObject ob = new JSONObject();
                        ob.put("VStackUserId", VStackUserId);
                        ob.put("name", "name_" + VStackUserId);
                        arrayContact.put(ob);
                    }
                    VStackClient.getInstance().getUserInfoComplete(arrayContact, purpose);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void viewUserInfo(String VStackUserId) {
            }

            @Override
            public JSONArray getListFriend() {
                JSONArray arrayContact = new JSONArray();
                try {
                    for (int i = 0; i < Config.listFriendVStackUserId.length; i++) {
                        JSONObject obContact = new JSONObject();
                        obContact.put("VStackUserId", Config.listFriendVStackUserId[i]);
                        obContact.put("name", Config.listFriendName[i]);
                        arrayContact.put(obContact);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return arrayContact;
            }
        });

        VStackClient.getInstance().showNotification(this, data);
    }
}
