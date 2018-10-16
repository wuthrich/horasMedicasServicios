package horas.pojo;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import horas.Util;

public class Persona {

	public Persona(JsonObject json) {
		Util.instancia.fillPojo(json, this);
	}
	
public Persona() {}

public String id;
public String tipo;    
public String nombre;
public String mail;
public String fono;
public String especialidad;
public String region;
public String comuna;    
public String centro;


public JsonObject toJson() {
	JsonObjectBuilder constructor = Json.createObjectBuilder();
	constructor.add("id", id);	
	constructor.add("tipo", tipo);
	constructor.add("nombre", nombre);
	constructor.add("mail", mail);
	constructor.add("fono", fono);	
	constructor.add("especialidad", especialidad);
	constructor.add("region", region);
	constructor.add("comuna", comuna);
	constructor.add("centro", centro);
	
	return constructor.build();
}

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTipo() {
	return tipo;
}
public void setTipo(String tipo) {
	this.tipo = tipo;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}
public String getFono() {
	return fono;
}
public void setFono(String fono) {
	this.fono = fono;
}
public String getEspecialidad() {
	return especialidad;
}
public void setEspecialidad(String especialidad) {
	this.especialidad = especialidad;
}
public String getRegion() {
	return region;
}
public void setRegion(String region) {
	this.region = region;
}
public String getComuna() {
	return comuna;
}
public void setComuna(String comuna) {
	this.comuna = comuna;
}
public String getCentro() {
	return centro;
}
public void setCentro(String centro) {
	this.centro = centro;
}  



}
