package horas;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.Map;

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
