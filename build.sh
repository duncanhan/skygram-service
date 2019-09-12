#!/usr/bin/env bash

#mvn clean package -DskipTests=true

docker build -t skygram-api .

docker tag skygram-api toandc/skygram-api:0.0.1

docker push toandc/skygram-api:0.0.1
