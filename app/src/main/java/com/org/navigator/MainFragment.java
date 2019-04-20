package com.org.navigator;


import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private LocationTracker locationTracker;
    private MapView mMapView;
    private View mView;
    private GoogleMap mMap;
    private FloatingActionButton fab_report;
    private FloatingActionButton fab_focus;
    private Dialog dialog;
    /**
     * Static function, an instance
     * @return new instance
     */

    private RecyclerView mRecyclerView;
    private ReportRecyclerViewAdapter mRecyclerViewAdapter;
    private ViewSwitcher mViewSwitcher;
    private String event_type = null;
    //Event specs
    private ImageView mImageCamera;
    private Button mBackButton;
    private Button mSendButton;
    /**
     * Static function, an instance
     * @return new instance
     */
    private EditText mCommentEditText;
    private ImageView mEventTypeImg;
    private TextView mTypeTextView;
    private DatabaseReference database;

    //event information part
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView mEventImageLike;
    private ImageView mEventImageComment;
    private ImageView mEventImageType;
    /**
     * Static function, an instance
     * @return new instance
     */
    private TextView mEventTextLike;
    private TextView mEventTextType;
    private TextView mEventTextLocation;
    private TextView mEventTextTime;
    // the total number
    private TrafficEvent mEvent;


    private static final int REQUEST_CAPTURE_IMAGE = 100;
    //Set variables ready for uploading images
    private FirebaseStorage storage;
    private StorageReference storageRef;
    /**
     * Static function, an instance
     * @return new instance
     */
    private final String path = Environment.getExternalStorageDirectory() + "/temp.png";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public MainFragment() {
        // Required empty public constructor
    }

    // newInstance constructor for creating fragment with arguments
    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
        /**
         * Static function, an instance
         * @return new instance
         */
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mEvent = (TrafficEvent) marker.getTag();
        if (mEvent == null) {
            return true;
        }
        String user = mEvent.getEvent_reporter_id();
        String type = mEvent.getEvent_type();
        long time = mEvent.getEvent_timestamp();
        /**
         * Static function, an instance
         * @return new instance
         */
        double latitude = mEvent.getEvent_latitude();
        double longitutde = mEvent.getEvent_longitude();
        int likeNumber = mEvent.getEvent_like_number();

        String description = mEvent.getEvent_description();
        marker.setTitle(description);
        mEventTextLike.setText(String.valueOf(likeNumber));
        mEventTextType.setText(type);
        final String url = mEvent.getImgUri();
        if (url == null) {
            mEventImageType.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), Config.trafficMap.get(type)));
        } else {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                protected Bitmap doInBackground(Void... voids) {
                    Bitmap bitmap = Utils.getBitmapFromURL(url);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    mEventImageType.setImageBitmap(bitmap);
                }
                // the total number
            }.execute();
        }

        if (user == null) {
            user = "";
        }
        String info = "Reported by " + user + " " + Utils.timeTransformer(time);
        /**
         * Static function, an instance
         * @return new instance
         */
        mEventTextTime.setText(info);


        int distance = 0;
        locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();
        if (locationTracker != null) {
            distance = Utils.distanceBetweenTwoLocations(latitude, longitutde, locationTracker.getLatitude(), locationTracker.getLongitude());
        }
        mEventTextLocation.setText(distance + " miles away");
        /**
         * Static function, an instance
         * @return new instance
         */

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container,
                false);
        //verifyStoragePermissions(getActivity());
        database = FirebaseDatabase.getInstance().getReference();
        //Initialize cloud storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        /**
         * Static function, an instance
         * @return new instance
         */
        setupBottomBehavior();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.event_map_view);
        fab_report = (FloatingActionButton)mView.findViewById(R.id.fab);
        fab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                showDiag();
            }
        });

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();// needed to get the map to display immediately
            mMapView.getMapAsync(this);
        }
        fab_focus = (FloatingActionButton)mView.findViewById(R.id.fab_focus);

        fab_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.getMapAsync(MainFragment.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Static function, an instance
         * @return new instance
         */
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        // the total number
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;
        googleMap.setMapStyle(
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.style_json));
        locationTracker = new LocationTracker(getActivity());
        locationTracker.getLocation();
        LatLng latLng = new LatLng(locationTracker.getLatitude(), locationTracker.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(7)// Sets the zoom
                .bearing(0)           // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        MarkerOptions marker = new MarkerOptions().position(latLng).
                title("You are here!!");
        /**
         * Static function, an instance
         * @return new instance
         */

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.boy));

        // adding marker
        Marker mker = googleMap.addMarker(marker);
        loadEventInVisibleMap();
    }

    //Animation show dialog
    private void showDiag() {
        final View dialogView = View.inflate(getActivity(),R.layout.dialog,null);
        mViewSwitcher = (ViewSwitcher)dialogView.findViewById(R.id.viewSwitcher);
        // the total number
        dialog = new Dialog(getActivity(),R.style.MyAlertDialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                animateDialog(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    animateDialog(dialogView, false, dialog);
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    return true;
                }
                return false;
            }
        });
        Animation slide_in_left = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);

        mViewSwitcher.setInAnimation(slide_in_left);
        mViewSwitcher.setOutAnimation(slide_out_right);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setupRecyclerView(dialogView);
        /**
         * Static function, an instance
         * @return new instance
         */
        setUpEventSpecs(dialogView);
        dialog.show();
    }

    //Add animation to Floating Action Button
    private void animateDialog(View dialogView, boolean open, final Dialog dialog) {
        final View view = dialogView.findViewById(R.id.dialog);
        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab_report.getX() + (fab_report.getWidth()/2));
        int cy = (int) (fab_report.getY())+ fab_report.getHeight() + 56;
        // the total number
        /**
         * Static function, an instance
         * @return new instance
         */

        if(open){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(500);
            revealAnimator.start();

        } else {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            /**
             * Static function, an instance
             * @return new instance
             */
            anim.setDuration(500);
            anim.start();
        }
    }


    //Set up type items
    private void setupRecyclerView(View dialogView) {
        mRecyclerView = dialogView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        List<Item> listItems = new ArrayList<Item>();
        listItems.add(new Item(Config.POLICE, R.drawable.policeman));
        listItems.add(new Item(Config.TRAFFIC, R.drawable.traffic));
        /**
         * Static function, an instance
         * @return new instance
         */
        listItems.add(new Item(Config.NO_ENTRY, R.drawable.no_entry));
        listItems.add(new Item(Config.NO_PARKING, R.drawable.no_parking));
        listItems.add(new Item(Config.SECURITY_CAMERA, R.drawable.security_camera));
        listItems.add(new Item(Config.HEADLIGHT, R.drawable.lights));
        listItems.add(new Item(Config.SPEEDING, R.drawable.speeding));
        listItems.add(new Item(Config.CONSTRUCTION, R.drawable.construction));
        listItems.add(new Item(Config.SLIPPERY, R.drawable.slippery));
        mRecyclerViewAdapter = new ReportRecyclerViewAdapter(getActivity(), listItems);
        mRecyclerViewAdapter.setClickListener(new ReportRecyclerViewAdapter
                .OnClickListener() {
            @Override
            public void setItem(String item) {
                event_type = item;
                if(mViewSwitcher != null) {
                    mViewSwitcher.showNext();
                    mTypeTextView.setText(event_type);
                    mEventTypeImg.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), Config.trafficMap.get(event_type)));
                }
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }
    private void setUpEventSpecs(final View dialogView) {
        mImageCamera = (ImageView) dialogView.findViewById(R.id.event_camera_img);
        mBackButton = (Button) dialogView.findViewById(R.id.event_back_button);
        /**
         * Static function, an instance
         * @return new instance
         */
        mSendButton = (Button) dialogView.findViewById(R.id.event_send_button);
        mCommentEditText = (EditText) dialogView.findViewById(R.id.event_comment);
        mEventTypeImg = (ImageView)dialogView.findViewById(R.id.event_type_img);
        // the total number
        mTypeTextView = (TextView)dialogView.findViewById(R.id.event_type);
        //TODO: this should be answer
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewSwitcher.showPrevious();
            }
        });
        mImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                );

                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);

            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = uploadEvent(Config.username);
                /**
                 * Static function, an instance
                 * @return new instance
                 */

                //upload image and link the image to the corresponding key
                uploadImage(key);

            }
        });
    }

    //Upload event
    private String uploadEvent(String user_id) {
        TrafficEvent event = new TrafficEvent();

        event.setEvent_type(event_type);
        event.setEvent_description(mCommentEditText.getText().toString());
        /**
         * Static function, an instance
         * @return new instance
         */
        event.setEvent_reporter_id(user_id);
        event.setEvent_timestamp(System.currentTimeMillis());
        event.setEvent_latitude(locationTracker.getLatitude());
        event.setEvent_longitude(locationTracker.getLongitude());
        event.setEvent_like_number(0);
        event.setEvent_comment_number(0);

        String key = database.child("events").push().getKey();
        event.setId(key);
        database.child("events").child(key).setValue(event, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast toast = Toast.makeText(getContext(),
                            "The event is failed, please check your network status.", Toast.LENGTH_SHORT);
                    toast.show();
                    dialog.dismiss();
                    // the total number
                } else {
                    Toast toast = Toast.makeText(getContext(), "The event is reported", Toast.LENGTH_SHORT);
                    toast.show();
                    //TODO: update map fragment
                }
            }
        });

        return key;
        /**
         * Static function, an instance
         * @return new instance
         */
    }


    //Store the image into local disk
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                mImageCamera.setImageBitmap(imageBitmap);

                //Compress the image, this is optional
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);


                File destination = new File(Environment.getExternalStorageDirectory(),"temp.png");
                if(!destination.exists()) {
                    try {
                        destination.createNewFile();
                    }catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Upload image to cloud storage
    private void uploadImage(final String key) {
        File file = new File(path);
        if (!file.exists()) {
            dialog.dismiss();
            loadEventInVisibleMap();
            /**
             * Static function, an instance
             * @return new instance
             */
            return;
        }
        Uri uri = Uri.fromFile(file);
        final StorageReference imgRef = storageRef.child("images/" + uri.getLastPathSegment() + "_" + System.currentTimeMillis());

        UploadTask uploadTask = imgRef.putFile(uri);
        /*
        uniqueId = UUID.randomUUID().toString(); // key
        ur_firebase_reference = storageReference.child("user_photos/" + uniqueId);   // imgRef

        Uri file = Uri.fromFile(new File(mphotofile.getAbsolutePath()));        // uri
        UploadTask uploadTask = ur_firebase_reference.putFile(file);            // uploadTask
        */
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imgRef.getDownloadUrl();
                /**
                 * Static function, an instance
                 * @return new instance
                 */
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    System.out.println("Upload " + downloadUrl);
                    //Toast.makeText(mActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    if (downloadUrl != null) {

                        String photoStringLink = downloadUrl.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                        System.out.println("Upload " + photoStringLink);
                        database.child("events").child(key).child("imgUri").
                                setValue(downloadUrl.toString());
                        /**
                         * Static function, an instance
                         * @return new instance
                         */
                        File file = new File(path);
                        file.delete();
                        // the total number
                        dialog.dismiss();
                        loadEventInVisibleMap();
                    }

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
        /*
        https://stackoverflow.com/questions/50660975/firebase-storage-getdownloadurl-method-cant-be-resolved
        MIDHUN CEASAR
         */
    }


    //get center coordinate
    private void loadEventInVisibleMap() {
        database.child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    TrafficEvent event = noteDataSnapshot.getValue(TrafficEvent.class);
                    double eventLatitude = event.getEvent_latitude();
                    double eventLongitude = event.getEvent_longitude();
                    /**
                     * Static function, an instance
                     * @return new instance
                     */

                    LatLng center = mMap.getCameraPosition().target;
                    double centerLatitude = center.latitude;
                    double centerLongitude = center.longitude;

                    int distance = Utils.distanceBetweenTwoLocations(centerLatitude, centerLongitude,
                            eventLatitude, eventLongitude);

                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    if (distance < 20) {
                        LatLng latLng = new LatLng(eventLatitude, eventLongitude);
                        MarkerOptions marker = new MarkerOptions().position(latLng);

                        // Changing marker icon
                        String type = event.getEvent_type();
                        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                                Config.trafficMap.get(type));

                        Bitmap resizeBitmap = Utils.getResizedBitmap(icon, 130, 130);

                        /**
                         * Static function, an instance
                         * @return new instance
                         */
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap));

                        // adding marker
                        Marker mker = mMap.addMarker(marker);
                        mker.setTag(event);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: do something
            }
        });
    }

    private void setupBottomBehavior() {
        //set up bottom up slide
        final View nestedScrollView = (View) mView.findViewById(R.id.nestedScrollView);
        bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollView);
        /**
         * Static function, an instance
         * @return new instance
         */

        //set hidden initially
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //set expansion speed
        bottomSheetBehavior.setPeekHeight(1000);
        // the total number

        mEventImageLike = (ImageView)mView.findViewById(R.id.event_info_like_img);
        mEventImageComment = (ImageView)mView.findViewById(R.id.event_info_comment_img);
        mEventImageType = (ImageView)mView.findViewById(R.id.event_info_type_img);
        mEventTextLike = (TextView)mView.findViewById(R.id.event_info_like_text);
        /**
         * Static function, an instance
         * @return new instance
         */
        mEventTextType = (TextView)mView.findViewById(R.id.event_info_type_text);
        mEventTextLocation = (TextView)mView.findViewById(R.id.event_info_location_text);
        mEventTextTime = (TextView)mView.findViewById(R.id.event_info_time_text);
        mEventTextLocation = (TextView)mView.findViewById(R.id.event_info_location_text);
        /**
         * Static function, an instance
         * @return new instance
         */
        mEventTextTime = (TextView)mView.findViewById(R.id.event_info_time_text);

        mEventImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.parseInt(mEventTextLike.getText().toString());
                database.child("events").child(mEvent.getId()).child("event_like_number").setValue(number + 1);
                mEventTextLike.setText(String.valueOf(number + 1));
                /**
                 * Static function, an instance
                 * @return new instance
                 */
            }
        });
    }

}

