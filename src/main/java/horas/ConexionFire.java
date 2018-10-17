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
import horas.pojo.Persona;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
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
	
	public JsonObjectBuilder especialistasConHorariosGet(Integer region, Integer anio, Integer semana) throws Exception {		
		JsonObjectBuilder constructor = Json.createObjectBuilder();

		// Create a reference to the cities collection
		CollectionReference horariosSemanales = db.collection("personas");
		// Create a query against the collection.
		Query query = horariosSemanales.whereEqualTo("region", region);

		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		
		Boolean traedatos = querySnapshot.get().getDocuments().size()>0?true:false;
		constructor.add("traedatos", traedatos);
		
		if(traedatos) {
			
			JsonArrayBuilder lista = Json.createArrayBuilder();
			
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				Persona item = document.toObject(Persona.class);
				Boolean tieneCalendariosActivos = this.calendariosGetBoolean(item.getId(), anio, semana);
				if(tieneCalendariosActivos) {lista.add(item.toJson());}
				
			}

			constructor.add("personas", lista);
		}
		
		return constructor;
	}
	
	public Boolean calendariosGetBoolean(String personaId, Integer anio, Integer semana) throws Exception {
		// Create a reference to the cities collection
		CollectionReference horariosSemanales = db.collection("horarioSemanal");
		// Create a query against the collection.
		Query query = horariosSemanales.whereEqualTo("personaId", personaId).whereEqualTo("anio", anio)
				.whereGreaterThanOrEqualTo("semana", semana);

		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		
		Boolean traedatos = querySnapshot.get().getDocuments().size()>0?true:false;	
		
		return traedatos;
	}

	public JsonObjectBuilder calendariosGet(String personaId, Integer anio, Integer semana) throws Exception {		
		JsonObjectBuilder constructor = Json.createObjectBuilder();

		// Create a reference to the cities collection
		CollectionReference horariosSemanales = db.collection("horarioSemanal");
		// Create a query against the collection.
		Query query = horariosSemanales.whereEqualTo("personaId", personaId).whereEqualTo("anio", anio)
				.whereGreaterThanOrEqualTo("semana", semana);

		// retrieve query results asynchronously using query.get()
		ApiFuture<QuerySnapshot> querySnapshot = query.get();
		
		Boolean traedatos = querySnapshot.get().getDocuments().size()>0?true:false;
		constructor.add("traedatos", traedatos);
		
		if(traedatos) {
			
			JsonArrayBuilder calendarios = Json.createArrayBuilder();
			
			for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
				Calendariosemanal calendario = document.toObject(Calendariosemanal.class);				
				calendarios.add(calendario.toJson());
			}

			if (semana >= 52) {//Si es la ultima (o penultima) semana del año se buscan calendarios en el proximo año
				query = horariosSemanales.whereEqualTo("personaId", personaId).whereEqualTo("anio", anio+1)
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
		System.out.println("upset de Calendariosemanal id: " + calendario.idFirebase() + " hora:"
				+ future.get().getUpdateTime());
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
