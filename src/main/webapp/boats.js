document.getElementById('boats-button').addEventListener('click', ()=>{seeAllBoats('none')});

async function seeAllBoats(value){

    document.getElementById('text').innerHTML=originalTextEL;
    if(value!=='none'){
        let hiddenDeleteEL=document.querySelector("#boats-list [name='hidden-note']");
        hiddenDeleteEL.innerText=value;
        hiddenDeleteEL.style.color='green';
        hiddenDeleteEL.style.visibility='visible';
    }

    let response=await fetch('seeAllBoats', {
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
        let tableEL = document.querySelector("#boats-list [name='table']");

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Boats';
        document.getElementById('boats-list').style.display='inline';
        document.querySelector("#boats-list [name='add']").addEventListener('click', addBoat);

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='yellow-small-button';
            buttonEL.innerText='edit';
            buttonEL.value=data[i][0];
            let boat=data[i];
            buttonEL.addEventListener('click', () => {editBoat(boat)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='red-small-button';
            buttonEL.innerText='delete';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', () => {deleteBoat(buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }

}

async function selectNonOwnedBoat(member){

    document.getElementById('text').innerHTML=originalTextEL;

    let response=await fetch('seeNonOwnedBoats', {
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
        let tableEL = document.querySelector("#boats-choice-list [name='table']");

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Select Non Owned Boat';
        document.getElementById('boats-choice-list').style.display='inline';

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='green-small-button';
            buttonEL.innerText='select';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', ()=>{editMember(member,buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }

        document.querySelector("#boats-choice-list [name='none']").addEventListener('click', ()=>{editMember(member,0)});

    }
}

function addBoat(){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Add a Boat';
    document.getElementById('boat-form').style.display='inline';
    document.querySelector("#boat-form [name='submit']").addEventListener('click',validateAddBoatForm);
}

async function validateAddBoatForm(){
    let hiddenFeedbackEL=document.querySelector("#boat-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let nameEL=document.querySelector("#boat-form [name='name']");
    let isWideBoat=document.querySelector("#boat-form [name='is-wideBoat']").checked;
    let isSeaBoat=document.querySelector("#boat-form [name='is-seaBoat']").checked;
    let isCoxswain=document.querySelector("#boat-form [name='is-coxswain']").checked;
    let isDisabled=document.querySelector("#boat-form [name='is-disabled']").checked;

    let type;
    if(document.querySelector("#boat-form [name='type'][value='SINGLE']").checked){
        type='SINGLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='DOUBLE']").checked){
        type='DOUBLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='DOUBLE_ONE_PADDLE']").checked){
        type='DOUBLE_ONE_PADDLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='QUARTET']").checked){
        type='QUARTET';
    }
    else if(document.querySelector("#boat-form [name='type'][value='QUARTET_ONE_PADDLE']").checked){
        type='QUARTET_ONE_PADDLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='EIGHT']").checked){
        type='EIGHT';
    }
    else {
        type='EIGHT_ONE_PADDLE';
    }

    const Boat = {
        name: nameEL.value,
        type: type,
        isWideBoat:isWideBoat,
        isSeaBoat:isSeaBoat,
        isCoxswain:isCoxswain,
        isDisabled:isDisabled
    }

    if(flag===true){
        let response=await fetch('addBoat', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(Boat),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='added'){
                await seeAllBoats('Added !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function deleteBoat(value) {
    let hiddenDeleteEL=document.querySelector("#boats-list [name='hidden-note']");

    let response = await fetch('deleteBoat', {
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
            await seeAllBoats('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

function editBoat(boat) {

    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Edit a Boat';
    document.getElementById('boat-form').style.display='inline';
    let nameEL=document.querySelector("#boat-form [name='name']");
    nameEL.value=boat[1];
    let type=boat[2];
    document.querySelector("#boat-form [name='type'][value="+type+"]").checked=true;

    let isCoxswainEL=document.querySelector("#boat-form [name='is-coxswain']");
    if(boat[4]==='true'){
        isCoxswainEL.checked=true;
    }
    else{
        isCoxswainEL.checked=false;
    }

    let isSeaBoatEL=document.querySelector("#boat-form [name='is-seaBoat']");
    if(boat[5]==='true'){
        isSeaBoatEL.checked=true;
    }
    else{
        isSeaBoatEL.checked=false;
    }

    let isWideBoatEL=document.querySelector("#boat-form [name='is-wideBoat']");
    if(boat[6]==='true'){
        isWideBoatEL.checked=true;
    }
    else{
        isWideBoatEL.checked=false;
    }

    let isDisabledEL=document.querySelector("#boat-form [name='is-disabled']");
    if(boat[7]==='true'){
        isDisabledEL.checked=true;
    }
    else{
        isDisabledEL.checked=false;
    }

    document.querySelector("#boat-form [name='submit']").addEventListener('click',()=>{validateEditBoatForm(boat)});
}

async function validateEditBoatForm(boat) {
    let hiddenFeedbackEL=document.querySelector("#boat-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let nameEL=document.querySelector("#boat-form [name='name']");
    let isWideBoat=document.querySelector("#boat-form [name='is-wideBoat']").checked;
    let isSeaBoat=document.querySelector("#boat-form [name='is-seaBoat']").checked;
    let isCoxswain=document.querySelector("#boat-form [name='is-coxswain']").checked;
    let isDisabled=document.querySelector("#boat-form [name='is-disabled']").checked;

    let type;
    if(document.querySelector("#boat-form [name='type'][value='SINGLE']").checked){
        type='SINGLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='DOUBLE']").checked){
        type='DOUBLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='DOUBLE_ONE_PADDLE']").checked){
        type='DOUBLE_ONE_PADDLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='QUARTET']").checked){
        type='QUARTET';
    }
    else if(document.querySelector("#boat-form [name='type'][value='QUARTET_ONE_PADDLE']").checked){
        type='QUARTET_ONE_PADDLE';
    }
    else if(document.querySelector("#boat-form [name='type'][value='EIGHT']").checked){
        type='EIGHT';
    }
    else {
        type='EIGHT_ONE_PADDLE';
    }

    const Boat = {

        serialNumber: boat[0],
        name: nameEL.value,
        type: type,
        isWideBoat:isWideBoat,
        isSeaBoat:isSeaBoat,
        isCoxswain:isCoxswain,
        isDisabled:isDisabled
    }

    if(flag===true){
        let response=await fetch('editBoat', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(Boat),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='edited'){
                await seeAllBoats('Edited !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function selectMatchedBoat(reg){
    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#place-boats-choice-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeAllMatchedBoats', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        body:JSON.stringify(reg),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let element;
        let subelement;
        let tableEL = document.querySelector("#place-boats-choice-list [name='table']");

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Select Matched Boat';
        document.getElementById('place-boats-choice-list').style.display='inline';

        let i;
        for (i = 0; i < data.length; i++) {
            element=document.createElement('tr');
            let j;
            for (j = 0; j < data[i].length; j++) {
                subelement=document.createElement('td');
                subelement.innerText=data[i][j];
                element.append(subelement);
            }
            let buttonEL;

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='green-small-button';
            buttonEL.innerText='select';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', async()=>{

                const Reg = {
                    reg: reg,
                    boat: buttonEL.value,
                }

                let response=await fetch('placeReg', {
                    method:'post',
                    headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
                    body:JSON.stringify(Reg),
                    redirect:"follow",
                });

                if(response.redirected===true){
                    window.location.href=response.url;
                }
                else {
                    let data=await response.json();
                    if(data==='placed'){
                        await seeAllRegs('Placed !');
                    }
                    else{
                        hiddenFeedbackEL.innerText=data;
                        hiddenFeedbackEL.style.visibility='visible';
                    }
                }
            });
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }
}

