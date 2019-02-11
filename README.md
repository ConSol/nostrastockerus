# Nostrastockerus (MicroProfile Example App)
Tech stack: Java 8 + MicroProfile 2.1 + Open Liberty 18.0.0.4

### Functionality
Users can create "prophecies" (e.g. "stock X on date Y will have value Z) and vote for or against them. The prophecies are checked periodically (by a K8S `CronJob`). The aggregated statistics for a user can be then showed showing how many "prophecies" he/she has done, how many have been checked and how many have fulfilled (or, equivalently, which "precision" as a "prophet" the user has).

The app consists of 3 (micro-)services (user, prophecy and stats) which demonstrate usage of various MicroProfile technologies (Config, OpenAPI, OpenTracing etc.). Responsibilities of the services are the following:
* user service holds users and issues JWTs to them.
* prophecy service holds "prophecies" and accepts votes for and against them.
* stats service calls the prophecy service, collects prophecies and checks them. It also creates and aggregates statistics about users and theirs prophecies.

All three services have the following common features:
* they are secured via JWT
* they have `/health` and `/metrics` endpoints for liveness probing and metrics scraping respectively
* they use MicroProfile Config for configuration and support MicroProfile OpenTracing (Zipkin is used for trace collecting)

### Deployment
###### Dependencies
* `minikube`
* `kubectl`
* `helm`
* `JDK 1.8` ([keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html) will be needed)
* `maven`
* `curl`
* `docker`
###### Procedure
* `git clone https://github.com/progaddict/nostrastockerus.git`
* `cd nostrastockerus/utils/scripts`
* `minikube start # if not running yet`
* wait for minikube to start
* `source main.sh`

### Usage example
see the `utils/scripts/example-requests.sh`

### Prometheus metrics
* `source vars.sh`
* find the prometheus pod:
`POD=$(kubectl get pods --selector="app=prometheus,prometheus=${K8S_NAMESPACE}-prome-prometheus" | grep prometheus- | cut -d' ' -f 1)`
* the pod should be named similar to `prometheus-microprofile-article-prome-prometheus-0`
* ask K8S to proxy the `9090` port of the pod:
`kubectl port-forward ${POD} 9090`
* go to http://localhost:9090/ and observe the Prometheus' UI

### Zipkin traces
* `source vars.sh`
* `minikube service zipkin --namespace=${K8S_NAMESPACE}`
