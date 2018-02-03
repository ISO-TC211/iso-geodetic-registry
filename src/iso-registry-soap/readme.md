# Configuration of SOAP server

## Database connection

Connection details must be configured in 'WEB-INF/classes/hibernate.connection.xml'.
Additional parameters may be configured in 'WEB-INF/classes/hibernate.properties.xml'.

## Base path for proxy environments

The SOAP server base path, i.e. the external base URL, that is used to generate the
dynamic WSDL documents must be configured in 'WEB-INF/spring-ws-servlet.xml'.

There is one '<sws:dynamic-wsdl>' tag for every service provided. Its 'locationUri'
attribute controls the value of the service locator in dynamically created WSDLs.

If the value of the 'locationUri' attribute is an absolute path, the scheme, hostname, 
and port will be changed. If the location is a relative path, the scheme, hostname, port, 
and context path will be prepended. Therefore, proxy environments must provide an absolute path.

If e.g. the service can be reached externally via 'https://www.example.org/registry/ws'
the value of the 'locationUri' attribute must be 'http://localhost:8080/registry/ws'.

