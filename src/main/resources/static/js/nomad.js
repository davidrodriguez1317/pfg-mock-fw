function listNomadRunningJobs() {
    const elementList = document.getElementById('nomad-jobs');
    const nomadUrl = document.getElementById("nomadUrl").value;
    console.log("Getting running jobs from --> " + nomadUrl);

    if (dockerUrlStatus.key && nomadUrlStatus.key) {
        axios.get('/nomad/jobs?nomadUrl='.concat(nomadUrl))
            .then(function (response) {
                const data = response.data;
                console.log("Printing data --> " + data);

                elementList.innerHTML = "";

                data.forEach(function (job) {
                    const li = document.createElement('li');
                    li.classList.add("job-item");

                    const jobLabel = document.createElement('span');
                    jobLabel.classList.add("label-key-bold");
                    jobLabel.appendChild(document.createTextNode("JOB: "));
                    li.appendChild(jobLabel);

                    const jobId = document.createElement('span');
                    jobId.classList.add("label-value");
                    jobId.appendChild(document.createTextNode(job.id));
                    li.appendChild(jobId);

                    const statusLabel = document.createElement('span');
                    statusLabel.classList.add("label-key-bold");
                    statusLabel.appendChild(document.createTextNode("STATE:"));
                    li.appendChild(statusLabel);

                    const status = document.createElement('span');
                    status.classList.add("label-value");
                    status.appendChild(document.createTextNode(job.status.toLowerCase()));
                    li.appendChild(status);

                    if (job.status === "RUNNING") {
                        const button = document.createElement('button');
                        button.type = "button";
                        button.classList.add("stop-button");
                        button.appendChild(document.createTextNode('PARAR'));
                        button.addEventListener('click', function () {
                            stopNomadJob(job.id);
                        });
                        li.appendChild(button);
                    }

                    elementList.appendChild(li);
                });
            })
            .catch(function (error) {
                console.log(error);
                alert("Error en la llamada al servidor: " + error.response.data);
                elementList.innerHTML = "Jobs no listados. Servidor no accesible";
            });
    } else {
        console.log("Llamada de running jobs no realizada. " +
            "Estado de los servidores de docker y nomad: " + dockerUrlStatus.key + " - " + nomadUrlStatus.key);
        elementList.innerHTML = "Jobs no listados. Servidor no accesible";
    }
}

function stopNomadJob(jobId) {
    console.log("Se ha parado el job con ID: " + jobId);
    // Lógica para detener el trabajo en Nomad
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
