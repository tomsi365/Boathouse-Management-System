document.getElementById('login').addEventListener('click', login);
window.addEventListener('load', isConnected);

async function isConnected() {
    let response=await fetch('status', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        redirect:"follow",
    });

    if(response.redirected===true) {
        window.location.href = response.url;
    }
    else{
        let data=await response.json();
        if(data==='user'||data==='manager'){
            window.location.href='menu.html';
        }
    }
}

async function login(){

    let emailEL=document.getElementById('email');
    let passowrdEL=document.getElementById('password');

    const Connection = {
        email:emailEL.value,
        password:passowrdEL.value
    }

    let response=await fetch('login', {
        method:'post',
        headers:new Headers({'Content-Type':'application/json;charset=utf-8'}),
        body:JSON.stringify(Connection),
        redirect:"follow",
    });
    let data=await response.json();

    if(data==='connected'){
        window.location.href='error.html';
    }
    else if(data==='menu'){
        window.location.href='menu.html';
    }
    else{
        document.getElementById('hidden-form-field').innerText=data;
        document.getElementById('hidden-form-field').style.visibility='visible';
    }

}
