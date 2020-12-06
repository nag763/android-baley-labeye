# This is a special sub folder

## Only tested in my personnal environnement in dows

Explanation of the files

* `bash update_museum_list.sh` uses the following list of packages to upload the list of museums from opendata to firestore.
	* Inside, `curl` is used to update the `museum_list.csv` file from opendata

* the python package named [csvkit](https://csvkit.readthedocs.io/en/1.0.5/index.html) is used to convert the `museum_list.csv` file to the json format `museum_list.json`

* the `index.js` file uploads the just created `museum_list.csv` file to the firebase instance
