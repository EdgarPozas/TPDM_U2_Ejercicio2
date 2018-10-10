package com.mycompany.myapp;
import android.database.sqlite.*;
import android.database.*;
import android.app.*;
import android.content.*;
import java.io.*;
import java.sql.*;
import android.icu.text.*;

public class Poliza implements Serializable{
	private BaseDatos ac;
	private SQLiteDatabase db;
	public int idcoche;
	public String modelo,marca,tipopoliza,iddueno;
	public String fecha;
	public int año;
	public float precio;
	
	public Poliza(int idcoche,String modelo,String marca,int año,String fecha,float precio,String tipopoliza,String iddueno){
		this.idcoche=idcoche;
		this.modelo=modelo;
		this.marca=marca;
		this.tipopoliza=tipopoliza;
		this.iddueno=iddueno;
		this.fecha=fecha;
		this.año=año;
		this.precio=precio;
	}

	public Poliza(BaseDatos ac){
		this.ac=ac;
	}

	public Poliza(BaseDatos ac,int id){
		this.ac=ac;
		refresh(ac,id);
	}
	public void refresh(BaseDatos ac,int id){
		this.ac=ac;
		this.idcoche=id;
		try{
			this.db=ac.getReadableDatabase();
			Cursor c= db.rawQuery("select * from poliza where idcoche="+idcoche,null);
			if(!c.moveToFirst())
				return;
			do{
				
				this.modelo=c.getString(1);
				this.marca=c.getString(2);
				this.año=c.getInt(3);
				this.fecha=c.getString(4);
				this.precio=c.getFloat(5);
				this.tipopoliza=c.getString(6);
				this.iddueno=c.getString(7);
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
			v.put("modelo",modelo);
			v.put("marca",marca);
			v.put("año",año); 
			v.put("fechainicio",fecha);
			v.put("precio",precio);
			v.put("tipopoliza",tipopoliza);
			v.put("iddueno",iddueno);
			int r=-1;
			if(update){
				db.execSQL("update poliza set modelo='"+modelo+"',marca='"+marca+"',año="+año+",fechainicio='"+fecha+"',precio="+precio+",tipopoliza='"+tipopoliza+"',iddueno='"+iddueno+"' where idcoche="+idcoche);
				r=1;
			}else{
				r=(int)db.insert("poliza","idcoche",v);
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
			db.execSQL("delete from poliza where idcoche="+idcoche);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
}
