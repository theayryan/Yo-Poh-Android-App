package ayushb.com.yo_poh;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ayushb.com.yo_poh.YoPohClasses.Product;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ayushb on 21/11/15.
 */
public class AddComplaintDialog extends DialogFragment {
    private static AddComplaintDialog instance;
    private FragmentActivity activity;
    private YoPohApi yoPohApi;
    private SharedPreferences prefs;
    private ProgressDialog progress;
    private ArrayList<String> spinnerOptions = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private Product chosenProduct = new Product();

    public static AddComplaintDialog getInstance() {
        instance = new AddComplaintDialog();
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        //request a window without title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_complaint_dialog, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final Spinner productsOptions = (Spinner) view.findViewById(R.id.products);
        Button addComplaint = (Button) view.findViewById(R.id.add_complaint_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yoPohApi = retrofit.create(YoPohApi.class);
        showProgressDialog();
        yoPohApi.getMyProducts(prefs.getString(Constants.GCM_REG_KEY,"")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if(response!=null) {
                    String result = responseString(response);
                    if(!TextUtils.isEmpty(result)){
                        try {
                            JSONArray array = new JSONArray(result);
                            for (int i = 0;i<array.length();i++){
                                JSONObject jsonObject = array.getJSONObject(i);
                                Product product = new Product();
                                product.setProductId(jsonObject.getString("productId"));
                                product.setCategory(jsonObject.getString("category"));
                                product.setPrice(jsonObject.getString("price"));
                                product.setCompanyId(jsonObject.getString("companyId"));
                                product.setProductName(jsonObject.getString("productName"));
                                spinnerOptions.add(product.getProductName());
                            }
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,android.R.layout.select_dialog_item,spinnerOptions);
                            productsOptions.setAdapter(spinnerAdapter);
                            if(progress.isShowing()){
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (progress.isShowing())
                                progress.dismiss();
                            Toast.makeText(activity,"Some Problem Occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(activity,"Server Problem",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private String responseString(Response<ResponseBody> response){
        ResponseBody result = response.body();
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
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
        return sb.toString();
    }

    private void showProgressDialog(){
        if(progress==null) {
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Setting Up...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
        }
        if(!progress.isShowing())
            progress.show();
    }


}
