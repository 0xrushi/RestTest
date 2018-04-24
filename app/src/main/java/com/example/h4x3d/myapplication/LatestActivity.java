package com.example.h4x3d.myapplication;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LatestActivity extends AppCompatActivity {
    ArrayList<GridItemModel> itemsList;
    GridAdapter adapter;
    RecyclerView recyclerView;
    boolean mIsLoading;
    static int pagenum=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_display_layout);

        ImageButton refresh_btn=findViewById(R.id.refresh_btn);
        recyclerView=findViewById(R.id.recyclerView);

        final GridLayoutManager mLayoutManager=new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        itemsList=new ArrayList<>();
        mIsLoading=true;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    mIsLoading=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                //Log.e("dhan2",Integer.toString(visibleItemCount));
                if (mIsLoading&&(pastVisibleItems + visibleItemCount >= totalItemCount)) {
                    mIsLoading=false;
                    Log.e("dhan","end occurred page no is "+pagenum);
                    pagenum++;
                    fetchData(pagenum);
                }
            }
        });

        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData(pagenum);
                adapter=new GridAdapter(getApplicationContext(),itemsList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void fetchData(int p){
        final String URL="https://yts.am/api/v2/list_movies.json?page="+p;
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading ..");
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest =new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("RestResponse",response.toString());
                        if(response!=null){
                            try {
                                JSONObject data=response.getJSONObject("data");
                                JSONArray array= data.getJSONArray("movies");
                                if(array.length()>0){
                                    for(int i=0;i<array.length();i++){
                                        JSONObject individual_item = array.getJSONObject(i);
                                        String title= (String) individual_item.get("title_long");
                                        String mediumcover_image= (String)individual_item.get("medium_cover_image");
                                        GridItemModel item=new GridItemModel(title,mediumcover_image);
                                        itemsList.add(item);
                                        //Log.e("title",Integer.toString(itemsList.size()));
                                    }
                                }
                                for(GridItemModel i:itemsList)
                                    Log.e("gridlist", i.getName());
                                Log.e("gridlist", "---------------------------------------------------------------");
                                adapter=new GridAdapter(getApplicationContext(),itemsList);
                                //recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errorrr","a");
                    }
                }
        );

        requestQueue.add(objectRequest);
    }
}



class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
