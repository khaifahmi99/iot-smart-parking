// AJAX request
function request(route) {

    return new Promise((resolve, reject) => {
  
      var xhttp = new XMLHttpRequest();

      xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            resolve(JSON.parse(this.responseText));
        }
      };
      
      xhttp.open("GET", route, true);
     


      xhttp.send();
  
  
    });
  
}


//Draw all charts
async function draw(){


  let data = await request("https://cors-anywhere.herokuapp.com/https://smart-parking-iotprogramming.herokuapp.com/api");


  let available = 0;
  let occupied = 0;
  let booked =  0;
  let notBooked = 0;
   

  for (let i = 0; i < data.parking_spot.length; i++){

      switch(data.parking_spot[i].parking_status){
          case "Available" : available++;
          break;
          case "Occupied" : occupied++;
          break;
      }

      if(data.parking_spot[i].is_booked)
        booked++; 
      else
        notBooked++;
  }

  renderMetaData(data);
  console.log(data);

  drawPieChart("parkingStatus", available, occupied, ["Available", "Occupied"]);
  drawPieChart("booking", booked, notBooked, ["Booked", "Not Booked"]);

}

draw();

//Colors for the charts
const colors = {
  blue: {
    fill: '#d8f9ff',
    stroke: '#00d8ff',
  },
  purple: {
    fill: '#8fa8c8',
    stroke: '#75539e',
  },
  green: {
    fill: '#bafcec',
    stroke: '#0ad3a1',
  },
  red: {
    fill: '#ffc9c9',
    stroke: '#ff2d2d',
  },
  orange: {
    fill: '#fce7b3',
    stroke: '#f4af00',
  },
};


//Draw pie chart
function drawPieChart(canvasId, dataSetOne, dataSetTwo, labels){

  let ctx = document.getElementById(canvasId).getContext("2d");

  const myChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        data: [dataSetOne, dataSetTwo],
        backgroundColor: [colors["green"].fill, colors["orange"].fill]
      }]
    },
    options: {
      responsive: true,
      // Can't just just `stacked: true` like the docs say
       animation: {
        duration: 750,
      },
    }
  });




}



function renderMetaData(data){
  let title = document.getElementById("title").innerHTML = data.title;
  let unit = document.getElementById("unit").innerHTML = data["unit-code"] + " - " + data["unit-title"] + " - " + data["university"];
  let members = document.getElementById("members").innerHTML = data["members"][1] + ", " + data["members"][2] + " and " + data["members"][0];
}

