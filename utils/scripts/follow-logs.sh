#!/usr/bin/env bash

POD=$(source get-pod.sh ${CURRENT_SERVICE_NAME})
kubectl logs -f ${POD}
