package com.example.frutiapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.input.InputManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_bestScore;
    private MediaPlayer mp;

    int num_aleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText)findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView)findViewById(R.id.imageView_personaje);
        tv_bestScore = (TextView)findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;
        if ( num_aleatorio == 0 || num_aleatorio == 10 ){
            id = getResources().getIdentifier("mango","drawable",getPackageName());
        } else if ( num_aleatorio == 1 || num_aleatorio == 9 ){
            id = getResources().getIdentifier("fresa","drawable",getPackageName());
        } else if ( num_aleatorio == 2 || num_aleatorio == 8){
            id = getResources().getIdentifier("manzana","drawable",getPackageName());
        } else if ( num_aleatorio == 3 || num_aleatorio == 7 ){
            id = getResources().getIdentifier("sandia","drawable",getPackageName());
        } else {
            id = getResources().getIdentifier("uva","drawable",getPackageName());
        }

        iv_personaje.setImageResource(id);

        // Manejando la musica (Se inicia y se vuelve en un loop infinito)
        mp = MediaPlayer.create(this,R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

        // Administramos las entrada en la DB de SQLite
        AdminSQLiteOpenHelpter admin = new AdminSQLiteOpenHelpter(this, "database",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor query = db.rawQuery(
                "select * from puntaje where score = (select max(score) from puntaje)",null
        );

        if (query.moveToFirst()){
            // Extraemos la información de la consulta
            String temp_nombre = query.getString(0);
            String temp_score = query.getString(1);

            // Seteamos el texto
            tv_bestScore.setText("Record: " + temp_score + " de " + temp_nombre);

            db.close();
        }
        else {
            db.close();
        }


    } // Fin de la funcion onCreate

    public void Jugar(View view){
        String nombre = et_nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release(); // Destruye el objeto para liberar recursos y poder usarla denuevo

            // Cambiando de Activity
            Intent intent = new Intent(this,Main2Activity_Nivel_1.class);

            intent.putExtra("nombre_jugador", nombre);
            startActivity(intent);
            finish();
        } else {
            // mostramos el texto como un toast
            Toast.makeText(this,"Primero debes escribir tu nombre",Toast.LENGTH_SHORT).show();

            // mostramos el teclado por defecto
            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre,InputMethodManager.SHOW_IMPLICIT);
        }
    } // Fin de la Funcion Jugar

    // Sobreescribirmos el boton de "atras", para que no se reinicie
    @Override
    public void onBackPressed(){

    }
}
