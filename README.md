# Passwd as a Service

This app is a result of the coding challenge provided by Brain Corporation. It is a HTTP (GET) service that retrieves and exposes the user and group files that are typically locked away in the /etc/ directory (for Unix machines).

## Features

  - Reads and parse the passwd and group file 
  - Get all the users and/or groups
  - Get all users and/or by a combination of arguments
  - Responses made after changes to passwd and group files are reflects the changes made while the server is live

## Installation

Requires jdk 8 and maven to run.

If running on a Unix machine, be in the project root folder and run:
```sh
$ mvn clean install
$ java -jar target/http-rest-service-0.0.1-SNAPSHOT.jar
```

If running on OSx or Windows machine just load it up with your preferred ide and build.

## Configurations
Before running the project, be sure to look at `app.properties` at main directory and configure 
the directory of the passwd/group files:
```sh
name=http-rest-service
version=1.0


# Change custom to false if you want to use the default
# passwd / group files in ./data/
custom=true

# The passwd and group file provided are in ./data
default_passwd=./data/passwd
default_group=./data/group

# Edit the custom values to the path where you want to
# load your files
custom_passwd=./data/passwd
custom_group=./data/group

```
The app will automatically use the custom settings which is where you can list the directory to your own passwd/group file.

The configurations for the port can be found in `application.properties` which is located in `\src\main\resources\application.properties`. The default port is `3000`.
## Functionality

The HTTP service provides these methods by url:

#### localhost:3000/users
Returns a list of all users on the system as specified in the passwd file

#### localhost:3000/query[?name=<nq>][&uid=<uq>][&gid=<gq>][&comment=<cq>][&home=<hq>][&shell=<sq>]

Returns a list of users matching all the specified query fields. The bracket notations indicate that any of these query parameters can be supplied
  - name
  - uid
  - gid
  - comment
  - home
  - shell

Only exact matches are supported. If no users match the specified query fields, an empty list will be returned.

#### localhost:3000/users/<uid>

Returns a single user with <uid>. It returns a 404 if uid does not exist.

#### localhost:3000/users/<uid>/groups
Returns a list of groups that has user <uid> as a member.

#### localhost:3000/groups
Returns a list of all groups on the system as specified in the group file

#### localhost:3000/groups/query[?name=<nq>][&gid=<gq>][&member=<mq1>[&member=<mq2>][&...]]
Returns a list of groups matching all the specified query fields. The bracket notations indicate that any of these query parameters can be supplied
  - name
  - gid
  - member (can be repeated)
Any group containing all the specified members will be returned, i.e., when the query members are a subset of the group's members. Only exact matches are supported.

#### localhost:3000/groups/<gid>
Returns a single group with <gid>. Return 404 if gid does not exist.

