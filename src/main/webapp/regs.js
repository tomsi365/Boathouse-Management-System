document.getElementById('registrations-button').addEventListener('click', ()=>{seeAllRegs('none')});

async function seeAllRegs(value){

    document.getElementById('text').innerHTML=originalTextEL;
    if(value!=='none'){
        let hiddenDeleteEL=document.querySelector("#regs-list [name='hidden-note']");
        hiddenDeleteEL.innerText=value;
        hiddenDeleteEL.style.color='green';
        hiddenDeleteEL.style.visibility='visible';
    }

    let response=await fetch('seeAllRegs', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let response1 = await fetch('status', {
            method: 'post',
            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
            redirect: "follow",
        });

        if (response1.redirected === true) {
            window.location.href = response1.url;
        }
        else {
            let subelement;
            let tableEL = document.querySelector("#regs-list [name='table']");
            let firstRowEL = document.querySelector("#regs-list [name='first-row']");
            tableEL.style.fontSize="16px";

            document.getElementById('sub-title').style.display='inline';
            document.getElementById('sub-title').innerText='Registrations [unconfirmed]';
            document.getElementById('regs-list').style.display='inline';
            document.querySelector("#regs-list [name='add']").addEventListener('click', ()=>{addReg(null,null)});

            let data1=await response1.json();

            let header=document.createElement('th');
            header.innerText='friends';
            firstRowEL.append(header);

            let header2=document.createElement('th');
            header2.innerText='types';
            firstRowEL.append(header2);

            if (data1 === 'manager') {
                document.querySelector("#regs-list [name='unit']").style.visibility='visible';
                document.querySelector("#regs-list [name='unit']").addEventListener('click',unitRegs);
                document.querySelector("#regs-list [name='hidden-description']").style.visibility='visible';
                document.querySelector("#regs-list [name='hidden-description']").style.color='black';
                tableEL.style.width="98%";

                let header1=document.createElement('th');
                header1.innerText='duplicate';
                firstRowEL.append(header1);

                let header3=document.createElement('th');
                header3.innerText='place';
                firstRowEL.append(header3);

                let header4=document.createElement('th');
                header4.innerText='auto';
                firstRowEL.append(header4);
            }

            let i;
            for (i = 0; i < data.length; i++) {

                let flag=true;
                let element;
                element=document.createElement('tr');
                let value=data[i][0];
                element.setAttribute('value',value);
                element.addEventListener('click',()=>
                {
                    if(flag){
                        element.style.background='darkblue';
                        element.style.color='white';
                        element.setAttribute('name','marked');
                        flag=false;
                    }
                    else{
                        element.style.background='lightblue';
                        element.style.color='black';
                        element.setAttribute('name','unmarked');
                        flag=true;
                    }
                });
                element.addEventListener('mouseover',()=>
                {
                    element.style.background='blue';
                    element.style.color='white';
                });
                element.addEventListener('mouseout',()=>
                {
                    if(flag){
                        element.style.background='lightblue';
                        element.style.color='black';
                    }
                    else{
                        element.style.background='darkblue';
                        element.style.color='white';
                    }
                });

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
                buttonEL.style.fontSize="16px";
                buttonEL.innerText='edit';
                buttonEL.value=data[i][0];
                let reg=data[i];
                buttonEL.addEventListener('click', () => {editReg(reg)});
                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                subelement=document.createElement('td');
                buttonEL=document.createElement('button');
                buttonEL.className='red-small-button';
                buttonEL.style.fontSize="16px";
                buttonEL.innerText='delete';
                buttonEL.value=data[i][0];
                buttonEL.addEventListener('click', () => {deleteReg(buttonEL.value)});
                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                subelement=document.createElement('td');
                buttonEL=document.createElement('button');
                buttonEL.className='grey-small-button';
                buttonEL.style.fontSize="16px";
                buttonEL.innerText='friends';
                buttonEL.value=data[i][0];
                let reg3=data[i][0];
                buttonEL.addEventListener('click', () => {seeAllFriends(reg3)});
                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                subelement=document.createElement('td');
                buttonEL=document.createElement('button');
                buttonEL.className='grey-small-button';
                buttonEL.style.fontSize="16px";
                buttonEL.innerText='types';
                buttonEL.value=data[i][0];
                let reg1=data[i][0];
                buttonEL.addEventListener('click', () => {seeAllTypes(reg1)});
                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                if (data1 === 'manager') {

                    subelement=document.createElement('td');
                    buttonEL=document.createElement('button');
                    buttonEL.className='grey-small-button';
                    buttonEL.style.fontSize="16px";
                    buttonEL.innerText='duplicate';
                    buttonEL.value=data[i][0];
                    let reg2=data[i];
                    buttonEL.addEventListener('click', () => {duplicateReg(reg2)});
                    subelement.style.wordBreak= 'normal';
                    subelement.append(buttonEL);
                    element.append(subelement);

                    subelement=document.createElement('td');
                    buttonEL=document.createElement('button');
                    buttonEL.className='green-small-button';
                    buttonEL.style.fontSize="16px";
                    buttonEL.innerText='place';
                    buttonEL.value=data[i][0];
                    buttonEL.addEventListener('click', () => {selectMatchedBoat(buttonEL.value)});
                    subelement.style.wordBreak= 'normal';
                    subelement.append(buttonEL);
                    element.append(subelement);

                    subelement=document.createElement('td');
                    buttonEL=document.createElement('button');
                    buttonEL.className='darkgreen-small-button';
                    buttonEL.style.fontSize="16px";
                    buttonEL.innerText='auto';
                    buttonEL.value=data[i][0];
                    buttonEL.addEventListener('click', () => {autoReg(buttonEL.value)});
                    subelement.style.wordBreak= 'normal';
                    subelement.append(buttonEL);
                    element.append(subelement);

                }

                tableEL.append(element);
            }
        }
    }
}

