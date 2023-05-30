function listNomadRunningJobs() {
    axios.get('/nomad/jobs')
        .then(function (response) {
            const data = response.data;
            console.log("Printing data --> " + data);
            const elementList = document.getElementById('nomad-jobs');

            elementList.innerHTML = "";

            for (const key in data) {
                if (data.hasOwnProperty(key)) {
                    const elementSublist = data[key];
                    const li = document.createElement('li');
                    li.appendChild(document.createTextNode(key + ': '));

                    for (let i = 0; i < elementSublist.length; i++) {
                        const span = document.createElement('span');
                        span.appendChild(document.createTextNode(elementSublist[i]));
                        li.appendChild(span);

                        const button = document.createElement('button');
                        button.type = "button";
                        button.appendChild(document.createTextNode('Parar'));
                        button.addEventListener('click', function() {
                            console.log(`Se ha parado "${elementSublist[i]}"`);
                        });
                        li.appendChild(button);
                    }

                    elementList.appendChild(li);
                }
            }
        })
        .catch(function (error) {
            console.log(error);
            alert("Error en la llamada al servidor: " + error.response.data);
        });
}

function startJob(dockerUrl, nomadUrl, appName, appVersion) {
    const body = {
        dockerUrl: dockerUrl,
        nomadUrl: nomadUrl,
        appName: appName,
        appVersion: appVersion
    };

    axios.post('/nomad/start', JSON.stringify(body), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            const status = response.status;
            console.log(`El estado recibido es: ${status}`);
            alert(`El estado recibido es: ${status}`);
        })
        .catch(error => {
            const status = error.response.status;
            console.error('Error lanzando el job con estos datos:', body);
            alert(`El estado recibido es: ${status}`);
        });
}
