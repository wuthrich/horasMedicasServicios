package horas.pojo;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class Calendariosemanal {
public String  personaId;
public String personaNombre;
public Integer anio;
public Integer mes;
public Integer semana;
public Integer diaDeLaSemanaQueSeHizo;
public List<String> encabezados; //:Array<any>
public String desde;
public String hasta;
public Integer longitudHora;
public Integer numeroCitas;
public List<Hora> horas; //:Array<Hora>
public Boolean grabado;

public String getIDfirebase() {
	
	return this.personaId+"-"+this.anio+"-"+this.semana;
}
public String toJson() {
	JsonObjectBuilder constructor = Json.createObjectBuilder();
    constructor.add("personaId", personaId);
    constructor.add("personaNombre", personaNombre);
    constructor.add("anio", anio);
    constructor.add("mes", mes);
    constructor.add("semana", semana);
    constructor.add("diaDeLaSemanaQueSeHizo", diaDeLaSemanaQueSeHizo);
   //encabezados
    JsonArrayBuilder encabezados = Json.createArrayBuilder(); 
    
    for(Object item: this.encabezados) {
    	encabezados.add(""+item);
    }
    /*
    this.encabezados.forEach(item->{
    	
    	System.out.println(item);
    });
    */
    constructor.add("encabezados", encabezados);
  
    
    constructor.add("desde", desde);
    constructor.add("hasta", hasta);
    constructor.add("longitudHora", longitudHora);
    constructor.add("numeroCitas", numeroCitas);
    //horas
    constructor.add("grabado", grabado);    
    
	return constructor.build().toString();
}

public String getPersonaId() {
	return personaId;
}
public void setPersonaId(String personaId) {
	this.personaId = personaId;
}
public String getPersonaNombre() {
	return personaNombre;
}
public void setPersonaNombre(String personaNombre) {
	this.personaNombre = personaNombre;
}
public Integer getAnio() {
	return anio;
}
public void setAnio(Integer anio) {
	this.anio = anio;
}
public Integer getMes() {
	return mes;
}
public void setMes(Integer mes) {
	this.mes = mes;
}
public Integer getSemana() {
	return semana;
}
public void setSemana(Integer semana) {
	this.semana = semana;
}
public Integer getDiaDeLaSemanaQueSeHizo() {
	return diaDeLaSemanaQueSeHizo;
}
public void setDiaDeLaSemanaQueSeHizo(Integer diaDeLaSemanaQueSeHizo) {
	this.diaDeLaSemanaQueSeHizo = diaDeLaSemanaQueSeHizo;
}
public List<String> getEncabezados() {
	return encabezados;
}
public void setEncabezados(List<String> encabezados) {
	this.encabezados = encabezados;
}
public String getDesde() {
	return desde;
}
public void setDesde(String desde) {
	this.desde = desde;
}
public String getHasta() {
	return hasta;
}
public void setHasta(String hasta) {
	this.hasta = hasta;
}
public Integer getLongitudHora() {
	return longitudHora;
}
public void setLongitudHora(Integer longitudHora) {
	this.longitudHora = longitudHora;
}
public Integer getNumeroCitas() {
	return numeroCitas;
}
public void setNumeroCitas(Integer numeroCitas) {
	this.numeroCitas = numeroCitas;
}
public List<Hora> getHoras() {
	return horas;
}
public void setHoras(List<Hora> horas) {
	this.horas = horas;
}
public Boolean getGrabado() {
	return grabado;
}
public void setGrabado(Boolean grabado) {
	this.grabado = grabado;
}



}
