package aget.aget.workstation.manipulaciongpsgsm;

/**
 * Created by workstation on 21/07/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    Context contexto = null;

    public static final String DATABASE_NAME = "GestionGPS.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLA_DISPOSITIVO = "Dispositivo";
    public static final String COLUMNA_ID = "_id INTEGER";
    public static final String COLUMNA_GPS_PRIMERA_VEZ = "GPS_primera_vez INTEGER";
    public static final String COLUMNA_ESTADO_GPS = "Estado_GPS INTEGER";
    public static final String COLUMNA_MODO_LINK = "Modo_link INTEGER";
    public static final String COLUMNA_MODO_LLAMADA_GPS = "Modo_llamada_GPS TEXT";
    public static final String COLUMNA_TELEFONO_DISPOSITIVO = "Telefono_dispositivo TEXT";
    public static final String COLUMNA_ID_NOMBRE = "_id";
    public static final String COLUMNA_GPS_PRIMERA_VEZ_NOMBRE = "GPS_primera_vez";
    public static final String COLUMNA_ESTADO_GPS_NOMBRE = "Estado_GPS";
    public static final String COLUMNA_MODO_LINK_NOMBRE = "Modo_link";
    public static final String COLUMNA_MODO_LLAMADA_GPS_NOMBRE = "Modo_llamada_GPS";
    public static final String COLUMNA_TELEFONO_DISPOSITIVO_NOMBRE = "Telefono_dispositivo";
    public static final String TABLA_USUARIOS = "Usuarios";
    public static final String COLUMNA_NOMBRE_USUARIO = "Nombre_usuario TEXT";
    public static final String COLUMNA_TELEFONO_USUARIO = "Telefono_usuario TEXT";
    public static final String COLUMNA_DESCRIPCION_USUARIO = "Descripcion_usuario TEXT";
    public static final String TABLA_APP = "aplicacion";
    public static final String COLUMNA_PRIMER_USO = "Primer_uso INTEGER";
    public static final String COLUMNA_CONTRASE_NA = "Contrase_na TEXT";
    public static final String COLUMNA_CONTRASE_NA_NOMBRE = "Contrase_na";
    public static final String COLUMNA_ID_USUARIO = "idUsuario INTEGER";
    public static final String TABLA_AUX = "auxiliar";
    public static final String COLUMNA_TIPO_TIEMPO = "Tipo_tiempo TEXT";
    public static final String COLUMNA_TIPO_TIEMPO_NOMBRE = "Tipo_tiempo";
    public static final int ID_DEFECTO_DISPOSITIVO = 0;
    public static final int GPS_DEFECTO_PRIMERA_VEZ = 1;
    public static final String CONTRASE_NA_DEFECTO = "'123456'";
    public static final int ESTADO_DEFECTO_GPS = 1;
    public static final int MODO_DEFECTO_LINK = 0;
    public static final String MODO_DEFECTO_LLAMADA_GPS = "'GPS'";
    public static final String TELEFONO_DEFECTO_DISPOSITIVO = "'00000000'";
    public static final String TIPO_TIEMPO_DEFECTO = "'s'";
    public static final int ID_DEFECTO_AUXILIAR = 0;


    private static final String SQL_CREAR_TABLA_DISPOSITIVO = "CREATE TABLE IF NOT EXISTS  "
            + TABLA_DISPOSITIVO + " ("
            + COLUMNA_ID + " PRIMARY KEY, "
            + COLUMNA_GPS_PRIMERA_VEZ + ","
            + COLUMNA_CONTRASE_NA + ", "
            + COLUMNA_ESTADO_GPS + ", "
            + COLUMNA_MODO_LINK + ", "
            + COLUMNA_MODO_LLAMADA_GPS + ", "
            + COLUMNA_TELEFONO_DISPOSITIVO + ")";

    private static final String SQL_CREAR_TABLA_USUARIOS = "CREATE TABLE IF NOT EXISTS "
            + TABLA_USUARIOS + " ("
            + COLUMNA_ID + " PRIMARY KEY, "
            + COLUMNA_NOMBRE_USUARIO + ", "
            + COLUMNA_TELEFONO_USUARIO + ", "
            + COLUMNA_DESCRIPCION_USUARIO + ")";

    private static final String SQL_CREAR_TABLA_APP = "CREATE TABLE IF NOT EXISTS "
            + TABLA_APP + " ("
            + COLUMNA_ID + " PRIMARY KEY, "
            + COLUMNA_PRIMER_USO + ", "
            + COLUMNA_CONTRASE_NA + ", "
            + COLUMNA_ID_USUARIO + ")";

    private static final String SQL_CREAR_TABLA_AUXILIAR = "CREATE TABLE IF NOT EXISTS  "
            + TABLA_AUX + " ("
            + COLUMNA_ID + " PRIMARY KEY, "
            + COLUMNA_TIPO_TIEMPO + ")";


    private static final String SQL_INSERTAR_DATOS_DEFAULT = "INSERT INTO "
            + TABLA_DISPOSITIVO + " ("
            + COLUMNA_ID_NOMBRE + ", "
            + COLUMNA_GPS_PRIMERA_VEZ_NOMBRE + ", "
            + COLUMNA_CONTRASE_NA_NOMBRE + ", "
            + COLUMNA_ESTADO_GPS_NOMBRE + ", "
            + COLUMNA_MODO_LINK_NOMBRE + ", "
            + COLUMNA_MODO_LLAMADA_GPS_NOMBRE + ", "
            + COLUMNA_TELEFONO_DISPOSITIVO_NOMBRE + ") VALUES ("
            + ID_DEFECTO_DISPOSITIVO + ", "
            + GPS_DEFECTO_PRIMERA_VEZ + ", "
            + CONTRASE_NA_DEFECTO + ", "
            + ESTADO_DEFECTO_GPS + ", "
            + MODO_DEFECTO_LINK + ", "
            + MODO_DEFECTO_LLAMADA_GPS + ", "
            + TELEFONO_DEFECTO_DISPOSITIVO + ")";

    private static final String SQL_INSERTAR_DATOS_DEFAULT_AUXILIAR = "INSERT INTO "
            + TABLA_AUX + " ("
            + COLUMNA_ID_NOMBRE + ", "
            + COLUMNA_TIPO_TIEMPO_NOMBRE + ") VALUES ("
            + ID_DEFECTO_AUXILIAR + ", "
            + TIPO_TIEMPO_DEFECTO + ")";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contexto = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_TABLA_DISPOSITIVO);
        db.execSQL(SQL_CREAR_TABLA_USUARIOS);
        db.execSQL(SQL_CREAR_TABLA_APP);
        db.execSQL(SQL_CREAR_TABLA_AUXILIAR);
        db.execSQL(SQL_INSERTAR_DATOS_DEFAULT);
        db.execSQL(SQL_INSERTAR_DATOS_DEFAULT_AUXILIAR);

        /*Toast t1 = Toast.makeText(contexto, SQL_CREAR_TABLA_AUXILIAR, Toast.LENGTH_LONG);
        t1.setGravity(Gravity.TOP | Gravity.LEFT, 60, 110);
        t1.show();
        Toast t2 = Toast.makeText(contexto,SQL_INSERTAR_DATOS_DEFAULT_AUXILIAR,Toast.LENGTH_LONG);
        t2.setGravity(Gravity.BOTTOM | Gravity.RIGHT,60,100);*/
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // se elimina la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //db.execSQL("DROP TABLE IF EXISTS TablaPrueba");
        //db.execSQL(codeSQL);
    }

}
