package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.Toolbar.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import android.database.sqlite.*;
import android.database.*;
import android.widget.AdapterView.*;
import java.util.zip.*;
import org.apache.http.client.utils.*;

public class MainActivity extends Activity 
{
	private ArrayList<Poliza> polizas;
	private ArrayList<Dueño> dueños;
	private ListView ls;
	private BaseDatos bd;
	private Poliza pol_selec;
	private Dueño due_selec;

	
	private EditText id,nombre,domicilio,telefono,marca,modelo,año,fechainicio,precio,tipo;
	private Spinner dueño;
	
	private boolean poliza=true,actualizar=false;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
		System.out.println("-----------------------------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		bd=new BaseDatos(this);
		polizas=new ArrayList<Poliza>();
		dueños=new ArrayList<Dueño>();

		ls=findViewById(R.id.lista);
		ls.setOnItemClickListener(new AdapterView.OnItemClickListener(){  
				@Override
				public void onItemClick(AdapterView<?>adapter,View v, int position,long id){
					if(poliza)
						pol_selec=polizas.get(position);
					else
						due_selec=dueños.get(position);
					actualizar=true;
					ver_detalles();
				}
			});
		actualizar_lista();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(R.id.agregardueños==item.getItemId()){
			actualizar=false;
			poliza=false;
			ver_detalles();
		}
		else if(R.id.mostrardueños==item.getItemId()){
			poliza=false;
			actualizar=false;
			actualizar_lista();
		}
		else if(R.id.mostrarexpedientes==item.getItemId()){
			poliza=true;
			actualizar=false;
			actualizar_lista();
		}
		else if(R.id.agregarexpediente==item.getItemId()){
			actualizar=false;
			poliza=true;
			ver_detalles();
		}
		return true;
	}

	private void mensaje(String cont){
		Toast.makeText(this,cont,Toast.LENGTH_SHORT).show();
	}

