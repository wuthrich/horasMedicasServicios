package horas;

import com.google.api.core.ApiFuture;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import horas.pojo.Calendariosemanal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

public class ConexionFire {

	public static final ConexionFire con= new ConexionFire();

	private Firestore db;
	private String projectId = "firestore-216521";
	
	
	private ConexionFire() {
		
		
		FirestoreOptions firestoreOptions =
	            FirestoreOptions.getDefaultInstance().toBuilder()
	                .setProjectId(projectId)
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
	    
		if(null==id) {
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
	  
	  
	  
	  public String calendarioUpset(Calendariosemanal calendario) throws Exception {		  
		 	  
		  
		  ApiFuture<WriteResult> future = db.collection("horarioSemanal").document(calendario.getIDfirebase()).set(calendario);
		  System.out.println("upset de Calendariosemanal id: "+calendario.getIDfirebase()+" hora:"+future.get().getUpdateTime());
		  return future.get().getUpdateTime().toString();
		  // return "";
	  }
	  
	  public String personaUpset(JsonObject persona) throws Exception {
		  
		   Map<String, Object> docData = new HashMap<>();
		   
		   persona.keySet().forEach(key->{
			   //System.out.println("key: "+key+" value: "+persona.get(key).toString());
			   if(!"traedatos".equalsIgnoreCase(key)) {//flag para manejo de datos
				   docData.put(key, persona.get(key).toString());
			   }
			   
		   });		  
		  
		  ApiFuture<WriteResult> future = db.collection("personas").document(persona.getString("id")).set(docData);
		  System.out.println("upset de persona id: "+persona.getString("id")+" hora:"+future.get().getUpdateTime());
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
		      json.append("\"id\":\""+document.getId()+"\", ");
		      json.append("\"nombre\":\""+document.getString("nombre")+"\", ");
		      json.append("\"mail\":\""+document.getString("mail")+"\"");
		      json.append("}");
		    }
		    
		    json.append("]");
		    // [END fs_get_all]
		    
		    return json.toString();
		}
	
	

}
