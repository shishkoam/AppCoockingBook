package ua.shishkoam.appcoockingbook.retrofit

import android.app.Activity
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object GraphQLInstance {
    private const val BASE_URL: String = "http://194.56.80.14:9900"

    val graphQLService: GraphQLService by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(GraphQLService::class.java)
    }

    private fun post(activity: Activity, userId: String) {
        val retrofit = graphQLService
        val paramObject = JSONObject()
        paramObject.put("query", "query {ping(id:1){id,name,status}}")
        GlobalScope.launch {
            try {
                val response = retrofit.postDynamicQuery(paramObject.toString())
                Log.e("Network response", response.body().toString())
                activity.runOnUiThread {
                    response.body().toString()
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}