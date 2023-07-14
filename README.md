# pfg-mock-fw

## Description

This application is part of an undergraduate thesis project for the Spanish Universidad Nacional de Educación a Distancia (UNED). 

Its goal is to serve as a testing framework for the interaction between different microservices within a company. 

It allows listing the applications from the Docker registry and deploying them in Docker.

This deployment is orchestrated through Nomad, and the service discovery is got with Consul.

It is thought to be used in local environments through the usage of a virtual machine, where the Docker registry, Nomad and Consul will be deployed.

## Sonarqube

In order to check its coverage, just run in your terminal, providing you have Docker installed:

docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube
