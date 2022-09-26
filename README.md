
1. Deploy application to VMware Tanzu Application Platform
```
tanzu apps workload create --local-path high-mobility-source -f tap/workload-high-mobility-source.yaml --source-image harbor.emea.end2end.link/tap-wkld/high-mobility-sourc-source -n dev-space
```
2. Configure High Mobility Webhook
