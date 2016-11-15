package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gary.chemmaster.app.CYLChemApplication;

/**
 * Created by gary on 2016/11/11.
 */
public class CYLDBOpenHelper extends SQLiteOpenHelper {


    private String DBname;
    public static final String EDITOR_CHOICE_TABLE ="EDITROCHICE";
    public static final String EDITOR_CHOICE_HISTORY_TABLE ="EDITORCHICE_HISTORY";

    public CYLDBOpenHelper(Context context, String DBname)
    {
        super(context,DBname,null,1);
        this.DBname = DBname;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = null;

        if (DBname.equals(CYLChemApplication.SQlite_DataBase_EditorChoice))
        {
            sql = "CREATE TABLE "+   EDITOR_CHOICE_TABLE +"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "editorsChoicePubDate INTEGER," +
                    "journalTitle VARCHAR(30)," +
                    "author VARCHAR(100)," +
                    "title VARCHAR(200)," +
                    "articalAbstract VARCHAR(400)," +
                    "doi VARCHAR(50)," +
                    "picpath VARCHAR(100))";
        }
        else if (DBname.equals(CYLChemApplication.SQlite_DataBase_EditorChoice_History))
        {
            sql = "CREATE TABLE "+   EDITOR_CHOICE_HISTORY_TABLE +"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "pubDate INTEGER," +
                    "doi VARCHAR(50))";
        }


        if(sql.length() > 0)
        {
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
