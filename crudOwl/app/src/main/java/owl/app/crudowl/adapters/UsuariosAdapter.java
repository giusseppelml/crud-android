package owl.app.crudowl.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import owl.app.crudowl.R;
import owl.app.crudowl.models.Usuarios;


public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder>{
    private List<Usuarios> usuarios;
    private int layout;
    private OnClickListener listener;
    private OnLongClickListener listenerLong;

    private Context context;

    public UsuariosAdapter(List<Usuarios> usuarios, int layout, OnClickListener listener, OnLongClickListener listenerLong) {
        this.usuarios = usuarios;
        this.layout = layout;
        this.listener = listener;
        this.listenerLong = listenerLong;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(usuarios.get(position), listener, listenerLong);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView usuarioTextView;
        private TextView passwordTextView;
        private TextView emailTextView;

        public ViewHolder(View v){
            super(v);
            usuarioTextView = (TextView)itemView.findViewById(R.id.textViewUsuario);
            passwordTextView = (TextView)itemView.findViewById(R.id.textViewPassword);
            emailTextView = (TextView)itemView.findViewById(R.id.textViewEmail);
        }

        public void bind(final Usuarios usuarios, final OnClickListener listener, final OnLongClickListener listenerLong){
            usuarioTextView.setText(usuarios.getUsuario());
            passwordTextView.setText(usuarios.getPassword());
            emailTextView.setText(usuarios.getEmail());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(usuarios, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listenerLong.onLongItemClick(usuarios, getAdapterPosition());
                    return false;
                }
            });


        }
    }

    public interface OnClickListener{
    void onItemClick(Usuarios usuarios, int position);
    }

    public interface OnLongClickListener{
        void onLongItemClick(Usuarios usuarios, int position);
    }
}
