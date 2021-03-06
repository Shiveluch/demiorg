package com.shiveluch.demiorg;

import static com.shiveluch.demiorg.Refactor.stringToKey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import static com.shiveluch.demiorg.Refactor.refact;
import static com.shiveluch.demiorg.Refactor.defact;
import static com.shiveluch.demiorg.Refactor.stringToKey;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class aDemiOrgMain extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    TextView toolbartext, asplayer, asmaster, startanket, approvelogin,
            mastername, masterapprove;
    EditText anketname, anketphone, anketemail, anketaddinfo;
    ListView maineventslist, maineventslistinfo, eventinfo, eventslistformysubevent,
    mysubeventslist;
    LinearLayout mainEventLL, mysubeventsLL;
    Button createnewmysubevent;
    private static final int RC_SIGN_INplayer = 007;
    private static final int RC_SIGN_INmaster = 001;
    private SignInButton signInPlayer, signInMaster;
    ArrayList<MainList> commonlist = new ArrayList<>();
    Toolbar toolbar;
    Context context;
    Activity activity;
    SecretKey api;
    String getmaster, getplayer, getonlyareas, getmysubevents;
    String email;
    String getOnlyEvent = "http://a0568345.xsph.ru/demiorg/getonlyevent.php";
    String geteventinfo = "";
    String eventID;

    RelativeLayout anketa, masterLL, playerLL;
    boolean status = false;
    SharedPreferences settings;
    public final static String STATUS = "status";
    public final static String FIO = "fio";
    public final static String EMAIL = "email";
    public final static String PHONENUM = "phone";
    public final static String ADDINFO = "addinfo";
    public final static String APPROVED = "approved";
    public final static String ID = "id";
    public final static String DOMAIN = "http://a0568345.xsph.ru/demiorg/";
    String getLocations;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ademiorgmain);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        settings = getSharedPreferences("set", Context.MODE_PRIVATE);
        api = stringToKey(getResources().getString(R.string.api));
        context = getApplicationContext();
        activity = this;

        initVisualElements();

    }

    private void initVisualElements() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbartext = findViewById(R.id.textheader);
        signInPlayer = findViewById(R.id.google_signIn1);
        signInMaster = findViewById(R.id.google_signIn2);
        signInPlayer.setOnClickListener(this);
        signInMaster.setOnClickListener(this);
        anketname = findViewById(R.id.anketname);
        anketphone = findViewById(R.id.anketphone);
        anketemail = findViewById(R.id.anketemail);
        anketaddinfo = findViewById(R.id.anketaddinfo);
        anketemail.setEnabled(false);
        asplayer = findViewById(R.id.asplayer);
        asmaster = findViewById(R.id.asmaster);
        startanket = findViewById(R.id.startanket);
        asplayer.setOnClickListener(this);
        asmaster.setOnClickListener(this);
        anketa = findViewById(R.id.authRL);
        anketa.setVisibility(View.GONE);
        mysubeventsLL = findViewById(R.id.mysubeventsLL);
        mysubeventsLL.setVisibility(View.GONE);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setVisibility(View.GONE);
        toolbartext.setText("??????????????????????");
        approvelogin = findViewById(R.id.approvelogin);
        approvelogin.setOnClickListener(this);
        mastername = findViewById(R.id.mastername);
        masterapprove = findViewById(R.id.masterapprove);
        masterLL = findViewById(R.id.masterLL);
        maineventslist = findViewById(R.id.maineventslist);
        maineventslistinfo = findViewById(R.id.maineventslistinfo);
        mainEventLL = findViewById(R.id.mainevent);
        String status = settings.getString(STATUS, "");
        masterLL.setVisibility(View.GONE);
        eventinfo = findViewById(R.id.eventinfo);
        eventslistformysubevent = findViewById(R.id.eventslistformysubevents);
        createnewmysubevent = findViewById(R.id.createnewmysubevent);
        mysubeventslist = findViewById(R.id.mysubeventslist);

        createnewmysubevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String approved=settings.getString(APPROVED,"");
                if (approved.equals("0"))
                {
                    sendToast("???????????? ?????????????? ???? ??????????????!");
                    return;
                }

                if (eventID.length()<1)
                {
                    sendToast("???? ?????????????? ??????????????");
                    return;
                }
                itemsForEditText.clear();
                itemsForEditText.add(new MainList("", "???????????????? ??????????????????????: ", "?????????? ??????????????????????"));
                itemsForEditText.add(new MainList("", "?????????????????????????????????? (??????.)", "0"));
                itemsForEditText.add(new MainList("", "?????????????? ????????????????????: ", "0"));
                itemsForEditText.add(new MainList("", "???????????????? ????????????????????: ", "0"));
                itemsForEditText.add(new MainList("", "???????????? ???? ??????????:", "http://"));
                itemsForEditText.add(new MainList("", "??????????????:", "http://"));
                itemsForEditText.add(new MainList("", "?????????? ??????????:", "http://"));
                itemsForEditText.add(new MainList("", "????????????????????:", "????????????????,  D&D5"));
                itemsForEditText.add(new MainList("", "??????????, ??????.:", "0"));
                itemsForEditText.add(new MainList("", "???????????????? ??????????????", "+7(9..)-..."));
                showListWithEditTextDialog();
            }
        });

        eventslistformysubevent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView isID = findViewById(R.id.eventid);
                eventID = isID.getText().toString();
                getmysubevents = "http://a0568345.xsph.ru/demiorg/getsubeventsbymaster.php/get.php?nom="
                        +settings.getString(ID,"")+"&dp="+eventID;
                getJSON(getmysubevents);

            }
        });


        maineventslistinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView isImage = view.findViewById(R.id.id);
                TextView isMap = view.findViewById(R.id.addinfo);
                String getImage = isImage.getText().toString();
                String getMap = isMap.getText().toString();
                sOut("is map: " + getMap);
                if (getMap.contains("http")) {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(getMap));
                    startActivity(browserIntent);
                    sOut("is map: " + getMap);

                }

                if (getMap.contains("area")) {
                    sOut("AREA");
                    showListDialog();
                }

                if (getImage.contains("http") && getImage.contains("jpg")) {
                    Intent intent = new Intent(context, map.class);
                    intent.putExtra("maplink", getImage);
                    startActivity(intent);

                }
            }
        });
        if (status.length() == 0) {
            masterLL.setVisibility(View.GONE);
            //playerLL.setVisibility(View.GONE);
            anketa.setVisibility(View.VISIBLE);
        }

        if (status.equals("player")) {

        }
        if (status.equals("master")) {
            masterLL.setVisibility(View.VISIBLE);
            mastername.setText(settings.getString(FIO, ""));

            String _approve = settings.getString(APPROVED, "");
            if (_approve.equals("0")) {
                masterapprove.setText("???????????? ?????????????? ???????? ???? ??????????????");
                masterapprove.setTextColor(getResources().getColor(R.color.red));
            } else {
                masterapprove.setText("???????????? ?????????????? ??????????????");
                masterapprove.setTextColor(getResources().getColor(R.color.black));
            }

            maineventslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView isID = view.findViewById(R.id.eventid);
                    eventID = isID.getText().toString();
                    geteventinfo = "http://a0568345.xsph.ru/demiorg/geteventinfo.php/get.php?dp=" + eventID;
                    sOut(geteventinfo);
                    getonlyareas = "http://a0568345.xsph.ru/demiorg/getonlyareas.php/get.php?nom=" + eventID;
                    getJSON(getonlyareas);
                    sOut(getonlyareas);
                    getJSON(geteventinfo);
                }
            });
            NavMasterPanel();
        }
        if (status.length() < 1) {
            toolbar.setVisibility(View.GONE);
            toolbartext.setText("??????????????????????");
            startanket.setText("????????????");
            anketa.setVisibility(View.VISIBLE);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        startLoad();


    }

    private void startLoad() {
        if (settings.getString(STATUS, "").equals("master")) {
            try {
                getmaster = "http://a0568345.xsph.ru/demiorg/getmaster.php/get.php?dp=" + RTStoJSON(settings.getString(EMAIL, ""));

                sOut(getmaster);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getJSON(getmaster);
        }

        if (settings.getString(STATUS, "").equals("player")) {
            try {
                getplayer = "http://a0568345.xsph.ru/demiorg/getplayer.php/get.php?dp=" + RTStoJSON(settings.getString(EMAIL, ""));
                sOut(getplayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getJSON(getplayer);
        }


    }


//    private void NavPlayerPanel()
//    {
//
//        Drawer.Result build = new Drawer()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .withActionBarDrawerToggle(true)
//                .withHeader(R.layout.drawer_header)
//                .addDrawerItems(
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(getResources().getDrawable( R.drawable.header)).withIdentifier(1),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_map_marker),
//                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question),
//                        new DividerDrawerItem(),
//                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_phone).withIdentifier(1)
//                )
//                .withOnDrawerListener(new Drawer.OnDrawerListener() {
//                    @Override
//                    public void onDrawerOpened(View drawerView) {
////                        if (drawerView!=null) {
////                            InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
////                            if (inputMethodManager != null) {
////                                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
////                            }
////                        }
//                    }
//                    @Override
//                    public void onDrawerClosed(View drawerView) {
//                    }
//                })
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    // ?????????????????? ??????????
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
//                        if (drawerItem instanceof Nameable) {
//                            sOut (""+position);
//                            if (position==1)
//                            {
////                                locationLL.setVisibility(View.GONE);
////                                eventsLL.setVisibility(View.VISIBLE);
////                                getJSON(getEvents);
////                                textheader.setText("??????????????????????");
//                                sOut (""+position);
//                            }
//
//                            if (position==2)
//                            {
////                                locationLL.setVisibility(View.VISIBLE);
////                                eventsLL.setVisibility(View.GONE);
////                                getJSON(getLocations);
////                                Log.d ("data", getLocations);
////                                textheader.setText("????????????????/??????????????");
//
//
//                            }
//
//                            if (position==6)
//                            {
//                                SharedPreferences.Editor editor = settings.edit();
//                                editor.clear();
//                                editor.apply();
//                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                                        new ResultCallback<Status>() {
//                                            @Override
//                                            public void onResult(Status status) {
//                                                sendToast("???????????????? ??????????????????");
//                                                toolbar.setVisibility(View.GONE);
//                                                anketa.setVisibility(View.VISIBLE);
//                                                masterLL.setVisibility(View.GONE);sOut("295");
//                                            }
//                                        });
//
//                            }
//                            // Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
//                        }
////                        if (drawerItem instanceof Badgeable) {
////                            Badgeable badgeable = (Badgeable) drawerItem;
////                            if (badgeable.getBadge() != null) {
////                                // ????????????, ???? ?????????????? ??????, ???????? ?????? ?????????? ???????????????? ???????????? "+"
////                                try {
////                                    int badge = Integer.valueOf(badgeable.getBadge());
////                                    if (badge > 0) {
////                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
////                                    }
////                                } catch (Exception e) {
////                                    Log.d("test", "???? ?????????????????? ???? ??????????, ???????????????????? ????????! :)");
////                                }
////                            }
////                        }
//                    }
//                })
//                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
//                    @Override
//                    // ?????????????????? ???????????????? ??????????, ????????????????, ???????????? ?????? SecondaryDrawerItem
//                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
//                        if (drawerItem instanceof SecondaryDrawerItem) {
//                        }
//                        return false;
//                    }
//                })
//                .build();
//
//
//    }


    private void NavMasterPanel() {

        Drawer.Result build = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(getResources().getDrawable(R.drawable.header)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_my_subevents).withIcon(FontAwesome.Icon.faw_map_marker),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_all_subevents).withIcon(FontAwesome.Icon.faw_map_marker),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_phone).withIdentifier(1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_arrow_circle_left)

                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
//                        if (drawerView!=null) {
//                            InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                            if (inputMethodManager != null) {
//                                inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
//                            }
//                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // ?????????????????? ??????????
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            sOut("" + position);
                            if (position == 1) {
                                maineventslist.setVisibility(View.VISIBLE);
                                mainEventLL.setVisibility(View.VISIBLE);
                                mysubeventsLL.setVisibility(View.GONE);
                                getJSON(getOnlyEvent);
                            }

                            if (position == 3) {
                                eventID = "";
                                getJSON(getOnlyEvent);
                                mainEventLL.setVisibility(View.GONE);
                                mysubeventsLL.setVisibility(View.VISIBLE);
//                                locationLL.setVisibility(View.VISIBLE);
//                                eventsLL.setVisibility(View.GONE);
//                                getJSON(getLocations);
//                                Log.d ("data", getLocations);
//                                textheader.setText("????????????????/??????????????");


                            }

                            if (position == 7) {
                                SharedPreferences.Editor editor = settings.edit();
                                editor.clear();
                                editor.apply();
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                sendToast("???????????????? ??????????????????");
                                                toolbar.setVisibility(View.GONE);
                                                anketa.setVisibility(View.VISIBLE);
                                                masterLL.setVisibility(View.GONE);
                                                toolbartext.setText("??????????????????????");
                                            }
                                        });

                            }
                            // Toast.makeText(MainActivity.this, MainActivity.this.getString(((Nameable) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                        }
