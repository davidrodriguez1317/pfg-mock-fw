function checkUrl(idCampoUrl, idCheck, urlStatusValue) {
    const url = document.getElementById(idCampoUrl).value;
    axios.get('/status/server?serverUrl='.concat(url))
        .then(response => {
            const status = response.data;
            console.log(`El estado es: ${status}`);
            const resultadoDiv = document.getElementById(idCheck);
            if (status) {
                resultadoDiv.innerHTML = '<h3>&#x2705;</h3>';
                urlStatusValue.key = true;
            } else {
                resultadoDiv.innerHTML = '<h3>&#x274C;</h3>';
                urlStatusValue.key = false;
            }
        })
        .catch(error => {
            console.error('Error al obtener el estado de la URL:', error);
        });
}