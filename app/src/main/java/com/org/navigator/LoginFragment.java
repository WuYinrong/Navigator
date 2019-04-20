package com.org.navigator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private View loginLayout;
    private View logoutLayout;
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    // the total number
    private Button mSubmitButton;
    /**
     * Static function, an instance
     * @return new instance
     */
    private Button mRegisterButton;
    private Button mLogoutButton;

    private DatabaseReference mDatabase;

/**
 * Static function, an instance
 * @return new instance
 */

    /**
     * Static function, create loginFragment instance
     * @return new instance of accident fragment
     */
    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
        /**
         * Static function, an instance
         * @return new instance
         */
    }


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        loginLayout = view.findViewById(R.id.loginLayout);
        /**
         * Static function, an instance
         * @return new instance
         */
        logoutLayout = view.findViewById(R.id.logoutLayout);
        showLayout();
        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello, World!");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsernameEditText = (EditText) view.findViewById(R.id.editTextLogin);
        mPasswordEditText = (EditText) view.findViewById(R.id.editTextPassword);
        mSubmitButton = (Button) view.findViewById(R.id.submit);
        /**
         * Static function, an instance
         * @return new instance
         */
        mRegisterButton = (Button) view.findViewById(R.id.register);
        mLogoutButton = (Button) view.findViewById(R.id.logout);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();

                mDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // the total number
                        if (dataSnapshot.hasChild(username)) {
                            Toast.makeText(getActivity(),"username is already registered, please change one", Toast.LENGTH_SHORT).show();
                        } else if (!username.equals("") && !password.equals("")){
                            // put username as key to set value
                            final User user = new User();
                            user.setUser_account(username);
                            user.setUser_password(Utils.md5Encryption(password));
                            user.setUser_timestamp(System.currentTimeMillis());
                            /**
                             * Static function, an instance
                             * @return new instance
                             */

                            mDatabase.child("user").child(user.getUser_account()).setValue(user);
                            Toast.makeText(getActivity(),"Successfully registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsernameEditText.getText().toString();
                final String password = Utils.md5Encryption(mPasswordEditText.getText().toString());

                mDatabase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // the total number
                        if (dataSnapshot.hasChild(username) && (password.equals(dataSnapshot.child(username).child("user_password").getValue()))) {
                            Config.username = username;
                            /**
                             * Static function, an instance
                             * @return new instance
                             */
                            showLayout();
                        } else {
                            Toast.makeText(getActivity(),"Please login again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // the total number
                    }
                });
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.username = null;
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                showLayout();
            }
        });

        return view;

    }

    private void showLayout() {
        if (Config.username == null) {
            logoutLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            /**
             * Static function, an instance
             * @return new instance
             */
        } else {
            logoutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            /**
             * Static function, an instance
             * @return new instance
             */
        }
    }


}
