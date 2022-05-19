allow_k8s_contexts(k8s_context())

service_ref_args = ""
app_deps = []

mysql_resources = int(str(local('kubectl api-resources | grep "mysql.*sql.tanzu.vmware.com" | wc -l')).strip())
postgres_resources = int(str(local('kubectl api-resources | grep "postgres.*sql.tanzu.vmware.com" | wc -l')).strip())

if mysql_resources > 0:
    k8s_yaml('deploy/mysql.yaml')
    k8s_resource(new_name='music-mysql',
                 objects=['music-mysql'],
                 extra_pod_selectors=[{'mysql-instance': 'music-mysql'}])
    service_ref_args = " --service-ref db=with.sql.tanzu.vmware.com/v1:MySQL:music-mysql"
    app_deps = ['music-mysql']

if postgres_resources > 0 and mysql_resources == 0:
    k8s_yaml('deploy/postgres.yaml')
    k8s_resource(new_name='music-postgres',
                 objects=['music-postgres'],
                 extra_pod_selectors=[{'postgres-instance': 'music-postgres'}])
    service_ref_args = " --service-ref db=sql.tanzu.vmware.com/v1:Postgres:music-postgres"
    app_deps = ['music-postgres']

k8s_custom_deploy(
    'spring-music',
    apply_cmd = "tanzu apps workload apply spring-music " +
                " --live-update" +
                " --git-repo https://github.com/scottfrederick/spring-music" +
                " --git-branch tanzu" +
                " --type web" +
                " --label app.kubernetes.io/part-of=spring-music" +
                " --label tanzu.app.live.view=true" +
                " --label tanzu.app.live.view.application.name=spring-music" +
                " --annotation autoscaling.knative.dev/minScale=1" +
                service_ref_args +
                " --yes >/dev/null" +
                " && " +
                "kubectl get workload spring-music -o yaml",
    delete_cmd = "tanzu apps workload delete spring-music --yes",
    deps = ['src', 'build.gradle'],
    container_selector = 'workload',
    live_update = [
      sync('./build/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource('spring-music', port_forwards=["8080:8080", "8081:8081"],
            extra_pod_selectors=[{'serving.knative.dev/service': 'spring-music'}],
            resource_deps=app_deps)

update_settings(max_parallel_updates=3, k8s_upsert_timeout_secs=90, suppress_unused_image_warnings=None)
