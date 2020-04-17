FROM maven:3-jdk-8-alpine as APP
WORKDIR /app
ADD . /app
RUN mvn clean install -DskipTests -Paws
RUN mkdir -p dist
RUN cp -f src/iso-registry-client/target/*.war dist
RUN cp -f src/iso-registry-soap/target/*.war dist

FROM tomcat:8-jre8-alpine
RUN value=`cat /usr/local/tomcat/conf/server.xml` && echo "${value//8080/80}" >| /usr/local/tomcat/conf/server.xml
RUN rm -Rf /usr/local/tomcat/webapps
COPY --from=APP /app/dist/isoreg.war /usr/local/tomcat/webapps/ROOT.war
RUN echo -e "\norg.apache.level=WARNING" >> /usr/local/tomcat/conf/logging.properties
RUN echo -e "\norg.apache.catalina.startup.Catalina.level=INFO" >> /usr/local/tomcat/conf/logging.properties
RUN echo -e "\norg.hibernate.level=WARNING" >> /usr/local/tomcat/conf/logging.properties
#org.apache.level=WARNING
#org.apache.catalina.startup.Catalina.level=INFO
#org.hibernate.level=WARNING