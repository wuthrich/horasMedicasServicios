package horas;

import com.google.api.core.ApiFuture;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.Query;

import horas.pojo.Calendariosemanal;
import horas.pojo.Hora;
import horas.pojo.Persona;
import horas.pojo.ProblemaHora;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ConexionFire {

	public static final ConexionFire con = new ConexionFire();

	private Firestore db;
	private String projectId = "firestore-216521";

	private ConexionFire() {

		FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(projectId)
				.build();
		Firestore db = firestoreOptions.getService();
		// [END fs_initialize_project_id]
		this.db = db;
	}

	/**
	 * Retrieves document in collection as map.
	 *
	 * @return map (string => object)
	 */
	public Map<String, Object> getPersonaAsMap(String id) throws Exception {

		if (null == id) {
			return null;
		}
		// [START fs_get_doc_as_map]
		DocumentReference docRef = db.collection("personas").document(id);
		// asynchronously retrieve the document
		ApiFuture<DocumentSnapshot> future = docRef.get();
		// ...
		// future.get() blocks on response
		DocumentSnapshot document = future.get();
		// [END fs_get_doc_as_map]
		return (document.exists()) ? document.getData() : null;
	}

	public JsonObjectBuilder especialistasConHorariosGet(String region, Integer anio, Integer semana) throws Exception {
		JsonObjectBuilder constructor = Json.createObjectBuilder();

		// Create a reference to the cities collection
		CollectionReference horariosSemanales = db.collection("horarioSemanal");
		// Create a query against the collection.
		Query query = horariosSemanales.whereEqualTo("especialista.region", region).whereEqualTo("anio", anio)
				.whereGreaterThanOrEqualTo("semana", semana).orderBy("semana").orderBy("especialista.id");

		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		Boolean traedatos = querySnapshot.get().getDocuments().size() > 0 ? true : false;
		constructor.add("traedatos", traedatos);

		if (traedatos) {
			Set<String> ids = new HashSet<String>(100);
			JsonArrayBuilder lista = Json.createArrayBuilder();
			Integer i = 0;
			// Solo se agrega una persona a la lista aunque hallan varios calendarios con
			// esa persona
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				Calendariosemanal item = document.toObject(Calendariosemanal.class);
				// System.out.println("item: "+item.toJson().toString());
				if (0 == i) {// Solo para el primero elemento
					i++;
					ids.add(item.getEspecialista().getId());
					lista.add(item.getEspecialista().toJson());
					continue;
				}

				if (!ids.contains(item.getEspecialista().getId())) {
					ids.add(item.getEspecialista().getId());
					lista.add(item.getEspecialista().toJson());
				}

			}

			constructor.add("personas", lista);
		}

		return constructor;
	}

	public JsonObjectBuilder personaHorasTomadas(String id) throws Exception {
		JsonObjectBuilder constructor = Json.createObjectBuilder();

		DocumentReference calendario = db.collection("horarioSemanal").document("145118557-2018-42");
		ApiFuture<DocumentSnapshot> future = calendario.get();
		DocumentSnapshot document = future.get();

		// document.contains(fieldPath)
		/*
		 * // Create a reference to the cities collection CollectionReference
		 * horariosSemanales = db.collection("horarioSemanal"); // Create a query
		 * against the collection. Query query = horariosSemanales.whereEqualTo("horas",
		 * 1);
		 * 
		 * // retrieve query results asynchronously using query.get()
		 * ApiFuture<QuerySnapshot> querySnapshot = query.get();
		 * 
		 * 
		 * Boolean traedatos = querySnapshot.get().getDocuments().size()>0?true:false;
		 * constructor.add("traedatos", traedatos);
		 * 
		 * if(traedatos) { //Set<String> ids = new HashSet<String>(100);
		 * JsonArrayBuilder lista = Json.createArrayBuilder(); Integer i=0; //Solo se
		 * agrega una persona a la lista aunque hallan varios calendarios con esa
		 * persona for (DocumentSnapshot document : querySnapshot.get().getDocuments())
		 * { Calendariosemanal item = document.toObject(Calendariosemanal.class);
		 * lista.add(item.toJson()); }
		 * 
		 * constructor.add("calendarios", lista); }
		 */

		return constructor;
	}

	/*
	 * public Boolean calendariosGetBoolean(String personaId, Integer anio, Integer
	 * semana) throws Exception { // Create a reference to the cities collection
	 * CollectionReference horariosSemanales = db.collection("horarioSemanal"); //
	 * Create a query against the collection. Query query =
	 * horariosSemanales.whereEqualTo("personaId", personaId).whereEqualTo("anio",
	 * anio) .whereGreaterThanOrEqualTo("semana", semana);
	 * 
	 * // retrieve query results asynchronously using query.get()
	 * ApiFuture<QuerySnapshot> querySnapshot = query.get();
	 * 
	 * Boolean traedatos = querySnapshot.get().getDocuments().size()>0?true:false;
	 * 
	 * return traedatos; }
	 */

	public JsonObjectBuilder calendarioActualizaTodasHoras(Calendariosemanal semana) throws Exception {

		JsonObjectBuilder respuesta = Json.createObjectBuilder();
		JsonArrayBuilder problemasConHorasTomadas = Json.createArrayBuilder();    
		  /* 
		 for(Hora item: this.horas) {
		    	horas.add(item.toJson());
		    } 
		 */
		DocumentReference docRef = db.collection("horarioSemanal").document(semana.idFirebase());
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = future.get();
		Calendariosemanal calendario = null;
		if (document.exists()) {
			calendario = document.toObject(Calendariosemanal.class);
		} else {
			throw new Exception("No exite la referencia al calendario id: " + semana.idFirebase());
		}
		
		if(semana.getHoras().size() != calendario.getHoras().size()) { // Se cambio el calculo de hora, son mas o menos horas
			for (Hora horaCalendario : calendario.getHoras()) {
				if(horaCalendario.getTomada()) {
					HoraTomadaException exception = new HoraTomadaException("Ya se tomo una hora");
					ProblemaHora problema = new ProblemaHora();
					problema.setRazon("Ya se tomo una hora");
					problema.setHora(horaCalendario);
					exception.setProblema(problema);
					exception.setCalendarioActualizado(calendario);
					throw exception;
				}
			}
			
			calendario.setHoras(semana.getHoras());
			calendario.setDesde(semana.getDesde());//Se deben setear los parametros de las nuevas horas
			calendario.setHasta(semana.getHasta());
			calendario.setLongitudHora(semana.getLongitudHora());
			calendario.setNumeroCitas(semana.getNumeroCitas());
			docRef.set(calendario);
			
			respuesta.add("ok", true);
			respuesta.add("calendarioActualizado", calendario.toJson());
			respuesta.add("problemasConHorasTomadas", problemasConHorasTomadas.build());
			
			return respuesta;
			
		}
		
		for(int i=0;i<calendario.getHoras().size();i++) {//Recordemos que en este punto size es el mismo
			Hora horaCalendario = calendario.getHoras().get(i);
			Hora horaActualizada = semana.getHoras().get(i);
			
			if( horaCalendario.getDia().equals(horaActualizada.getDia()) && horaCalendario.getLinea().equals(horaActualizada.getLinea())  ) {
				
				if(horaCalendario.getTomada() && !horaActualizada.getOfrecida()) {//Si esta tomada, implica que esta ofrecida
					//No se puede dejar de ofrecer, y se informa de esto
					problemasConHorasTomadas.add(horaCalendario.toJson());
					
				}else {
					horaCalendario.setOfrecida(horaActualizada.getOfrecida());//Se ofrece o deja de ofrecer
				}			
				
			}else {//En el caso que no esten en el mismo orden, espero que improvable
				System.out.println("No estan en el mismo orden las listas de Horas");
				
				for(Hora horaActualizadaBuscada : semana.getHoras()) {
					if( horaCalendario.getDia().equals(horaActualizadaBuscada.getDia()) && horaCalendario.getLinea().equals(horaActualizadaBuscada.getLinea())  ) {
						if(horaCalendario.getTomada() && !horaActualizadaBuscada.getOfrecida()) {//Si esta tomada, implica que esta ofrecida
							//No se puede dejar de ofrecer, y se informa de esto
							problemasConHorasTomadas.add(horaCalendario.toJson());
							
						}else {
							horaCalendario.setOfrecida(horaActualizadaBuscada.getOfrecida());//Se ofrece o deja de ofrecer
						}
						
						break;
					}
				}
				
			}
			
		}
		


		docRef.set(calendario);
		
		respuesta.add("ok", true);
		respuesta.add("calendarioActualizado", calendario.toJson());
		respuesta.add("problemasConHorasTomadas", problemasConHorasTomadas.build());		

		return respuesta;
	}
	
	public Calendariosemanal calendarioActualizaHora(Calendariosemanal semana, Hora hora) throws Exception {

		// ApiFuture<WriteResult> future =
		// db.collection("horarioSemanal").document(calendario.idFirebase()).set(calendario);

		DocumentReference docRef = db.collection("horarioSemanal").document(semana.idFirebase());
		ApiFuture<DocumentSnapshot> future = docRef.get();
		DocumentSnapshot document = future.get();
		Calendariosemanal calendario = null;
		if (document.exists()) {
			calendario = document.toObject(Calendariosemanal.class);
		} else {
			throw new Exception("No exite la referencia al calendario id: " + semana.idFirebase());
		}

		for (Hora horaCalendario : calendario.getHoras()) {
			if (hora.getDia().equals(horaCalendario.getDia()) && hora.getLinea().equals(horaCalendario.getLinea())) {

				if (hora.getTomada()) {// Al tomar la hora
					if (horaCalendario.getTomada()) {
						throw new Exception("Hora ya tomada");
					}

					horaCalendario.setTomada(hora.getTomada());// deveria ser true
					horaCalendario.setPersona(hora.getPersona());
				} else {// Al dejar la hora
					horaCalendario.setTomada(hora.getTomada());// deveria ser false
					horaCalendario.setPersona(null);
				}

				break;
			}
		}

		docRef.set(calendario);

		return calendario;
	}

	public JsonObjectBuilder calendariosGet(String personaId, Integer anio, Integer semana) throws Exception {
		JsonObjectBuilder constructor = Json.createObjectBuilder();

		// Create a reference to the cities collection
		CollectionReference horariosSemanales = db.collection("horarioSemanal");
		// Create a query against the collection.
		Query query = horariosSemanales.whereEqualTo("especialista.id", personaId).whereEqualTo("anio", anio)
				.whereGreaterThanOrEqualTo("semana", semana);

		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();

		Boolean traedatos = querySnapshot.get().getDocuments().size() > 0 ? true : false;
		constructor.add("traedatos", traedatos);

		if (traedatos) {

			JsonArrayBuilder calendarios = Json.createArrayBuilder();

			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				Calendariosemanal calendario = document.toObject(Calendariosemanal.class);
				calendarios.add(calendario.toJson());

				// Cast de persona
				// no convierte la persona al pojo, asi que hay que hacerlo manualmente
				/*
				 * int i = 0; for (Hora hora : calendario.getHoras()) { if (hora.getTomada()) {
				 * ArrayList listahoras = (ArrayList) document.get("horas"); HashMap
				 * horaConPersona = (HashMap) listahoras.get(i); HashMap persona = (HashMap)
				 * horaConPersona.get("persona"); hora.getPersona().fromHashMap(persona);
				 * System.out.println("hora con persona: "+hora.toJson().toString());
				 * System.out.println("persona: "+hora.getPersona().toJson().toString()); } i++;
				 * }
				 */
				// End Cast de persona
			}

			if (semana >= 52) {// Si es la ultima (o penultima) semana del año se buscan calendarios en el
								// proximo año
				query = horariosSemanales.whereEqualTo("especialista.id", personaId).whereEqualTo("anio", anio + 1)
						.whereGreaterThanOrEqualTo("semana", 1);

				// retrieve query results asynchronously using query.get()
				querySnapshot = query.get();

				for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
					Calendariosemanal calendario = document.toObject(Calendariosemanal.class);
					calendarios.add(calendario.toJson());
				}
			}

			constructor.add("calendarios", calendarios);
		}

		return constructor;
	}

	public String calendarioUpset(Calendariosemanal calendario) throws Exception {

		ApiFuture<WriteResult> future = db.collection("horarioSemanal").document(calendario.idFirebase())
				.set(calendario);
		System.out.println(
				"upset de Calendariosemanal id: " + calendario.idFirebase() + " hora:" + future.get().getUpdateTime());
		return future.get().getUpdateTime().toString();
		// return "";
	}

	public String personaUpset(JsonObject persona) throws Exception {

		Map<String, Object> docData = new HashMap<>();

		persona.keySet().forEach(key -> {
			// System.out.println("key: "+key+" value: "+persona.get(key).toString());
			if (!"traedatos".equalsIgnoreCase(key)) {// flag para manejo de datos
				docData.put(key, persona.getString(key));
			}

		});

		ApiFuture<WriteResult> future = db.collection("personas").document(persona.getString("id")).set(docData);
		System.out.println("upset de persona id: " + persona.getString("id") + " hora:" + future.get().getUpdateTime());
		return future.get().getUpdateTime().toString();
	}

	/**
	 * Add a document to a collection using a map.
	 *
	 * @return document data
	 */
	Map<String, Object> addSimpleDocumentAsMap() throws Exception {
		// [START fs_add_doc_as_map]
		// Create a Map to store the data we want to set
		Map<String, Object> docData = new HashMap<>();
		docData.put("name", "Los Angeles");
		docData.put("state", "CA");
		docData.put("country", "USA");
		docData.put("regions", Arrays.asList("west_coast", "socal"));
		// Add a new document (asynchronously) in collection "cities" with id "LA"
		ApiFuture<WriteResult> future = db.collection("personas").document("LA").set(docData);
		// ...
		// future.get() blocks on response
		System.out.println("Update time : " + future.get().getUpdateTime());
		// [END fs_add_doc_as_map]

		return docData;
	}

	public String retrieveAllDocuments() throws Exception {
		// [START fs_get_all]
		// asynchronously retrieve all users
		ApiFuture<QuerySnapshot> query = db.collection("personas").get();
		// ...
		// query.get() blocks on response
		QuerySnapshot querySnapshot = query.get();
		List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
		StringBuilder json = new StringBuilder();
		json.append("[");
		for (QueryDocumentSnapshot document : documents) {
//		      System.out.println("id: " + document.getId());
//		      System.out.println("nombre: " + document.getString("nombre"));		     
//		      System.out.println("mail: " + document.getString("mail"));

			json.append("{");
			json.append("\"id\":\"" + document.getId() + "\", ");
			json.append("\"nombre\":\"" + document.getString("nombre") + "\", ");
			json.append("\"mail\":\"" + document.getString("mail") + "\"");
			json.append("}");
		}

		json.append("]");
		// [END fs_get_all]

		return json.toString();
	}

}
