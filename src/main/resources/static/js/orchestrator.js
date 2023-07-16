async function listRunningJobs() {

    const elementList = document.getElementById('jobs');
    const orchestratorUrl = document.getElementById("orchestratorUrl").value;
    const path = '/orchestrator/jobs?orchestratorUrl='.concat(orchestratorUrl);

    if (platformUrlStatus.key && orchestratorUrlStatus.key) {
        const data = await getRequest(path);

        if(data.length === 0) {
            elementList.innerHTML = "No hay jobs corriendo en en la plataforma"
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
                addLogs(job.id);
            });
            li.appendChild(buttonLogs);

            const buttonRemove = document.createElement('button');
            buttonRemove.type = "button";
            buttonRemove.classList.add('btn', 'btn-danger', 'ms-2');
            buttonRemove.appendChild(document.createTextNode('Eliminar'));
            buttonRemove.addEventListener('click', function () {
                stopJob(job.id);
            });
            li.appendChild(buttonRemove);

            elementList.appendChild(li);
        });
    } else {
        console.log("Llamada de running jobs no realizada. " +
            "Estado de los servidores de la plataforma y orquestación: " + platformUrlStatus.key + " - " + orchestratorUrlStatus.key);
        elementList.innerHTML = "Jobs no listados. Servidor no accesible";
    }
}

async function stopJob(jobName) {
    let orchestratorUrl = document.getElementById("orchestratorUrl").value;

    const path = '/orchestrator/stop?orchestratorUrl='
        .concat(orchestratorUrl)
        .concat("&jobName=")
        .concat(jobName);

    await deleteRequest(path);

    const divId = "logs-".concat(jobName);
    let appLogsContainer = document.getElementById(divId);
    appLogsContainer.remove();
}

async function startJob(appName, appVersion, eMap) {
    const platformUrl = document.getElementById("platformUrl").value;
    const orchestratorUrl = document.getElementById("orchestratorUrl").value;

    const body = {
        platformUrl: platformUrl,
        orchestratorUrl: orchestratorUrl,
        appName: appName,
        appVersion: appVersion,
        envs: Object.fromEntries(eMap)
    };

    await postRequest("/orchestrator/start", body);
}

async function listFixedJobs() {
    console.log("Getting fixed jobs");

    if (platformUrlStatus.key && orchestratorUrlStatus.key) {
        const path = "/orchestrator/fixed-jobs";
        const data = await getRequest(path);

        const elementList = document.getElementById('fixed-jobs');

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
    fixedJob.orchestratorUrl = document.getElementById("orchestratorUrl").value;
    await postRequest("/orchestrator/start-fixed-job", fixedJob);
}

async function addLogs(jobId) {

    const logsContainer = document.getElementById('logs');
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
    let orchestratorUrl = document.getElementById("orchestratorUrl").value;

    const path = '/orchestrator/logs?orchestratorUrl='
        .concat(orchestratorUrl)
        .concat("&jobName=")
        .concat(jobName);

    return await getRequest(path);
}
