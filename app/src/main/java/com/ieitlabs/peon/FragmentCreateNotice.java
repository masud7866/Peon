package com.ieitlabs.peon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCreateNotice.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCreateNotice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCreateNotice extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentCreateNotice() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCreateNotice.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCreateNotice newInstance(String param1, String param2) {
        FragmentCreateNotice fragment = new FragmentCreateNotice();
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
        View v = inflater.inflate(R.layout.fragment_create_notice, container, false);

        final EditText txtNoticeSubject = (EditText)v.findViewById(R.id.txt_create_group_subject);
        final EditText txtNoticeMessage = (EditText)v.findViewById(R.id.txt_create_group_message);
        Button btnCreateNotice = (Button)v.findViewById(R.id.btn_create_notice);

        btnCreateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNoticeSubject.setError(null);
                txtNoticeMessage.setError(null);
                Boolean cancel = false;

                if(TextUtils.isEmpty(txtNoticeMessage.getText().toString()))
                {
                    cancel=true;
                    txtNoticeMessage.setError("Message is empty");
                    txtNoticeMessage.requestFocus();
                }
                if(TextUtils.isEmpty(txtNoticeSubject.getText().toString()))
                {
                    cancel=true;
                    txtNoticeSubject.setError("Subject is empty");
                    txtNoticeSubject.requestFocus();
                }

                if(!cancel)
                {
                    try
                    {
                        DatabaseAdapter d = new DatabaseAdapter(getContext());
                        Toast.makeText(getContext(),"Info: Please wait!!",Toast.LENGTH_LONG).show();
                        String url= "http://peon.ml/api/createnotices?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses="+URLEncoder.encode(d.getAppMeta("session"),"UTF-8")+"&message="+URLEncoder.encode(txtNoticeMessage.getText().toString(),"UTF-8")+"&subject="+ URLEncoder.encode(txtNoticeSubject.getText().toString(),"UTF-8");
                        ServerTasker mNoticeCreateTask = new ServerTasker(getContext(),getActivity(),8,url);
                        mNoticeCreateTask.execute((Void)null);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"Error: Something wrong!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return  v;
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
