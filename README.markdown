# Spring Music

This is a sample application for using database services on [Cloud Foundry](http://cloudfoundry.com)
with [Spring Framework](http://www.springframework.org).

## Running the application locally

This application uses [Spring Profiles](http://static.springsource.org/spring/docs/current/spring-framework-reference/html/new-in-3.1.html#new-in-3.1-bean-definition-profiles)
to select one database provider that the application should use. The database profile is selected by setting the system
property `spring.profiles.active` when starting the app.

The application can be started locally using the following command:

~~~
$ ./gradlew tomcatRun -Dspring.profiles.active=<profile>
~~~

where `<profile>` is one of the following values:

* `in-memory` (no external database required)
* `mysql`
* `postgres`
* `mongodb`

If no profile is provided, `in-memory` will be used. If any other profile is provided, the appropriate database server
must be started separately using default ports. The application will use the host name `localhost` to connect to the
database.

If more than one of these profiles is provided, the application will throw an exception and fail to start.

## Running the application on Cloud Foundry

When running on Cloud Foundry, the application will detect the type of database service bound to the application
(if any) and configure the appropriate Spring profile automatically. This is currently done by inspecting the name
of the service to see if it contains one of the following values anywhere in the name:

* `mysql`
* `postgres`
* `mongodb`

If one of these values is found anywhere in the name of a bound service, then the appropriate Spring profile will be
configured to use the database service. The connection strings and credentials needed to use the service will be
extracted from the Cloud Foundry environment.

If no bound services are found containing any of these values in the name, then the `in-memory` profile will be used.

If more than one service containing any of these values is bound to the application, the application will throw an
exception and fail to start.

After install in the 'cf' [command-line interface for Cloud Foundry](http://docs.cloudfoundry.com/docs/using/managing-apps/cf/),
the application can be pushed using these commands:

~~~
$ ./gradlew assemble

$ cf push --path=build/libs/spring-music.war
~~~

You can bind the application to a database service when it is pushed, or you can run it without a bound service (in
the `in-memory` profile).

If you don't create and bind a database service when the app is pushed, you can do this later using these commands:

~~~
$ cf create-service
<choose a database service from the list; be sure to include the appropriate profile in the name>
$ cf bind-service
<choose the created database service and the spring-music application>
~~~
