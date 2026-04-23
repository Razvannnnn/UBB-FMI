
import React from  'react';
import './UserApp.css'

class UserRow extends React.Component{

    handleDelete=(event)=>{
        console.log('delete button pentru '+this.props.user.id);
        this.props.deleteFunc(this.props.user.id);
    }

    render() {
        return (
            <tr>
                <td>{this.props.user.id}</td>
                <td>{this.props.user.name}</td>
                <td><button  onClick={this.handleDelete}>Delete</button></td>
            </tr>
        );
    }
}

class UserTable extends React.Component {
    render() {
        let rows = [];
        let functieStergere=this.props.deleteFunc;
        this.props.users.forEach(function(user) {

            rows.push(<UserRow user={user}  key={user.id} deleteFunc={functieStergere} />);
        });
        return (<div className="UserTable">

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
}

export default UserTable;