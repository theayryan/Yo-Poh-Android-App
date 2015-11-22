package ayushb.com.yo_poh;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by ayushb on 19/11/15.
 */
public interface YoPohApi {

    @FormUrlEncoded
    @POST("/send")
    public void sendMessage(
            @Field("from") String from,
            @Field("fromn") String fromn,
            @Field("to") String mobno,
            @Field("msg") String msg,
            Callback<Response<String>> callback
    );

    @FormUrlEncoded
    @GET("/getuser")
    public void getUsers(
            @Query("mobno") String mobno,
            Callback<Response<String>> callback
    );

    @FormUrlEncoded
    @POST("/logout")
    public void userLogout(
            @Field("mobno") String mobno,
            Callback<Response<String>> callback
    );

    @FormUrlEncoded
    @POST("/post/customer")
    public Call<ResponseBody> userLogin(
            @Field("customerId") String customerId,
            @Field("name") String name,
            @Field("emailId") String emailId,
            @Field("phoneNum") String phoneNum,
            @Field("address") String address
    );

    @GET("/get/addtomyproducts")
    public Call<ResponseBody> addProduct(
            @Query("customerId") String customerId,
            @Query("productId") String productId
    );

    @GET("/get/allproducts")
    public Call<ResponseBody> getAllProducts();

    @GET("/get/myproducts")
    public Call<ResponseBody> getMyProducts(
            @Query("customerId") String customerId
    );

    @GET("/get/allcompanies")
    public Call<ResponseBody> getAllCompanies();

}
