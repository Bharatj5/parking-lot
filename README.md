# Parking Lot Application

### Getting Started
All Maven plugins and dependencies are available from [Maven Central](https://search.maven.org/). Please have installed

* JDK 11 (tested with OpenJDK)
* Spring boot 2.7+
* Maven 3.8+ 
* Docker 19.03.4


### Build

`mvn clean install`

### Run

#### Spring Boot Style

Typical java jar created

`java -jar target/parking-app-0.0.1-SNAPSHOT.jar`

Should start the application in terminal

#### Docker Style

First, build image (don't miss the dot(.) in the end of below command)

```docker build -t parking-app . ```

Then run the below command (it will not work in detachment mode i.e.-d)
```
docker run -it parking-app:latest
```

### Configure Parking lot
On startup, application ask to configure the Parking Lot. 
Once configure, app should ready to use.
 
### Test the application 
There are two commands configured as `check-in` and `check-out` for vehicle in or out of the parking lot. 

To check-in vehicle use parameter as `check-in -c=CAR_REG_NO` or `check-in -m=CAR_REG_NO` for car and motorcycle respectively. 

To check-out vehicle use parameter as `check-out -c=CAR_REG_NO` or `check-out -m=CAR_REG_NO` for car and motorcycle respectively. 

### Notes
* Parking lot is configured in memory but per structure it is easy to add persistence layer. 
* This app uses most of Spring boot features

Thanks!
