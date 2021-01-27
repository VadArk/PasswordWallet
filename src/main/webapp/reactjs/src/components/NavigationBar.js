import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Navbar, Nav, Card} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faUserPlus, faSignInAlt, faSignOutAlt, faEquals} from '@fortawesome/free-solid-svg-icons';
import {logoutUser} from '../services/index';

class NavigationBar extends Component {
    logout = () => {
        this.props.logoutUser();
    };

    render() {
        const guestLinks = (
            <>
                <div className="mr-auto"/>
                <Nav className="navbar-right">
                    <Link to={"register"} className="nav-link">Registration</Link>
                    <Link to={"login"} className="nav-link">Login</Link>
                </Nav>
            </>
        );
        const userLinks = (
            <>
                <Nav className="mr-auto">
                    <Link to={"/add"} className="nav-link">Add new password</Link>
                    <Link to={"/list"} className="nav-link">Password list</Link>
                    <Link to={"/actions"} className="nav-link">History</Link>
                    <Link to={"/change"} className="nav-link">Change master password</Link>
                </Nav>
                <Nav className="navbar-right">
                    <Link className="nav-link" >Current mode: {localStorage.getItem("mode")}</Link>
                    <Link to={"logout"} className="nav-link" onClick={this.logout}><FontAwesomeIcon icon={faSignOutAlt} /> Logout</Link>
                </Nav>
            </>
        );
        return (
            <Navbar bg="light" variant="light">
                <Link to={""} className="navbar-brand">
                    Password Wallet
                </Link>
                {this.props.auth.isLoggedIn === "0" ? userLinks : guestLinks}
            </Navbar>
        );
    };
};

const mapStateToProps = state => {
    return {
        auth: state.auth
    };
};

const mapDispatchToProps = dispatch => {
    return {
        logoutUser: () => dispatch(logoutUser())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(NavigationBar);