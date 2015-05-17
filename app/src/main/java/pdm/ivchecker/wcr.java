package pdm.ivchecker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import pdm.red.ConexionServidor;
import pdm.red.Jugador;


public class wcr extends ActionBarActivity {

    private TextView columnaPosicion, columnaNombre, columnaPuntos, columnaPais;

    private ConexionServidor miConexion = new ConexionServidor();

    List<Jugador>rankingJugadores;


    PopupWindow popUp;

    //Publicidad
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wcr);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        columnaPosicion=(TextView)findViewById(R.id.columnaPosicion);
        columnaNombre=(TextView)findViewById(R.id.columnaNombre);
        columnaPuntos=(TextView)findViewById(R.id.columnaPuntos);
        columnaPais=(TextView)findViewById(R.id.columnaPais);

        columnaPosicion.setText("");
        columnaNombre.setText("");
        columnaPuntos.setText("");
        columnaPais.setText("");

        //Llamada clave de la clase
        rankingJugadores = miConexion.pedirRankingNueva();



        if(rankingJugadores!=null) {
            System.out.println("Recibidos "+rankingJugadores.size()+" elementos");

            int n = 1;

            for (Jugador jugador : rankingJugadores) {
                columnaPosicion.append(Integer.toString(n) + "\n");
                columnaNombre.append(jugador.getNombre() + "\n");
                columnaPuntos.append(Integer.toString(jugador.getPuntuacion()) + "\n");
                columnaPais.append(jugador.getPais() + "\n");
                n++;
            }
        }else{
            //Mostramos una ventana emergenente avisando de que ha ocurrido un error y que se revise la conexión a la red:
            popUpFalloInternet();

        }


        //Publicidad:
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                .addTestDevice("9011F9E7CEC921EF1BB8A17A36B24813") //El telefono de Juan
                .build();
        adView.loadAd(adRequest);

    }//Fin onCreate


    private void popUpFalloInternet(){


        try{


            Context context = wcr.this;
            String title = "Error de red";
            String message = "Parece que hay algun problema con tu conexion. Revisala e intentalo de nuevo.";
            String button1String = "Ok";
            String button2String = "Recargar";

            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle(title);
            ad.setMessage(message);
            ad.setCancelable(false);

            ad.setPositiveButton(
                    button1String,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(wcr.this, ActividadPrincipal.class);
                            startActivity(intent);
                        }
                    }
            );

            ad.setNegativeButton(
                    button2String,
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(wcr.this, wcr.class);
                            startActivity(intent);
                        }
                    }
            );

            //
            ad.show();




            /*

            // Created a new Dialog
            Dialog dialog = new Dialog(wcr.this);

// Set the title
           // dialog.setTitle("Dialog Title");

// inflate the layout
            dialog.setContentView(R.layout.popup);

// Set the dialog text -- this is better done in the XML
            //TextView text = (TextView)dialog.findViewById(R.id.dialog_text_view);
            //text.setText("This is the text that does in the dialog box");

// Display the dialog
            dialog.show();
            /*
            System.out.println("abriendo popup");
            LayoutInflater inflater = (LayoutInflater)wcr.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            PopupWindow pw = new PopupWindow(
                    inflater.inflate(R.layout.popup, null, false),
                    100,
                    100,
                    true);
            // The code below assumes that the root container has an id called 'main'
            pw.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);
            */
            /*
            View layout = inflater.inflate(R.layout.popup,(ViewGroup)findViewById(R.id.fragment_container));

            popUp = new PopupWindow(layout, 300,300, true);
            popUp.showAtLocation(layout, Gravity.CENTER,0,0);
            */

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(wcr.this, ActividadPrincipal.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }


}
