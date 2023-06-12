function listNomadRunningJobs() {
    const elementList = document.getElementById('nomad-jobs');
    const nomadUrl = document.getElementById("nomadUrl").value;
    console.log("Getting running jobs from --> " + nomadUrl);

    if (dockerUrlStatus.key && nomadUrlStatus.key) {
        axios.get('/nomad/jobs?nomadUrl='.concat(nomadUrl))
            .then(function (response) {
                const data = response.data;
                console.log("Printing response --> " + response);

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
                        button.appendChild(document.createTextNode('Parar'));
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

function stopNomadJob(jobName) {
    let nomadUrl = document.getElementById("nomadUrl").value;

    console.log("Parando el job con ID: ".concat(jobName));

    axios.delete('/nomad/stop?nomadUrl='.concat(nomadUrl).concat("&jobName=").concat(jobName))
        .then(function (response) {
            const data = response.data;
            console.log("Printing response --> " + response);
        })
        .catch(function (error) {
            console.log(error);
            alert("No se pudo parar el job: " + error.response.data);
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


function listFixedNomadJobs() {
    console.log("Getting fixed jobs from");

    axios.get('/nomad/fixed-jobs')
        .then(function (response) {
            const data = response.data;
            console.log("Printing response --> " + data);
            const elementList = document.getElementById('nomad-fixed-jobs');

            if(data.length === 0) {
                elementList.innerHTML = "No hay fixed jobs disponibles en el backend"
            } else {
                elementList.innerHTML = "";

                for (const i in data) {
                    if (data.hasOwnProperty(i)) {
                        const fixedJob = data[i];
                        const li = document.createElement('li');

                        const nameSpan = document.createElement('span');
                        nameSpan.appendChild(document.createTextNode(fixedJob.name + " " + fixedJob.version));
                        li.appendChild(nameSpan);

                        const button = document.createElement('button');
                        button.type = "button";
                        button.appendChild(document.createTextNode('Añadir'));
                        button.addEventListener('click', function () {
                            startFixedJob(data[i]);
                        });
                        li.appendChild(button);
                        elementList.appendChild(li);
                    }
                }
            }
        })
        .catch(function (error) {
            console.log(error);
            alert("Error en la llamada al servidor: " + error.response.data);
        });
}

function startFixedJob(fixedJob) {

    fixedJob.nomadUrl = document.getElementById("nomadUrl").value;

    axios.post('/nomad/start-fixed-job', JSON.stringify(fixedJob), {
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
            console.error('Error lanzando el job con estos datos:', fixedJob);
            alert(`El estado recibido es: ${status}`);
        });
}
