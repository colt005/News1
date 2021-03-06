package com.example.news1.news1;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news1.news1.models.NewsModel;
import com.example.news1.news1.models.Parent;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.ActionBar;



import static com.example.news1.news1.R.layout.activity_main;
import static com.example.news1.news1.R.layout.activity_search_results;

public class MainActivity extends AppCompatActivity {
    final ArrayList urllist = new ArrayList();
    SearchView svTopic;
    private String total;
    private ListView listView;
    private String topic;
    private TextView tvRead;
    private String SpinnerItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(activity_main);

        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher_background) // resource or drawable
                .build();
        Intent SpinnerIntent = getIntent();
        SpinnerItem = SpinnerIntent.getStringExtra("SelectedItem");
        if(SpinnerItem.contains("everything")) {
            new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&apiKey=9973f0618b1f4f9483f05e9f95885a73");
        }else
        {
            new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&category="+ SpinnerItem +"&apiKey=9973f0618b1f4f9483f05e9f95885a73");

        }

        listView = (ListView) findViewById(R.id.lvNews);


               // new JSONTask().execute("https://newsapi.org/v2/top-headlines?sources=associated-press&apiKey=9973f0618b1f4f9483f05e9f95885a73"); // + R.string.API_KEY);
                //new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");
       // Button btSearch = null;
      //  btSearch = (Button) findViewById(R.id.btSearch);
       // EditText etSearch = null;
      //  etSearch = (EditText) findViewById(R.id.etSearch);
       //  topic = etSearch.getText().toString();
        svTopic = (SearchView) findViewById(R.id.svTopic);
        svTopic.setQueryHint("Search Topic..");
        svTopic.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&apiKey=9973f0618b1f4f9483f05e9f95885a73");
                return true;
            }
        });
        svTopic.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String topic = s;
                new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&q=" + topic + "&sortBy=popularity&apiKey=9973f0618b1f4f9483f05e9f95885a73");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                svTopic.setQuery(" ",false);
                String topic = s;
                String bu = svTopic.getQuery().toString();
                Log.e("Query",bu);
                Log.e("Query2",svTopic.getQuery().toString());
                if(svTopic.getQuery().toString().contains(bu))
                {
                    new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&apiKey=9973f0618b1f4f9483f05e9f95885a73");

                }
//                new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&q=" + topic + "&sortBy=popularity&apiKey=9973f0618b1f4f9483f05e9f95885a73");
                return true;
            }


        });

//        final EditText finalEtSearch = etSearch;
//        btSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                topic = finalEtSearch.getText().toString();
//
//                new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=in&q=" + topic + "&sortBy=popularity&apiKey=9973f0618b1f4f9483f05e9f95885a73");
//
//
//            }
//        });






    }




    public  class JSONTask extends AsyncTask<String, String, List<NewsModel>>{


        @Override
        protected List<NewsModel> doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = " ";
                while ((line = reader.readLine()) != null) {

                    buffer.append(line);
                }

                String finaljson = buffer.toString();


                JSONObject parentobject = new JSONObject(finaljson);
                JSONArray parentarray = parentobject.getJSONArray("articles");
                Gson gson1 = new Gson();
                Parent parent =  gson1.fromJson(parentobject.toString(),Parent.class);
                Log.e("Res",parent.getTotalResults());
                total = parent.getTotalResults();
                if(parent.getTotalResults().equals("0")){
                    Handler handler =  new Handler(MainActivity.this.getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("No Results")
                                    .setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.show();

//                            Toast.makeText(MainActivity.this,"No Results",Toast.LENGTH_LONG).show();
                         }
                    });


                }

//                int results =  NewsModel.setTotalResults(parentobject.getInt("totalResults"));
//                Log.e("Results", "Value : "+Float.toString(NewsModel.getTotalResults()));
//                JSONObject results = parentobject.getJSONObject("totalResults");
//                Log.e("Results",results.toString());
                List<NewsModel> newsModelList = new ArrayList<>();
                Gson gson = new Gson();
                for(int i=0;i<parentarray.length();i++) {

                    JSONObject finalobject = parentarray.getJSONObject(i); //to fetch objects from articles
                    NewsModel newsModel = gson.fromJson(finalobject.toString(),NewsModel.class);
                    //final ArrayList urllist = new ArrayList();
                    urllist.add(newsModel.getUrl());
                    Log.e("URLS",urllist.toString());
                   



                    
//                    newsModel.setAuthor(finalobject.getString("author"));
//                    newsModel.setDescription(finalobject.getString("description"));
//                    newsModel.setUrl(finalobject.getString("url"));
//                    newsModel.setTitle(finalobject.getString("title"));
//                    newsModel.setUrlToImage(finalobject.getString("urlToImage"));
//                    newsModel.setPublishedAt(finalobject.getString("publishedAt"));
//
//                    JSONObject sourceobj = finalobject.getJSONObject("source");
//
//
//                       NewsModel.source Source = new NewsModel.source();
//                        Source.setId(sourceobj.getString("id"));
//                        Source.setName(sourceobj.getString("name"));


                    newsModelList.add(newsModel);

//                    Log.d("NewsModel",newsModelList.toString());


                }
                return newsModelList;



            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(List<NewsModel> result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this,"Total Results "+total,Toast.LENGTH_LONG).show();

            newsAdapter adapter = new newsAdapter(getApplicationContext(),R.layout.row2, result);
            listView.setAdapter(adapter);
