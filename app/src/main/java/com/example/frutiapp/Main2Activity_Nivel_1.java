package com.example.frutiapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity_Nivel_1 extends AppCompatActivity {

    // Objetos en el Activity
    private TextView tv_nombre, tv_score;
    private ImageView iv_num_1, iv_num_2, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    // Atributos para programacion de la logica
    int score;
    int random_num_1,random_num_2;
    int resultado, vidas = 3;

    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__nivel_1);

        // Seteamos para que apresca el icono
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        Toast.makeText(this,"Nivel 1 - Sumas Básicas", Toast.LENGTH_SHORT).show();

        tv_nombre = (TextView)findViewById(R.id.textView_nombre);
        tv_score = (TextView)findViewById(R.id.textView_score);
        iv_num_1 = (ImageView)findViewById(R.id.imageView_num1);
        iv_num_2 = (ImageView)findViewById(R.id.imageView_num2);
        iv_vidas = (ImageView)findViewById(R.id.imageView_vidas);
        et_respuesta = (EditText)findViewById(R.id.editText_resultado);

        // Seteamos el nombre del jugador
        nombre_jugador = getIntent().getStringExtra("nombre_jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);

        // Activamos la musica
        mp = MediaPlayer.create(this,R.raw.goats);
        mp.start();
        mp.setLooping(true);

        mp_great = MediaPlayer.create(this,R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();
    } // Fin de la funcion onCreate

    public void NumAleatorio(){
        if (score <= 9){
            do {
                // Numeros Aleatorios de 0 a 9
                random_num_1 = (int)(Math.random() * 10);
                random_num_2 = (int)(Math.random() * 10);

                resultado = random_num_1 + random_num_2;
            }while(resultado > 10);

            int id1 = getResources().getIdentifier(numero[random_num_1], "drawable", getPackageName());
            iv_num_1.setImageResource(id1);

            int id2 = getResources().getIdentifier(numero[random_num_2], "drawable", getPackageName());
            iv_num_2.setImageResource(id2);

        } else {
            Intent intent = new Intent(this, Main2Activity_Nivel_2.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);

            // Enviamos la info a la siguiente activity
            intent.putExtra("nombre_jugador",nombre_jugador);
            intent.putExtra("score",string_score);
            intent.putExtra("vidas",string_vidas);

            startActivity(intent);
            finish();
            mp.stop();
            mp.release();
        }
    } // Fin de NumAleatorio

    public void Comprobar(View view){
        String respuesta = et_respuesta.getText().toString();

        // Validamos la respuesta
        if (!respuesta.equals("")){
            int res = Integer.parseInt(respuesta);

            // Verificamos si la respuesta es correcta
            if (res == resultado){
                mp_great.start();

                // Actualizamos el score
                score++;
                tv_score.setText("Score: " + score); // Python eres tu?

                // Actualizamos la base de Datos
                BaseDeDatos();

            } else {
                mp_bad.start();
                vidas--;
                // Actualizamos la base de Datos
                BaseDeDatos();

                switch (vidas){
                    case 2:
                        Toast.makeText(this,"Te quedan 2 manzanas",Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this,"Te queda 1 manzana",Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this,"Has perdido todas tus manzanas",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,MainActivity.class);

                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                } // Fin de Switch
            } // Fin de else

            // Limpiamos el edit text
            et_respuesta.setText("");

            // Actualizamos los valores de NumAleatorio
            NumAleatorio();

        } else {
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }

    } // Fin de la funcion Comprobar

    /*
    La forma en que se actualiza esta base de datos es sólo agregando un registro(1 sólo row)
    Este registro va actualizando el mejor score
    Cuando se mejora el score no solo lo actualiza sino tb actualiza el nombre del jugador
    Sin embargo se sigue manteniendo un solo registro en la base de datos
     */
    public void BaseDeDatos(){
        AdminSQLiteOpenHelpter admin = new AdminSQLiteOpenHelpter(this, "database",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor query = db.rawQuery("select * from puntaje where score = (select max(score) from puntaje)",null);

        if (query.moveToFirst()){
            String temp_nombre = query.getString(0);
            String temp_score = query.getString(1);

            int best_score = Integer.parseInt(temp_score);

            if (score > best_score) {
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("score", score);

                // Actualizamos la base de datos
                db.update("puntaje",modificacion,"score=" + best_score, null);
            }
        }
        else{
            ContentValues insertar = new ContentValues();

            insertar.put("nombre",nombre_jugador);
            insertar.put("score",score);

            db.insert("puntaje",null,insertar);
        }
        // Cerramos la DB
        db.close();

    } // Fin de la funcion BaseDeDatos

    // Sobreescribirmos el boton de "atras", para que no se reinicie
    @Override
    public void onBackPressed(){

    }

} // Fin de la Clase Main2Activity_Nivel_1
