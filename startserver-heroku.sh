#point to the correct configuration and webapp
CATALINA_BASE=`pwd`/target/cargo/configurations/tomcat6x
export CATALINA_BASE

#copy over the Heroku config files
cp ./server.xml ./target/cargo/configurations/tomcat6x/conf/server.xml
#cp ./persistence-heroku.xml ./target/cargo/configurations/tomcat6x/webapps/ROOT/WEB-INF/classes/META-INF/persistence.xml

#make the Tomcat scripts executable
chmod a+x ./target/cargo/installs/apache-tomcat-6.0.18/apache-tomcat-6.0.18/bin/*.sh

#set the correct port and database settings
JAVA_OPTS="$JAVA_OPTS -Dhttp.port=$PORT -Dhibernate.connection.url=$DATABASE_URL"
export JAVA_OPTS

#start Tomcat
./target/cargo/installs/apache-tomcat-6.0.18/apache-tomcat-6.0.18/bin/catalina.sh run
