package com.mycompany.myapp;
import android.database.sqlite.*;
import android.database.*;
import android.content.*;
import android.app.*;
import java.io.*;

public class Dueño implements Serializable{
	private BaseDatos ac;
	private SQLiteDatabase db;
	public String id;
	public String nombre,domicilio,telefono;

	public Dueño(String id,String nombre,String domicilio,String telefono){
		this.id=id;
		this.nombre=nombre;
		this.domicilio=domicilio;
		this.telefono=telefono;
	}
	
	public Dueño(BaseDatos ac){
		this.ac=ac;
	}

	public Dueño(BaseDatos ac,String id){
		this.ac=ac;
		refresh(ac,id);
	}
	public void refresh(BaseDatos ac,String id){
		this.id=id;
		try{
			this.db=ac.getReadableDatabase();
			Cursor c= db.rawQuery("select * from dueño where id='"+id+"'",null);
			if(!c.moveToFirst())
				return;
			do{
				this.nombre=c.getString(1);
				this.domicilio=c.getString(2);
				this.telefono=c.getString(3);
			}while(c.moveToNext());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public boolean save(BaseDatos ac,boolean update){
		this.ac=ac;
		this.db=ac.getWritableDatabase();
		try{
			ContentValues v=new ContentValues();
			v.put("id",id);
			v.put("nombre",nombre);
			v.put("domicilio",domicilio);
			v.put("telefono",telefono);
			int r=-1;
			if(update){
				db.execSQL("update dueño set nombre='"+nombre+"',domicilio='"+domicilio+"',telefono='"+telefono+"' where id='"+id+"'");
				r=1;
			}else{
				r=(int)db.insert("dueño",null,v);
			}
			return r>0;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean borrar(BaseDatos ac){
		this.ac=ac;
		this.db=ac.getWritableDatabase();
		try{
			db.execSQL("delete from dueño where id='"+id+"'");
			return true;
		}catch(Exception ex){
			return false;
		}
	}
}
	
