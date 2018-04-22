package owl.app.crudowl.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import owl.app.crudowl.R;
import owl.app.crudowl.api.Api;
import owl.app.crudowl.api.RequestHandler;

public class CreateUpdateActivity extends AppCompatActivity {

    private TextView tituloTextView;
    private TextView idtextView;

    private EditText nombreEditText;
    private EditText psswordEditText;
    private EditText emailEditText;

    private ProgressBar progressBar;
    private Button buttonAddUpdate;

    private boolean isUpdating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update);

        //activar flecha ir atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Tomar los datos del intent
        Bundle bundle = getIntent().getExtras();

        //checa que resiva los parametros correctos del intent
        isUpdating = (bundle.getBoolean("verificar")) ? true:false;
        String titulo = (isUpdating) ? "AGREGAR":"EDITAR";

        tituloTextView = (TextView)findViewById(R.id.textViewTitulo);
        idtextView = (TextView)findViewById(R.id.textViewID);

        nombreEditText = (EditText)findViewById(R.id.editTextNombre);
        psswordEditText = (EditText)findViewById(R.id.editTextPassword);
        emailEditText = (EditText)findViewById(R.id.editTextEmail);

        buttonAddUpdate = (Button) findViewById(R.id.button);

        tituloTextView.setText(titulo);
        if(!isUpdating){
            String id = String.valueOf(bundle.getInt("id"));
            idtextView.setText(id);

            nombreEditText.setText(bundle.getString("usuario"));
            psswordEditText.setText(bundle.getString("password"));
            emailEditText.setText(bundle.getString("email"));
        }

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    createUsuario();
                    Toast.makeText(CreateUpdateActivity.this, "Creado", Toast.LENGTH_SHORT).show();
                } else {
                    updateUsuario();
                    Toast.makeText(CreateUpdateActivity.this, "Editado", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUsuario() {
        String nombre = nombreEditText.getText().toString().trim();
        String password = psswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();


        if (TextUtils.isEmpty(nombre)) {
            nombreEditText.setError("Escribe un nombre");
            nombreEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            psswordEditText.setError("Escribe una contraseña");
            psswordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Escribe un correo electronico");
            emailEditText.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("usuarios", nombre);
        params.put("password", password);
        params.put("email", email);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_USUARIOS, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private void updateUsuario() {
        String id = idtextView.getText().toString().trim();
        String nombre = nombreEditText.getText().toString().trim();
        String password = psswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();


        if (TextUtils.isEmpty(nombre)) {
            nombreEditText.setError("Escribe un nombre");
            nombreEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            psswordEditText.setError("Escribe una contraseña");
            psswordEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Escribe un correo electronico");
            emailEditText.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("usuarioslml", nombre);
        params.put("password", password);
        params.put("email", email);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_USUARIOS, params, Api.CODE_POST_REQUEST);
        request.execute();

        startActivity(new Intent(CreateUpdateActivity.this, MainActivity.class));

        //buttonAddUpdate.setText("Add");

        //editTextName.setText("");
        //editTextRealname.setText("");
        //ratingBar.setRating(0);
        //spinnerTeam.setSelection(0);

        //isUpdating = false;
    }

    // clase interna para realizar la solicitud de red extendiendo un AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        // la url donde necesitamos enviar la solicitud
        String url;

        //the parameters
        HashMap<String, String> params;

        // el código de solicitud para definir si se trata de un GET o POST
        int requestCode;

        // constructor para inicializar valores
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        // este método dará la respuesta de la petición
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    // refrescar la lista después de cada operación
                    // para que obtengamos una lista actualizada
                    startActivity(new Intent(CreateUpdateActivity.this, MainActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // la operación de red se realizará en segundo plano
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == Api.CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == Api.CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
