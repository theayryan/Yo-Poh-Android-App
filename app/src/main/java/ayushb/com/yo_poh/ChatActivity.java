package ayushb.com.yo_poh;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ayushb.com.yo_poh.YoPohClasses.Complaint;


public class ChatActivity extends Activity {
    SharedPreferences prefs;
    List<NameValuePair> params;
    EditText chat_msg;
    Button send_btn;
    Bundle bundle;
    TableLayout tab;
    ArrayList<Message> messagesArray = new ArrayList<>();
    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("Message");
            String from = intent.getStringExtra("From");
            String channel = intent.getStringExtra("Channel");
        }
    };
    private Complaint complaint;
    private Pubnub pubnub;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tab = (TableLayout) findViewById(R.id.tab);
        bundle = getIntent().getBundleExtra("INFO");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        complaint = (Complaint) getIntent().getSerializableExtra(Constants.CHAT_DETAILS);
        SharedPreferences.Editor edit = prefs.edit();

        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        pubnub = new Pubnub(
                "pub-c-11fab31d-7614-4488-b338-2953846af28a" /* replace with your publish key */,
                "sub-c-17a0ba6c-90ef-11e5-b0f3-02ee2ddab7fe" /* replace with your subscribe key */);
        if (complaint != null) {
            pubnub.enablePushNotificationsOnChannel(
                    complaint.getChannel(),
                    prefs.getString(Constants.GCM_REG_KEY, ""));
        } else {
            pubnub.enablePushNotificationsOnChannel(
                    bundle.getString("Channel"),
                    prefs.getString(Constants.GCM_REG_KEY, ""));
        }

        try {
            if (complaint != null)
                pubnub.subscribe(complaint.getChannel(), new Callback() {

                            @Override
                            public void connectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            @Override
                            public void disconnectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            public void reconnectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            @Override
                            public void successCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : " + channel + " : "
                                        + message.getClass() + " : " + message.toString());
                            }

                            @Override
                            public void errorCallback(String channel, PubnubError error) {
                                System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                        + " : " + error.toString());
                            }
                        }
                );
            else
                pubnub.subscribe(bundle.getString("Channel"), new Callback() {

                            @Override
                            public void connectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            @Override
                            public void disconnectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            public void reconnectCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                        + " : " + message.getClass() + " : "
                                        + message.toString());
                            }

                            @Override
                            public void successCallback(String channel, Object message) {
                                System.out.println("SUBSCRIBE : " + channel + " : "
                                        + message.getClass() + " : " + message.toString());
                            }

                            @Override
                            public void errorCallback(String channel, PubnubError error) {
                                System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                        + " : " + error.toString());
                            }
                        }
                );
        } catch (PubnubException e) {
            e.printStackTrace();
        }


        Callback callback = new Callback() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
                try {
                    JSONArray responseJson = (JSONArray) response;
                    JSONArray messages = (JSONArray) responseJson.getJSONArray(0);
                    for (int i = 0; i < messages.length(); i++) {

                        Message message = new Message();
                        if (messages.get(i).toString().contains("pn_gcm")) {
                            JSONObject object = messages.getJSONObject(i);
                            JSONObject pn_gcm = (JSONObject) object.get("pn_gcm");
                            JSONObject data = (JSONObject) pn_gcm.get("data");
                            if (data.toString().contains("Message"))
                                message.message = data.getString("Message");
                            if (data.toString().contains("From"))
                                message.from = data.getString("From");
                            else
                                message.from = " ";
                        } else {
                            message.from = "You";
                            message.message = messages.get(i).toString();
                        }
                        messagesArray.add(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Message message : messagesArray)
                            addRow(message);
                    }
                });

            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
            }
        };
        if (complaint != null)
            pubnub.history(complaint.getChannel(), 100, true, callback);
        else
            pubnub.history(bundle.getString("Channel"), 100, true, callback);


        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));

        if (bundle != null) {
            if (bundle.get("From") != null) {
                Message message = new Message();
                message.from = bundle.getString("From");
                message.message = bundle.getString("Message");
                addRow(message);

            }
        }

        chat_msg = (EditText) findViewById(R.id.chat_msg);
        send_btn = (Button) findViewById(R.id.sendbtn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.from = "You";
                message.message = chat_msg.getText().toString();
                addRow(message);
                Callback callback = new Callback() {
                    public void successCallback(String channel, Object response) {
                        System.out.println(response.toString());
                    }

                    public void errorCallback(String channel, PubnubError error) {
                        System.out.println(error.toString());
                    }
                };
                if (complaint != null)
                    pubnub.publish(complaint.getChannel(), chat_msg.getText().toString(), callback);
                else
                    pubnub.publish(bundle.getString("Channel"), chat_msg.getText().toString(), callback);

            }
        });


    }

    public void addRow(Message message) {


        TableRow tr2 = new TableRow(getApplicationContext());
        tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView textview = new TextView(getApplicationContext());
        textview.setTextSize(20);
        if (message.from.equalsIgnoreCase("You")) {
            textview.setTextColor(Color.parseColor("#A901DB"));
            textview.setGravity(Gravity.RIGHT);
        } else
            textview.setTextColor(Color.parseColor("#0B0719"));
        textview.setText(Html.fromHtml("<b>" + message.from + ":" + "</b>" + message.message));
        tr2.addView(textview);
        tab.addView(tr2);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    class Message {
        String from;
        String message;
    }

}