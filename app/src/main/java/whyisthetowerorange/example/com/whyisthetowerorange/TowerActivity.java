package whyisthetowerorange.example.com.whyisthetowerorange;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.text.format.DateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TowerActivity extends ActionBarActivity {

    private TextView textView;
    private ScrollView layout;
    private boolean towerIsOrange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tower_activity);

        textView = (TextView) findViewById(R.id.towerText);
        layout = (ScrollView) findViewById(R.id.layout);
        readWebpage();

//        ActionBar actionBar = getActionBar();
//        String dateString = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date());
//        actionBar.setTitle(dateString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tower, menu);
        return true;
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String str = "";
                    while ((str = buffer.readLine()) != null) {
                        if (str.startsWith("<div id=\"reason\">")) {
                            str = str.substring(str.indexOf(">", 17) + 1);
                            int index = 0;
                            while (str.charAt(index) != '<') {
                                response += str.charAt(index);
                                index++;
                            }
                            response += " Sad Day.";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("It's not. Sad Day.")) {
                layout.setBackgroundResource(R.drawable.whiteuttower3);
                textView.setTextColor(Color.BLACK);
                towerIsOrange=false;
            }
            else {
                layout.setBackgroundResource(R.drawable.orangeuttower3);
                textView.setTextColor(Color.WHITE);
                towerIsOrange=true;
//                textView.setText("THIS IS A VERY LONG STRING SO I CAN SEE HOW LONG STRINGS WILL LOOK IN THIS TEXT VIEW. I HOPE THIS WORKS OKAY. WHAT IS THE LIMIT OF HOW LONG THIS STRING CAN BE BEFORE IT IS TOO LONG FOR THE APP. PERHAPS I SHOULD CONSIDER CHANGING IT TO SCROLLVIEW??????? BECAUSE IF I HAVE ONE MORE LINE THEN WHAT WILL HAPPEN?? UH OH!!!!");
            }
            textView.setText(Html.fromHtml(result));
        }
    }

    public void readWebpage() {
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute("http://whyisthetowerorange.com");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.date) {
            showDate();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDate() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, null);
        PopupWindow popup = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popup.setOutsideTouchable(true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        ((TextView)popup.getContentView().findViewById(R.id.datepopup)).setText(currentDate);
        if (towerIsOrange == true)
            ((TextView)popup.getContentView().findViewById(R.id.datepopup)).setTextColor(Color.WHITE);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }
}