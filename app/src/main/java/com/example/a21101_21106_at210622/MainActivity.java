package com.example.a21101_21106_at210622;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.XMLFormatter;

import javax.net.ssl.HttpsURLConnection;


    public class MainActivity extends AppCompatActivity {

        private Button btn;
        private InputStreamReader corpoReader;
        private List<String> listaNomes;

        private ListView lista;
        private ArrayAdapter<String> listaAdapter1;
        private ArrayAdapter<String> listaAdapter2;
        private List<String> palavras;
        Context context;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btn = (Button) findViewById(R.id.lvAlunos);
            lista = (ListView) findViewById(R.id.lvAlunos2);
            palavras = listaNomes;
            context = getApplicationContext();
            btn.setOnClickListener(v -> {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            URL githubEndpoint = new
                                    URL("https://ds302.herokuapp.com/url");
                            HttpsURLConnection conexao =
                                    (HttpsURLConnection) githubEndpoint.openConnection();

                            if (conexao.getResponseCode() == 200) {
                                InputStream corpo = conexao.getInputStream();

                                corpoReader = new InputStreamReader(corpo, "UTF-8");
                                new carregaImg().execute();
                                ;
                            } else {
                                Log.e("RESPONSE", "Erro ");
                            }
                            conexao.disconnect();
                        } catch (Exception e) {
                            Log.e("URL", e.getMessage());
                        }
                    }
                });
            });

       /*     listaAdapter1 = new ArrayAdapter<String>(
                    // parâmetros: contexto, quem é o layout, quem é a fonte do loyout.
                    this,
                    android.R.layout.simple_list_item_1,
                    palavras
            );
            lista.setAdapter(listaAdapter1);
*/


            lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    String nome = listaNomes.get(i).toString();
                    if ((nome.length()) >0){
                        String ultimonome = nome.substring(i);
                         id = nome.charAt(i);

                        showAlert ("Você selecionou o nome : " + nome + "com id: " + id + " com ultimo nome :" + ultimonome );                  }


                }



                private void showAlert(String s) {
                   
                }

                public void run() {
                    try {

                        URL githubEndpoint2 = new
                                URL("https://ds302.herokuapp.com/url?id=");
                        HttpsURLConnection conexao2 =
                                (HttpsURLConnection) githubEndpoint2.openConnection();

                        if (conexao2.getResponseCode() == 200) {
                            InputStream corpo2 = conexao2.getInputStream();

                            corpoReader = new InputStreamReader(corpo2, "UTF-8");
                            //new carregaLista().execute();
                            ;
                        } else {
                            Log.e("RESPONSE", "Erro ");
                        }
                        conexao2.disconnect();
                    } catch (Exception e) {
                        Log.e("URL", e.getMessage());
                    }
                }
            });

            }

                private class carregaImg extends AsyncTask<String, Void, Bitmap> {


            @Override
            protected Bitmap doInBackground(String... str) {
// aqui desmontamos o json e podemos utiliza-lo
                Bitmap fig = null;
                JSONArray jObj = null;
                try {
                    StringBuilder sb = new StringBuilder();
                    for (int ch; (ch = corpoReader.read()) != -1; ) {
                        sb.append((char) ch);
                    }
                    Log.d("ddd", sb.toString());
                    jObj = new JSONArray(sb.toString());

                    listaNomes = new ArrayList<String>();

                    for (int i = 0; i < jObj.length(); i++) {
                        JSONObject c = jObj.getJSONObject(i);

// Storing each json item in variable
                        String name = c.getString("firstname");
                        Log.d("nome", name);
                        listaNomes.add(name);
                    }

                } catch (JSONException | IOException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }


                return fig;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listaAdapter2 = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        listaNomes);
                lista.setAdapter(listaAdapter2);
            }

        }



    }