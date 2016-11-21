package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gary.chemmaster.app.CYLChemApplication;

import static sqlite.CYLDBOpenHelper.EDITOR_CHOICE_HISTORY_TABLE;

/**
 * Created by gary on 2016/11/11.
 */
public class CYLDBOpenHelper extends SQLiteOpenHelper {


    private String DBname;
    private String TableName;
    public static final String EDITOR_CHOICE_TABLE ="EDITROCHICE";
    public static final String EDITOR_CHOICE_HISTORY_TABLE ="EDITORCHICE_HISTORY";
    public static final String NAME_REACTION_LIST_TABLE = "NAME_REACTION_LIST";
    public static final String TOTAL_SYNTHESIS_LIST_TABLE = "TOTAL_SYNTHESIS_LIST";

    public CYLDBOpenHelper(Context context, String DBname, String TBName)
    {
        super(context,DBname,null,1);
        this.DBname = DBname;
        this.TableName = TBName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = null;

        if (DBname.equals(CYLChemApplication.SQlite_DataBase_EditorChoice))
        {
            if (TableName.equals(EDITOR_CHOICE_TABLE))
            {
                sql = "CREATE TABLE IF NOT EXISTS "+   EDITOR_CHOICE_TABLE +"("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "editorsChoicePubDate INTEGER," +
                        "journalTitle VARCHAR(30)," +
                        "author VARCHAR(100)," +
                        "title VARCHAR(200)," +
                        "articalAbstract VARCHAR(400)," +
                        "doi VARCHAR(50)," +
                        "picpath VARCHAR(100))";

                Log.d("cyl",EDITOR_CHOICE_TABLE);
            }
        }
        else if (DBname.equals(CYLChemApplication.SQlite_DataBase_EditorChoice_History))
        {
            if (TableName.equals(EDITOR_CHOICE_HISTORY_TABLE))
            {
                sql = "CREATE TABLE IF NOT EXISTS "+   EDITOR_CHOICE_HISTORY_TABLE +"("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "pubDate INTEGER," +
                        "doi VARCHAR(50))";

                Log.d("cyl",EDITOR_CHOICE_HISTORY_TABLE);
            }
        }
        else if (DBname.equals(CYLChemApplication.SQlite_DataBase_COMMON))
        {

                sql = "CREATE TABLE IF NOT EXISTS "+   NAME_REACTION_LIST_TABLE +"("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "desc VARCHAR(500)," +
                        "name VARCHAR(50)," +
                        "urlpath VARCHAR(100)," +
                        "bitmap BLOB)";

                Log.d("cyl",NAME_REACTION_LIST_TABLE);

            db.execSQL(sql);

                sql = "CREATE TABLE IF NOT EXISTS "+   TOTAL_SYNTHESIS_LIST_TABLE +"("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "desc VARCHAR(500)," +
                        "name VARCHAR(50)," +
                        "urlpath VARCHAR(100)," +
                        "year VARCHAR(10)," +
                        "author VARCHAR(50)," +
                        "bitmap BLOB)";

                Log.d("cyl",TOTAL_SYNTHESIS_LIST_TABLE);

            db.execSQL(sql);

        }


        if(sql.length() > 0)
        {
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop if table exists "+NAME_REACTION_LIST_TABLE);
        db.execSQL("drop if table exists "+TOTAL_SYNTHESIS_LIST_TABLE);

    }
}
