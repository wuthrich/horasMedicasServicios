package horas.pojo;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import horas.Util;

public class Hora {
	
	public Hora(JsonObject json) {
		Util.instancia.fillPojo(json, this);
	}

    public Hora() {}

	public Boolean ofrecida;
    public Boolean tomada;
    public Integer dia;
    public String hora;//ej 9:20
    public Integer linea;
    public Persona persona;
       
    public JsonObject toJson() {
    	JsonObjectBuilder constructor = Json.createObjectBuilder();
    	constructor.add("ofrecida", ofrecida);
    	if(null!=tomada)constructor.add("tomada", tomada);
    	constructor.add("dia", dia);
    	constructor.add("hora", hora);
    	constructor.add("linea", linea);
    	return constructor.build();
    }
    
	public Boolean getOfrecida() {
		return ofrecida;
	}
	public void setOfrecida(Boolean ofrecida) {
		this.ofrecida = ofrecida;
	}
	public Boolean getTomada() {
		return tomada;
	}
	public void setTomada(Boolean tomada) {
		this.tomada = tomada;
	}
	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public Integer getLinea() {
		return linea;
	}
	public void setLinea(Integer linea) {
		this.linea = linea;
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	} 
    
    

}
