package com.tdkankan.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.dialog.LoadingDialog;
import com.tdkankan.Adapter.SearchListAdapter;
import com.tdkankan.Cache.ImageCacheManager;
import com.tdkankan.R;
import com.tdkankan.Reptile.SearchBook;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    private ArrayList<HashMap<String, String>> list=new ArrayList<>();
    EditText editText;
    TextView textView;
    TextView tv_dearch_hinit;
    String searchContent;
    LoadingDialog loadingDialog;
    SearchListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        listView = findViewById(R.id.listview_search);
        editText = findViewById(R.id.edittext_search);
        textView = findViewById(R.id.tv_search_btn);
        tv_dearch_hinit = findViewById(R.id.tv_search_hinit);
        tv_dearch_hinit.setVisibility(View.GONE);
        loadingDialog = new LoadingDialog(SearchActivity.this);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchContent = editText.getText().toString();
                    new SearchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                return false;
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent = editText.getText().toString();
                new SearchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }
    public class SearchTask extends AsyncTask<Void,Integer,Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.setMessage("loading");
            loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
            loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
            loadingDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            list= SearchBook.SearchBookEvent(searchContent);
            list= SearchBook.searchBookEvent(searchContent);
            adapter = new SearchListAdapter(list,SearchActivity.this);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismiss();
                    if(list.size()==0)
                    {
                        tv_dearch_hinit.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }else {
                        tv_dearch_hinit.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(adapter);
                    }
                }
            },2000);
        }
    }
}
