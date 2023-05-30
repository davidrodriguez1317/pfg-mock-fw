function listDockerRegistryImages() {
    var dockerUrl = document.getElementById("dockerUrl").value;
    console.log("Getting docker images from --> " + dockerUrl);

    axios.get('/docker/images?dockerUrl='.concat(dockerUrl))
        .then(function (response) {
            const data = response.data;
            console.log("Printing data --> " + data);
            const elementList = document.getElementById('docker-registry-images');

            elementList.innerHTML = "";

            for (const i in data) {
                if (data.hasOwnProperty(i)) {
                    const repository = data[i];
                    const li = document.createElement('li');

                    const sortedTags = repository.tags.sort();

                    const nameSpan = document.createElement('span');
                    nameSpan.appendChild(document.createTextNode(repository.name));
                    li.appendChild(nameSpan);

                    li.appendChild(document.createTextNode(': '));

                    for (let j = 0; j < sortedTags.length; j++) {
                        const span = document.createElement('span');
                        span.appendChild(document.createTextNode(sortedTags[j]));
                        li.appendChild(span);

                        const button = document.createElement('button');
                        button.type = "button";
                        button.appendChild(document.createTextNode('Añadir'));
                        button.addEventListener('click', createAddNomadJobHandler(sortedTags[j]));
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

function createAddNomadJobHandler(tag) {
    return function() {
        const dockerUrl = document.getElementById("dockerUrl").value;
        const nomadUrl = document.getElementById("nomadUrl").value;
        const li = this.parentNode;
        const nameSpan = li.querySelector('span:first-child').textContent;
        addNomadJob(dockerUrl, nomadUrl, nameSpan, tag);
    };
}

function addNomadJob(dockerUrl, nomadUrl, appName, appVersion) {
    console.log(`Lanzando nomad job: ${dockerUrl}, ${nomadUrl}, ${appName}, ${appVersion}`);
    startJob(dockerUrl, nomadUrl, appName, appVersion);
}