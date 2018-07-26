#!/bin/bash
echo "Build maven project"
mvn package
echo "run docker-compose"
docker-compose up
