
import React from  'react';
import './UserApp.css'

function UserRow({user, deleteFunc}){
    function handleDelete(event){
        console.log('delete button pentru '+user.id);
        deleteFunc(user.id);
    }
    return (
        <tr>
            <td>{user.id}</td>
            <td>{user.name}</td>
            <td><button  onClick={handleDelete}>Delete</button></td>
        </tr>
    );
}


export default function UserTable({usersList, deleteFunc}){
    console.log("In UserTable");
    console.log(usersList);
    let rows = [];
    let functieStergere=deleteFunc;
    usersList.forEach(function(user) {
        rows.push(<UserRow user={user}  key={user.id} deleteFunc={functieStergere} />);
    });


    return (
        <div className="UserTable">
            <table className="center">
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Name</th>

                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}

