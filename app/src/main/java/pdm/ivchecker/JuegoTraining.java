package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class JuegoTraining extends ActionBarActivity {

    //Matriz donde se almacenan los verbos:
    private String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;
    private TextView infinitivo, pasado, participio;

    private int puntuacionJugada;
    private int numPartida=0;

    private int numVerbo, numForma, numLetrasForma;
    private String misterio="";
    private FileOutputStream flujo_fichero;

    //Variables de control del entrenamiento
    private int nivel=0, lista_a_preguntar=0,numero_verbos=0;

    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
        Intent intent = getIntent();
        this.nivel=intent.getIntExtra("nivel",0);
        this.lista_a_preguntar = intent.getIntExtra("lista",0);
        this.numero_verbos = intent.getIntExtra("numero_verbos",0);

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButtonTraining);
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosaTraining);

        infinitivo=(TextView)findViewById(R.id.infinitivoTraining);
        pasado=(TextView)findViewById(R.id.pasadoTraining);
        participio=(TextView)findViewById(R.id.participioTraining);

        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        comprobarVerbo();
                        numPartida++;
                        if(numPartida==numero_verbos) {
                            acabarPartida();
                        }
                        jugar();


                    }
                }
        );

        leerVerbos();



    }

    private void leerVerbos(){

        //Seleccionamos la lista a preguntar
        if(lista_a_preguntar ==0){
            Random rnd = new Random();
            lista_a_preguntar=(int)(rnd.nextDouble() % 3) +1;
        }

        switch(lista_a_preguntar){
            case 1: //Lista soft
                inputStream=getResources().openRawResource(R.raw.ivsoft);
                break;
            case 2: //Lista medium
                inputStream=getResources().openRawResource(R.raw.ivmedium);
                break;
            default:    //Lista Hard
                inputStream=getResources().openRawResource(R.raw.ivhard);
                break;
        }

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


        try {
            //Leemos la primera linea del CSV para crear la matriz de verbos
            String line;
            line = reader.readLine();
            //Creamos la matriz de verbos
            verbos = new String [Integer.parseInt(line)][3];
            //Leemos los verbos
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbos[fila][0] = RowData[0];
                verbos[fila][1] = RowData[1];
                verbos[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Después de cargar los datos comienza el juego:
        this.jugar();
    }

    //Metodo utilizado para guardar la puntuacion en un fichero local
    private void salvar_puntuacion_local(){

        String fichero= "puntuaciones.csv";

        try {

            //Apertura del fichero.
                             /* ######################## COMO BORRAR EL FICHERO DE PUNTUACIONES:

                            + this.flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);
                            + flujo_fichero.close();
                            + */


            this.flujo_fichero = openFileOutput(fichero, MODE_APPEND);
            String prueba = "ESTO_ES_UNA_PRUEBA\n";
            flujo_fichero.write(prueba.getBytes());
            flujo_fichero.close();

            inputStream = openFileInput(fichero);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while(true) {

                String line;
                System.out.println("Lectura");
                line = reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                System.out.println(RowData[0]);

            }
            inputStream.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("ERROR: No ha sido posible abrir el fichero de puntuaciones");
        }
    }
    public void jugar(){
        //Sección de la lógica del juego

        //Asociamos los objetos de la lógica a los de la vista.


        //Para la generación de números:
        Random rnd = new Random();


        //Generamos el verbo a mostrar:
        numVerbo=(int)(rnd.nextDouble() * 16 + 0);

        System.out.println("Verbo elegido: "+numVerbo);

        //Generamos la forma que no aparecerá
        numForma=(int)(rnd.nextDouble() * 3 + 0);

        System.out.println("Forma elegida: "+numForma);

        //Obtenermos el verbo que falta en forma de rallitas:
        numLetrasForma=verbos[numVerbo][numForma].length();
        System.out.println("forma elegida: "+verbos[numVerbo][numForma]+ "  tam: "+numLetrasForma);
        for(int i=0; i<numLetrasForma; i++){
            misterio+=" _ ";
        }

        //Escribimos en la pantalla:
        if(numForma==0)
            infinitivo.setText(misterio);
        else
            infinitivo.setText(verbos[numVerbo][0]);

        if(numForma==1)
            pasado.setText(misterio);
        else
            pasado.setText(verbos[numVerbo][1]);

        if(numForma==2)
            participio.setText(misterio);
        else
            participio.setText(verbos[numVerbo][2]);

        //Después misterio vuelve a estar vacía
        misterio="";


    }

    public void comprobarVerbo(){
        System.out.println("Texto introducido: "+txtVerbo.getText());
        System.out.println("Verbo a comparar: "+verbos[numVerbo][numForma]);

        if(txtVerbo.getText().toString().equals(verbos[numVerbo][numForma])){
            puntuacionJugada++;
        }


        txtVerbo.setText("");
    }

    public void acabarPartida(){
        //Creamos el intent:

        Intent intent = new Intent(Juego.this, Resultados.class);

        //Creamos la información a pasar entre actividades:
        Bundle b = new Bundle();
        b.putString("PUNTOS", String.valueOf(puntuacionJugada));

        //Añadimos la información al intent:
        intent.putExtras(b);

        //Nos vamos al activity resultados:
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego_training, menu);
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
}
