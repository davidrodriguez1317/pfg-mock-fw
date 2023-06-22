async function listNomadRunningJobs() {
    const elementList = document.getElementById('nomad-jobs');
    const nomadUrl = document.getElementById("nomadUrl").value;

    const path = '/nomad/jobs?nomadUrl='.concat(nomadUrl);

    if (dockerUrlStatus.key && nomadUrlStatus.key) {
        const data = await getRequest(path);

        if(data.length === 0) {
            elementList.innerHTML = "No hay jobs corriendo en Nomad"
        } else {
            elementList.innerHTML = "";
        }

        data.forEach(function (job) {
            const li = document.createElement('li');
            li.classList.add('list-group-item', 'justify-content-between', 'align-items-center');

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

            const button = document.createElement('button');
            button.type = "button";
            button.classList.add('btn', 'btn-success', 'ms-2');
            button.appendChild(document.createTextNode('Eliminar'));
            button.addEventListener('click', function () {
                stopNomadJob(job.id);
            });
            li.appendChild(button);

            elementList.appendChild(li);
        });
    } else {
        console.log("Llamada de running jobs no realizada. " +
            "Estado de los servidores de docker y nomad: " + dockerUrlStatus.key + " - " + nomadUrlStatus.key);
        elementList.innerHTML = "Jobs no listados. Servidor no accesible";
    }
}

async function stopNomadJob(jobName) {
    let nomadUrl = document.getElementById("nomadUrl").value;

    const path = '/nomad/stop?nomadUrl='
        .concat(nomadUrl)
        .concat("&jobName=")
        .concat(jobName);

    await deleteRequest(path);
}

async function startJob(appName, appVersion, eMap) {
    const dockerUrl = document.getElementById("dockerUrl").value;
    const nomadUrl = document.getElementById("nomadUrl").value;

    const body = {
        dockerUrl: dockerUrl,
        nomadUrl: nomadUrl,
        appName: appName,
        appVersion: appVersion,
        envs: Object.fromEntries(eMap)
    };

    await postRequest("/nomad/start", body);
}

async function listFixedNomadJobs() {
    console.log("Getting fixed jobs from");

    const path = "/nomad/fixed-jobs";
    const data = await getRequest(path);

    const elementList = document.getElementById('nomad-fixed-jobs');

    if (data.length === 0) {
        elementList.innerHTML = "No hay jobs fijos disponibles en el backend";
    } else {
        elementList.innerHTML = "";

        for (const i in data) {
            if (data.hasOwnProperty(i)) {
                const fixedJob = data[i];
                const li = document.createElement('li');
                li.classList.add('list-group-item', 'justify-content-between', 'align-items-center');

                const nameSpan = document.createElement('span');
                nameSpan.appendChild(document.createTextNode(fixedJob.name + " " + fixedJob.version));
                li.appendChild(nameSpan);

                const button = document.createElement('button');
                button.type = "button";
                button.classList.add('btn', 'btn-success', 'ms-2');
                button.appendChild(document.createTextNode('Añadir'));
                button.addEventListener('click', function () {
                    startFixedJob(data[i]);
                });
                li.appendChild(button);
                elementList.appendChild(li);
            }
        }
    }
}

async function startFixedJob(fixedJob) {
    fixedJob.nomadUrl = document.getElementById("nomadUrl").value;
    await postRequest("/nomad/start-fixed-job", fixedJob);
}
