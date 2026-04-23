
import React from  'react';
import { useState } from 'react';
export default function UserForm({addFunc}){

    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [passwd, setPasswd] = useState('');


   function handleSubmit (event){

        let user={id:username,
            name:name,
            passwd:passwd
        }
        console.log('A user was submitted: ');
        console.log(user);
         addFunc(user);
        event.preventDefault();
    }
    return(
    <form onSubmit={handleSubmit}>
        <label>
            Username:
            <input type="text" value={username} onChange={e=>setUsername(e.target.value)} />
        </label><br/>
        <label>
            Name:
            <input type="text" value={name} onChange={e=>setName(e.target.value)} />
        </label><br/>
        <label>
            Passwd:
            <input type="password" value={passwd}  onChange={e=>setPasswd(e.target.value)}  />
        </label><br/>

        <input type="submit" value="Add user" />
    </form>);
}
