package VTTPday15.inclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InclassApplication {

	public static void main(String[] args) {
		SpringApplication.run(InclassApplication.class, args);
	}

}
//on ubuntu (redis-cli)
	//redis-cli
//select database
	//select 0
//navigate database
	//set name fred
	//get name
	//set {key} {boolean}
	//get {key}
	//keys *
	//keys *m*
	//del {key}
	//incr {int}
	//decr {int}
	//exists {key}

//On fresh mvn project
	//1. update pom.xml with redis dependency aand java version
	//2. update application properties with spring.data.redis
	//3. use AppConfig.java template
	//4. use redis cloud for deployment

//1.
//to link redis to mvn project(java base language)
	//google maven repository -> search for jedis -> go in and enter more update to date version without beta/alpha
	//copy mavn dependency from mvn repo and paste in pom.xml file dependency section

//2.	
//in application properties
	// spring.data.redis.host=localhost
	// spring.data.redis.port=6379
	// spring.data.redis.database=0
	// spring.data.redis.username=
	// spring.data.redis.password=
//3.
//copy over AppConfig .java file
	//centralized place to setup and configure redis connection details, and environment settings for a Spring application.


//4. 
//using redis cloud for deployment
	//connect
	//copy from '@redis' to '.com' is our redis host
	//username:password@host:port
	//default:password@redis-18836.c246.us-east-1-4.ec2.redns.redis-cloud.com:18836
	//use railway 'new' environment variable to set the the host/port/database/username/password etc to match above.


