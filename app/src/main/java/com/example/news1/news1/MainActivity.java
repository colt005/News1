package com.example.news1.news1;

import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news1.news1.models.NewsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import static com.example.news1.news1.R.id.action_refresh;
import static com.example.news1.news1.R.id.lvNews;
import static com.example.news1.news1.R.id.title;
import static com.example.news1.news1.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(activity_main);



        listView = (ListView) findViewById(R.id.lvNews);


               // new JSONTask().execute("https://newsapi.org/v2/top-headlines?sources=associated-press&apiKey=9973f0618b1f4f9483f05e9f95885a73"); // + R.string.API_KEY);
                //new JSONTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt");
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

                List<NewsModel> newsModelList = new ArrayList<>();

                for(int i=0;i<parentarray.length();i++) {
                    NewsModel newsModel = new NewsModel();
                    JSONObject finalobject = parentarray.getJSONObject(i); //to fetch objects from articles


                    newsModel.setAuthor(finalobject.getString("author"));
                    newsModel.setDescription(finalobject.getString("description"));
                    newsModel.setUrl(finalobject.getString("url"));
                    newsModel.setTitle(finalobject.getString("title"));
                    newsModel.setUrlToImage(finalobject.getString("urlToImage"));
                    newsModel.setPublishedAt(finalobject.getString("publishedAt"));
                    JSONObject sourceobj = finalobject.getJSONObject("source");


                        NewsModel.source Source = new NewsModel.source();
                        Source.setId(sourceobj.getString("id"));
                        Source.setName(sourceobj.getString("name"));


                    newsModelList.add(newsModel);
                    Log.d("NewsModel",newsModelList.toString());

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

            newsAdapter adapter = new newsAdapter(getApplicationContext(),R.layout.row, result);
            listView.setAdapter(adapter);

            //TODO need to set datas to LIST
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


            if (convertView == null){
                convertView = inflator.inflate(resource,null);

            }
     //       ImageView ivIcon;
            TextView tvTitle;
            TextView tvDescription;
            TextView tvAuthor;
            TextView tvURL;
            TextView tvPublishedAt;
            TextView tvId;
            TextView tvName;
            
            // When you are trying to get views, they should be from relevant view group.
            // It means, if you are retrieving view from activity, you can directly use findViewById() method.
            // But here, you are trying to inflate a row into the list and views are in your row viewGroup.
            // Hence your retrieval reference must be from inflated view.

            //       ivIcon = (ImageView) findViewById(R.id.ivIcon);
            tvTitle = findViewById(R.id.tvTitle);
            tvDescription =  findViewById(R.id.tvDescription);
            tvAuthor =  findViewById(R.id.tvAuthor);
            tvURL =  findViewById(R.id.tvURL);
            tvPublishedAt =  findViewById(R.id.tvPublishedAt);
            tvId =  findViewById(R.id.tvId);
            tvName =  findViewById(R.id.tvName);

            tvTitle.setText(newsModelList.get(position).getTitle());
            tvDescription.setText(newsModelList.get(position).getDescription());
            tvAuthor.setText(newsModelList.get(position).getAuthor());
            tvURL.setText(newsModelList.get(position).getUrl());
            tvPublishedAt.setText(newsModelList.get(position).getPublishedAt());
           tvId.setText(newsModelList.get(position).toString());
            tvName.setText(newsModelList.get(position).toString());
            return convertView;
        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh:
                new JSONTask().execute("https://newsapi.org/v2/top-headlines?country=us&apiKey=9973f0618b1f4f9483f05e9f95885a73");
                break;
            case R.id.action_app:
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
