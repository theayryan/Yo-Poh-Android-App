package ayushb.com.yo_poh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ayushb.com.yo_poh.YoPohClasses.Company;
import ayushb.com.yo_poh.YoPohClasses.Product;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ayushb on 21/11/15.
 */
public class AddProductScreen extends Fragment {
    AutoCompleteTextView name, companyTextView, categoryTextView;
    EditText price;
    Button addProductButton;
    private FragmentActivity activity;
    private SharedPreferences prefs;
    private ArrayList<Company> companyArrayList = new ArrayList<>();
    private ArrayList<String> companyNames = new ArrayList<>();
    private ArrayList<Product> productArrayList = new ArrayList<>();
    private ArrayList<String> productNames = new ArrayList<>();
    private YoPohApi yoPohApi;
    private ProgressDialog progress;
    private String[] categories = new String[]{"Electronics", "Mobiles", "Computers"};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_product_layout, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


        companyTextView = (AutoCompleteTextView) view.findViewById(R.id.company_name);
        categoryTextView = (AutoCompleteTextView) view.findViewById(R.id.category);
        name = (AutoCompleteTextView) view.findViewById(R.id.name);
        price = (EditText) view.findViewById(R.id.price);
        addProductButton = (Button) view.findViewById(R.id.add_product_button);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.6.3.177:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yoPohApi = retrofit.create(YoPohApi.class);
        showProgressDialog();

        yoPohApi.getAllProducts().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                if (response != null) {
                    String resultString = responseString(response);
                    Log.d("Response", resultString);
                    try {
                        JSONArray array = new JSONArray(resultString);
                        if (array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                Product product = new Product();
                                product.setProductName(object.getString("productName"));
                                product.setCompanyId(object.getString("companyId"));
                                productNames.add(object.getString("productName"));
                                product.setProductId(object.getString("productId"));
                                product.setCategory(object.getString("category"));
                                product.setPrice(object.getString("price"));
                                productArrayList.add(product);
                            }
                            ArrayAdapter<String> productsAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, productNames);
                            name.setAdapter(productsAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progress.isShowing())
                            progress.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        yoPohApi.getAllCompanies().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                String resultString = responseString(response);
                Log.d("Response", resultString);
                if (progress.isShowing()) {
                    progress.dismiss();
                }
                try {
                    JSONArray array = new JSONArray(resultString);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Company company = new Company();
                        company.setCompanyDivision(object.getString("companyDivision"));
                        company.setCompanyId(object.getString("companyId"));
                        companyNames.add(object.getString("companyName"));
                        company.setCompanyName(object.getString("companyName"));
                        companyArrayList.add(company);
                    }
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, companyNames);
                    companyTextView.setAdapter(companyAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progress.isShowing())
                        progress.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (progress.isShowing())
                    progress.dismiss();
                Toast.makeText(getActivity(), "Could not connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item, categories);
        categoryTextView.setAdapter(categoriesAdapter);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = null;
                for (Product product : productArrayList) {
                    if (product.getProductName().equalsIgnoreCase(name.getText().toString())) {
                        productId = product.getProductId();
                    }
                }
                yoPohApi.addProduct(
                        prefs.getString(Constants.GCM_REG_KEY, ""),
                        productId
                ).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        String resultString = responseString(response);
                        Log.d("Response", resultString);
                        if (!TextUtils.isEmpty(resultString)) {
                            Toast.makeText(activity, "Product Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "Some problem occurred", Toast.LENGTH_SHORT).show();
                        }
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(activity, "Some problem occurred", Toast.LENGTH_SHORT).show();
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }
                    }
                });
            }
        });
        return view;
    }

    private String responseString(Response<ResponseBody> response) {
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
