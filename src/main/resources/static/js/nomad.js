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


            const buttonLogs = document.createElement('button');
            buttonLogs.type = "button";
            buttonLogs.classList.add('btn', 'btn-success', 'ms-2', 'medium-margin-left');
            buttonLogs.appendChild(document.createTextNode('Logs'));
            buttonLogs.addEventListener('click', function () {
                addNomadLogs(job.id);
            });
            li.appendChild(buttonLogs);

            const buttonRemove = document.createElement('button');
            buttonRemove.type = "button";
            buttonRemove.classList.add('btn', 'btn-danger', 'ms-2');
            buttonRemove.appendChild(document.createTextNode('Eliminar'));
            buttonRemove.addEventListener('click', function () {
                stopNomadJob(job.id);
            });
            li.appendChild(buttonRemove);

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

    const divId = "logs-".concat(jobName);
    let appLogsContainer = document.getElementById(divId);
    appLogsContainer.remove();
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
    console.log("Getting fixed jobs");

    if (dockerUrlStatus.key && nomadUrlStatus.key) {
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
}

async function startFixedJob(fixedJob) {
    fixedJob.nomadUrl = document.getElementById("nomadUrl").value;
    await postRequest("/nomad/start-fixed-job", fixedJob);
}

async function addNomadLogs(jobId) {

    const logsContainer = document.getElementById('nomad-logs');
    const divId = "logs-".concat(jobId);
    const data = await getLogsForJob(jobId);

    if (data.length !== 0) {
        let appLogsContainer = createDivIfNotExistOrEmptyIt(divId, logsContainer)

        const appHeader = document.createElement('h4');
        appHeader.textContent = 'Logs de ' + jobId;

        const button = document.createElement('button');
        button.type = "button";
        button.classList.add('btn', 'btn-danger', 'ms-2', 'medium-margin-left');
        button.appendChild(document.createTextNode('Eliminar'));
        button.addEventListener('click', function () {
            appLogsContainer.remove();
        });
        appHeader.appendChild(button);

        const logLinesContainer = document.createElement('div');
        logLinesContainer.classList.add('log-lines');

        const lines = data.split("\n");

        lines.forEach((line, index) => {
            const lineElement = document.createElement("p");
            lineElement.textContent = line;
            if (index % 2 !== 0) {
                lineElement.classList.add("odd");
            }
            logLinesContainer.appendChild(lineElement);
        });

        appLogsContainer.appendChild(appHeader);
        appLogsContainer.appendChild(logLinesContainer);
    }
}

function createDivIfNotExistOrEmptyIt(divId, parentDiv){
    let appLogsContainer = document.getElementById(divId);

    if (!appLogsContainer) {
        appLogsContainer = document.createElement('div');
        appLogsContainer.classList.add('app-logs');
        appLogsContainer.id = divId;
        parentDiv.appendChild(appLogsContainer);
    } else {
        while (appLogsContainer.firstChild) {
            appLogsContainer.removeChild(appLogsContainer.firstChild);
        }
    }
    return appLogsContainer;
}


async function getLogsForJob(jobName) {
    let nomadUrl = document.getElementById("nomadUrl").value;

    const path = '/nomad/logs?nomadUrl='
        .concat(nomadUrl)
        .concat("&jobName=")
        .concat(jobName);

    return await getRequest(path);
}
