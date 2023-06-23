async function sendJarFile() {
    const localFileInput = document.getElementById('local-file');

    if (localFileInput.files.length > 0) {
        currentLocalNomadJob = localFileInput.files[0].name;
        await postRequestMultiPart('/nomad/upload-local-job', localFileInput.files[0])
        await getEnvs('loc-docker-images-envs', 'docker-images-envs');
    } else {
        alert('No se seleccionó ninguna app para desplegar en Nomad')
    }
}


async function startLocalJob(jarName, eMap) {
    const nomadUrl = document.getElementById("nomadUrl").value;

    const body = {
        nomadUrl: nomadUrl,
        fileName: currentLocalNomadJob,
        envs: Object.fromEntries(eMap)
    };

    await postRequest( '/nomad/start-local-job', body);
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

