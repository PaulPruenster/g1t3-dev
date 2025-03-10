# g1t3

# Getting started


## Clone this repo

```
git clone link_to_this_repo
cd into_folder
```

## Webserver

```
cd webapp
```

Open the folder in the Intellij IDE or run

```
mvn spring-boot:run
```
The webserver is now running on your localhost and your external IP on port 8080

## Accesspoint (Raspberrypi)

SSH into your rapberrypi.

Clone this repo to your raspberrypi and cd into the folder.

```
cd raspberry/code
```

Change the 'url' tag in the conf.yaml to the IP of the webserver. Save and exit.

Make the configure.sh script executable with

```
chmod +x configure.sh
```
and execute it with

```
./configure.sh
```

## SensorStation (Arduino)

Plug the Arduino into the power outlet.

On the webapp you should now see the Accesspoint (Raspberrypi) in the "Übersicht" tab when logged in as admin.

- Right click the Accesspoint and select 'Open Accesspoint'
- Click 'Connect new Sensorstation' Button
- Acrivate the Search Connections toggle and wait
- Your bluetooth device should appear in the list below, click the green connect button
- The SensorStation (Arduino) should confirm the connection with a jingle
- The toggle should be deactivated automatically

# You are now set!

## License
MIT
