package com.newsproj.newsproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.newsproj.newsproject.Model.DataValue;
import com.newsproj.newsproject.Model.News;
import com.newsproj.newsproject.api.Client;
import com.newsproj.newsproject.api.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rview;
    private List<News> dList;
    private RequestQueue requestQueue;
    private ProgressBar pbar;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int top = 10;
    private int skip = 0;
    private PaginationAdapter adapter;
    private Service service;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pbar = findViewById(R.id.pbar);

        rview = findViewById(R.id.rview);
        linearLayoutManager = new LinearLayoutManager(this);
        rview.setHasFixedSize(true);
        rview.setLayoutManager(linearLayoutManager);
        dList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);
//        newsAdaptor = new NewsAdaptor(this, dList);
        adapter = new PaginationAdapter(this);
        rview.setAdapter(adapter);

        getSupportActionBar().setTitle("News App");

        pbar.showContextMenu();

        //init service and load data
        service = Client.getClient().create(Service.class);
//        loadNews();
        loadFirstPage();
        rview.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                skip += 10;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        loadNextPage();
                        loadNext();
                    }

                }, 100);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    //  using  volley
//
//    private void loadNextPage() {
//        adapter.removeLoadingFooter();
//        isLoading = false;
//        Toast.makeText(this, String.valueOf(skip), Toast.LENGTH_SHORT).show();
//
//        String newsURL = "http://school3.yarshatech.com/api/schooldata/NewsAnnouncements?$top=" + top + "&$skip=" + skip;
////        http://school3.yarshatech.com/api/schooldata/NewsAnnouncements
//        Toast.makeText(this, newsURL, Toast.LENGTH_SHORT).show();
//        StringRequest strReq = new StringRequest(Request.Method.GET, newsURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    JSONObject jObj = new JSONObject(response);
//                    JSONArray jArray = jObj.getJSONArray("value");
//
//                    for (int i = 0; i <= jArray.length(); i++) {
//                        News data = new News();
//                        JSONObject jData = jArray.getJSONObject(i);
//                        data.setId(jData.getString("Id"));
//                        data.setDecription(jData.getString("Description"));
//                        data.setTitle(jData.getString("Title"));
//                        String image = jData.getString("Image");
//                        if (image != null) {
//                            data.setImage(jData.getString("Image"));
//                        }
//                        data.setUserName(jData.getString("UserName"));
//                        data.setShortdesc(jData.getString("ShortDescription"));
//                        data.setPubAt(jData.getString("PublishDate"));
//
//                        dList.add(data);
//
//                        pbar.setVisibility(View.GONE);
//
////                        rview.setAdapter(new NewsAdaptor(MainActivity.this, dList));
//                        adapter.addAll(dList);
//                    }
//                } catch (JSONException e) {
//                    pbar.setVisibility(View.GONE);
//                    e.printStackTrace();
//                }
////                if (currentPage != TOTAL_PAGES) {
////                    adapter.addLoadingFooter();
////                } else {
////                    isLastPage = true;
////                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pbar.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(strReq);
//    }

//    private void loadNews() {
//        String newsURL = "http://school3.yarshatech.com/api/schooldata/NewsAnnouncements?$top=" + top + "&$skip=" + skip;
////        http://school3.yarshatech.com/api/schooldata/NewsAnnouncements
//        StringRequest strReq = new StringRequest(Request.Method.GET, newsURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    JSONObject jObj = new JSONObject(response);
//                    JSONArray jArray = jObj.getJSONArray("value");
//
//                    for (int i = 0; i <= jArray.length(); i++) {
//                        List<News> dlist = new ArrayList<>();
//                        News data = new News();
//                        JSONObject jData = jArray.getJSONObject(i);
//                        data.setId(jData.getString("Id"));
//                        data.setDecription(jData.getString("Description"));
//                        data.setTitle(jData.getString("Title"));
//                        String image = jData.getString("Image");
//                        if (image != null) {
//                            data.setImage(jData.getString("Image"));
//                        }
//                        data.setUserName(jData.getString("UserName"));
//                        data.setShortdesc(jData.getString("ShortDescription"));
//                        data.setPubAt(jData.getString("PublishDate"));
//
//                        dlist.add(data);
//
//                        pbar.setVisibility(View.GONE);
//
//                        Log.d("check", jData.getString("UserName"));
////                        rview.setAdapter(new NewsAdaptor(MainActivity.this, dList));
//                        adapter.addAll(dlist);
//                    }
//                } catch (JSONException e) {
//                    pbar.setVisibility(View.GONE);
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pbar.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(strReq);
//    }

    //USING RETROFIT :)
    private void loadFirstPage() {
        callStudentsDataApi().enqueue(new Callback<DataValue>() {
            @Override
            public void onResponse(Call<DataValue> call, retrofit2.Response<DataValue> response) {
                // Got data. Send it to adapter
                if (response.isSuccessful()) {
                    pbar.setVisibility(View.GONE);
                    Log.d("respo", new Gson().toJson(response.body()));
//                    List<News> studentsDataList = fetchResults(response);
                    DataValue dataValue= response.body();
                    List<News> st= dataValue.getDataValueList();
                    if (st.size() != 0) {
//                        check = true;
                        adapter.addLoadingFooter();
                        adapter.addAll(st);
                    }
                    else{
                        isLastPage=true;
                    }

                } else {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<DataValue> call, Throwable t) {
                t.printStackTrace();
                pbar.setVisibility(View.GONE);
                Log.d("respoerror", t.getMessage());

            }
        });

    }

    private void loadNext() {
//        Toast.makeText(this, "top = " + String.valueOf(top) + " " + "skip=" + String.valueOf(skip), Toast.LENGTH_SHORT).show();
        callStudentsDataApi().enqueue(new Callback<DataValue>() {
            @Override
            public void onResponse(Call<DataValue> call, retrofit2.Response<DataValue> response) {
                adapter.removeLoadingFooter();
                isLoading = false;
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<News> results = fetchResults(response);
                if (results.size() != 0) {
                    adapter.addAll(results);
                    isLastPage = false;
                    adapter.addLoadingFooter();

                } else {

                    isLastPage = true;
                    Toast.makeText(MainActivity.this, "no more data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DataValue> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private List<News> fetchResults(retrofit2.Response<DataValue> response) {
        DataValue stdData = response.body();
        return stdData.getDataValueList();
    }

    //call function
    private Call<DataValue> callStudentsDataApi() {
        return service.getstudentsresponse(top, skip);
    }


}
