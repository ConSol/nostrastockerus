#!/usr/bin/env bash

kubectl config set-context $(kubectl config current-context) --namespace=${K8S_NAMESPACE}
