const admin = require('./node_modules/firebase-admin');
const serviceAccount = require("./key_to_fb.json");
const data = require("./museum-list.json");
const collectionKey = "museums"; 
//name of the collectio
admin.initializeApp(
	{  
		credential: admin.credential.cert(serviceAccount),  
		databaseURL: "https://your-database.firebaseio.com"
	});
const firestore = admin.firestore();
const settings = {timestampsInSnapshots: true};
firestore.settings(settings);
if (data && (typeof data === "object")) 
	{
		Object.keys(data).forEach(docKey => 
			{ firestore.collection(collectionKey).doc(docKey).set(data[docKey]).then((res) => 
				{
					console.log("Document " + docKey + " successfully written!");}).catch((error) => {   console.error("Error writing document: ", error);
					});
			});
	}
