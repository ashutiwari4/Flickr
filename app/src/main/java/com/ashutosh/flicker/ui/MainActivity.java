package com.ashutosh.flicker.ui;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashutosh.flicker.R;
import com.ashutosh.flicker.app.BaseActivity;
import com.ashutosh.flicker.data.MyLocalServer;
import com.ashutosh.flicker.modals.PhotoModal;
import com.ashutosh.flicker.remote.NetworkLoader;
import com.ashutosh.flicker.remote.ServerResponse;
import com.ashutosh.flicker.remote.WebUtils;
import com.ashutosh.flicker.ui.adapters.PhotoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, PhotoAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<ServerResponse> {

    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.ibSearch)
    ImageButton mIbSearch;
    @BindView(R.id.rv_photos)
    RecyclerView mRvPhoto;


    private PhotoAdapter photoAdapter;
    private boolean loadingMore = false;
    int pageNo = 0;
    private ProgressDialog progressDialog;
    private List<PhotoModal> photoListModal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setReverseLayout(true);
        mRvPhoto.setLayoutManager(mLayoutManager);


        photoAdapter = new PhotoAdapter(this, photoListModal);
        mRvPhoto.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(this);


        mRvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int maxPositions = layoutManager.getItemCount();

                if (lastVisibleItemPosition == maxPositions - 1) {
                    if (loadingMore)
                        return;
                    loadingMore = true;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

   /* private void getLocalData() {
        Bundle b = new Bundle();
        pageNo += 1;
        b.putInt("page_no", pageNo);
        getLoaderManager().restartLoader(0, b, this);
    }*/


    @Override
    public void onClick(View v) {
        if (v.equals(mIbSearch)) {
            if (mEtSearch.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please Enter your quehry", Toast.LENGTH_LONG).show();
                return;
            }
            makeRequest(1, mEtSearch.getText().toString());

        }
    }

    private void makeRequest(int pageId, String query) {
        if (getSupportLoaderManager() != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(WebUtils.PAGE_ID, pageId);
            getSupportLoaderManager().restartLoader(pageId, bundle, this);
        }
    }



   /* @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        AppPreferences.getInstance(this).setPageNo(args.getInt("page_no"));
        return PhotoLoder.newAllArticlesInstance(MainActivity.this, args.getInt("page_no"));
    }
*/
   /* @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Cursor cursor = ((PhotoAdapter) mRvPhoto.getAdapter()).getCursor();
        if (cursor != null)
            pageNo = cursor.getCount() / 10;
        MatrixCursor mx = new MatrixCursor(PhotoLoder.Query.PROJECTION);
        fillMx(cursor, mx);
        fillMx(data, mx);

        ((PhotoAdapter) mRvPhoto.getAdapter()).swapCursor(data);
        toggleProgressbar(progressBar, mRvPhoto);
        loadingMore = false;
    }*/




   /* private void fillMx(Cursor data, MatrixCursor mx) {
        if (data == null)
            return;

        data.moveToPosition(-1);
        while (data.moveToNext()) {
            mx.addRow(new Object[]{
                    data.getString(data.getColumnIndex(PhotoContract.Photos._ID)),
                    data.getString(data.getColumnIndex(PhotoContract.Photos.PREDICATE)),
                    data.getString(data.getColumnIndex(PhotoContract.Photos.COMPOSER)),
                    data.getString(data.getColumnIndex(PhotoContract.Photos.TAB_AND_CHORD)),
                    data.getString(data.getColumnIndex(PhotoContract.Photos.SECRET)),
            });
        }
    }*/


    @Override
    public android.support.v4.content.Loader<ServerResponse> onCreateLoader(int id, Bundle args) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        String url = WebUtils.baseUrl + WebUtils.API_KEY_END_POINT + "1" + WebUtils.query + "hello" + WebUtils.API_END_POINT;
        System.out.println(url);
        /*WebUtils.baseUrl + args.getString("currentFilter") + WebUtils.API_KEY_END_POINT + args.getInt(WebUtils.PAGE_ID)*/
        return new NetworkLoader(this, url, NetworkLoader.GET);
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ServerResponse> loader, ServerResponse data) {
        System.out.println("I am in On LoadFines");
        if (progressDialog.isShowing()) progressDialog.cancel();
        if (data.getResponseCode() == 200) {
            System.out.println("Data: " + data.getServerResponse());
            parseResult(data.getServerResponse());
            mEtSearch.setText("");
            new MyLocalServer(MainActivity.this, photoListModal);
        } else if (data.getServerResponse() == null || data.getServerResponse().equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_data_available), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), data.getException().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void parseResult(String s) {
        try {
            JSONArray jsonArray = new JSONObject(s).getJSONObject("photos").getJSONArray("photo");
            for (int i = 0; i < jsonArray.length(); i++) {
                PhotoModal photoModal = new PhotoModal();
                JSONObject jo = jsonArray.getJSONObject(i);
                photoModal.setId(jo.getString("id"));
                photoModal.setFarm_id(jo.getString("farm"));
                photoModal.setServer_id(jo.getString("server"));
                photoModal.setTitle(jo.getString("title"));
                photoModal.setSecret(jo.getString("secret"));
                photoListModal.add(photoModal);
                photoAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ServerResponse> loader) {

    }

    @Override
    public void onItemClick(PhotoAdapter.ItemHolder item, PhotoModal photoModal, int position) throws JSONException {

    }
}
