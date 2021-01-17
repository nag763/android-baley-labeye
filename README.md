![Licence](https://img.shields.io/bower/l/bootstrap)

# TIC - Android :  BALEY Romain & LABEYE Loïc

This project is part of our studies in our third year at ESIGELEC.

The subject is to create an Android app that will display the museums nearby the user and save datas around it while using the Firebase technology.

## Download app 

![qrcode](https://github.com/nag763/android-baley-labeye/blob/master/dlapp.png)

## Demo

![user](https://github.com/nag763/android-baley-labeye/blob/master/user.gif)
![admin](https://github.com/nag763/android-baley-labeye/blob/master/admin.gif)

## TODO List

- [x] Authentication and inscription of the user

- Main menu :

	- [x] Can see his profile
		- [x] Display the user's profile picture
		- [x] Display the user's name
		- [x] Display the user's phone number
		- [x] Display the user's city
		- [x] Display the visited museums

	- [x] Can see the list of museums
		- On click show a new activity that :
			- [x] Display the currrent datetime
			- [x] Display the distance in kms from the user
			- [x] On click show a new activity that show the travel to the user
			- [x] Shows the route path to the user
	- [x] Can see the list of the latest museums he visited
		- [x] Display the name of the museum
		- [x] Display the visit date
	- [x] Can evaluate the visits he already did
	- [x] Can see the statistics around his visits
	- [x] Can quit the application

If the user is authenticated as an admin :

- [x] Can see the integrated museums in a map

- [x] Can see the application usage stats

- [x] Can quit the application

## Known Issues

- [x] If data from fetched field is null, the application crashs
	
	- Solved by removing some useless fields in the museum collection

- [x] If the connection isn't successful, the connection loops

	- Solved on commit [46c050ab650adc5f1c4549458e2d00687d3a9cac](https://github.com/nag763/android-baley-labeye/commit/46c050ab650adc5f1c4549458e2d00687d3a9cac)

- [x] If a visit isn't evaluated, the application crash

	- Fixed on a  old commit

- [ ] If no visit saved, application crash on evaluate visit (EvalAct)

- [ ] Issue with city icon (ProfilFrag)

- [ ] Fix ressources in switch (Main and Admin act)

- [ ] Total of visits doesn't appear (AdminStats)

## TODO

- [x] Fix deprecated *ProgressDialog* in *InscriptionActivity*

- [x] Rename MainActivity2 to something more explicit

- [x] Change App Name and App Logo on phone

- [x] *MyToolbar* displayed at the top
