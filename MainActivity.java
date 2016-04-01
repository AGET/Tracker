package aget.aget.workstation.manipulaciongpsgsm;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ManipulacionDatos manipulacionDatos;

    String telefonoDispositivo = "";
    String password = "";
    String retornoInicializacionCorrecto = "begin OK";
    String retornoInicializacionFallido = "begin fail";
    String cmdIniciarPrimeravezGPS = "begin" + password;//"beginpassword";El celularrespondera "begin ok" | Contraceña por defecto 123456
    String cmdCambiarPassword = "password+old password+space+new password”"; //debe ser de 6 digitos
    String cmdEstablecerNumeroAdministrador = "admin+password+space+cell";//respuesta admin ok!
    String cmdBorrarNumeroAutorizado = "noadmin+password+space+authorized number ";
    //Auto Track  el intervalo no debe ser inferior a 20 segundos.
    String cmdGeoPosicionIntervalosAutoTrack = "t030s005n+password";//En este formato se puede cambiar la “s” por S: segundo, m: minuto o h: hora). La configuración debe ser de 3 dígitos y en 255 en el valor máximo.
    String cmdCancelarGeoPsicionIntervalosAutoTrack = "notn+password";//eliminar la opción de "auto Track"
    //Modo Track y modo Vigilancia (para escucha de voz)
    String cmdModoMonitor = "monitor + password";
    String modoMonitorCorrecto = "OK monitor!";
    String cmdModoVigilancia = "Tracker + password";
    String modoVigilanciaCorrecto = "OK tracker";
    //
    String cmdCancelarSOS = "help me!";
    String cmdBateria = "low battery+lat./long.";
    String cmdVelocidad = "speed+password+space+080";
    String respuestaExcesoVelocidad = "OK velocidad!";
    String cmdCancelarAlarmaVelocidad = "nospeed + contraseña";

    String cmdActivarUbicacionLink = "smslink123456";

    String tipoTiempo = "", auxiliar = "";
    int tiempoMaximo = 0;

    Button btnInicioGPS, btncambiarPassword, btnAddAdmin, btnBorrarAdmin, btnObtenerPosicionIntervalos, btnCancelarIntervalos,
            btnCancelarSOS, btnEstablecerVelocidadMaxima,btnMonitoreo,btnVelocidad;
    //btnBateria

    ToggleButton toggleBtnModoLink;

    Switch switch_gps_OR_llamada;

    boolean validador = false;

    private Dialog dialogo;

    public static final String TABLA_APP = "Aplicacion";
    public static final String TABLA_USUARIOS = "Usuarios";
    public static final String TABLA_DISPOSITIVO = "Dispositivo";
    public static final String COLUMNA_ID_NOMBRE = "_id";
    public static final String COLUMNA_GPS_PRIMERA_VEZ_NOMBRE = "GPS_primera_vez";
    public static final String COLUMNA_CONTRASE_NA_NOMBRE = "Contrase_na";
    public static final String COLUMNA_ESTADO_GPS_NOMBRE = "Estado_GPS";
    public static final String COLUMNA_MODO_LINK_NOMBRE = "Modo_link";
    public static final String COLUMNA_MODO_LLAMADA_GPS_NOMBRE = "Modo_llamada_GPS";
    public static final String COLUMNA_TELEFONO_DISPOSITIVO_NOMBRE = "Telefono_dispositivo";
    public static final String TABLA_AUX = "auxiliar";
    public static final String COLUMNA_TIPO_TIEMPO_NOMBRE = "Tipo_tiempo";

    static final String tituloContrase_naApp = "Introdusca la contraseña de la aplicacion";
    static final String tituloContrase_naDispositivo = "Introdusca la contraseña del dispositivo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manipulacionDatos = new ManipulacionDatos(this);

        btnInicioGPS = (Button) findViewById(R.id.btnInicioGPS);
        btncambiarPassword = (Button) findViewById(R.id.btnCambiarPassword);
        btnAddAdmin = (Button) findViewById(R.id.btnAddAdmin);
        btnBorrarAdmin = (Button) findViewById(R.id.btnBorrarAdmin);
        btnObtenerPosicionIntervalos = (Button) findViewById(R.id.btnObtenerPosicionIntervalos);
        btnCancelarIntervalos = (Button) findViewById(R.id.btnCancelarIntervalos);
        btnCancelarSOS = (Button) findViewById(R.id.btnCancelarSOS);
        btnMonitoreo =(Button)findViewById(R.id.btnMonitoreo);
        btnVelocidad=(Button)findViewById(R.id.btnVelocidad);
        btnEstablecerVelocidadMaxima = (Button) findViewById(R.id.btnEstablecerVelocidadMaxima);
        toggleBtnModoLink = (ToggleButton) findViewById(R.id.toggleBtnModoLink);
        switch_gps_OR_llamada = (Switch) findViewById(R.id.switch_gps_OR_llamada);

        String columnas[] = {COLUMNA_GPS_PRIMERA_VEZ_NOMBRE};
        String gpsIniciado = manipulacionDatos.getDatoDispositivo(columnas);

        columnas = new String[]{COLUMNA_MODO_LINK_NOMBRE};
        String modolink = manipulacionDatos.getDatoDispositivo(columnas);

        if (gpsIniciado.compareTo("0") == 0) {
            if (modolink.compareTo("1") == 0) {
                toggleBtnModoLink.setEnabled(true);
            } else {
                toggleBtnModoLink.setEnabled(false);
            }
            btnInicioGPS.setBackgroundResource(R.drawable.ic_media_pause);
            btnInicioGPS.setEnabled(false);
            btncambiarPassword.setEnabled(true);
            btnAddAdmin.setEnabled(true);
            btnBorrarAdmin.setEnabled(true);
            btnObtenerPosicionIntervalos.setEnabled(true);
            btnCancelarIntervalos.setEnabled(true);
            btnCancelarSOS.setEnabled(true);
            btnMonitoreo.setEnabled(true);
            btnVelocidad.setEnabled(true);
            btnEstablecerVelocidadMaxima.setEnabled(true);
            switch_gps_OR_llamada.setEnabled(true);
        } else {
            //Advertencia: quitar el comentario
            btnInicioGPS.setEnabled(true);
            btncambiarPassword.setEnabled(false);
            btnAddAdmin.setEnabled(false);
            btnBorrarAdmin.setEnabled(false);
            btnMonitoreo.setEnabled(false);
            btnObtenerPosicionIntervalos.setEnabled(false);
            btnCancelarIntervalos.setEnabled(false);
            btnCancelarSOS.setEnabled(false);
            btnEstablecerVelocidadMaxima.setEnabled(false);
            switch_gps_OR_llamada.setEnabled(false);
        }

        if (manipulacionDatos.primerUsoApp()) {
            dialogoRegistroEnAPP();
        } else {
            if (dialogoConrase_na(tituloContrase_naApp)) {
                if (btnInicioGPS.isEnabled() == false) {
                    btnInicioGPS.setEnabled(true);
                    btncambiarPassword.setEnabled(true);
                    btnAddAdmin.setEnabled(true);
                    btnBorrarAdmin.setEnabled(true);
                    btnObtenerPosicionIntervalos.setEnabled(true);
                    btnCancelarIntervalos.setEnabled(true);
                    btnCancelarSOS.setEnabled(true);
                    btnMonitoreo.setEnabled(true);
                    //btnBateria.setEnabled(true);
                    btnEstablecerVelocidadMaxima.setEnabled(true);
                    switch_gps_OR_llamada.setEnabled(true);
                }
            }
        }
        password = manipulacionDatos.getContrase_naDispositivo();
        String columna[] = {COLUMNA_TELEFONO_DISPOSITIVO_NOMBRE};
        telefonoDispositivo = manipulacionDatos.getDatoDispositivo(columna);

        Log.v("AGET", "Datos; Paswdword: " + password + " telefono: " + telefonoDispositivo + " Dispositivo Iniciado: " + gpsIniciado + " ModoLink: " + modolink);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mtIniciarGPS(View view) {

        cmdIniciarPrimeravezGPS = "begin" + password;


        manipulacionDatos.sendSMS(telefonoDispositivo, cmdIniciarPrimeravezGPS);
        Log.v("AGET", "mtIniciarGPS: telefono: " + telefonoDispositivo + " comando: " + cmdIniciarPrimeravezGPS);

        String telefono = "";

        telefono = manipulacionDatos.getTelefonoUsuario();
        cmdEstablecerNumeroAdministrador = "admin" + password + " " + telefono;
        manipulacionDatos.sendSMS(telefonoDispositivo, cmdEstablecerNumeroAdministrador);
        Log.v("AGET", "mtIniciarGPS: telefono: " + telefonoDispositivo + " comando: " + cmdEstablecerNumeroAdministrador);


        String[] datosTablaDispositivo = {"0"};
        manipulacionDatos.actualizarDispositivo(COLUMNA_GPS_PRIMERA_VEZ_NOMBRE, datosTablaDispositivo);


        btnInicioGPS.setBackgroundResource(R.drawable.ic_media_pause);

        btnInicioGPS.setEnabled(true);
        btncambiarPassword.setEnabled(true);
        btnAddAdmin.setEnabled(true);
        btnBorrarAdmin.setEnabled(true);
        btnObtenerPosicionIntervalos.setEnabled(true);
        btnCancelarIntervalos.setEnabled(true);
        btnCancelarSOS.setEnabled(true);
        btnMonitoreo.setEnabled(true);
        btnEstablecerVelocidadMaxima.setEnabled(true);
        switch_gps_OR_llamada.setEnabled(true);
        btnInicioGPS.setEnabled(false);
    }

    public void mtModoLink(View view) {
        if (toggleBtnModoLink.isChecked()) {
            String[] datosTablaDispositivo = {"1"};
            manipulacionDatos.actualizarDispositivo(COLUMNA_MODO_LINK_NOMBRE, datosTablaDispositivo);
            cmdActivarUbicacionLink = "smslink" + password;
            manipulacionDatos.sendSMS(telefonoDispositivo, cmdActivarUbicacionLink);
            toggleBtnModoLink.setEnabled(false);
            Toast.makeText(this, "Modo link: Activado", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Modo link: Desactivado", Toast.LENGTH_SHORT).show();
    }

    public void mtCambiarPass(View view) {
        dialogoCambiarPassword();
    }

    public void mtA_nadirAdmin(View view) {
        dialogoNuevoAdministrador();
    }

    public void mtBorrarAdmin(View view) {
        Intent intent = new Intent(this,
                Administrador.class);
        intent.putExtra("password", password);
        intent.putExtra("telefonoDispositivo", telefonoDispositivo);
        startActivity(intent);
    }

    public void mtObtenerPosisionIntervalos(View view) {
        dialogoAutotrack();
    }

    public void mtCancelarIntervalos(View view) {
        cmdCancelarGeoPsicionIntervalosAutoTrack = "notn" + password;
        manipulacionDatos.sendSMS(telefonoDispositivo, cmdCancelarGeoPsicionIntervalosAutoTrack);
        Toast.makeText(this, "Comando enviado", Toast.LENGTH_SHORT).show();

    }

    public void mtBateria(View view) {
        Toast.makeText(this, "Implement Batery", Toast.LENGTH_LONG).show();
    }


    public void mtCancelarSOS(View view) {
        cmdCancelarSOS = "help me!";
        manipulacionDatos.sendSMS(telefonoDispositivo, cmdCancelarSOS);
        Toast.makeText(this, "Comando enviado", Toast.LENGTH_SHORT).show();
    }

    public void mtMonitorearDispositivo(View view) {
        callOptenerInformaicon(telefonoDispositivo);
    }

    public void callOptenerInformaicon(String telefonoDispositivo) {
        try {
            if (telefonoDispositivo.length() > 0) {
                //realiza la llamada
                Uri llamada = Uri.parse("tel:" + telefonoDispositivo);
                Intent intent = new Intent(Intent.ACTION_CALL, llamada);
                startActivity(intent);
            } else {
                //si no se escribio numero telefonico, avisa
                Toast.makeText(this, "El numero de telefono no fue encontrado", Toast.LENGTH_SHORT).show();
            }

        } catch (ActivityNotFoundException activityException) {
            //si se produce un error, se muestra en el LOGCAT
            android.util.Log.e("ET", "No se pudo realizar la peticion....", activityException);
        }

    }

    public void mtEstablecerVelocidad(View view) {
        dialogoVelocidad();

    }

    public void mtModoLlamada_OR_GPS(View view) {
        if (switch_gps_OR_llamada.isChecked()) {
            cmdModoMonitor = "monitor" + password;
            manipulacionDatos.sendSMS(telefonoDispositivo, cmdModoMonitor);

            Toast.makeText(this, "Monitor: ON", Toast.LENGTH_SHORT).show();
        } else {
            cmdModoVigilancia = "tracker" + password;
            manipulacionDatos.sendSMS(telefonoDispositivo, cmdModoVigilancia);
            Toast.makeText(this, "Tracker: ON", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogoVelocidad() {

        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialogo.setCancelable(true);
        dialogo.setTitle("Nuevo Admin");
        dialogo.setContentView(R.layout.dialogo_velocidad);
        final SeekBar seekBarVelocidad = (SeekBar) dialogo.findViewById(R.id.seekBarVelocidad);
        final TextView textResultadoSeekBarVelocidad = (TextView) dialogo.findViewById(R.id.textResultadoSeekBarVelocidad);

        final String[] velocidad = new String[1];

        seekBarVelocidad.setMax(350);
        seekBarVelocidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textResultadoSeekBarVelocidad.setText(progress + "");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                velocidad[0] = textResultadoSeekBarVelocidad.getText().toString();

                String aux = "";
                if (velocidad[0].length() == 1) {
                    aux = velocidad[0];
                    velocidad[0] = "00" + aux;
                } else if (velocidad[0].length() == 2) {
                    aux = velocidad[0];
                    velocidad[0] = "0" + aux;
                }

                cmdVelocidad = "speed" + password + " " + velocidad[0];
                manipulacionDatos.sendSMS(telefonoDispositivo, cmdVelocidad);
                Toast.makeText(dialogo.getContext(), "Comando enviado", Toast.LENGTH_SHORT).show();
                Toast.makeText(dialogo.getContext(), cmdVelocidad, Toast.LENGTH_LONG).show();

            }
        });
        dialogo.show();
    }

    public void dialogoAutotrack() {

        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialogo.setCancelable(true);
        dialogo.setTitle("Nuevo Admin");
        dialogo.setContentView(R.layout.dialogo_autotrack);
        final Spinner spinnerTiempo = (Spinner) dialogo.findViewById(R.id.spinnerTiempo);
        final TextView textResultadoSpinnerTiempo = (TextView) dialogo.findViewById(R.id.textResultadoSpinnerTiempo);
        final SeekBar seekBarTiempo = (SeekBar) dialogo.findViewById(R.id.seekBarTiempo);
        final TextView textResultadoSeekBarTiempo = (TextView) dialogo.findViewById(R.id.textResultadoSeekBarTiempo);
        final SeekBar seekBarCantidad = (SeekBar) dialogo.findViewById(R.id.seekBarCantidad);
        final TextView textResultadoSeekBarCantidad = (TextView) dialogo.findViewById(R.id.textResultadoSeekBarCantidad);

        final String[] seleccionado = {""};
        final String[] tiempo = new String[1];
        final String[] cantidad = new String[1];


        final List<String> lista;
        lista = new ArrayList<String>();
        lista.add("Segundos");
        lista.add("Minutos");
        lista.add("Horas");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTiempo.setAdapter(adaptador);

        spinnerTiempo.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(), "Seleccionado: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                seleccionado[0] = "" + parent.getItemAtPosition(position).toString();
                textResultadoSpinnerTiempo.setText(seleccionado[0]);

                String[] datos = {seleccionado[0]};
                manipulacionDatos.actualizarAuxiliar(COLUMNA_TIPO_TIEMPO_NOMBRE, datos);
                establecer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void establecer() {
                String columnas[] = {COLUMNA_TIPO_TIEMPO_NOMBRE};
                auxiliar = manipulacionDatos.getDatoAuxiliar(columnas);

                Log.v("AGET", "TIEMPO-AUXILIAR: " + textResultadoSpinnerTiempo.getText().toString() + " auxi: " + auxiliar + " clase:");
                if (auxiliar.compareToIgnoreCase("Segundos") == 0 || auxiliar.compareToIgnoreCase("s") == 0) {
                    tiempoMaximo = 39;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "s";
                    Log.v("AGET", "TIEMPO: s");
                }
                if (auxiliar.compareToIgnoreCase("Minutos") == 0) {
                    tiempoMaximo = 59;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "m";
                    Log.v("AGET", "TIEMPO: m");
                }

                if (auxiliar.compareToIgnoreCase("Horas") == 0) {
                    tiempoMaximo = 23;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "h";
                    Log.v("AGET", "TIEMPO: h");
                }

            }
        });
        //////


        //////

        seekBarTiempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seleccionado[0].compareTo(lista.get(0)) == 0) {
                    textResultadoSeekBarTiempo.setText(progress + 20 + "");
                } else {
                    textResultadoSeekBarTiempo.setText(progress + "");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        seekBarCantidad.setMax(300);
        seekBarCantidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /*private Toast toastStart = Toast.makeText(MainActivity.this, getText(R.string.start), Toast.LENGTH_SHORT);
            private Toast toastStop = Toast.makeText(MainActivity.this, getText(R.string.stop), Toast.LENGTH_SHORT);*/

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textResultadoSeekBarCantidad.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /*toastStop.cancel();
                toastStart.setGravity(Gravity.TOP | Gravity.LEFT, 60, 110);
                toastStart.show();*/
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /*toastStart.cancel();
                toastStop.setGravity(Gravity.TOP | Gravity.RIGHT, 60, 110);
                toastStop.show();*/
            }
        });


        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tiempo[0] = textResultadoSeekBarTiempo.getText().toString();
                cantidad[0] = textResultadoSeekBarCantidad.getText().toString();
                String aux = "";
                if (tiempo[0].length() == 1) {
                    aux = tiempo[0];
                    tiempo[0] = "00" + aux;
                } else if (tiempo[0].length() == 2) {
                    aux = tiempo[0];
                    tiempo[0] = "0" + aux;
                }

                if (cantidad[0].length() == 1) {
                    aux = cantidad[0];
                    cantidad[0] = "00" + aux;
                } else if (cantidad[0].length() == 2) {
                    aux = cantidad[0];
                    cantidad[0] = "0" + aux;
                }
                cmdGeoPosicionIntervalosAutoTrack = "t" + tiempo[0] + tipoTiempo + cantidad[0] + "n" + password;
                manipulacionDatos.sendSMS(telefonoDispositivo, cmdGeoPosicionIntervalosAutoTrack);
                Toast.makeText(dialogo.getContext(), "Comando enviado", Toast.LENGTH_SHORT).show();
                Toast.makeText(dialogo.getContext(), cmdGeoPosicionIntervalosAutoTrack, Toast.LENGTH_LONG).show();

            }
        });
        dialogo.show();
    }

    public void dialogoNuevoAdministrador() {

        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialogo.setCancelable(true);
        dialogo.setTitle("Nuevo Admin");
        dialogo.setContentView(R.layout.nuevo_administrador);
        final EditText edtIntroducirNombre = (EditText) dialogo.findViewById(R.id.edtIntroducirNombre);
        final EditText edtIntroducirTelefono = (EditText) dialogo.findViewById(R.id.edtIntroducirTelefono);
        final EditText edtIntroducirDescripcion = (EditText) dialogo.findViewById(R.id.edtIntroducirDescripcion);
        final TextView alerta = (TextView) dialogo.findViewById(R.id.alerta);

        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
            String nombre = "", telefono = "", descripcion = "", contrasenia = "", corroborarContrasenia = "";

            @Override
            public void onClick(View view) {
                nombre = edtIntroducirNombre.getText().toString();
                telefono = edtIntroducirTelefono.getText().toString();
                descripcion = edtIntroducirDescripcion.getText().toString();
                if (nombre.length() > 1 && telefono.length() > 1 && descripcion.length() > 1) {

                    String[] datosTablaUsuario = {nombre, telefono, descripcion};
                    manipulacionDatos.insertar(TABLA_USUARIOS, datosTablaUsuario);

                    cmdEstablecerNumeroAdministrador = "admin" + password + " " + telefono;

                    manipulacionDatos.sendSMS(telefonoDispositivo, cmdEstablecerNumeroAdministrador);
                    Log.v("AGET", "mtA_nadirAdmin: telefono: " + telefonoDispositivo + " Comando: " + cmdEstablecerNumeroAdministrador);

                    edtIntroducirNombre.setText("");
                    edtIntroducirTelefono.setText("");
                    edtIntroducirDescripcion.setText("");
                    dialogo.dismiss();

                } else {
                    alerta.setText("Faltan algunos datos en el registro\nasegurece lenar todos los campos");
                }
            }
        });
        dialogo.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        dialogo.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,(75 * height) / 100);
        dialogo.getWindow().setGravity(Gravity.TOP | Gravity.LEFT);
    }

    public boolean dialogoCambiarPassword() {
        validador = false;
        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        dialogo.setCancelable(true);

        dialogo.setTitle("Ingrese solo nombre");
        dialogo.setContentView(R.layout.nuevo_password);

        final TextView txtTitulo = (TextView) dialogo.findViewById(R.id.titulo_contrase_na);
        final EditText edtContrase_na = (EditText) dialogo.findViewById(R.id.edtContrase_na);
        final EditText edtContrase_naCorroborar = (EditText) dialogo.findViewById(R.id.edtContrase_naCorroborar);
        final TextView alerta = (TextView) dialogo.findViewById(R.id.alerta);

        txtTitulo.setText("");

        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {

            String contrase_na = "", contrase_naCorroborar = "";

            @Override
            public void onClick(View view) {
                contrase_na = edtContrase_na.getText().toString();
                contrase_naCorroborar = edtContrase_naCorroborar.getText().toString();
                if (contrase_na.compareTo(contrase_naCorroborar) == 0) {
                    if (contrase_naCorroborar.length() == 6) {
                        cmdCambiarPassword = "password" + password + " " + contrase_naCorroborar;
                        manipulacionDatos.sendSMS(telefonoDispositivo, cmdCambiarPassword);
                        String[] datos = {contrase_naCorroborar};
                        manipulacionDatos.actualizarDispositivo(COLUMNA_CONTRASE_NA_NOMBRE, datos);
                        Log.v("AGET", "mtCambiarPass: telefono: " + telefonoDispositivo + " Comando: " + cmdCambiarPassword);
                        password = contrase_naCorroborar;
                        dialogo.dismiss();
                    } else {
                        alerta.setText("La contraseña debe contener 6 digitos");
                    }
                } else {
                    alerta.setText("Las contraseñas no coinciden");
                }
            }
        });
        dialogo.show();
        return validador;
    }

    public boolean dialogoConrase_na(final String titulo) {
        validador = false;
        // con este tema personalizado evitamos los bordes por defecto
        //dialogo = new Dialog(this,R.style.Theme_Dialog_Translucent);
        //dialogo = new Dialog(this, R.style.Base_V14_Theme_AppCompat_Light_Dialog);
        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);

        //deshabilitamos el título por defecto
        //dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        dialogo.setCancelable(false);

        //establecemos el contenido de nuestro dialog
        dialogo.setTitle("Ingrese solo nombre");
        dialogo.setContentView(R.layout.introducir_password);

        final TextView txtTitulo = (TextView) dialogo.findViewById(R.id.titulo_contrase_na);
        final EditText edtContrase_na = (EditText) dialogo.findViewById(R.id.contrase_na);
        final TextView alerta = (TextView) dialogo.findViewById(R.id.alerta);

        txtTitulo.setText(titulo);

        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
            String contrase_na = "";

            @Override
            public void onClick(View view) {
                contrase_na = edtContrase_na.getText().toString();
                if (contrase_na.length() > 0) {
                    if (titulo.compareTo(tituloContrase_naApp) == 0) {
                        if (manipulacionDatos.login(contrase_na, TABLA_APP)) {
                            validador = true;
                            dialogo.dismiss();
                        } else {
                            alerta.setText("Error de contraseña");
                        }
                    } else if (titulo.compareTo(tituloContrase_naDispositivo) == 0) {
                        if (manipulacionDatos.login(contrase_na, TABLA_DISPOSITIVO)) {
                            validador = true;
                            dialogo.dismiss();
                        } else {
                            alerta.setText("Error de contraseña");
                        }
                    }
                } else {
                    alerta.setText("Escriba la contraceña");
                }
            }
        });

        dialogo.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        dialogo.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, (50 * height) / 100);
        return validador;
    }

    public void dialogoRegistroEnAPP() {

        dialogo = new Dialog(this, R.style.Base_Theme_AppCompat_Light_Dialog_Alert);
        dialogo.setCancelable(false);
        dialogo.setTitle("Registro APP");
        dialogo.setContentView(R.layout.primer_uso_app);
        final EditText edtIntroducirNombre = (EditText) dialogo.findViewById(R.id.edtIntroducirNombre);
        final EditText edtIntroducirTelefono = (EditText) dialogo.findViewById(R.id.edtIntroducirTelefono);
        final EditText edtIntroducirDescripcion = (EditText) dialogo.findViewById(R.id.edtIntroducirDescripcion);
        final EditText edtIntroducirContrase_na = (EditText) dialogo.findViewById(R.id.edtIntroducirContrase_na);
        final EditText edtIntroducirCorroborarContrase_na = (EditText) dialogo.findViewById(R.id.edtIntroducirCorroborarContrase_na);
        final EditText edtIntroducirTelefonoDispositivo = (EditText) dialogo.findViewById(R.id.edtIntroducirTelefonoDispositivo);
        final TextView alerta = (TextView) dialogo.findViewById(R.id.alerta);

        ((Button) dialogo.findViewById(R.id.aceptar)).setOnClickListener(new View.OnClickListener() {
            String nombre = "", telefono = "", descripcion = "", contrasenia = "", corroborarContrasenia = "";

            @Override
            public void onClick(View view) {
                nombre = edtIntroducirNombre.getText().toString();
                telefono = edtIntroducirTelefono.getText().toString();
                descripcion = edtIntroducirDescripcion.getText().toString();
                contrasenia = edtIntroducirContrase_na.getText().toString();
                corroborarContrasenia = edtIntroducirCorroborarContrase_na.getText().toString();
                telefonoDispositivo = edtIntroducirTelefonoDispositivo.getText().toString();
                if (nombre.length() > 1 && telefono.length() > 1 && descripcion.length() > 1 && contrasenia.length() > 0 && corroborarContrasenia.length() > 0) {
                    if (contrasenia.compareTo(corroborarContrasenia) == 0) {


                        String[] datosTablaUsuario = {nombre, telefono, descripcion};
                        manipulacionDatos.insertar(TABLA_USUARIOS, datosTablaUsuario);

                        String[] datosTablaApp = {"0", corroborarContrasenia, manipulacionDatos.getIdPrimerUsuario()};
                        manipulacionDatos.insertar(TABLA_APP, datosTablaApp);

                        String[] datosTablaDispositivo = {telefonoDispositivo};
                        manipulacionDatos.actualizarDispositivo(COLUMNA_TELEFONO_DISPOSITIVO_NOMBRE, datosTablaDispositivo);

                        edtIntroducirNombre.setText("");
                        edtIntroducirTelefono.setText("");
                        edtIntroducirDescripcion.setText("");
                        edtIntroducirContrase_na.setText("");
                        edtIntroducirCorroborarContrase_na.setText("");
                        dialogo.dismiss();
                    } else {
                        alerta.setText("Las contraseñas no son las mismas\nasegurece de que las dos coincidan");
                    }
                } else {
                    alerta.setText("Faltan algunos datos en el registro\nasegurece lenar todos los campos");
                }
            }
        });
        dialogo.show();
    }
}