//            Handler handler =  new Handler(MainActivity.this.getMainLooper());
//            handler.post( new Runnable(){
//                public void run(){
                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(view.getContext(), NewsActivity.class);
                        String uRl = (String) urllist.get(i);
                        intent.putExtra("url", uRl);
                        Log.e("urllldsf", (String) urllist.get(i));
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Swipe Right to go back.",Toast.LENGTH_LONG).show();
                       }
                   });
//                }
//            });

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    //TODO On item click -> New Activity
//
//                        Intent intent = new Intent(view.getContext(), NewsActivity.class);
//                        String uRl = (String) urllist.get(i);
//                        intent.putExtra("url", uRl);
//                        Log.e("urlll", (String) urllist.get(i));
//
//
//                }
//            });

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override

                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                        if (i == 0) {
                            // check if we reached the top or bottom of the list
                            View v = listView.getChildAt(0);
                            int offset = (v == null) ? 0 : v.getTop();
                            if (offset == 0) {
                                // reached the top: visible header and footer
                                svTopic.setVisibility(View.VISIBLE);

                            }
                        } else if (i2 - i1 == i) {
                            View v = listView.getChildAt(i2 - 1);
                            int offset = (v == null) ? 0 : v.getTop();
                            if (offset == 0) {
                                // reached the bottom: visible header and footer
                                svTopic.setVisibility(View.VISIBLE);


                            }
                        }else if (i2 - i1 > i){
                            // on scrolling
                            svTopic.setVisibility(View.GONE);


                        }

                    }
                });



        }
    }

    public class newsAdapter extends ArrayAdapter<NewsModel>
    {
        private List<NewsModel> newsModelList;
        private int resource;
        private LayoutInflater inflator;

        public newsAdapter(@NonNull Context context, int resource, @NonNull List<NewsModel> objects) {
            super(context, resource, objects);
            newsModelList = objects;
            this.resource=resource;
            inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null){
                holder = new ViewHolder();
                convertView = inflator.inflate(resource,null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

                holder.tvDescription =  (TextView) convertView.findViewById(R.id.tvDescription);

//                holder.tvDescription.setSingleLine(false);
//                holder.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
//                int n = 1; // the exact number of lines you want to display
//                holder.tvDescription.setLines(n);
                 //holder.tvAuthor =  (TextView) convertView.findViewById(R.id.tvAuthor);
               // holder.tvURL =  (TextView) convertView.findViewById(R.id.tvURL);
               // holder.tvPublishedAt =  (TextView) convertView.findViewById(R.id.tvPublishedAt);
               // holder.tvId =  (TextView) convertView.findViewById(R.id.tvId);
                holder.tvName =  (TextView) convertView.findViewById(R.id.tvName);
                convertView.setTag(holder);
            }else{

                holder = (ViewHolder) convertView.getTag();
            }




            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar3);



            holder.tvTitle.setText(newsModelList.get(position).getTitle());
            holder.tvDescription.setText(newsModelList.get(position).getDescription());
            //holder.tvAuthor.setText(newsModelList.get(position).getAuthor());
          //  holder.tvURL.setText(newsModelList.get(position).getUrl());
          //  holder.tvPublishedAt.setText(newsModelList.get(position).getPublishedAt());
            ImageLoader.getInstance().displayImage(newsModelList.get(position).getUrlToImage(), holder.ivIcon, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.VISIBLE);


                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);


                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);


                }
            });



            // Default options will be used
            //holder.tvId.setText(newsModelList.get(position).getSource().getId());
            holder.tvName.setText(newsModelList.get(position).getSource().getName());
            return convertView;


        }

        class ViewHolder{

            private ImageView ivIcon;
            private TextView tvTitle;
            private TextView tvDescription;
           // private TextView tvAuthor;
            //private TextView tvURL;
            //private TextView tvPublishedAt;
           // private TextView tvId;
            private TextView tvName;

        }

    }



//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        return true;
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.action_refresh:
//                Intent intent = new Intent(getApplicationContext(),PrefActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.action_app:
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Exit?")
                .setMessage("Are You Sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton("No",null);
        AlertDialog alert = builder.create();
        alert.show();

    }


}
