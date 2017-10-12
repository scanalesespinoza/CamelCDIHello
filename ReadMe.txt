Camel Router Project para Java 
=========================================

Proyecto Camel Java DSL

Para contruir el proyecto

    mvn clean install

Para publicar el proyecto en JBoss Fuse usando Fabric8

    mvn fabric8:deploy
    
Par publicar el proyecto a un servidor remoto
    
    mvn fabric8:deploy -Dfabric8.jolokiaUrl=http://someServer:8181/jolokia
    
Configuración de autenticación

    ~/.m2/settings.xml:
                <server>
                        <username>admin</username>
                        <password>admin</password>
                        <id>fabric8.upload.repo</id>
                </server>

Ejecución standalone

mvn exec:java -Dexec.mainClass=org.apache.camel.main.Main -Dexec.args='-r com.redhat.ws.proxy.CamelRoute'
