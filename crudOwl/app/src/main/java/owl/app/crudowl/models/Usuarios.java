package owl.app.crudowl.models;

public class Usuarios {

    private int id;
    private String usuario;
    private String password;
    private String email;

    public Usuarios(int id, String usuario, String password, String email) {
        setId(id);
        setUsuario(usuario);
        setPassword(password);
        setEmail(email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
