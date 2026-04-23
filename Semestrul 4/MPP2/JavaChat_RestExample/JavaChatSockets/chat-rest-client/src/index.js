import React from 'react';
//import ReactDOM from 'react-dom';
import {createRoot} from 'react-dom/client';
import './index.css';
import UserApp from "./UserApp";

const container=document.getElementById('root');
const root=createRoot(container);
root.render( <UserApp/>);

/*ReactDOM.render(
  <div>
 <UserApp/>
  </div>,
  document.getElementById('root')
);*/
