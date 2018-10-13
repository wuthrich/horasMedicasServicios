package horas.pojo;

public class Hora {

    public Boolean ofrecida;
    public Boolean tomada;
    public Integer dia;
    public String hora;//ej 9:20
    public Integer linea;
    public Persona persona;
    
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
