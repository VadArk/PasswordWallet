import React from 'react';
import './App.css';

import {Container, Row, Col} from 'react-bootstrap';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import NavigationBar from './components/NavigationBar';
import Welcome from './components/Welcome';
import PasswordList from './components/Password/PasswordList';
import ActionsList from './components/Password/ActionsList';
import UserList from './components/User/UserList';
import Register from './components/User/Register';
import Login from './components/User/Login';
import Footer from './components/Footer';
import Password from "./components/Password/Password";
import Change from "./components/User/Change";
import HistoryList from "./components/Password/HistoryList";

export default function App() {

  const heading = "Welcome to simple Password Wallet";
  const quote = "";
  const footer = "by Vadym Arkaikin";

  return (
    <Router>
        <NavigationBar/>
        <Container>
            <Row>
                <Col lg={12} className={"margin-top"}>
                    <Switch>
                        <Route path="/" exact component={() => <Welcome heading={heading} quote={quote} footer={footer}/>}/>
                        <Route path="/add" exact component={Password}/>
                        <Route path="/edit/:id" exact component={Password}/>
                        <Route path="/history/:id" exact component={HistoryList}/>
                        <Route path="/list" exact component={PasswordList}/>
                        <Route path="/actions" exact component={ActionsList}/>
                        <Route path="/users" exact component={UserList}/>
                        <Route path="/register" exact component={Register}/>
                        <Route path="/change" exact component={Change}/>
                        <Route path="/login" exact component={Login}/>
                        <Route path="/logout" exact component={Login}/>
                    </Switch>
                </Col>
            </Row>
        </Container>
        <Footer/>
    </Router>
  );
}