function addReg(type,date){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Add a Registration';
    document.getElementById('add-reg-form').style.display='inline';

    if(!(type===null||date===null)){
        document.querySelector("#add-reg-form [name='date']").value=date;
        document.querySelector("#add-reg-form [name='type'][value="+type+"]").checked=true;
    }

    document.querySelector("#add-reg-form [name='next']").addEventListener('click',validateAddRegForm);
}

async function validateAddRegForm(){

    let date=document.querySelector("#add-reg-form [name='date']").value;
    let type;
    if(document.querySelector("#add-reg-form [name='type'][value='SINGLE']").checked){
        type='SINGLE';
    }
    else if(document.querySelector("#add-reg-form [name='type'][value='DOUBLE']").checked){
        type='DOUBLE';
    }
    else if(document.querySelector("#add-reg-form [name='type'][value='DOUBLE_ONE_PADDLE']").checked){
        type='DOUBLE_ONE_PADDLE';
    }
    else if(document.querySelector("#add-reg-form [name='type'][value='QUARTET']").checked){
        type='QUARTET';
    }
    else if(document.querySelector("#add-reg-form [name='type'][value='QUARTET_ONE_PADDLE']").checked){
        type='QUARTET_ONE_PADDLE';
    }
    else if(document.querySelector("#add-reg-form [name='type'][value='EIGHT']").checked){
        type='EIGHT';
    }
    else {
        type='EIGHT_ONE_PADDLE';
    }

    await addSelectWindow(type, date);
}

