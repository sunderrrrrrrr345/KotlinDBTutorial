package pyarishayyari.sunda.com.kotlindbtutorial.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import pyarishayyari.sunda.com.kotlindbtutorial.model.ContactData

class DatabaseHandler:SQLiteOpenHelper {

    companion object {
        val Tag = "DatabaseHandler"
        val DBName = "ContactDB"
        val DBVersion = 1

        val tableName = "phoneTable"
        val ConID = "id"
        val FirstName = "fname"
        val LastName = "lname"
        val Number = "number"
        val Email = "email"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        @Suppress("UNREACHABLE_CODE")
        var sql1: String="CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" + ConID + " INTEGER PRIMARY KEY," +
                FirstName + " TEXT, " + LastName + " TEXT, " + Email +
                " TEXT," + Number + " TEXT );"
        @Suppress("UNREACHABLE_CODE")
        db!!.execSQL(sql1);

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        @Suppress("UNREACHABLE_CODE")
        db!!.execSQL("Drop table IF EXISTS $tableName")
        @Suppress("UNREACHABLE_CODE")
        onCreate(db)
    }

    var context:Context?=null;
    var sqlObj: SQLiteDatabase
    constructor(context: Context): super(context, DBName, null, DBVersion) {
        this.context=context;
        sqlObj=this.writableDatabase;
    }

    fun AddContact(values: ContentValues):String{
        var Msg:String="error";
        var ID=sqlObj!!.insert(tableName,"",values);
        if(ID>0){
            Msg="Ok";
        }
        return Msg;
    }
    fun FetchContacts(keyword: String): ArrayList<ContactData> {

        var arraylist = ArrayList<ContactData>()

        val sqb = SQLiteQueryBuilder()
        sqb.tables = tableName
        val cols = arrayOf("id", "fname", "lname", "email", "number")
        val rowSelArg = arrayOf(keyword)

        val cur = sqb.query(sqlObj, cols, "fname like ?", rowSelArg, null, null, "fname")

        if (cur.moveToFirst()) {

            do {
                val id = cur.getInt(cur.getColumnIndex("id"))
                val fname = cur.getString(cur.getColumnIndex("fname"))
                val lname = cur.getString(cur.getColumnIndex("lname"))
                val email = cur.getString(cur.getColumnIndex("email"))
                val number = cur.getString(cur.getColumnIndex("number"))

                arraylist.add(ContactData(id, fname, lname, email, number))

            } while (cur.moveToNext())
        }

        var count: Int = arraylist.size

        return arraylist
    }

    fun UpdateContact(values: ContentValues, id: Int): String {

        var selectionArs = arrayOf(id.toString())

        val i = sqlObj!!.update(tableName, values, "id=?", selectionArs)
        if (i > 0) {
            return "ok";
        } else {

            return "error";
        }
    }

    fun RemoveContact(id: Int): String {

        var selectionArs = arrayOf(id.toString())

        val i = sqlObj!!.delete(tableName, "id=?", selectionArs)
        if (i > 0) {
            return "ok";
        } else {

            return "error";
        }
    }
}