function getEnvs() {
    envsMap = new Map();
    document.getElementById("docker-images-envs").style.display = "block";
}

function addKeyValuePair() {
    const key = document.getElementById("envs-key-input").value;
    const value = document.getElementById("envs-value-input").value;

    const pairElement = document.createElement("div");
    pairElement.className = "key-value-entry"
    pairElement.innerHTML = `<input type="text" value="${key}" disabled> : <input type="text" value="${value}" disabled>`;
    document.getElementById("envs-key-value-pairs").appendChild(pairElement);

    document.getElementById("envs-key-input").value = "";
    document.getElementById("envs-value-input").value = "";
}

async function launchJobWithEnvs() {
    const envPairs = document.getElementById("envs-key-value-pairs").children;
    collectKeyValuePairs(envsMap, envPairs);
    console.log(envsMap);
    cleanLaunchJobWithEnvs();
    await startJob(currentNomadJob, currentNomadTag, envsMap)
}

function collectKeyValuePairs(aMap, pairs) {
    for (let i = 0; i < pairs.length; i++) {
        const keyInput = pairs[i].querySelector("input:first-child").value;
        const valueInput = pairs[i].querySelector("input:last-child").value;
        aMap.set(keyInput, valueInput);
    }
}

function cleanLaunchJobWithEnvs() {
    document.getElementById("envs-key-value-pairs").innerHTML = "";
    document.getElementById("docker-images-envs").style.display = "none";
}