async function deleteReg(value) {
    let hiddenDeleteEL=document.querySelector("#regs-list [name='hidden-note']");

    let response = await fetch('deleteReg', {
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
            await seeAllRegs('Deleted !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

function editReg(reg){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Edit a Registration';
    document.getElementById('edit-reg-form').style.display='inline';

    document.querySelector("#edit-reg-form [name='date']").value=reg[4]+'-'+reg[3]+'-'+reg[2];

    document.querySelector("#edit-reg-form [name='next']").addEventListener('click',()=>{validateEditRegForm(reg)});
}

async function validateEditRegForm(reg){
    let date=document.querySelector("#edit-reg-form [name='date']").value;
    await editSelectWindow(reg, date);
}

function duplicateReg(reg){
    document.getElementById('text').innerHTML=originalTextEL;
    document.getElementById('sub-title').style.display='inline';
    document.getElementById('sub-title').innerText='Duplicate a Registration';
    document.getElementById('duplicate-reg-form').style.display='inline';

    document.querySelector("#duplicate-reg-form [name='date']").value=reg[4]+'-'+reg[3]+'-'+reg[2];

    document.querySelector("#duplicate-reg-form [name='duplicate']").addEventListener('click',()=>{validateDuplicateRegForm(reg)});
}

async function validateDuplicateRegForm(reg){
    let hiddenFeedbackEL=document.querySelector("#duplicate-reg-form [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let date=document.querySelector("#duplicate-reg-form [name='date']").value;

    const Reg = {
        id: reg[0],
        date: date,
    }

    let response=await fetch('duplicateReg', {
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
        if(data==='duplicated'){
            await seeAllRegs('Duplicated !');
        }
        else{
            hiddenFeedbackEL.innerText=data;
            hiddenFeedbackEL.style.visibility='visible';
        }
    }
}

async function seeAllFriends(reg) {
    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#friends-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeSecureAllMembers', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let response1 = await fetch('seeAllFriends', {
            method: 'post',
            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
            body: JSON.stringify(reg),
            redirect: "follow",
        });

        if (response1.redirected === true) {
            window.location.href = response1.url;
        }
        else {
            let element;
            let subelement;
            let tableEL = document.querySelector("#friends-list [name='table']");
            tableEL.style.width="60%";

            document.getElementById('sub-title').style.display='inline';
            document.getElementById('sub-title').innerText='Manage Friends';
            document.getElementById('friends-list').style.display='inline';

            let data1=await response1.json();

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

                let flag=false;
                let k;
                for (k = 0; k < data1.length; k++) {
                    if(data1[k][0]===data[i][0])
                        flag=true;
                }

                if(flag){
                    buttonEL.className='red-small-button';
                    buttonEL.innerText='remove';
                    let member=data[i];
                    buttonEL.addEventListener('click', async () => {
                        const Friend = {
                            id: reg,
                            friend: member[0],
                        }

                        let response2 = await fetch('removeFriend', {
                            method: 'post',
                            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
                            body: JSON.stringify(Friend),
                            redirect: "follow",
                        });

                        if(response2.redirected===true){
                            window.location.href=response2.url;
                        }
                        else {
                            let data2=await response2.json();
                            if(data2==='removed'){
                                await seeAllFriends(reg);
                            }
                            else{
                                hiddenFeedbackEL.innerText=data2;
                                hiddenFeedbackEL.style.visibility='visible';
                            }
                        }

                    });
                }
                else{
                    buttonEL.className='green-small-button';
                    buttonEL.innerText='add';
                    let member1=data[i];
                    buttonEL.addEventListener('click', async () => {
                        const Friend = {
                            id: reg,
                            friend: member1[0],
                        }

                        let response3 = await fetch('addFriend', {
                            method: 'post',
                            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
                            body: JSON.stringify(Friend),
                            redirect: "follow",
                        });

                        if(response3.redirected===true){
                            window.location.href=response3.url;
                        }
                        else {
                            let data3=await response3.json();
                            if(data3==='added'){
                                await seeAllFriends(reg);
                            }
                            else{
                                hiddenFeedbackEL.innerText=data3;
                                hiddenFeedbackEL.style.visibility='visible';
                            }
                        }

                    });
                }

                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                tableEL.append(element);
            }
        }
    }
}

async function seeAllTypes(reg) {
    document.getElementById('text').innerHTML=originalTextEL;

    let hiddenFeedbackEL=document.querySelector("#types-list [name='hidden-feedback']");
    hiddenFeedbackEL.innerText='';
    hiddenFeedbackEL.style.visibility='hidden';

    let response=await fetch('seeAllTypes', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true){
        window.location.href=response.url;
    }
    else {
        let data = await response.json();

        let response1 = await fetch('seeAllRegsTypes', {
            method: 'post',
            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
            body: JSON.stringify(reg),
            redirect: "follow",
        });

        if (response1.redirected === true) {
            window.location.href = response1.url;
        }
        else {
            let element;
            let subelement;
            let tableEL = document.querySelector("#types-list [name='table']");
            tableEL.style.width="40%";

            document.getElementById('sub-title').style.display='inline';
            document.getElementById('sub-title').innerText='Manage Types';
            document.getElementById('types-list').style.display='inline';

            let data1=await response1.json();

            let i;
            for (i = 0; i < data.length; i++) {
                element=document.createElement('tr');

                subelement=document.createElement('td');
                subelement.innerText=data[i];
                element.append(subelement);

                let buttonEL;

                subelement=document.createElement('td');
                buttonEL=document.createElement('button');

                let flag=false;
                let k;
                for (k = 0; k < data1.length; k++) {
                    if(data1[k]===data[i])
                        flag=true;
                }

                if(flag){
                    buttonEL.className='red-small-button';
                    buttonEL.innerText='remove';
                    let type=data[i];
                    buttonEL.addEventListener('click', async () => {
                        const Type = {
                            id: reg,
                            type: type,
                        }

                        let response2 = await fetch('removeType', {
                            method: 'post',
                            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
                            body: JSON.stringify(Type),
                            redirect: "follow",
                        });

                        if(response2.redirected===true){
                            window.location.href=response2.url;
                        }
                        else {
                            let data2=await response2.json();
                            if(data2==='removed'){
                                await seeAllTypes(reg);
                            }
                            else{
                                hiddenFeedbackEL.innerText=data2;
                                hiddenFeedbackEL.style.visibility='visible';
                            }
                        }

                    });
                }
                else{
                    buttonEL.className='green-small-button';
                    buttonEL.innerText='add';
                    let type1=data[i];
                    buttonEL.addEventListener('click', async () => {
                        const Type = {
                            id: reg,
                            type: type1,
                        }

                        let response3 = await fetch('addType', {
                            method: 'post',
                            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
                            body: JSON.stringify(Type),
                            redirect: "follow",
                        });

                        if(response3.redirected===true){
                            window.location.href=response3.url;
                        }
                        else {
                            let data3=await response3.json();
                            if(data3==='added'){
                                await seeAllTypes(reg);
                            }
                            else{
                                hiddenFeedbackEL.innerText=data3;
                                hiddenFeedbackEL.style.visibility='visible';
                            }
                        }

                    });
                }

                subelement.style.wordBreak= 'normal';
                subelement.append(buttonEL);
                element.append(subelement);

                tableEL.append(element);
            }
        }
    }
}

async function autoReg(value) {
    let hiddenDeleteEL=document.querySelector("#regs-list [name='hidden-note']");

    let response = await fetch('autoPlacement', {
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
        if(data==='placed'){
            await seeAllRegs('Placed !');
        }
        else{
            hiddenDeleteEL.innerText=data;
            hiddenDeleteEL.style.color='red';
            hiddenDeleteEL.style.visibility='visible';
        }
    }
}

async function unitRegs() {
    let hiddenDeleteEL=document.querySelector("#regs-list [name='hidden-note']");
    let regs=[];
    document.querySelectorAll("#regs-list tr[name='marked']").forEach((reg)=>{regs.push(reg.getAttribute('value'))});


    if(regs.length!==2){
        hiddenDeleteEL.innerText='Must be marked , exactly 2 registrations !';
        hiddenDeleteEL.style.color='red';
        hiddenDeleteEL.style.visibility='visible';
    }
    else{
        let response = await fetch('unitRegs', {
            method: 'post',
            headers: new Headers({'Content-Type': 'application/json;charset=utf-8'}),
            body: JSON.stringify(regs),
            redirect: "follow",
        });

        if(response.redirected===true){
            window.location.href=response.url;
        }
        else {
            let data=await response.json();
            if(data==='united'){
                await seeAllRegs('United !');
            }
            else{
                hiddenDeleteEL.innerText=data;
                hiddenDeleteEL.style.color='red';
                hiddenDeleteEL.style.visibility='visible';
            }
        }
    }
}
