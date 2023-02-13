# GateKeeper

A project to learn Spring Boot by creating a simple gatekeeper app that logs a user's visit into a community.

## Description
This is a simplified Gatekeeper application that is used to automate the task of logging a user's visit into a gated community.
Conceptually, a user of this application can use it to manage community(Community Manager), join a community(Resident), 
and visit other communities as a Visitor.

## Built With
* [Spring Framework 6](https://spring.io/projects/spring-framework)
* [Spring Boot 3](https://spring.io/projects/spring-boot)

## Tools
* [Hibernate](https://hibernate.org/orm/documentation/6.1/)
* [Project Lombok](https://projectlombok.org/)
* [IntelliJ Idea](https://www.jetbrains.com/idea/)

## Requirements
For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3](https://maven.apache.org)
- [MySQL](https://www.mysql.com/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine.
One way is to execute the `main` method in the `com.GateKeeper.gateKeeper.GateKeeperApplication` class from your IDE.

## API Endpoints

### Home Page
#### Request
```
GET /
```
```
curl --location --request GET 'localhost:8080/'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 12
 
Body:
Hello World!
```

### Login Request
#### Request
```
POST /login
```
```
curl --location --request POST 'localhost:8080/login' \
--header 'Cookie: JSESSIONID=BAA34AD5C8C3E43B3C93FC58C189B28B' \
--form 'username="shaileshdagar"' \
--form 'password="password"'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 12
 
Body:
Hello World!
```

### Register New User
#### Request
```
POST /register
```
```
curl --location 'localhost:8080/register' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: JSESSIONID=1726324AF545AA27278243ABBC460461' \
--data-urlencode 'username=shaileshdagar' \
--data-urlencode 'password=password' \
--data-urlencode 'email=shaileshdagar@gmail.com' \
--data-urlencode 'role=USER'
```
#### Response
```
HTTP/1.1 201 Created
Status: 201 Created
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 12
 
Body:
User Created
```

### Get User Details
#### Request
```
GET /user/{username}
```
```
curl --location 'localhost:8080/user/shaileshdagar' \
--header 'Cookie: JSESSIONID=3D45D44BE16CECA4586D41D2E133CD32' \
--data ''
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: application/json
 
Body:
{
    "username": "shaileshdagar",
    "email": "shaileshdagar@gmail.com",
    "password": "$2a$10$.j7alwouRP2JXzTMwN4caOm8OzDMm35FqJdT4kQUUnl8ECFr7o6I6",
    "role": "USER"
}
```

### Update User Address
#### Request
```
PATCH /user/address
```
```
curl --location --request PATCH 'localhost:8080/user/address' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: JSESSIONID=3D45D44BE16CECA4586D41D2E133CD32' \
--data-urlencode 'areaCode=92101' \
--data-urlencode 'city=San Deigo' \
--data-urlencode 'state=California' \
--data-urlencode 'country=U.S.A.'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: application/json
 
Body:
{
    "areaCode": "92101",
    "city": "San Deigo",
    "state": "California",
    "country": "U.S.A."
}
```

### Find Community
#### Request
```
GET /community/find
```
```
curl --location --request GET 'localhost:8080/community/find' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=3D45D44BE16CECA4586D41D2E133CD32' \
--data '{
    "communityName": "Yamuna EnClave",
    "communityAddress":{
        "areaCode" : "132103"
    }
}'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: application/json
 
Body:
{
    "communityName": "Yamuna Enclave",
    "communityAddress": {
        "areaCode": "132103",
        "city": "Panipat",
        "state": "Haryana",
        "country": "India"
    },
    "communityUsers": [
        "elonmusk",
        "shaileshdagar",
        "geohot"
    ]
}
```

### Delete User
#### Request
```
DELETE /user/delete
```
```
curl --location --request DELETE 'localhost:8080/user/delete' \
--header 'Cookie: JSESSIONID=DDAB10A7DF71CDA431213E3810146509'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 12
 
Body:
User Deleted
```

### Join a Community
#### Request
```
PATCH /community/join/flat/A-129
```
```
curl --location --request PATCH 'localhost:8080/community/join/flat/A-129' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=87ED7A0AADECB656A9BF927BDAC17919' \
--data '{
    "communityName": "Yamuna Enclave",
    "communityAddress":{
        "areaCode" : "132103"
    }
}'
```
#### Response
```
HTTP/1.1 200 OK
Status: 200 OK
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 43
 
Body:
Your Request has been successfully recorded
```

### Register New Community
#### Request
```
POST /community/new
```
```
curl --location 'localhost:8080/community/new' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=988C358227372EDEC81DA89808D2C831' \
--data '{
    "communityName": "Ganga Enclave",
    "communityAddress":{
        "areaCode" : "132103",
        "city" : "Panipat",
        "state" : "Haryana",
        "country" : "India"
    },
    "communityUsers": [
        "rupanshuchoudhary"
    ]
}'
```
#### Response
```
HTTP/1.1 201 Created
Status: 201 Created
Connection: keep-alive
Content-Type: application/json
 
Body:
{
    "communityName": "Ganga Enclave",
    "communityAddress": {
        "areaCode": "132103",
        "city": "Panipat",
        "state": "Haryana",
        "country": "India"
    },
    "communityUsers": [
        "rupanshuchoudhary",
        "sudhakantpanda"
    ]
}
```

### New Visit
#### Request
```
POST /create/visit
```
```
curl --location 'localhost:8080/create/visit' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=988C358227372EDEC81DA89808D2C831' \
--data '{
    "flatNumber": "A-129",
    "communityDTO": {
        "communityName": "Yamuna EnClave",
        "communityAddress":{
            "areaCode" : "132103"
        }
    }
}'
```
#### Response
```
HTTP/1.1 201 CREATED
Status: 201 CREATED
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 36
 
Body:
Visit has been successfully created.
```

### Complete a Visit
#### Request
```
PATCH /outbound/visit
```
```
curl --location --request PATCH 'localhost:8080/outbound/visit' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=3E512BAA26D62A4AE08CD98EEFC40B69' \
--data '{
    "communityName": "Yamuna Enclave",
    "communityAddress":{
        "areaCode" : "132103"
    }
}'
```
#### Response
```
HTTP/1.1 202 ACCEPTED
Status: 202 ACCEPTED
Connection: keep-alive
Content-Type: text/plain;charset=UTF-8
Content-Length: 24
 
Body:
Visit has been Completed
```

## Authors

*  [Shailesh Dagar](https://github.com/ShaileshDagar)

## Acknowledgments

Inspiration, code snippets, etc.
* [awesome-readme](https://github.com/matiassingers/awesome-readme)