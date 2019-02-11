#!/usr/bin/env bash

TOKEN_SERVICE=user
echo "asking service to generate admin token"
URL=$(echo "$(minikube service ${TOKEN_SERVICE} --namespace=${K8S_NAMESPACE} --url)/token/admin" | sed -e 's/http/https/')
curl --insecure ${URL}

rm -rf admin-token.txt
POD=$(source get-pod.sh ${TOKEN_SERVICE})
echo "copying the token from ${POD} to admin-token.txt"
kubectl cp ${POD}:/app/admin-token.txt ./admin-token.txt --namespace=${K8S_NAMESPACE}
