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


  let resFromCurrent = await request("https://cors-anywhere.herokuapp.com/https://smart-parking-iotprogramming.herokuapp.com/api/current");

  let resFromUsers = await request("https://cors-anywhere.herokuapp.com/https://smart-parking-iotprogramming.herokuapp.com/api/users");

  let resFromOverview = await request("https://cors-anywhere.herokuapp.com/https://smart-parking-iotprogramming.herokuapp.com/api/overview");

  
  let currentData = processCurrent(resFromCurrent["parking_spot"]);
  let usersData = processUsers(resFromUsers["users"]);
  let overviewData = processOverview(resFromOverview["areas"][0]["stats"]);

  renderMetaData(resFromCurrent);

  drawDoughnutChart("chartParkingStatus", "parkingSpinner",   ["Available", "Occupied"], [currentData["available"], currentData["occupied"]]);
  drawDoughnutChart("chartBookingStatus", "bookingSpinner", ["Booked", "Unbooked"], [currentData["booked"], currentData["unbooked"]] );


  drawBarChart("chartUsersPerDate", "usersSpinner", usersData["dates"], "users", usersData["usersPerDate"], "red");
  drawBarChart("chartRevenuePerDate", "revenueSpinner", usersData["dates"], "revenue", usersData["fees"], "purple");


  drawLineChart("chartParkingOverview", "overviewSpinner", overviewData["labels"], overviewData["dataset"]);
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
function drawDoughnutChart(canvasId, spinnerId, labels, data){

  let ctx = document.getElementById(canvasId).getContext("2d");
  let spinner = document.getElementById(spinnerId);
  spinner.style.display = 'none';

  const myChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        data: data,
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


//Draw bar chart
function drawBarChart(chartId, spinnerId, labels, title, dataSet, color){
  let ctx = document.getElementById(chartId).getContext("2d");
  let spinner = document.getElementById(spinnerId);
  spinner.style.display = 'none';

  const myChart = new Chart(ctx, {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: title,
        fill: true,
        backgroundColor: colors[color].fill,
        borderColor: colors[color].stroke,
        data: dataSet,
      },
     ]
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

//Draw line chart
function drawLineChart(chartId, spinnerId, labels, datasets){
  let ctx = document.getElementById(chartId).getContext("2d");

  let spinner = document.getElementById(spinnerId);
  spinner.style.display = 'none';

  const myChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: datasets
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



function renderMetaData(current){
  let title = document.getElementById("title").innerHTML = current.title;
  let unit = document.getElementById("unit").innerHTML = current["unit-code"] + " - " + current["unit-title"] + " - " + current["university"];
  let members = document.getElementById("members").innerHTML = current["members"][1] + ", " + current["members"][2] + " and " + current["members"][0];
}


function processCurrent(current){
  let available = 0;
  let occupied = 0;
  let booked =  0;
  let unbooked = 0;
   

  for (let i = 0; i < current.length; i++){

      switch(current[i].parking_status){
          case "Available" : available++;
          break;
          case "Occupied" : occupied++;
          break;
      }

      if(current[i].is_booked)
        booked++; 
      else
        unbooked++;
  }

  let chartData = {
    available: available,
    occupied: occupied,
    booked: booked,
    unbooked: unbooked,
  }

  return chartData;
}

function processUsers(users){

  let meanOfFee = 0;
  let medianOfFee = 0;
  let arrOfFees = [];
  let labels = [];
  let meanArrOffee = [];
  let medianArrOffee  = [];
  let arrOfDates = [];
  let arrOfDateOccurences = [];
  let tempDate;
  let prevDate;
  let count = 0;
  var tempIndex = 0;
  let feePerDate = 0;

  console.log(users);

  users.sort(function(a,b){
    var c = new Date(a.ts_payment);
    var d = new Date(b.ts_payment);
    return c-d;
  });
  
  for (let i = 0; i < users.length; i++){

    meanOfFee += users[i].user_fee;
    tempDate = users[i].ts_payment.split(" ");
    if (!arrOfDates.includes(tempDate[0]))
        arrOfDates.push(tempDate[0]);

    if (prevDate != tempDate[0]){
        if (count != 0)
          tempIndex++;
                     
        count = 1;
        feePerDate = users[i].user_fee;
      
        if (count == 1){
          arrOfDateOccurences[tempIndex] = count;  
          arrOfFees[tempIndex] = feePerDate;
        }
        
        prevDate = tempDate[0];
      
    }else {
        count++;
        feePerDate += users[i].user_fee;

        arrOfDateOccurences[tempIndex] = count;  
        arrOfFees[tempIndex] = feePerDate;
    }
     
    labels.push(tempDate[0]);
  }

 
   meanOfFee /= users.length;
  //  medianOfFee = getMedian(arrOfFees); 
  
   for (let i = 0; i < arrOfFees.length; i++){
      meanArrOffee.push(meanOfFee);
      medianArrOffee.push(medianOfFee);
   }



   console.log(arrOfFees);

  let chartData = {
    fees: arrOfFees,
    mean: meanArrOffee,
    median: medianArrOffee,
    labels: labels,
    dates: arrOfDates,
    usersPerDate: arrOfDateOccurences
  }

  arrOfFees = [];
  meanArrOffee = [];

  return chartData;

}


function getMedian(values){

  if(values.length ===0) return 0;

	values.sort(function(a,b){
  	return a-b;
  });
  var half = Math.floor(values.length / 2);
  
  if (values.length % 2)
  	return values[half];
  else
  	return (values[half - 1] + values[half]) / 2.0;
}


function processOverview(stats){
  
  let labels = [];
  let arrOfAvailable = [];
  let arrOfOccupied = [];
  let arrOfBooked = [];
  let arrOfUnbooked = [];
  
  for (let i = 0; i < stats.length; i++){
     labels.push(stats[i]["ts"]);
     arrOfAvailable.push(stats[i]["available"]);
     arrOfOccupied.push(stats[i]["occupied"]);
     arrOfBooked.push(stats[i]["booked"]);
     arrOfUnbooked.push(stats[i]["not booked"]);
  }





 let chartData = {

    labels: labels,
    dataset: [
      {
       label: "Available",
       fill: false,
       backgroundColor: colors["red"].fill,
       borderColor: colors["red"].stroke,
       data: arrOfAvailable,
     },
     {
       label: "Occupied",
       fill: false,
       backgroundColor: colors["blue"].fill,
       borderColor: colors["blue"].stroke,
       data: arrOfOccupied,
     },{
       label: "Booked",
       fill: false,
       backgroundColor: colors["green"].fill,
       borderColor: colors["green"].stroke,
       data: arrOfBooked,
     }, 
     {
      label: "Not Booked",
      fill: false,
      backgroundColor: colors["purple"].fill,
      borderColor: colors["purple"].stroke,
      data: arrOfUnbooked,
    }]

  }


  return chartData;
}



function generateRandomColor(){
   const randomColor = Math.floor(Math.random()*16777215).toString(16);
   return "#" + randomColor;
}


//Every 2 mins, the charts refreshes
setInterval(()=>{
  draw();
}, 1000 * 60 * 2);