#!/usr/bin/env bash

helm del --purge ${PROMETHEUS_OPERATOR_RELEASE_NAME}

# https://github.com/helm/charts/tree/master/stable/prometheus-operator#uninstalling-the-chart
kubectl delete crd prometheuses.monitoring.coreos.com --namespace ${K8S_NAMESPACE}
kubectl delete crd prometheusrules.monitoring.coreos.com --namespace ${K8S_NAMESPACE}
kubectl delete crd servicemonitors.monitoring.coreos.com --namespace ${K8S_NAMESPACE}
kubectl delete crd alertmanagers.monitoring.coreos.com --namespace ${K8S_NAMESPACE}
