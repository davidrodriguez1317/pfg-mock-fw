let dockerUrlStatus = { key: false };
let nomadUrlStatus = { key: false };
let runningJobsPollingTime = 10;
let currentNomadJob = "";
let currentNomadTag = "";
let envsMap = new Map();

document.addEventListener('DOMContentLoaded', function() {
    axios.get('/configuration')
        .then(function(response) {
            runningJobsPollingTime = response.data.runningJobsPollingTime;
            console.log("runningJobsPollingTime: " + runningJobsPollingTime)

            setInterval(listNomadRunningJobs, runningJobsPollingTime * 1000);
        })
        .catch(function(error) {
            console.error('Error getting global variables:', error);
        });
});

async function getRequest(path) {
    console.log("GET request to: " + path);

    try {
        const response = await axios.get(path);
        const status = response.status;
        console.log("Received status: " + status);
        return response.data;
    } catch (error) {
        printError(error);
        throw error;
    }
}

async function postRequest(path, body) {
    const consoleString = "POST request to: "
        .concat(path)
        .concat(". Data: ")
        .concat(body);

    console.log(consoleString);
    try {
        const response = await axios.post(path, JSON.stringify(body), {
            headers: {
                'Content-Type': 'application/json'
            }
        });

        console.log("Received status: " + response.status);
        return response.data;
    } catch (error) {
        printError(error);
        printErrorOnScreen(error);
        throw error;
    }
}

async function deleteRequest(path) {
    console.log("DELETE request to: ".concat(path));
    try {
        const response = await axios.delete(path);
        console.log("Received status: " + response.status);
        return response.data;
    } catch (error) {
        printError(error);
        printErrorOnScreen(error);
        throw error;
    }
}

function printError(error) {
    const alertString = "Received status"
        .concat(error.response.status)
        .concat(". Error: ")
        .concat(error.response.data);
    console.error(alertString);
}

function printErrorOnScreen(error) {
    const alertString = "Received status"
        .concat(error.response.status)
        .concat(". Error: ")
        .concat(error.response.data);
    alert(alertString);
}
