package com.example.myapplication2.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private TextView jokeText = null;
    private Button btnRandom = null;
    private Button btnRandomQueue = null;

    private TextView jokeByIdText = null;
    private Button btnJokeById = null;
    private EditText jokeId = null;

    private Button btnClear = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jokeText = (TextView) this.findViewById(R.id.jokeText);
        btnRandom = (Button) this.findViewById(R.id.btnRandom);
        btnRandomQueue = (Button) this.findViewById(R.id.btnRandomQueue);
        jokeByIdText = (TextView) this.findViewById(R.id.jokeText1);
        btnJokeById = (Button) this.findViewById(R.id.btnRandom1);
        jokeId = (EditText) this.findViewById(R.id.editText);
        btnClear = (Button) this.findViewById(R.id.btnClear);

        final VolleyDao dao = VolleyDao.getInstance(this);

        btnRandomQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jokeText.setText("Queueing lots of jokes");
                queueLotsOfMessages(dao, 100);
            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queueLotsOfMessages(dao, 1);
            }
        });

        btnJokeById.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(jokeId.getText().toString());
                String joke = dao.getCachedJokeById(id);

                if (joke != null && joke != ""){
                    jokeByIdText.setText(joke);
                    return;
                }

                queueGetJokeById(id, dao);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dao.clearCache();
                jokeByIdText.setText("Cache Cleared!!");
            }
        });
    }

    private void queueGetJokeById(int id, VolleyDao dao) {
        //dao.getJokeById(id, new Response.Listener<JSONObject>() {
        dao.getImmediateJokeById(id, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            jokeByIdText.setText(jsonObject.getJSONObject("value").getString("joke"));
                        } catch (JSONException e) {
                            jokeByIdText.setText(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        jokeByIdText.setText(volleyError.getMessage());
                    }
                }
        );
    }

    private void queueLotsOfMessages(VolleyDao dao, int count) {
        for (int i = 0; i < count; i++) {
            dao.getRandomJoke(new Response.Listener<JSONObject>() {
                                  @Override
                                  public void onResponse(JSONObject jsonObject) {
                                      try {
                                          jokeText.setText(jsonObject.getJSONObject("value").getString("joke"));
                                      } catch (JSONException e) {
                                          jokeText.setText(e.getMessage());
                                      }
                                  }
                              }, new Response.ErrorListener() {
                                  @Override
                                  public void onErrorResponse(VolleyError volleyError) {
                                      jokeText.setText(volleyError.getMessage());
                                  }
                              }
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
