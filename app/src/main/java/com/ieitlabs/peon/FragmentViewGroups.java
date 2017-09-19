package com.ieitlabs.peon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentViewGroups.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentViewGroups#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentViewGroups extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;




    public FragmentViewGroups() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentViewGroups.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentViewGroups newInstance(String param1, String param2) {
        FragmentViewGroups fragment = new FragmentViewGroups();
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
        View v = inflater.inflate(R.layout.fragment_fragment_view_groups, container, false);
        DatabaseAdapter d = new DatabaseAdapter(getContext());


        TableView<String> tableView = (TableView<String>)v.findViewById(R.id.tblGroupView);
        try
        {
            String url= "http://peon.ml/api/viewgroups?u=3"+ URLEncoder.encode(d.getAppMeta("email"),"UTF-8") +"&ses=" + URLEncoder.encode(d.getAppMeta("session"),"UTF-8");
            ServerTasker mViewGroupTask = new ServerTasker(getContext(),getActivity(),4,url);
            mViewGroupTask.v = v;
            mViewGroupTask.execute((Void)null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        String[] spaceProbeHeaders={"Title","Member"};


        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),spaceProbeHeaders));
        tableView.setColumnCount(2);

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
