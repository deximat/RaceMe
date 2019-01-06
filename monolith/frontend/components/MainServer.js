

let MainServer = {
    address : "139.162.179.179",
    port : 5566,
    fetch : function (path, data, callback) {
        console.log("Request path: " + path);
        fetch('http://' + this.address + ":" + this.port + "/" + path, {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        }).then((response) => response.json())
            .then(callback)
            .catch((error) => console.log("ERROR:" + error));
    }
};

export default MainServer;