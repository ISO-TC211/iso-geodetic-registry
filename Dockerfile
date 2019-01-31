FROM tomcat:7-jre7-alpine

RUN value=`cat /usr/local/tomcat/conf/server.xml` && echo "${value//8080/80}" >| /usr/local/tomcat/conf/server.xml

RUN rm -Rf /usr/local/tomcat/webapps
COPY dist/isoreg.war /usr/local/tomcat/webapps/ROOT.war

