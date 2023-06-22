async function sendJarFile() {
    const localFileInput = document.getElementById('local-file');
    const nomadUrl = document.getElementById("nomadUrl").value;

    if (localFileInput.files.length > 0) {
        const path = '/nomad/start-local-job?nomadUrl='
            .concat(nomadUrl)
            .concat("&jobName=")
            .concat(localFileInput.files[0].name);

        await postRequestMultiPart(path, localFileInput.files[0])
    }
}
