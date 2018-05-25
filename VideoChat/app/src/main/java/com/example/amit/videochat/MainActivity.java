package com.example.amit.videochat;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{

    private static String API_KEY = "46125382";
    private static String SESSION_ID = "1_MX40NjEyNTM4Mn5-MTUyNzE1NTUzNzc5NX5DUmtCVG9sZWJ2cTZKSWd0S2oyWUlkQ1l-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjEyNTM4MiZzaWc9M2YxNzkwOTU5ZWM4NTVmNWYwODdlZWZjMTYyNjllNGRjNDNkMWE0MDpzZXNzaW9uX2lkPTFfTVg0ME5qRXlOVE00TW41LU1UVXlOekUxTlRVek56YzVOWDVEVW10Q1ZHOXNaV0oyY1RaS1NXZDBTMm95V1Vsa1ExbC1mZyZjcmVhdGVfdGltZT0xNTI3MTU1NTkwJm5vbmNlPTAuNzUwNzA2NzAzNDY1NzEyJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE1MjcxNzcxODkmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0=";
    private static final int RC_SETTING = 123;

    private Session session;

    private FrameLayout publisherContainer, subscriberContainer;

    private Publisher publisher;
    private Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        publisherContainer = findViewById(R.id.publisher_container);
        subscriberContainer = findViewById(R.id.subscriber_container);

        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this);
    }

    @AfterPermissionGranted(RC_SETTING)
    private void requestPermission(){
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perm)){

            session = new Session.Builder(this,API_KEY,SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This apps need to access you Camera, Audio and Internet",RC_SETTING, perm);
        }
    }

    @Override
    public void onConnected(Session session) {
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        publisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        if (subscriber == null){
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);

            subscriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if (subscriber != null){
            subscriber = null;
            subscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}
