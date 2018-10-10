package com.mycompany.myapp;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;
import java.sql.*;

public class BaseDatos extends SQLiteOpenHelper
{
	public BaseDatos(Context c){
		super(c,"segurocoche",null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase p1)
	{
		String c1="create table dueño(id varchar(200) primary key not null,nombre varchar(500),domicilio varchar(500),telefono varchar(30))";
		p1.execSQL(c1);
		c1="create table poliza(idcoche integer primary key autoincrement,modelo varchar(60),marca varchar(200),año integer,fechainicio date,precio float,tipopoliza varchar(200),iddueno varchar(20),foreign key(iddueno) references dueño(id))";
		p1.execSQL(c1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase p1, int p2, int p3)
	{
		// TODO: Implement this method
	}

}

