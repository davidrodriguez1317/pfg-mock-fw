async function listDockerRegistryImages() {
    const dockerUrl = document.getElementById("dockerUrl").value;
    const elementList = document.getElementById('docker-registry-images');

    console.log("Getting docker images from --> " + dockerUrl);

    const path = "/docker/images?dockerUrl=".concat(dockerUrl)

    const data = await getRequest(path);

    if (data.length === 0) {
        elementList.innerHTML = "<div class='alert alert-info'>No hay repositorios disponibles en Docker</div>";
    } else {
        elementList.innerHTML = "";

        for (const i in data) {
            if (data.hasOwnProperty(i)) {
                const repository = data[i];
                const li = document.createElement('li');
                li.classList.add('list-group-item', 'justify-content-between', 'align-items-center');

                const sortedTags = repository.tags.sort();

                const nameSpan = document.createElement('span');
                nameSpan.classList.add('fw-bold');
                nameSpan.appendChild(document.createTextNode(repository.name));
                li.appendChild(nameSpan);

                const tagContainer = document.createElement('span');
                li.appendChild(tagContainer);

                for (let j = 0; j < sortedTags.length; j++) {
                    const span = document.createElement('span');
                    span.classList.add('badge', 'bg-primary', 'me-2');
                    span.appendChild(document.createTextNode(sortedTags[j]));
                    tagContainer.appendChild(span);

                    const button = document.createElement('button');
                    button.type = "button";
                    button.classList.add('btn', 'btn-success', 'ms-2');
                    button.appendChild(document.createTextNode('Añadir'));
                    button.addEventListener('click', await setNomadVarsAndGetEnvs(sortedTags[j]));
                    tagContainer.appendChild(button);
                }

                elementList.appendChild(li);
            }
        }
    }
}

async function setNomadVarsAndGetEnvs(tag) {
    return async function() {
        const li = this.parentNode;
        currentNomadJob = li.querySelector('span:first-child').textContent;
        currentNomadTag = tag;
        await getEnvs();
    };
}
