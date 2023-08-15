LOCAL_PATH = os.getenv("LOCAL_PATH", default='.')
db_resource = os.getenv("DB_RESOURCE", default='')

allow_k8s_contexts(k8s_context())

service_claim_apply_cmd = ""
service_claim_delete_cmd = ""
service_ref_args = ""

if db_resource == '':
    db_resource = str(local("tanzu service class list | grep 'mysql' | awk '{print $1;}'")).strip()

if db_resource == '':
    db_resource = str(local("tanzu service class list | grep 'postgres' |  awk '{print $1;}'")).strip()

if db_resource == '':
    db_resource = str(local("tanzu service class list | grep 'mongodb' |  awk '{print $1;}'")).strip()

if db_resource == '':
    db_resource = str(local("tanzu service class list | grep 'redis' |  awk '{print $1;}'")).strip()

if db_resource != '':
    service_claim_apply_cmd = "tanzu service class-claim create music-db --class " + db_resource + " && "
    service_ref_args = " --service-ref music-db=services.apps.tanzu.vmware.com/v1alpha1:ClassClaim:music-db"
    service_claim_delete_cmd = " && tanzu service class-claim delete music-db --yes"

k8s_custom_deploy(
    'spring-music',
    apply_cmd = service_claim_apply_cmd +
                "tanzu apps workload apply spring-music " +
                " --local-path " + LOCAL_PATH +
                " --type web" +
                " --live-update" +
                " --label app.kubernetes.io/part-of=spring-music" +
                " --label apps.tanzu.vmware.com/auto-configure-actuators=true " +
                " --annotation autoscaling.knative.dev/minScale=1" +
                " --build-env BP_JVM_VERSION=17" +
                service_ref_args +
                " --yes" +
                " --output yaml",
    delete_cmd = "tanzu apps workload delete spring-music --yes" +
                 service_claim_delete_cmd,
    deps = ['src', 'build.gradle'],
    container_selector = 'workload',
    live_update = [
      sync('./build/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource('spring-music', port_forwards=["8080:8080", "8081:8081"],
            extra_pod_selectors=[{'carto.run/workload-name': 'spring-music', 'app.kubernetes.io/component': 'run'}])

update_settings(max_parallel_updates=3, k8s_upsert_timeout_secs=90, suppress_unused_image_warnings=None)
