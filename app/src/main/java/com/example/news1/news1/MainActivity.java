package com.example.news1.news1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import static com.example.news1.news1.R.id.action_refresh;
import static com.example.news1.news1.R.id.title;

public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
                    newsModel.setUrlToImage(finalobject.getString("UrlToImage"));
                    newsModel.setPublishedAt(finalobject.getString("publishedAt"));
                    List<NewsModel.source> sourceList = new ArrayList<>();
                    for(int j = 0; j<finalobject.getJSONArray("source").length(); j++){

                        NewsModel.source Source = new NewsModel.source();
                        Source.setId(finalobject.getString("id"));
                        Source.setName(finalobject.getString("name"));
                        sourceList.add(Source);

                    }
                    newsModel.setSourceList(sourceList);
                    newsModelList.add(newsModel);


                }
                return newsModelList;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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



            //TODO need to set datas to LIST
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
