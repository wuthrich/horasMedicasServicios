package horas.pojo;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import horas.Util;

public class ProblemaHora {

	public ProblemaHora(JsonObject json) {
		Util.instancia.fillPojo(json, this);
	}

    public ProblemaHora() {}
    
	public String razon;
	public Hora hora;
	
    public JsonObject toJson() {
    	JsonObjectBuilder constructor = Json.createObjectBuilder();
    	constructor.add("razon", razon);    	
    	if(null!=hora)constructor.add("hora", hora.toJson());
    	return constructor.build();
    }

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

	public Hora getHora() {
		return hora;
	}

	public void setHora(Hora hora) {
		this.hora = hora;
	}
    
    
	
}
