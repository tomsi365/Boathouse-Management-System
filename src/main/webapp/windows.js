document.getElementById('windows-button').addEventListener('click', ()=>{seeAllWindows('none')});

async function seeAllWindows(value){

    document.getElementById('text').innerHTML=originalTextEL;
    if(value!=='none'){
        let hiddenDeleteEL=document.querySelector("#windows-list [name='hidden-note']");
        hiddenDeleteEL.innerText=value;
        hiddenDeleteEL.style.color='green';
        hiddenDeleteEL.style.visibility='visible';
    }

    let response=await fetch('seeAllWindows', {
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
        let tableEL = document.querySelector("#windows-list [name='table']");
        tableEL.style.width="60%";

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Windows';
        document.getElementById('windows-list').style.display='inline';
        document.querySelector("#windows-list [name='add']").addEventListener('click', addWindow);

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
            let window=data[i];
            buttonEL.addEventListener('click', () => {editWindow(window)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            subelement=document.createElement('td');
            buttonEL=document.createElement('button');
            buttonEL.className='red-small-button';
            buttonEL.innerText='delete';
            buttonEL.value=data[i][0];
            buttonEL.addEventListener('click', () => {deleteWindow(buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }
    }

}

function addWindow(){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Add a Window';
    document.getElementById('window-form').style.display='inline';
    document.querySelector("#window-form [name='submit']").addEventListener('click',validateAddWindowForm);
}

async function validateAddWindowForm(){
    let hiddenFeedbackEL=document.querySelector("#window-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let nameEL=document.querySelector("#window-form [name='name']");
    let isSeaBoat=document.querySelector("#window-form [name='is-seaBoat']").checked;

    let time;
    if(document.querySelector("#window-form [name='time'][value='EIGHT_TEN_PM']").checked){
        time='EIGHT_TEN_PM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TEN_TWELVE_PM']").checked){
        time='TEN_TWELVE_PM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TWELVE_TWO_AM']").checked){
        time='TWELVE_TWO_AM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TWO_FOUR_AM']").checked){
        time='TWO_FOUR_AM';
    }
    else if(document.querySelector("#window-form [name='time'][value='FOUR_SIX_AM']").checked){
        time='FOUR_SIX_AM';
    }
    else{
        time='SIX_EIGHT_AM';
    }

    const Window = {
        name: nameEL.value,
        time: time,
        isSeaBoat: isSeaBoat
    }

    if(flag===true){
        let response=await fetch('addWindow', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(Window),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='added'){
                await seeAllWindows('Added !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function deleteWindow(value) {
    let hiddenDeleteEL=document.querySelector("#windows-list [name='hidden-note']");

    let response = await fetch('deleteWindow', {
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
            await seeAllWindows('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

function editWindow(window) {

    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Edit a Window';
    document.getElementById('window-form').style.display='inline';
    let nameEL=document.querySelector("#window-form [name='name']");
    nameEL.value=window[0];

    let isSeaBoatEL=document.querySelector("#window-form [name='is-seaBoat']");
    if(window[3]==='true'){
        isSeaBoatEL.checked=true;
    }
    else{
        isSeaBoatEL.checked=false;
    }

    let begin=window[1];
    let time;
    if(begin==='08')
        time='EIGHT_TEN_PM';
    else if(begin==='10')
        time='TEN_TWELVE_PM';
    else if(begin==='12')
        time='TWELVE_TWO_AM';
    else if(begin==='14')
        time='TWO_FOUR_AM';
    else if(begin==='16')
        time='FOUR_SIX_AM';
    else
        time='SIX_EIGHT_AM';
    document.querySelector("#window-form [name='time'][value="+time+"]").checked=true;

    document.querySelector("#window-form [name='submit']").addEventListener('click',()=>{validateEditWindowForm()});
}

async function validateEditWindowForm() {
    let hiddenFeedbackEL=document.querySelector("#window-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';
    let flag=true;

    let nameEL=document.querySelector("#window-form [name='name']");
    let isSeaBoat=document.querySelector("#window-form [name='is-seaBoat']").checked;

    let time;
    if(document.querySelector("#window-form [name='time'][value='EIGHT_TEN_PM']").checked){
        time='EIGHT_TEN_PM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TEN_TWELVE_PM']").checked){
        time='TEN_TWELVE_PM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TWELVE_TWO_AM']").checked){
        time='TWELVE_TWO_AM';
    }
    else if(document.querySelector("#window-form [name='time'][value='TWO_FOUR_AM']").checked){
        time='TWO_FOUR_AM';
    }
    else if(document.querySelector("#window-form [name='time'][value='FOUR_SIX_AM']").checked){
        time='FOUR_SIX_AM';
    }
    else{
        time='SIX_EIGHT_AM';
    }

    const Window = {
        name: nameEL.value,
        time: time,
        isSeaBoat: isSeaBoat
    }

    if(flag===true){
        let response=await fetch('editWindow', {
            method:'post',
            headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
            body:JSON.stringify(Window),
            redirect:"follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='edited'){
                await seeAllWindows('Edited !');
            }
            else{
                hiddenFeedbackEL.innerText=data;
                hiddenFeedbackEL.style.visibility='visible';
            }
        }
    }
}

async function addSelectWindow(type,date){

    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#add-windows-choice-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeSecureAllWindows', {
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
        let tableEL = document.querySelector("#add-windows-choice-list [name='table']");
        tableEL.style.width="30%";

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Select a Window';
        document.getElementById('add-windows-choice-list').style.display='inline';

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
            buttonEL.addEventListener('click', ()=>{addSelectMember(type,date,buttonEL.value)});
            subelement.style.wordBreak= 'normal';
            subelement.append(buttonEL);
            element.append(subelement);

            tableEL.append(element);
        }

        document.querySelector("#add-windows-choice-list [name='back']").addEventListener('click',()=>addReg(type,date));
    }
}

async function editSelectWindow(reg,date){

    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#edit-windows-choice-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeSecureAllWindows', {
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
        let tableEL = document.querySelector("#edit-windows-choice-list [name='table']");
        tableEL.style.width="30%";

        document.getElementById('sub-title').style.display='inline';
        document.getElementById('sub-title').innerText='Select a Window';
        document.getElementById('edit-windows-choice-list').style.display='inline';

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
            buttonEL.addEventListener('click', async ()=>{
                const Reg = {
                    id: reg[0],
                    window: buttonEL.value,
                    date: date,
                }

                let response=await fetch('editReg', {
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
                    if(data==='edited'){
                        await seeAllRegs('Edited !');
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
        document.querySelector("#edit-windows-choice-list [name='back']").addEventListener('click',()=>editReg(reg));
    }
}