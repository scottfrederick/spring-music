Spring Music
============

This is a sample application for using database services on [Cloud Foundry](http://cloudfoundry.com)
with the [Spring Framework](http://www.springframework.org).

This application has been built to store the same domain objects in one of a variety of different persistence technologies - relational, document, and key-value stores. This is not meant to represent a realistic use case for these technologies, since you would typically choose the one most applicable to the type of data you need to store, but it is useful for testing and experimenting with different types of services on Cloud Foundry. 

The application use Spring Java configuration and [bean profiles](http://static.springsource.org/spring/docs/current/spring-framework-reference/html/new-in-3.1.html#new-in-3.1-bean-definition-profiles) to configure the application and the connection objects needed to use the persistence stores. It also uses the [cloudfoundry-runtime](https://github.com/cloudfoundry/vcap-java/tree/master/cloudfoundry-runtime) library to inspect the environment when running on Cloud Foundry. See the [Cloud Foundry documentation](http://docs.cloudfoundry.com/docs/using/services/spring-service-bindings.html) for details on configuring a Spring application for Cloud Foundry using the cloudfoundry-runtime library.

## Running the application locally

One Spring bean profile should be activated to choose the database provider that the application should use. The profile is selected by setting the system property `spring.profiles.active` when starting the app.

The application can be started locally using the following command:

~~~
$ ./gradlew tomcatRun -Dspring.profiles.active=<profile>
~~~

where `<profile>` is one of the following values:

* `in-memory` (no external database required)
* `mysql`
* `postgres`
* `mongodb`
* `redis`

If no profile is provided, `in-memory` will be used. If any other profile is provided, the appropriate database server
must be started separately. The application will use the host name `localhost` and the default port to connect to the database.

If more than one of these profiles is provided, the application will throw an exception and fail to start.

## Running the application on Cloud Foundry

When running on Cloud Foundry, the application will detect the type of database service bound to the application
(if any). If a service of one of the supported types (MySQL, Postgres, MongoDB, or Redis) is bound to the app, the
appropriate Spring profile will be configured to use the database service. The connection strings and credentials
needed to use the service will be extracted from the Cloud Foundry environment.

If no bound services are found containing any of these values in the name, then the `in-memory` profile will be used.

If more than one service containing any of these values is bound to the application, the application will throw an
exception and fail to start.

After installing the 'cf' [command-line interface for Cloud Foundry](http://docs.cloudfoundry.com/docs/using/managing-apps/cf/),
targeting a Cloud Foundry instance, and logging in, the application can be built and pushed using these commands:

~~~
$ ./gradlew assemble

$ cf push
...
Push successful! App 'spring-music' available at http://spring-music-db130.cfapps.io
~~~

The application will be pushed using settings in the provided `manifest.yml` file. The settings include some random 
characters in the host to make sure the URL for the app is unique in the Cloud Foundry environment. The last line of the ouput will show the URL that has been assigned to the application. 

You can bind the application to a database service when it is pushed, or you can run it without a bound service (in
the `in-memory` profile).

If you do not create and bind a database service when the app is pushed, you can do this later using these commands:

~~~
$ cf create-service
<follow the prompts to choose a database service>
$ cf bind-service
<follow the prompts to choose the spring-music app and the created database service>
$ cf restart
~~~

To test the application with different services, you can simply stop the app, unbind a service, bind a different
database service, and start the app:

~~~
$ cf unbind-service
<follow the prompts to choose the spring-music app, and a service to unbind from the app>
$ cf bind-service
<follow the prompts to choose the spring-music app, and a differet service to bind>
$ cf restart
~~~
