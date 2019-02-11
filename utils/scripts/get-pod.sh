#!/usr/bin/env bash

SERVICE_NAME=$1
echo $(kubectl get pods --namespace=${K8S_NAMESPACE} --selector="app=${SERVICE_NAME},component=rest-application" | grep ${SERVICE_NAME}- | cut -d' ' -f 1)
