package com.ieitlabs.peon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import java.net.URLEncoder;

import de.codecrafters.tableview.TableView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNoticeBoard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentNoticeBoard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNoticeBoard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentNoticeBoard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNoticeBoard.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNoticeBoard newInstance(String param1, String param2) {
        FragmentNoticeBoard fragment = new FragmentNoticeBoard();
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
        View v=  inflater.inflate(R.layout.fragment_fragment_notice_board, container, false);
        Button btnPostNotice = (Button)v.findViewById(R.id.btnPostNotice);
        DatabaseAdapter d = new DatabaseAdapter(getContext());
        if(!d.getAppMeta("group_role").equals("1"))
        {
            btnPostNotice.setVisibility(View.GONE);
        }

        btnPostNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FragmentCreateNotice();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });

        GridView gvNoticeBoard = (GridView)v.findViewById(R.id.notice_grid);

        try
        {
            String url= "http://peon.ml/api/viewnotices?u="+ URLEncoder.encode(d.getAppMeta("uid"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");
            //Log.d("ViewGroups",url);
            ServerTasker mViewGroupTask = new ServerTasker(getContext(),getActivity(),9,url);
            mViewGroupTask.v = v;
            mViewGroupTask.gv = gvNoticeBoard;
            mViewGroupTask.execute((Void)null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


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
