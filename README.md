![Licence](https://img.shields.io/bower/l/bootstrap)

# TIC - Android :  BALEY Romain & LABEYE Loïc

This project is part of our studies in our third year at ESIGELEC.

The subject is to create an Android app that will display the museums nearby the user and save datas around it while using the Firebase technology.

## TODO List

- [x] Authentication and inscription of the user

- Main menu :

	- [x] Can see his profile
		- [x] Display the user's profile picture
		- [x] Display the user's name
		- [x] Display the user's phone number
		- [x] Display the user's city
		- [ ] Display the visited museums

	- [x] Can see the list of museums
		- On click show a new activity that :
			- [x] Display the currrent datetime
			- [x] Display the distance in kms from the user
			- [x] On click show a new activity that show the travel to the user
			- [x] Shows the route path to the user
	- [ ] Can see the list of the latest museums he visited
		- [ ] Display the name of the museum
		- [ ] Display the visit date
	- [ ] Can evaluate the visits he already did
	- [ ] Can see the statistics around his visits
	- [x] Can quit the application

If the user is authenticated as an admin :

- [ ] Can see the integrated museums in a map

- [ ] Can see the application usage stats

- [ ] Can quit the application

## Known Issues

- [ ] If data from fetched field is null, the application crashs
