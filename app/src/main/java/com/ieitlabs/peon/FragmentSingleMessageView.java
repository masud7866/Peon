package com.ieitlabs.peon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import cz.msebera.android.httpclient.util.TextUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSingleMessageView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSingleMessageView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSingleMessageView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentSingleMessageView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSingleMessageView.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSingleMessageView newInstance(String param1, String param2) {
        FragmentSingleMessageView fragment = new FragmentSingleMessageView();
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
        View v=  inflater.inflate(R.layout.fragment_fragment_single_message_view, container, false);
       final DatabaseAdapter d = new DatabaseAdapter(getContext());
        final String strConvID = getArguments().getString("conv_id");
        String strSubject = getArguments().getString("subject");
        TextView txtConvID = (TextView)v.findViewById(R.id.txtConvID);
        final TextView txtSubject = (TextView) v.findViewById(R.id.txt_subject);
        txtConvID.setText(strConvID);
        txtSubject.setText(strSubject);
        final GridView gvNoticeBoard = (GridView)v.findViewById(R.id.single_message_grid);

        try
        {
            String url= "http://peon.ml/api/viewsinglemessage?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8") + "&mid=" + URLEncoder.encode(strConvID,"UTF-8");
            //Log.d("ViewGroups",url);
            ServerTasker mViewGroupTask = new ServerTasker(getContext(),getActivity(),15,url);
            mViewGroupTask.v = v;
            mViewGroupTask.gv = gvNoticeBoard;
            mViewGroupTask.execute((Void)null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final EditText txtMessageBox = (EditText)v.findViewById(R.id.txt_message);
        Button btnSend = (Button)v.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMessageBox.setError(null);
                Boolean cancel = false;
                if(TextUtils.isEmpty(txtMessageBox.getText().toString()))
                {
                    cancel = true;
                    txtMessageBox.setError("Message box is empty");
                    txtMessageBox.requestFocus();
                }

                if(!cancel)
                {
                    try
                    {
                        String url= "http://peon.ml/api/pushmessage?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses="+URLEncoder.encode(d.getAppMeta("session"),"UTF-8")+"&mid="+URLEncoder.encode(strConvID,"UTF-8") + "&message=" + URLEncoder.encode(txtMessageBox.getText().toString(),"UTF-8");
                        ServerTasker mSendMessageTask = new ServerTasker(getContext(),getActivity(),16,url);
                        mSendMessageTask.v = v;
                        mSendMessageTask.mid = strConvID;
                        mSendMessageTask.gv = gvNoticeBoard;
                        mSendMessageTask.execute((Void)null);
                        txtMessageBox.setText("");

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
