package aget.aget.workstation.manipulaciongpsgsm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by workstation on 10/11/15.
 */
public class ManipulacionBD {

    public static final String DATABASE_NAME = "GestionGPS.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLA_DISPOSITIVO = "Dispositivo";
    public static final String COLUMNA_ID = "_id";
    public static final String COLUMNA_GPS_PRIMERA_VEZ = "GPS_primera_vez";
    public static final String COLUMNA_ESTADO_GPS = "Estado_GPS";
    public static final String COLUMNA_MODO_LINK = "Modo_link";
    public static final String COLUMNA_MODO_LLAMADA_GPS = "Modo_llamada_GPS";
    public static final String COLUMNA_TELEFONO_DISPOSITIVO = "Telefono_dispositivo";
    public static final String TABLA_USUARIOS = "Usuarios";
    public static final String COLUMNA_NOMBRE_USUARIO = "Nombre_usuario";
    public static final String COLUMNA_TELEFONO_USUARIO = "Telefono_usuario";
    public static final String COLUMNA_DESCRIPCION_USUARIO = "Descripcion_usuario";
    public static final String TABLA_APP = "Aplicacion";
    public static final String COLUMNA_PRIMER_USO = "Primer_uso";
    public static final String COLUMNA_CONTRASE_NA = "Contrase_na";
    public static final String COLUMNA_ID_USUARIO = "idUsuario";
    public static final String TABLA_AUX = "auxiliar";
    public static final String COLUMNA_TIPO_TIEMPO = "Tipo_tiempo";

    SQLHelper sqlhelper;
    SQLiteDatabase db;

    ManipulacionBD(Context contexto) {
        sqlhelper = new SQLHelper(contexto);
    }

    public boolean eliminarDato(String tabla,String columna,String dato){
        abrirEscrituraBD();
        db.delete(tabla, columna+"="+dato, null);
        return true;
    }

    public void abrirEscrituraBD() {
        db = sqlhelper.getWritableDatabase();
    }

    public void cerrarBD() {
        db.close();
    }

    public boolean actualizarDatoDispositivo(String columna, String dato){
        abrirEscrituraBD();
        ContentValues valores = new ContentValues();
        valores.put(columna,dato);
        db.update(TABLA_DISPOSITIVO, valores, COLUMNA_ID+"=0", null);
        return true;
    }

    public boolean actualizarDatoAuxiliar(String columna, String dato){
        abrirEscrituraBD();
        ContentValues valores = new ContentValues();
        valores.put(columna,dato);
        db.update(TABLA_AUX, valores, COLUMNA_ID+"=0", null);
        return true;
    }

    public Cursor obtenerDatos(String tabla, String[] campos, String where, String[] datosWhere) {
        abrirEscrituraBD();
        Cursor c;
        if (where.length() > 0) {
            c = db.query(tabla, campos, where + "=?", datosWhere, null, null, null);
        } else {
            c = db.query(tabla, campos, null, null, null, null, null);
        }
        return c;
    }

    public boolean insertar(String tabla, String[] datosColumnas) {
        abrirEscrituraBD();
        ContentValues nuevoRegistro = new ContentValues();
        if (tabla == TABLA_USUARIOS) {
            nuevoRegistro.put(COLUMNA_NOMBRE_USUARIO, datosColumnas[0]);
            nuevoRegistro.put(COLUMNA_TELEFONO_USUARIO, datosColumnas[1]);
            nuevoRegistro.put(COLUMNA_DESCRIPCION_USUARIO, datosColumnas[2]);

            db.insert(TABLA_USUARIOS, null, nuevoRegistro);
            return true;
        } else if (tabla == TABLA_APP) {
            nuevoRegistro.put(COLUMNA_PRIMER_USO, datosColumnas[0]);
            nuevoRegistro.put(COLUMNA_CONTRASE_NA, datosColumnas[1]);
            nuevoRegistro.put(COLUMNA_ID_USUARIO, datosColumnas[2]);
            db.insert(TABLA_APP, null, nuevoRegistro);
            return true;
        } else if(tabla == TABLA_AUX){
            nuevoRegistro.put(COLUMNA_TIPO_TIEMPO,datosColumnas[0]);
            db.insert(TABLA_AUX,null,nuevoRegistro);
        }
        return false;
    }
}
