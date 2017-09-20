package com.ieitlabs.peon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import cz.msebera.android.httpclient.util.TextUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCreateGroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCreateGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  FragmentCreateGroup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentCreateGroup() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCreateGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCreateGroup newInstance(String param1, String param2) {
        FragmentCreateGroup fragment = new FragmentCreateGroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment_create_group, container, false);
        Button btnCreateGroup = (Button) v.findViewById(R.id.btn_create_group);
        final EditText groupTitle = (EditText)v.findViewById(R.id.group_title);
        final EditText ownerEmail = (EditText)v.findViewById(R.id.owner_email);

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupTitle.setError(null);
                ownerEmail.setError(null);
                boolean cancel = false;
                if(TextUtils.isEmpty(groupTitle.getText().toString()))
                {
                    groupTitle.setError("Empty title");
                    groupTitle.requestFocus();
                    cancel = true;
                }
                if(TextUtils.isEmpty(ownerEmail.getText().toString()))
                {
                    ownerEmail.setError("Empty email");
                    ownerEmail.requestFocus();
                    cancel = true;
                }
                if(!isEmailValid(ownerEmail.getText().toString()))
                {
                    ownerEmail.setError("Invalid email");
                    ownerEmail.requestFocus();
                    cancel = true;
                }

                if(!cancel)
                {
                    try
                    {
                        DatabaseAdapter d = new DatabaseAdapter(getContext());
                        Toast.makeText(getContext(),"Info: Please wait!!",Toast.LENGTH_LONG).show();
                        String url= "http://peon.ml/api/creategroup?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses="+URLEncoder.encode(d.getAppMeta("session"),"UTF-8")+"&title="+URLEncoder.encode(groupTitle.getText().toString(),"UTF-8")+"&owner="+ URLEncoder.encode(ownerEmail.getText().toString(),"UTF-8");
                        ServerTasker mGroupCreateTask = new ServerTasker(getContext(),getActivity(),3,url);
                        mGroupCreateTask.execute((Void)null);

                    }
                    catch (Exception e)
                    {
                    e.printStackTrace();
                        Toast.makeText(getContext(),"Error: Something wrong!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return v;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(TextUtils.isEmpty(email))
        {
            return false;
        }
        return email.contains("@") ;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
