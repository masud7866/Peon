package com.ieitlabs.peon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.util.TextUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAddUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAddUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAddUser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentAddUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAddUser.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAddUser newInstance(String param1, String param2) {
        FragmentAddUser fragment = new FragmentAddUser();
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
        View v =  inflater.inflate(R.layout.fragment_fragment_add_user, container, false);

        final EditText txtOwnerEmail = (EditText)v.findViewById(R.id.add_user_text);
        final Button btnInviteUser = (Button)v.findViewById(R.id.add_user_button);
        final Spinner spinnerRole = (Spinner) v.findViewById(R.id.spinner_add_user_role);



      btnInviteUser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              txtOwnerEmail.setError(null);
              Boolean cancel = false;
              String strPos = Integer.toString(spinnerRole.getSelectedItemPosition()+1);
              if(!isEmailValid(txtOwnerEmail.getText().toString()))
              {
                  cancel = true;
                  txtOwnerEmail.requestFocus();
                  txtOwnerEmail.setError("Invalid Email");
              }
              if(!cancel)
              {
                  try
                  {
                      DatabaseAdapter d = new DatabaseAdapter(getContext());
                      Toast.makeText(getContext(),"Info: Please wait!!",Toast.LENGTH_LONG).show();
                      String url= "http://peon.ml/api/invitetogroup?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses="+URLEncoder.encode(d.getAppMeta("session"),"UTF-8")+"&gid="+URLEncoder.encode(d.getAppMeta("group_role"),"UTF-8")+"&owner="+ URLEncoder.encode(txtOwnerEmail.getText().toString(),"UTF-8")+"&role="+ URLEncoder.encode(strPos,"UTF-8");
                      //Log.d("FragmentCreateGroup",url);
                      ServerTasker mGroupCreateTask = new ServerTasker(getContext(),getActivity(),5,url);
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
