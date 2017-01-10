package com.example.sendphone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

	TextView salida;
	private Button boton;
	private EditText destinatario,asunto,texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        boton= (Button)findViewById(R.id.button1);
        destinatario=(EditText)findViewById(R.id.editText1);
        asunto=(EditText)findViewById(R.id.editText2);
        texto=(EditText)findViewById(R.id.editText3);
        
        boton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!destinatario.getText().toString().equals("") && !asunto.getText().toString().equals("") && !texto.getText().toString().equals("")){
					salida.setText("marco="+destinatario.getText().toString()+"&polo="+asunto.getText().toString()+"&md5sum="+texto.getText().toString());
					new Connection().execute(salida);
				}
			}
		});
    }
    
    static class Connection extends AsyncTask<TextView, Void, String>{
    	BufferedReader bfr;
    	TextView salida;
		@Override
		protected String doInBackground(TextView... params) {
			// TODO Auto-generated method stub
			String url = params[0].getText().toString();
			return getDatos(url);
		}
		@Override
		protected void onPostExecute(String result){
			salida.setText(result);
		}
		public String getDatos(String url){
			HttpClient cliente = new DefaultHttpClient();
			   HttpGet htpget = new HttpGet("http://192.168.1.4/rest/hello?"+url);
			   String resultado="Fallo";

			   try{
				   HttpResponse respuesta = cliente.execute(htpget);
				   bfr=new BufferedReader(new InputStreamReader(respuesta.getEntity().getContent()));
				   StringBuffer stb = new StringBuffer();
				   String linea="";
				   String NL="";
				   while((linea = bfr.readLine())!= null){
					   stb.append(linea +NL);
				   }
				   bfr.close();
				   resultado=stb.toString();
			   }catch(IllegalStateException e){
			      e.printStackTrace();
			   }catch(IOException e){
			      e.printStackTrace();
			   }
			   
			   return resultado;
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
