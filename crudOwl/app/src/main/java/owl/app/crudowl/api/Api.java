package owl.app.crudowl.api;

public class Api {

    private static final String ROOT_URL = "http://192.168.1.77/json/Api.php?apicall=";

    public static final String URL_CREATE_USUARIOS = ROOT_URL + "createusuario";
    public static final String URL_READ_USUARIOS = ROOT_URL + "readusuarios";
    public static final String URL_UPDATE_USUARIOS = ROOT_URL + "updateusuarios";
    public static final String URL_DELETE_USUARIOS = ROOT_URL + "deleteusuario&id=";

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

}
