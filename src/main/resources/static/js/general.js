let dockerUrlStatus = { key: false };
let nomadUrlStatus = { key: false };
let runningJobsPollingTime = 10;

document.addEventListener('DOMContentLoaded', function() {
    axios.get('/configuration')
        .then(function(response) {

            runningJobsPollingTime = response.data.runningJobsPollingTime;
            console.log("runningJobsPollingTime: " + runningJobsPollingTime)

            setInterval(listNomadRunningJobs, runningJobsPollingTime * 1000);

        })
        .catch(function(error) {
            console.error('Error al obtener el objeto de variables globales:', error);
        });
});