package aget.aget.workstation.manipulaciongpsgsm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by workstation on 17/11/15.
 */
public class Administrador extends MainActivity {

    public static final String TABLA_USUARIOS = "Usuarios";
    public static final String COLUMNA_TELEFONO_USUARIO = "Telefono_usuario";
    ListView lista;
    String cmdBorrarNumeroAutorizado = "noadmin+password+space+authorized number ";
    String password = "", telefonoDispositivo = "";
    ManipulacionDatos manipulacionDatos;
    int posicion = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        Bundle parametros = getIntent().getExtras();
        password = parametros.getString("password");
        telefonoDispositivo = parametros.getString("telefonoDispositivo");

        manipulacionDatos = new ManipulacionDatos(this);

        lista = (ListView) findViewById(R.id.lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicion = position + 1;
                Log.v("AGET","position: "+position);
                Log.v("AGET","posicion: "+posicion);
                String valor = (String) lista.getItemAtPosition(position);
                StringTokenizer st = new StringTokenizer(valor, "-");
                String _id = st.nextToken();
                String nombre = st.nextToken();
                String telefono = st.nextToken();
                String descripcion = st.nextToken();
                lanzarAlerta(nombre, telefono, descripcion);
            }
        });
    }


    public void lanzarAlerta(final String nom, final String tel, final String descripcion) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Administrador.this);
        dialog.setTitle("Que desea realizar?");
        dialog.setMessage(nom);
        dialog.setCancelable(true);
        dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("AGET", tel);
                if (posicion > 1) {
                    borrar(tel);
                } else {
                    Toast.makeText(getApplicationContext(), "EL usuario es administrador de la aplicacion y no se puede eliminar", Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.setNegativeButton("Cencelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void borrar(String telefono) {
        if (manipulacionDatos.eliminarDato(TABLA_USUARIOS, COLUMNA_TELEFONO_USUARIO, telefono)) {
            Toast.makeText(this, "Correcto!", Toast.LENGTH_LONG).show();
        }
        cmdBorrarNumeroAutorizado = "noadmin" + password + " " + telefono;
        manipulacionDatos.sendSMS(telefonoDispositivo, cmdBorrarNumeroAutorizado);
        Log.v("AGET", "mtBorrar: telefono: " + telefonoDispositivo + " Comando: " + cmdBorrarNumeroAutorizado);
        actualiza();
    }

    protected void actualiza() {
        ArrayList<String> datos = manipulacionDatos.getDatosUsuarios();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        datos);
        lista.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            actualiza();
        }
    }

}
