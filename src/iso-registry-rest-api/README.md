### Standard Tomcat8 Dev Setup

- JDK 8 is required
- Database Postgres 9+ ; `db` is default database hostname
- Tomcat8 installed
- Run `[root]/bash build` to get final `war` file to deploy to tomcat for test
    - Use isoreg.war in /iso-geodetic-registry/src/iso-registry-rest-api/target


### AWS LAMBDA LOCAL Dev Setup

- Install `aws-sam-cli`
- Run `[root]/bash build aws` to build final `isoreg-lambda.zip`
- Use docker to start Postgres Docker with sample DB to test (might be db init done using html-web application version)
- Run ` sam local start-api  --docker-network docker_postgres_network`

