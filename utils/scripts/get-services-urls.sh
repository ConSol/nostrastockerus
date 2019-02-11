#!/usr/bin/env bash

for SERVICE_NAME in "${SERVICES[@]}"
do
    URL_HTTP=$(minikube service ${SERVICE_NAME} --namespace=${K8S_NAMESPACE} --url)
    URL_HTTPS=$(echo ${URL_HTTP} | sed --expression='s/http:/https:/g')
    echo "${URL_HTTPS} -> ${SERVICE_NAME}"
done
