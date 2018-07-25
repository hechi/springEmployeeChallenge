echo "wait for database to start"
sleep 20 && java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=prod
