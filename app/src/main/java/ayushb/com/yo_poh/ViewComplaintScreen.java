package ayushb.com.yo_poh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ayushb.com.yo_poh.YoPohClasses.Complaint;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ayushb on 22/11/15.
 */
public class ViewComplaintScreen extends Fragment {
    private FragmentActivity activity;
    private SharedPreferences prefs;
    private ListView complaintsListView;
    private YoPohApi yoPohApi;
    private ProgressDialog progress;
    private ArrayList<Complaint> complaints = new ArrayList<>();
    ComplaintsAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_complaints_layout, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        complaintsListView = (ListView) view.findViewById(R.id.complaints_list);

        complaintsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Complaint chosenComplaint = complaints.get(position);
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra(Constants.CHAT_DETAILS,chosenComplaint);
                getActivity().startActivity(intent);
            }
        });

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yoPohApi = retrofit.create(YoPohApi.class);

        showProgressDialog();

        yoPohApi.getMyTickets(prefs.getString(Constants.GCM_REG_KEY, "")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if(response!=null){
                    String result =responseString(response);
                    if(!TextUtils.isEmpty(result)){
                        try {
                            JSONArray array = new JSONArray(result);
                            for(int i = 0;i<array.length();i++){
                                Complaint complaint = new Complaint();
                                JSONObject jsonObject = array.getJSONObject(i);
                                complaint.setProductName(jsonObject.getString("productName"));
                                complaint.setProductId(jsonObject.getString("productId"));
                                complaint.setCompanyId(jsonObject.getString("companyId"));
                                complaint.setChannel(jsonObject.getString("channel"));
                                complaint.setCustomerId(jsonObject.getString("customerId"));
                                complaint.setCustomerName(jsonObject.getString("customerName"));
                                complaint.setDateCreated(jsonObject.getLong("dateCreated"));
                                complaint.setTicketNumber(jsonObject.getLong("ticketNumber"));
                                complaints.add(complaint);
                            }
                            adapter = new ComplaintsAdapter(activity,complaints,activity);
                            complaintsListView.setAdapter(adapter);
                            if(progress.isShowing())
                                progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if(progress.isShowing())
                                progress.dismiss();
                            Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if(progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity,"Server Error",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private String responseString(Response<ResponseBody> response) {
        ResponseBody result = response.body();
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        if(result!=null) {
            try {

                reader = new BufferedReader(new InputStreamReader(result.byteStream()));

                String line;

                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private void showProgressDialog() {
        if (progress == null) {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Setting Up...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
        }
        if (!progress.isShowing())
            progress.show();
    }

}
