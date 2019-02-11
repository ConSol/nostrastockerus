#!/usr/bin/env bash

source helm-uninstall-prometheus-operator.sh
kubectl delete daemonsets,replicasets,services,deployments,pods,rc,cronjobs --all --namespace=${K8S_NAMESPACE}
