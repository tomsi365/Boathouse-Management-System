document.getElementById('alerts-button').addEventListener('click', ()=>{seeAllManagerAlerts('none')});
document.getElementById('notification-button').addEventListener('click', seeAllCombinedAlerts);

async function seeAllManagerAlerts(value){

    document.getElementById('text').innerHTML=originalTextEL;
    if(value!=='none'){
        let hiddenDeleteEL=document.querySelector("#alerts-list [name='hidden-note']");
        hiddenDeleteEL.innerText=value;
        hiddenDeleteEL.style.color='green';
        hiddenDeleteEL.style.visibility='visible';
    }

    let response=await fetch('seeAllManagerNotifications', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#alerts-list [name='table']");
        tableEL.style.width="65%";

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Manage Notifications';
        document.getElementById('alerts-list').style.display='inline';
        document.querySelector("#alerts-list [name='add']").addEventListener('click', addAlert);

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length-2; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='red-small-button';
            buttonEL.innerText='delete';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', () => {deleteAlert(buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }
}

function addAlert(){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Add a Notification';
    document.getElementById('alert-form').style.display='inline';
    document.querySelector("#alert-form [name='submit']").addEventListener('click',validateAddAlertForm);
}

async function validateAddAlertForm(){
    let hiddenFeedbackEL=document.querySelector("#alert-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let notification=document.querySelector("#alert-form [name='notes']").value;

    if(flag===true){
        let response=await fetch('addNotification', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(notification),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='added'){
                await seeAllManagerAlerts('Added !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function deleteAlert(value) {
    let hiddenDeleteEL=document.querySelector("#alerts-list [name='hidden-note']");

    let response = await fetch('deleteNotification', {
        method: 'post',
        headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
        body: JSON.stringify(value),
        redirect: "follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data=await response.json();
        if(data==='deleted'){
            await seeAllManagerAlerts('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

async function seeAllCombinedAlerts() {
    document.getElementById('text').innerHTML=originalTextEL;

    let response=await fetch('seeAllCombinedNotifications', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#personal-alerts-list [name='table']");

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Notifications';
        document.getElementById('personal-alerts-list').style.display='inline';
        document.querySelector("#personal-alerts-list [name='refresh']").addEventListener('click', seeAllCombinedAlerts);

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                if(data[i][4]==='false'){
                    subelement.style.fontWeight='bold';
                }
                subelement.innerText=data[i][j];
                element.append(subelement);
            }

            tableEL.append(element);
        }
    }
}

async function checkAlerts() {
    let response = await fetch('isThereNewNotifications', {
        method: 'post',
        headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
        redirect: "follow",
    });
    if (response.redirected === true) {
        window.location.href = response.url;
    } else {
        let data = await response.json();
        if (data === 'true') {
            document.getElementById('notification-img').src = 'img2.png';
        }
        else{
            document.getElementById('notification-img').src = 'img1.png';
        }
    }


}

checkAlerts();
setInterval(checkAlerts, 60000);
