
## Prerequisites
- TAP >=1.2 installation
- RabbitMQ operator

## Setup

### Ops (should be automated and provided as self-service for prod env)

```
kubectl apply -f tap/ops/scan-policy.yaml -n $DEVELOPER_NAMESPACE
```

#### Config Server
```
kubectl create secret generic configserver-secret --from-literal=git-url=https://github.com/<user>/<config-repo>.git --from-literal=username=<git-username> --from-literal=password=<git-personal-access-token> -n $DEVELOPER_NAMESPACE
kubectl apply -f tap/ops/config-server.yaml -n $DEVELOPER_NAMESPACE
```

#### RabbitMQ
```
kubectl apply -f tap/ops/rabbit.yaml -n $DEVELOPER_NAMESPACE
```

### Devs:
Deploy application to VMware Tanzu Application Platform
```
kubectl apply -f tap/test-pipeline.yaml -f -n $DEVELOPER_NAMESPACE
kubectl apply -f tap/workload-high-mobility-source.yaml -n $DEVELOPER_NAMESPACE
```
Configure High Mobility Webhook
