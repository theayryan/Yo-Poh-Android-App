package ayushb.com.yo_poh;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.ResponseBody;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class LoginFragment extends Fragment {
    SharedPreferences prefs;
    EditText nameInput, mobnoInput, emailIdInput, addressInput;
    Button login;
    List<NameValuePair> params;
    ProgressDialog progress;
    YoPohApi yoPohApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        nameInput = (EditText) view.findViewById(R.id.name);
        mobnoInput = (EditText) view.findViewById(R.id.mobno);
        emailIdInput = (EditText) view.findViewById(R.id.emailId);
        addressInput = (EditText) view.findViewById(R.id.address);

        nameInput.setText(prefs.getString(Constants.NAME, ""));
        mobnoInput.setText(prefs.getString(Constants.MOB_NUMBER, ""));
        emailIdInput.setText(prefs.getString(Constants.USER_EMAIL, ""));
        addressInput.setText(prefs.getString(Constants.USER_ADDRESS, ""));

        login = (Button) view.findViewById(R.id.log_btn);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Registering ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.6.3.177:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        yoPohApi = retrofit.create(YoPohApi.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                final SharedPreferences.Editor edit = prefs.edit();
                edit.putString(Constants.MOB_NUMBER, mobnoInput.getText().toString());
                edit.putString(Constants.NAME, nameInput.getText().toString());
                edit.putString(Constants.USER_EMAIL, emailIdInput.getText().toString());
                edit.putString(Constants.USER_ADDRESS, addressInput.getText().toString());

                Call<ResponseBody> call = yoPohApi.userLogin(
                        prefs.getString(Constants.GCM_REG_KEY, ""),
                        nameInput.getText().toString(),
                        emailIdInput.getText().toString(),
                        mobnoInput.getText().toString(),
                        addressInput.getText().toString()
                );
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
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


                        String resultString = sb.toString();
                        Log.d("Response", resultString);
                        progress.dismiss();
                        if (!TextUtils.isEmpty(resultString)) {
                            edit.putBoolean(Constants.REGISTERED, true);
                            edit.commit();
                            Fragment reg = new HomeScreen();
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, reg);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.commit();
                        } else {
                            Toast.makeText(getActivity(), "Succesfully Registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d("Error", t.getMessage());
                        progress.dismiss();
                    }
                });
            }
        });

        return view;
    }

}