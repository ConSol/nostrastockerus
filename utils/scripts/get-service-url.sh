#!/usr/bin/env bash

minikube service ${CURRENT_SERVICE_NAME} --namespace=${K8S_NAMESPACE} --url
