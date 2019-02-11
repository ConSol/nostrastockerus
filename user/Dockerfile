FROM openliberty/open-liberty:microProfile1-java8-ibmsfj

# disable automatic generation of a default keystore
ENV KEYSTORE_REQUIRED "false"

COPY target/*.war /app/app.war
COPY src/main/liberty/config/server.xml /config/server.xml
COPY target/jdbc/postgresql-jdbc-driver.jar /app/jdbc/postgresql-jdbc-driver.jar
COPY target/zipkin/extension /opt/ol/wlp/usr/extension
