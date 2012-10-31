JAVA_OPTS="$JAVA_OPTS -Dhttp.port=$PORT -Dhibernate.connection.url=$DATABASE_URL"
export JAVA_OPTS
web:    java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war