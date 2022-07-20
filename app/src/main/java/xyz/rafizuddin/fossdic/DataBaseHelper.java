package xyz.rafizuddin.fossdic;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String db_name) {
        super(context, db_name, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getserial(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from english where `word` like '" + tableName+"%' limit 20", null);
        //db.close();
        return res;

    }

    public Cursor getbangla(String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from bangla where `serial` like '" + tableName+"' limit 1", null);
      //  db.close();
        return res;

    }

    public Cursor searchEn( String chr) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select serial,word from `english` where `word` like '" + chr + "%'  limit 7", null);
        if(chr.length()>0) {


            int unihex = chr.codePointAt(0);
            if (unihex >= 0x0980 && unihex <= 0x09FF) {
                Cursor resBn = db.rawQuery("select distinct serial,word from `bangla` where `word` like '" + chr + "%'  limit 7", null);
                return resBn;
            } else {
                return res;
            }
        }else{
            return res;
        }


    }
    public int getSerial( String chr) {
        String temp;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select serial,word from `english` where `word` like '" + chr + "'  limit 1", null);
        if(chr.length()>0) {


            int unihex = chr.codePointAt(0);
            if (unihex >= 0x0980 && unihex <= 0x09FF) {
                Cursor resBn = db.rawQuery("select distinct serial,word from `bangla` where `word` like '" + chr + "'  limit 1", null);
                if(resBn!=null && resBn.moveToFirst()) {

                    temp = resBn.getString(0);
                    resBn.close();
                    return Integer.parseInt(temp);
                }else return 0;


            } else {
                if(res!=null && res.moveToFirst()) {

                    temp = res.getString(0);
                    res.close();
                    return Integer.parseInt(temp);
                }else return 0;

            }

        }else{
            return 0;
        }


    }

    public Cursor getExtra( String chr) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from `bn_en` where `serial` in "+chr+" ", null);
        //db.close();
        return res;


    }

    public Cursor getEnWord( String serial) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT word from `english` where `serial` like '"+serial+"' ", null);

        return res;


    }
}
