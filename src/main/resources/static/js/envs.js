function getEnvs(envsDiv, divToHide) {
    envsMap = new Map();
    document.getElementById(envsDiv).style.display = "block";
    document.getElementById(divToHide).style.display = "none";

}

function addKeyValuePair(kvDiv, kDiv, vDiv) {
    const key = document.getElementById(kDiv).value;
    const value = document.getElementById(vDiv).value;

    const pairElement = document.createElement("div");
    pairElement.className = "key-value-entry container-all-one-line";
    pairElement.innerHTML = `
        <input type="text" class="form-control" value="${key}" disabled>&nbsp:
        <input type="text" class="form-control" value="${value}" disabled>`;
    document.getElementById(kvDiv).appendChild(pairElement);

    document.getElementById(kDiv).value = "";
    document.getElementById(vDiv).value = "";
}

async function launchJobWithEnvs(kvDiv, envsDiv) {
    const envPairs = document.getElementById(kvDiv).children;
    collectKeyValuePairs(envsMap, envPairs);
    console.log(envsMap);
    cleanLaunchJobWithEnvs(kvDiv, envsDiv);
    await startJob(currentNomadJob, currentNomadTag, envsMap);
}

async function launchLocalJobWithEnvs(kvDiv, envsDiv) {
    const envPairs = document.getElementById(kvDiv).children;
    collectKeyValuePairs(envsMap, envPairs);
    console.log(envsMap);
    cleanLaunchJobWithEnvs(kvDiv, envsDiv);
    await startLocalJob(currentLocalNomadJob, envsMap);
}

function collectKeyValuePairs(aMap, pairs) {
    for (let i = 0; i < pairs.length; i++) {
        const keyInput = pairs[i].querySelector("input:first-child").value;
        const valueInput = pairs[i].querySelector("input:last-child").value;
        aMap.set(keyInput, valueInput);
    }
}

function cleanLaunchJobWithEnvs(kvDiv, envsDiv) {
    document.getElementById(kvDiv).innerHTML = "";
    document.getElementById(envsDiv).style.display = "none";
}
