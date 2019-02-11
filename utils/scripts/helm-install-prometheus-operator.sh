#!/usr/bin/env bash

helm install stable/prometheus-operator --name ${PROMETHEUS_OPERATOR_RELEASE_NAME} --namespace ${K8S_NAMESPACE}
