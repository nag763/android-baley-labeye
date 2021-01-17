#!/bin/bash

curl "https://data.culture.gouv.fr/explore/dataset/liste-et-localisation-des-musees-de-france/download?format=csv&amp;timezone=Europe/Berlin&amp;use_labels_for_header=false" --output museum-list.csv 

python3 format_csv.py

csvjson museum-list.csv > museum-list.json -d ";"

node index.js
