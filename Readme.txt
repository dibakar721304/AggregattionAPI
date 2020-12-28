
Aggregation API

Tech stack/tools:
1)Java 8
2)Springboot 2
3)Apache ActiveMQ 5
4)H2 database
5)Postman
#### API know how

Web Service is deployed on local( embedded in spring boot).

How to build:
1)Checkout or download code from github.
2) Import as a maven project in eclipse.
3) Do a maven build with goal "clean install"

How to run:
1) Right click on project and go to run as-> run configuration.
2) Under maven build,create a run configuration with goal "spring-boot:run"
3) Application will be started
or else, we can directly right click on AggregationApplication.java and run as java application.

Docker backend API set up
1)Install docker desktop APP.
2)Sign in to docker desktop APP.
3)Do a docker pull from https://hub.docker.com/r/xyzassessment/backend-services.
4)Login to power shell and execute below command:
docker run -d --<name> $<name> -p 8080:8080 $<name>
Here name is image id of xyzassessment .
           or
   In desktop APP, go to Images->Local. And then click on run button for xyzassessment. 
   After that go to Container/Apps, click on start button for the selected image
5) Test it from postman( SSL security should be off in postman) http://localhost:8080/shipments?q=109347263,123456891
Response should be :
200 OK
{
"109347263": ["box", "box", "pallet"],
"123456891": ["envelope"]
}


Active MQ:
1) Download and unzip apache active MQ
2) Go to <C:\Projects\>apache-activemq-5.15.14\bin\win64
3) Double click on activemq.bat 
4) Check the logs in cmd screen, it should be started at 8161 port.
5) Start the springboot application
6) Login to http://localhost:8161/admin/connections.jsp with username and password as admin and admin.
7) Click on queue
8) There should be three queues , pricing, shipments and tracking 

To test aggregation API:
1) Install postman, disable SSL security
2) select method type as GET
3) Hit http://localhost:8081/aggregation?pricing=NL&track=109347263&shipments=109347263
Response status should be 200 ok and similar response as below should come
{
    "shipments": {
        "109347263": [
            "box",
            "box",
            "envelope"
        ]
    },
    "pricing": {
        "NL": 41.550926
    },
    "tracking": {
        "109347263": "NEW"
    }
}
4)Check the messages enqueued and dequeued in active MQ console.( it should be same)
5) We can test all three APIs individually too . Below are the sample end points:
http://localhost:8081/pricing?q=NL
http://localhost:8081/track?q=109347263
http://localhost:8081/shipments?q=109347263,123456891

Note: For any queries regarding this API, kindly contact me at divakar721304@gmail.com



