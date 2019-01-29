FROM tomcat:8-jre8-alpine

RUN value=`cat /usr/local/tomcat/conf/server.xml` && echo "${value//8080/80}" >| /usr/local/tomcat/conf/server.xml

RUN rm -Rf /usr/local/tomcat/webapps
COPY dist/isoreg.war /usr/local/tomcat/webapps/ROOT.war

ADD scripts/wait_for.sh /wait
RUN chmod +x /wait

## Launch the wait tool and then your application
CMD /wait $WAIT_ARGS -- catalina.sh run

##command: ["./wait-for-it/wait-for-it.sh", "db:5432", "--", "npm",  "start"]
