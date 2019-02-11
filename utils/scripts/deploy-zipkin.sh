#!/usr/bin/env bash

kubectl apply -f ../k8s/zipkin.k8s.yaml --namespace=${K8S_NAMESPACE}
