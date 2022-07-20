package xyz.rafizuddin.fossdic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.database.sqlite.*;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class  MainActivity extends AppCompatActivity {
    String DB_NAME = "db.sqlite";
    Button ttsB,searchBtn;
    DataBaseHelper myDBHelper;
    ListView listV;
    TextToSpeech tts;

    public static class Globals {
     static String[] serial=new String[7];
     static   int is_night=0;
    }
public void onInit(int init){

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts=new TextToSpeech(this,this::onInit);
       ttsB=findViewById(R.id.ttsbtn);
        searchBtn=findViewById(R.id.srchbtn);
        AutoCompleteTextView actv=findViewById(R.id.searchIn);
        myDBHelper = new DataBaseHelper(this, DB_NAME);
        ttsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=actv.getText().toString();

                tts.speak(text,TextToSpeech.QUEUE_ADD,null,null);


            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=actv.getText().toString();
                    int ser=myDBHelper.getSerial(text);


                print(ser+"");
            }
        });

        actv.setThreshold(1);

        listV=findViewById(R.id.listV);




           
        // Copy database
        AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(this, DB_NAME);
        assetDatabaseOpenHelper.saveDatabase();



actv.setOnKeyListener(new View.OnKeyListener() {
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if ((i==KeyEvent.KEYCODE_ENTER)){

                String text=actv.getText().toString();
            int ser=myDBHelper.getSerial(text);


            print(ser+"");
            actv.clearFocus();

        return true;
        }

        return false;
    }
});



               actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String text=actv.getText().toString();
                int ser=myDBHelper.getSerial(text);


                print(ser+"");

            }
        });

           actv.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               }

               @Override
               public void onTextChanged(CharSequence charSequence, int i0, int i1, int i2) {

/*
if(charSequence.length()!=0&&charSequence.charAt(charSequence.length()-1)=='\n'){
    Toast.makeText(MainActivity.this, "en", Toast.LENGTH_SHORT).show();

    int ser=myDBHelper.getSerial(charSequence.toString());


    print(ser+"");
}

 */
                   if (charSequence.toString() !="") {
                       Cursor cr = myDBHelper.searchEn(charSequence.toString());

                       String[] mydata;
                       if (cr.moveToFirst() && cr.getCount() >= 1) {
                           cr.moveToFirst();

                           mydata = new String[cr.getCount()];
                           int i = 0;
                           do {
                               mydata[i] = cr.getString(1);
                               Globals.serial[i] = cr.getString(0);
                               i++;
                           } while (cr.moveToNext());
                   cr.close();

                           ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                   android.R.layout.simple_dropdown_item_1line, mydata);

                           actv.setAdapter(adapter);


                       }
                   }


               }

               @Override
               public void afterTextChanged(Editable editable) {

               }
           });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.about:
                //Toast.makeText(this, "Rafiz", Toast.LENGTH_SHORT).show(); return true;
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Version: " +BuildConfig.VERSION_NAME+
                    "\nThis free and open source application helps translating words between Bengali and English languages. It also shows following extra things for many words: " +
                    "Antonyms, " +
                    "Synonyms, " +
                    "Parts of speech, " +
                    "Definitions, " +
                    "Examples, " +
                    "Phonetic of Bengali words. \n" +
                    ":About Developer:\n" +
                    "Md. Rafiz Uddin\n" +
                    "Dept. of Computer Science and Engineering\n" +
                    "Batch Summer-30th,\n" +
                    "Varendra University,\n" +
                    "Rajshahi, Bangladesh\n").setTitle("স্বচ্ছ অভিধান").setCancelable(true);
            builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogI, int i) {
               dialogI.cancel();
                }
            });
            builder.setPositiveButton("Visit Github!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rafiz001/swoccho-dic"));
                            startActivity(intent);
                        }
                    });
                builder.setNeutralButton("Email to Dev!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:rafiz001@protonmail.ch?Subject=About%20Swoccho%20Dictionary"));
                        startActivity(intent);
                    }
                });
                    AlertDialog alert = builder.create();
                    alert.show();

        }
        return(super.onOptionsItemSelected(item));
    }

    public void print(String serial){
        if(Integer.parseInt(serial)==0){
            Toast.makeText(MainActivity.this, "Sorry this word not found!", Toast.LENGTH_SHORT).show();
        }else {
            Cursor resultSet2 = myDBHelper.getbangla(serial);
            resultSet2.moveToFirst();

            String[] titleText = {"English", "Bangla", "Extra", "Phonetic", "Antonyms", "Definitions", "Examples"};
            String[] result = new String[7];
            for (int ti = 0; ti <= 6; ti++) {
                switch (ti) {
                    case 0: {
                        Cursor cr = myDBHelper.getEnWord(resultSet2.getString(ti));
                        cr.moveToFirst();
                        result[ti] = cr.getString(0);
                        cr.close();
                    }
                    break;
                    case 1:
                    case 3: {
                        result[ti] = resultSet2.getString(ti) + " ";
                    }
                    break;
                    case 6: {
                        result[ti] = resultSet2.getString(ti) + ".";
                    }
                    break;
                    case 2:
                    case 4:
                    case 5: {
                        if (resultSet2.getString(ti) != null) {
                            try {
                                JSONArray jsary = new JSONArray(resultSet2.getString(ti));

                                result[ti] = jsary.getString(0);

                                String temp = "";
                                for (int o = 0; o < jsary.length(); o++) {
                                    if (ti != 2) {
                                        temp += "<li>" + jsary.getString(o) + "</li>";
                                    } else {
                                        JSONArray jsary2 = new JSONArray(jsary.getString(o));
                                        temp += "<li>";
                                        String[] extra = {"", "ARTICLE", "MORE", "ADVERB", "ADJECTIVE", "NOUN", "PREFIX", "PREPOSITION", "VERB", "ABBREVIATION", "CONJUNCTION", "PRONOUN", "PHRASE", "INTERJECTION", "", "AUXILIARY VERB", "SUFFIX", "PARTICLE"};
                                        String temp2 = "(";
                                        for (int u = 0; u < jsary2.length(); u++) {
                                            // temp +=""+ jsary2.getString(u)+" - ";
                                            if (u != 0 && u != (jsary2.length() - 1)) {

                                                temp2 += jsary2.getString(u) + ",";
                                            }
                                            if (u == (jsary2.length() - 1))
                                                temp2 += jsary2.getString(u);
                                            if (u == 0) {
                                                temp += "<u><b>" + extra[Integer.parseInt(jsary2.getString(0))] + "</b></u>: <br>";
                                            }
                                        }
                                        temp2 += ")";


                                        Cursor cr = myDBHelper.getExtra(temp2);

                                        String[] temp3 = new String[cr.getCount()]; //bn
                                        String[] temp4 = new String[cr.getCount()]; //en
                                        int i3 = 0;
                                        if (cr.moveToFirst() && cr.getCount() >= 1) {
                                            cr.moveToFirst();


                                            do {
                                                temp3[i3] = cr.getString(1);
                                                temp4[i3] = cr.getString(2);
                                                i3++;
                                            } while (cr.moveToNext());
                                            cr.close();

                                        }

                                        for (int i4 = 0; i4 < i3; i4++) {

                                            String temp5 = "";
                                            JSONArray jsary3 = new JSONArray(temp4[i4]);
                                            for (int p = 0; p < jsary3.length(); p++) {
                                                if (p != jsary3.length() - 1) {
                                                    temp5 += jsary3.getString(p) + ", ";
                                                } else {
                                                    temp5 += jsary3.getString(p);
                                                }
                                            }
                                            temp += (i4 + 1) + ": " + temp3[i4] + " - " + temp5 + "<br><br>";

                                        }

                                        temp += "</li>";
                                    }
                                    temp += "\n ";


                                }
                                result[ti] = temp;


                            } catch (final JSONException e) {
                                Log.e("rafiz", "Problem fetching json." + e);
                            }
                        }

                    }
                    break;
                }

            }

            //ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.list,R.id.subTitle,result);
            customAAdapter aAdapter = new customAAdapter(MainActivity.this, titleText, result);
            listV.setAdapter(aAdapter);
            resultSet2.close();

        }
    }

    }









