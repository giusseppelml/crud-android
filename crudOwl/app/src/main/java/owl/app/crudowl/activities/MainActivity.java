package owl.app.crudowl.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import owl.app.crudowl.R;
import owl.app.crudowl.adapters.UsuariosAdapter;
import owl.app.crudowl.api.Api;
import owl.app.crudowl.api.RequestHandler;
import owl.app.crudowl.models.Usuarios;

public class MainActivity extends AppCompatActivity {

    //usaremos esta lista para mostrar el héroe en listview
    List<Usuarios> usuariosList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewUsuarios);
        usuariosList = new ArrayList<>();
        readUsuarios();
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
                    refreshContenidoList(object.getJSONArray("contenido"));
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

    private void readUsuarios() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USUARIOS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshContenidoList(JSONArray contenido) throws JSONException {
        // limpiar noticias anteriores
        usuariosList.clear();
        int i;

        // recorrer todos los elementos de la matriz json
        // el json que recibimos de la respuesta
        for (i = 0; i < contenido.length(); i++) {
            // obteniendo cada objeto noticia
            JSONObject obj = contenido.getJSONObject(i);

            // Añadiendo la noticia a la lista
            usuariosList.add(new Usuarios(
                    obj.getInt("id"),
                    obj.getString("usuario"),
                    obj.getString("password"),
                    obj.getString("email")
            ));



        }

        // crear el adaptador y configurarlo en la vista de lista
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new UsuariosAdapter(usuariosList, R.layout.card_view_usuarios, new UsuariosAdapter.OnClickListener() {
            @Override
            public void onItemClick(Usuarios usuarios, int position) {
                Intent intent = new Intent(MainActivity.this, CreateUpdateActivity.class);
                intent.putExtra("id", usuarios.getId());
                intent.putExtra("usuario", usuarios.getUsuario());
                intent.putExtra("password", usuarios.getPassword());
                intent.putExtra("email", usuarios.getEmail());
                startActivity(intent);
            }
        }, new UsuariosAdapter.OnLongClickListener() {
            @Override
            public void onLongItemClick(Usuarios usuarios, int position) {
                deleteUsuario(usuarios.getId());
                Toast.makeText(MainActivity.this, "haz eliminado al usuario: "
                        + usuarios.getUsuario() + " con el ID: " + usuarios.getId(),
                        Toast.LENGTH_LONG).show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.agregar:
                Intent intent = new Intent(MainActivity.this, CreateUpdateActivity.class);
                intent.putExtra("verificar", true);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteUsuario(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_USUARIOS + id, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Usuarios user = usuarios.get(this.getAdapterPosition());

        menu.setHeaderTitle(user.getUsuario());
        //menu.setHeaderIcon(usuarios.getIcono());

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.context_menu_usuarios, menu);

        for (int i = 0; i < menu.size(); i++)
            menu.getItem(i).setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.delete:
                fruta.remove(getPosition());
                notifyDataSetChanged();
                return  true;
            case R.id.update:


                return true;
            default:
                return false;
        }
    }

    */

}