	private void actualizar_lista(){
		ls.setAdapter(null);
		ArrayList<String> arr=new ArrayList<String>();
		polizas.clear();
		dueños.clear();
		try{
			SQLiteDatabase db=bd.getReadableDatabase();
			Cursor c=db.rawQuery(poliza?"select * from poliza":"select * from dueño",null);
			if(!c.moveToFirst()){
				mensaje("No hay registros");
				return;
			}

			do{
				if(poliza){
				
					Poliza r=new Poliza(c.getInt(0),c.getString(1),c.getString(2),c.getInt(3),c.getString(4),c.getFloat(5),c.getString(6),c.getString(7));
					arr.add(r.idcoche+","+r.fecha+","+r.marca+","+r.modelo);
					polizas.add(r);
				}else{
					Dueño r=new Dueño(c.getString(0),c.getString(1),c.getString(2),c.getString(3));
					arr.add(r.nombre+","+r.domicilio+","+r.telefono);
					dueños.add(r);
				}
			}while(c.moveToNext());

			ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);
			ls.setAdapter(adapter);
		}catch(Exception ex){
			mensaje("Error al leer");
			ex.printStackTrace();
		}
	}
	
	private void ver_detalles(){
		AlertDialog.Builder alerta=new AlertDialog.Builder(this);
		if(poliza){
			if(!actualizar)
				pol_selec=new Poliza(bd);
		}else{
			if(!actualizar)
				due_selec=new Dueño(bd);
		}
		alerta.setTitle(poliza? ( actualizar?"Actualizar poliza":"Ingresar poliza"):(actualizar?"Actualizar dueño":"Agregar dueño"));
		View v=poliza? getLayoutInflater().inflate(R.layout.expediente,null):getLayoutInflater().inflate(R.layout.duenos,null);
		if(poliza){
			
			marca=v.findViewById(R.id.marca);
			modelo=v.findViewById(R.id.modelo);
			año=v.findViewById(R.id.año);
			fechainicio=v.findViewById(R.id.fechainicio);
			precio=v.findViewById(R.id.precio);
			dueño=v.findViewById(R.id.dueños);
			tipo=v.findViewById(R.id.tipo);
			
			if(actualizar){
			
				marca.setText(pol_selec.marca);
				modelo.setText(pol_selec.modelo);
				año.setText(pol_selec.año+"");
				fechainicio.setText(pol_selec.fecha);
				precio.setText(pol_selec.precio+"");
				tipo.setText(pol_selec.tipopoliza);
			}
			ArrayList<String> arr=new ArrayList<String>();
			dueños.clear();
			try{
				SQLiteDatabase db=bd.getReadableDatabase();
				Cursor c=db.rawQuery("select * from dueño",null);
				if(!c.moveToFirst()){
					arr.add("No hay registros");		
					return;
				}

				do{
					Dueño r=new Dueño(c.getString(0),c.getString(1),c.getString(2),c.getString(3));
					arr.add(r.nombre+","+r.domicilio+","+r.telefono);
					dueños.add(r);

				}while(c.moveToNext());

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, arr);

				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dueño.setAdapter(adapter);
			}catch(Exception ex){
				mensaje("Error al leer");
				ex.printStackTrace();
			}
			
		}else{
			id=v.findViewById(R.id.iddu);
			nombre=v.findViewById(R.id.nombre);
			domicilio=v.findViewById(R.id.domicilio);
			telefono=v.findViewById(R.id.telefono);
			
			if(actualizar){
				id.setText(due_selec.id);
				nombre.setText(due_selec.nombre);
				domicilio.setText(due_selec.domicilio);
				telefono.setText(due_selec.telefono);
			}
		}
		alerta.setView(v);
		
		alerta.setPositiveButton(actualizar?"Actualizar":"Ingresar",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface d,int i){
					
					if(poliza){
						
						if(marca.getText().toString().equals("")||
						   modelo.getText().toString().equals("")||
						   año.getText().toString().equals("")||
						   precio.getText().toString().equals("")||
						   fechainicio.getText().toString().equals("")||
							tipo.getText().toString().equals("")){
								mensaje("Llena los campos");
								return;
							}
							
			
						pol_selec.marca=marca.getText().toString();
						pol_selec.modelo=modelo.getText().toString();
						pol_selec.año=Integer.parseInt(año.getText().toString());
						pol_selec.precio=Float.parseFloat(precio.getText().toString());
						pol_selec.fecha=fechainicio.getText().toString();
						pol_selec.tipopoliza=tipo.getText().toString();
						pol_selec.iddueno=dueños.get(dueño.getSelectedItemPosition()).id;
						
						if(pol_selec.save(bd,actualizar)){
							mensaje(actualizar?"Actualizado":"Agregado");
						}else{
							mensaje(actualizar?"No se pudo actualizar":"No se pudo agregar");
						}
					}else{
						
						if(id.getText().toString().isEmpty()||nombre.getText().toString().isEmpty()||domicilio.getText().toString().isEmpty()){
							mensaje("Llena todos los campos");
							return;
						}
							
						due_selec.id=id.getText().toString();
						due_selec.nombre=nombre.getText().toString();
						due_selec.domicilio=domicilio.getText().toString();
						due_selec.telefono=telefono.getText().toString();
						if(due_selec.save(bd,actualizar)){
							mensaje(actualizar?"Actualizado":"Agregado");
						}else{
							mensaje(actualizar?"No se pudo actualizar":"No se pudo agregar");
						}
					}		
					d.dismiss();
					actualizar_lista();
				}
			});
			
		if(actualizar){
			alerta.setNeutralButton("Borrar",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface d,int idl){
					
					if(poliza?pol_selec.borrar(bd):due_selec.borrar(bd))
						mensaje("Eliminado");
					else
						mensaje("No se pudo eliminar");

					d.dismiss();
					actualizar_lista();
				}
			});
		}

		alerta.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface d,int id){
					d.cancel();
				}
			});
		alerta.show();
	}
}
