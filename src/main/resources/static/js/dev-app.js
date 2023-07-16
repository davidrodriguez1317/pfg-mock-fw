async function sendJarFile() {
    const localFileInput = document.getElementById('local-file');

    if (platformUrlStatus.key && orchestratorUrlStatus.key) {
        if (localFileInput.files.length > 0) {
            currentLocalJob = localFileInput.files[0].name;
            await postRequestMultiPart('/orchestrator/upload-local-job', localFileInput.files[0])
            await getEnvs('loc-platform-images-envs', 'platform-images-envs');
        } else {
            alert('No se seleccionó ninguna app para desplegar en el orquestador')
        }
    }
}


async function startLocalJob(jarName, eMap) {
    const orchestratorUrl = document.getElementById("orchestratorUrl").value;

    const body = {
        orchestratorUrl: orchestratorUrl,
        fileName: currentLocalJob,
        envs: Object.fromEntries(eMap)
    };

    await postRequest( '/orchestrator/start-local-job', body);
}

function updateFileName() {
    const fileInput = document.getElementById('local-file');
    const fileLabel = document.getElementById('file-label');

    if (fileInput.files.length > 0) {
        fileLabel.innerText = fileInput.files[0].name;
    } else {
        fileLabel.innerText = 'Seleccionar archivo';
    }
}

