FROM maven:3-jdk-8-alpine as APP
WORKDIR /app
ADD . /app
RUN mkdir -p dist
RUN mvn clean install -DskipTests
COPY src/iso-registry-client/target/*.war dist
COPY src/iso-registry-soap/target/*.war dist

FROM tomcat:8-jre8-alpine
RUN value=`cat /usr/local/tomcat/conf/server.xml` && echo "${value//8080/80}" >| /usr/local/tomcat/conf/server.xml
RUN rm -Rf /usr/local/tomcat/webapps
COPY --from=APP /app/dist/isoreg.war /usr/local/tomcat/webapps/ROOT.war
