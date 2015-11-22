package ayushb.com.yo_poh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by ayushb on 22/11/15.
 */
public class ViewComplaintScreen extends Fragment {
    private FragmentActivity activity;
    private SharedPreferences prefs;
    private ListView complaintsListView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_complaints_layout, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        complaintsListView = (ListView) view.findViewById(R.id.complaints_list);
        return view;
    }

}
