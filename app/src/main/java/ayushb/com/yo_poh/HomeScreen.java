package ayushb.com.yo_poh;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by ayushb on 20/11/15.
 */
public class HomeScreen extends Fragment implements View.OnClickListener {

    private FragmentActivity activity;
    private SharedPreferences prefs;
    private Button addProduct, addComplaint, viewComplaints, editDetails;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homescreen_layout, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        addProduct = (Button) view.findViewById(R.id.add_product);
        addComplaint = (Button) view.findViewById(R.id.add_complaint);
        editDetails = (Button) view.findViewById(R.id.edit_details);
        viewComplaints = (Button) view.findViewById(R.id.view_complaints);

        addComplaint.setOnClickListener(this);
        editDetails.setOnClickListener(this);
        addProduct.setOnClickListener(this);
        viewComplaints.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Fragment reg;
        FragmentTransaction ft;
        switch (id){
            case R.id.view_complaints:
                break;
            case R.id.add_complaint:
                break;
            case R.id.add_product:
                reg = new AddProductScreen();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, reg);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.edit_details:
                reg = new LoginFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, reg);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
    }
}
