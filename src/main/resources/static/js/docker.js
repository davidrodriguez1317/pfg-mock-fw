async function listDockerRegistryImages() {
    const dockerUrl = document.getElementById("dockerUrl").value;
    const elementList = document.getElementById('docker-registry-images');

    console.log("Getting docker images from --> " + dockerUrl);

    const path = "/docker/images?dockerUrl=".concat(dockerUrl)

    const data = await getRequest(path);

    if(data.length === 0) {
        elementList.innerHTML = "No hay repositorios disponibles en Docker"
    } else {
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
                    button.addEventListener('click', await createAddNomadJobHandler(sortedTags[j]));
                    li.appendChild(button);
                }

                elementList.appendChild(li);
            }
        }
    }
}

async function createAddNomadJobHandler(tag) {
    return async function() {
        const dockerUrl = document.getElementById("dockerUrl").value;
        const nomadUrl = document.getElementById("nomadUrl").value;
        const li = this.parentNode;
        const nameSpan = li.querySelector('span:first-child').textContent;
        await startJob(dockerUrl, nomadUrl, nameSpan, tag);
    };
}