//                        if (drawerItem instanceof Badgeable) {
//                            Badgeable badgeable = (Badgeable) drawerItem;
//                            if (badgeable.getBadge() != null) {
//                                // ????????????, ???? ?????????????? ??????, ???????? ?????? ?????????? ???????????????? ???????????? "+"
//                                try {
//                                    int badge = Integer.valueOf(badgeable.getBadge());
//                                    if (badge > 0) {
//                                        drawerResult.updateBadge(String.valueOf(badge - 1), position);
//                                    }
//                                } catch (Exception e) {
//                                    Log.d("test", "???? ?????????????????? ???? ??????????, ???????????????????? ????????! :)");
//                                }
//                            }
//                        }
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    // ?????????????????? ???????????????? ??????????, ????????????????, ???????????? ?????? SecondaryDrawerItem
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
                        }
                        return false;
                    }
                })
                .build();


    }

    private void sendToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.google_signIn1://player

            case R.id.asplayer:
                sOut("player");
                signInPlayerInfo();
                break;

            case R.id.google_signIn2://master

            case R.id.asmaster:
                signInMasterInfo();
                break;

            case R.id.approvelogin://?????????????????????? ????????
                try {
                    doLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }

    }

    private void doLogin() throws Exception {
        String a_name = anketname.getText().toString();
        String a_email = anketemail.getText().toString();
        String a_phone = anketphone.getText().toString();
        String a_addinfo = anketaddinfo.getText().toString();

        if (a_name.length() == 0 || a_email.length() == 0 || a_phone.length() < 8 || a_addinfo.length() == 0) {
            sendToast("???????? ???? ?????????????????? ?????? ?????????????????? ??????????????");
            return;
        }
        if (status) {
            new uploadAsyncTask().execute("addmaster", RTS(a_name), RTS(a_email), RTS(a_phone), RTS(a_addinfo));
        }

        if (!status) {
            new uploadAsyncTask().execute("addplayer", RTS(a_name), RTS(a_email), RTS(a_phone), RTS(a_addinfo));
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_INplayer) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result, false);
        }

        if (requestCode == RC_SIGN_INmaster) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result, true);
        }
    }

    private void signInPlayerInfo() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_INplayer);
    }

    private void signInMasterInfo() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_INmaster);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class uploadAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            postData(params[0], params[1], params[2], params[3], params[4]);
            return null;
        }

        protected void onPostExecute(Double result) {
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(String isUri, String post1, String post2, String post3, String post4) {
            HttpClient httpclient = new DefaultHttpClient();
            String getUri = String.format(DOMAIN + "%s.php", isUri);
            HttpPost httppost = new HttpPost(getUri);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("post1", post1));
                nameValuePairs.add(new BasicNameValuePair("post2", post2));
                nameValuePairs.add(new BasicNameValuePair("post3", post3));
                nameValuePairs.add(new BasicNameValuePair("post4", post4));
//                nameValuePairs.add(new BasicNameValuePair("event",event ));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                sOut(getUri + ", " + post1 + ", " + post2 + ", " + post3);


                if (getUri.contains("addplayer")) {
                    try {
                        getplayer = "http://a0568345.xsph.ru/demiorg/getplayer.php/get.php?dp=" + RTStoJSON(email);

                        sOut(getplayer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getJSON(getplayer);
                }

                if (getUri.contains("addmaster")) {
                    try {
                        getmaster = "http://a0568345.xsph.ru/demiorg/getmaster.php/get.php?dp=" + RTStoJSON(email);
                        sOut(getmaster);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    getJSON(getmaster);
                }


            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

        }
    }

    public void sOut(String message) {
        System.out.println(message);
    }

    public void getJSON(final String urlWebService) {
        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // Log.d("dataJSON", "s is: "+s);

                if (s != null) {
                    try {

                        if (urlWebService == getmaster) {
                            loadMaster(s);
                        }

                        if (urlWebService == getplayer) {
                            loadPlayer(s);
                        }

                        if (urlWebService == getOnlyEvent) {
                            loadOnlyEvent(s);
                        }

                        if (urlWebService == geteventinfo) {
                            loadeventinfo(s);
                        }

                        if (urlWebService == getonlyareas) {
                            loadareas(s);
                        }

                        if (urlWebService == getmysubevents) {
                            loadmysubevents(s);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            protected String doInBackground(Void... voids) {


                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }


        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadMaster(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Log.d("MasterArray", "Array is: " + jsonArray);
        //  PoligonsAdapter poligonsAdapter;
        if (jsonArray.length() == 0) {
            sendToast("???????????????????????? ???? ????????????, ??????????????????" +
                    " ???????????????????? ????????????");
            toolbar.setVisibility(View.GONE);
            toolbartext.setText("??????????????????????");
            anketa.setVisibility(View.VISIBLE);
            return;
        }
        if (jsonArray.length() > 0) {
            JSONObject obj = jsonArray.getJSONObject(0);
            String _id = obj.getString("id");
            String name = (DTS(obj.getString("field1")));
            String phone = (obj.getString("field2"));
            String addinfo = (DTS(obj.getString("field3")));
            String approved = obj.getString("field4");
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ID, _id);
            editor.putString(FIO, name);
            editor.putString(PHONENUM, phone);
            editor.putString(ADDINFO, addinfo);
            editor.putString(APPROVED, approved);
            editor.putString(STATUS, "master");
            editor.apply();
            toolbar.setVisibility(View.VISIBLE);
            anketa.setVisibility(View.GONE);
            toolbartext.setText("?????????????? ??????????????");

            masterLL.setVisibility(View.VISIBLE);
            sOut("ID is:" + settings.getString(APPROVED, ""));
            mastername.setText(settings.getString(FIO, ""));

            String _approve = settings.getString(APPROVED, "");
            if (_approve.equals("0")) {
                masterapprove.setText("???????????? ?????????????? ???????? ???? ??????????????");
                masterapprove.setTextColor(getResources().getColor(R.color.red));
            } else {
                masterapprove.setText("???????????? ?????????????? ??????????????");
                masterapprove.setTextColor(getResources().getColor(R.color.black));
            }
            NavMasterPanel();

        }


    }


    private void loadPlayer(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Log.d("PlayerArray", "Array is: " + jsonArray);
        //  PoligonsAdapter poligonsAdapter;
        if (jsonArray.length() == 0) {
            sendToast("???????????????????????? ???? ????????????, ??????????????????" +
                    " ???????????????????? ????????????");
            toolbar.setVisibility(View.GONE);
            toolbartext.setText("??????????????????????");
            anketa.setVisibility(View.VISIBLE);
            return;
        }
        if (jsonArray.length() > 0) {
            JSONObject obj = jsonArray.getJSONObject(0);
            String _id = obj.getString("id");
            String name = (DTS(obj.getString("field1")));
            String phone = DTS(obj.getString("field2"));
            String addinfo = (DTS(obj.getString("field3")));
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ID, _id);
            editor.putString(FIO, name);
            editor.putString(PHONENUM, phone);
            editor.putString(ADDINFO, addinfo);
            editor.putString(STATUS, "master");
            editor.apply();
            toolbar.setVisibility(View.VISIBLE);
            anketa.setVisibility(View.GONE);
            toolbartext.setText("?????????????? ????????????");

        }


    }

    private void loadOnlyEvent(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        MainListAdapter mainListAdapter;

        if (jsonArray.length() == 0) {
            sendToast("?????????????? ??????????????");
            return;
        }
        if (jsonArray.length() > 0) {
            sOut("" + jsonArray);
            ArrayList<MainList> mainLists = new ArrayList<>();
            String[] id = new String[jsonArray.length()];
            String[] name = new String[jsonArray.length()];
            String[] info = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                id[i] = obj.getString("field1");
                name[i] = DTS(obj.getString("field2"));
                info[i] = obj.getString("field3");
                mainLists.add(new MainList(id[i], name[i], info[i]));
                sOut("positionID: " + mainLists.get(i).id);

            }

            mainListAdapter = new MainListAdapter(context, mainLists, activity);
            maineventslist.setAdapter(mainListAdapter);
            eventslistformysubevent.setAdapter(mainListAdapter);

        }


    }

    private void loadareas(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        commonlist.clear();
        if (jsonArray.length() == 0) {

            return;
        }
        if (jsonArray.length() > 0) {

            sOut("commonlist " + jsonArray);
            String[] id = new String[jsonArray.length()];
            String[] name = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                id[i] = obj.getString("field1");
                name[i] = (obj.getString("field2"));
                commonlist.add(new MainList(id[i], name[i], ""));
                sOut(commonlist.get(i).addinfo);
            }

            for (int i = 0; i < commonlist.size(); i++) {
                sOut("Is commonlist data:" + commonlist.get(i).addinfo);
            }


        }


    }

    private void loadmysubevents(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<dataFour> mainLists = new ArrayList<>();
        if (jsonArray.length() == 0) {

            return;
        }
        if (jsonArray.length() > 0) {

            sOut("commonlist " + jsonArray);
            String[] id = new String[jsonArray.length()];
            String[] name = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                id[i] = obj.getString("field1");
                name[i] = (obj.getString("field2"));
                commonlist.add(new MainList(id[i], name[i], ""));
                sOut(commonlist.get(i).addinfo);
            }

            for (int i = 0; i < commonlist.size(); i++) {
                sOut("Is commonlist data:" + commonlist.get(i).addinfo);
            }


        }


    }


    private void loadeventinfo(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        sOut("" + jsonArray);
        dataFourAdapter dataFourAdapter;
        ArrayList<dataFour> dataFours = new ArrayList<>();
        if (jsonArray.length() == 0) {
            return;
        }
        if (jsonArray.length() > 0) {

            JSONObject obj = jsonArray.getJSONObject(0);
            String data = DTS(obj.getString("field1"));
            sOut("" + data);
            dataFours.add(new dataFour("", "??????????????: ", data, ""));
            data = obj.getString("field2");
            dataFours.add(new dataFour("", "?????????? ????????????????????: ", data, ""));
            data = DTS(obj.getString("field3"));
            dataFours.add(new dataFour("", "????????????????: ", data, ""));
            data = obj.getString("field4");
            String link = obj.getString("field5");
            sOut(link);
            dataFours.add(new dataFour("", "?????????? ???????????????????? (?????????????? ?? ????????????????????): ", data, link));
            data = obj.getString("field6");
            dataFours.add(new dataFour("", "???????????????? ????????????????????????:", DTS(data), ""));
            data = obj.getString("field7");
            dataFours.add(new dataFour(DTS(data), "", "???????????????? ???????? ????????????????????", ""));
            dataFours.add(new dataFour("", "", "???????????????? ???????????? ????????????????", "areas"));

        }

        dataFourAdapter = new dataFourAdapter(context, dataFours, activity);
        maineventslistinfo.setAdapter(dataFourAdapter);


    }


    private void handleSignInResult(GoogleSignInResult result, boolean _status) {
        if (result.isSuccess()) {


            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            email = acct.getEmail();
            anketname.setText(personName);
            anketemail.setText(email);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(EMAIL, email);
            editor.apply();
            sendToast("?????????????????????? ???????????? ??????????????");
            if (_status) {
                startanket.setText("???????????? ??????????????");
                status = true;
                try {
                    getmaster = "http://a0568345.xsph.ru/demiorg/getmaster.php/get.php?dp=" + RTStoJSON(email);

                    sOut(getmaster);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getJSON(getmaster);
            }

            if (!_status) {
                startanket.setText("???????????? ??????????????????");
                status = false;

                try {
                    getplayer = "http://a0568345.xsph.ru/demiorg/getplayer.php/get.php?dp=" + RTStoJSON(email);

                    sOut(getplayer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getJSON(getplayer);
            }

        } else {
            // Signed out, show unauthenticated UI.
            sendToast("??????-???? ?????????? ???? ??????");
        }
    }


    private String RTS(String s) throws Exception {
        byte[] encrypted = new byte[0];
        encrypted = refact(s, api);
        String rts = Base64.encodeToString(encrypted, Base64.DEFAULT);
        rts = rts.substring(0, rts.length() - 1);
        return rts;
    }

    private String DTS(String s) {
        String dts = null;
        try {
            dts = defact(Base64.decode(s, Base64.DEFAULT), api);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dts;
    }


    private String RTStoJSON(String s) throws Exception {
        byte[] encrypted = new byte[0];
        encrypted = refact(s, api);
        String rts = Base64.encodeToString(encrypted, Base64.DEFAULT);
        rts = rts.substring(0, rts.length() - 1);
        rts = rts.replace("+", "%2b");
        rts = rts.replace("\\", "%2b");
        rts = rts.replace(",", "%2c");
        rts = rts.replace(":", "%3a");
        rts = rts.replace(";", "%3b");
        rts = rts.replace("?", "%3f");
        rts = rts.replace("@", "40%");
        return rts;
    }

    AlertDialog.Builder alert;
    AlertDialog dialog;
    Uri uri;
    ListView dialoglist;

    private void showListDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.showlistdialog, null);
        dialoglist = view.findViewById(R.id.dialoglist);
        Button dismiss = view.findViewById(R.id.dismiss);
        MainListAdapter adapter = new MainListAdapter(this, commonlist, this);
        dialoglist.setAdapter(adapter);
        for (int i = 0; i < commonlist.size(); i++) {
            sOut(commonlist.get(i).name);
        }
        alert.setView(view);
        alert.setCancelable(false);
        // mess.setText("?????????????? ???????????????????");
        dialog = alert.create();


        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    public static ArrayList<MainList> itemsForEditText = new ArrayList<MainList>();

    private void showListWithEditTextDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(this);
        }
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.showlistwithedittextdialog, null);
        dialoglist=view.findViewById(R.id.dialoglistwithedittext);
        Button dismiss = view.findViewById(R.id.dismiss);
        Button createsubevent=view.findViewById(R.id.createsubevent);
        MyAdapter myAdapter = new MyAdapter(this, itemsForEditText );
        dialoglist.setAdapter(myAdapter);
//        ArrayList<EditModel> editModelArrayList = populateList();
//         CustomAdapter customeAdapter = new CustomAdapter(this,editModelArrayList);
//  dialoglist.setAdapter(customeAdapter);

        for (int i=0;i<commonlist.size();i++)
        {
            sOut(commonlist.get(i).name);
        }
        alert.setView(view);
        alert.setCancelable(false);
        // mess.setText("?????????????? ???????????????????");
        dialog = alert.create();



        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        createsubevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                         String upload="'"+itemsForEditText.get(0).addinfo+"', '"+itemsForEditText.get(1).addinfo+"','0', '"
                        +itemsForEditText.get(2).addinfo+"', '"+itemsForEditText.get(3).addinfo+"', '"
                        +itemsForEditText.get(4).addinfo+"', '"+itemsForEditText.get(5).addinfo+"', '"
                        +itemsForEditText.get(6).addinfo+"', '"+itemsForEditText.get(7).addinfo+"', '"
                        +itemsForEditText.get(8).addinfo+"', '"+itemsForEditText.get(9).addinfo+"', '"
                        +eventID+"', '"+"0', '"+settings.getString(ID,"")+"', '"+"0'";

                         sOut(upload);
                         new uploadAsyncTask().execute("addsubevent", upload,"","","","");


            }
        });


        dialog.show();
    }

    private ArrayList<EditModel> populateList(){
        ArrayList<EditModel> list = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            EditModel editModel = new EditModel();
            editModel.setEditTextValue(String.valueOf(i));
            list.add(editModel);
        }
        return list;
    }

}