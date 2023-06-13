async function checkUrl(idFieldUrl, idCheck, urlStatusValue) {
    const url = document.getElementById(idFieldUrl).value;

    const path = "/status/server?serverUrl=".concat(url)
    const data = await getRequest(path);

    const resultDiv = document.getElementById(idCheck);
    if (data) {
        resultDiv.innerHTML = '<h3>&#x2705;</h3>';
        urlStatusValue.key = true;
    } else {
        resultDiv.innerHTML = '<h3>&#x274C;</h3>';
        urlStatusValue.key = false;
    }
}