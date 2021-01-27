import React, {Component} from 'react';

import {connect} from 'react-redux';
import {savePassword, fetchpassword, updatepassword} from '../../services/index';

import {Card, Form, Button, Col, InputGroup, Image} from 'react-bootstrap';
import {faSave, faPlusSquare, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import MyToast from '../MyToast';
import axios from 'axios';

class Password extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state = {
            genres: [],
            languages : [],
            show : false
        };
        this.passwordChange = this.passwordChange.bind(this);
        this.submitpassword = this.submitpassword.bind(this);
    }

    initialState = {
        id:'', login:'', password:'', web_address:'', description:'', ownerId: ''
    };

    componentDidMount() {
        const passwordId = +this.props.match.params.id;
        if(passwordId) {
            this.findpasswordById(passwordId);
        }

    }



    findpasswordById = (passwordId) => {
        console.log(this.state);
        this.props.fetchpassword(passwordId);
        setTimeout(() => {
            let password = this.props.passwordObject.password;
            if(password != null) {
                this.setState({
                    id: password.id,
                    login: password.login,
                    password: password.password,
                    web_address: password.web_address,
                    description: password.description
                });
            }
        }, 1000);

    };

    resetpassword = () => {
        this.setState(() => this.initialState);
    };


    submitpassword = event => {
        event.preventDefault();

        const password = {

            login: this.state.login,
            password: this.state.password,
            web_address: this.state.web_address,
            description: this.state.description
        };

        this.props.savePassword(password);
        setTimeout(() => {
            if(this.props.savedpasswordObject.password != null) {
                this.setState({"show":true, "method":"post"});
                setTimeout(() => this.setState({"show":false}), 3000);
            } else {
                this.setState({"show":false});
            }
        }, 500);

        const pass = {
            masterPassword: localStorage.getItem('masterPassword'),
            userLogin: localStorage.getItem('login'),
            login: password.login,
            password: password.password,
            web_address: password.web_address,
            description: password.description
        };
        console.log(pass, "masterPASS object")

        axios.post("http://localhost:8080/api/v1/password/add", pass)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"post"});
                    setTimeout(() => this.setState({"show":false}), 0);
                } else {
                    this.setState({"show":false});
                }
            });

        this.setState(this.initialState);
    };



    updatepassword = event => {
        event.preventDefault();

        const password = {
            userLogin: localStorage.getItem('login'),
            passwordId: this.state.id,
            newLogin: this.state.login,
            newPassword: this.state.password,
            newWeb: this.state.web_address,
            newDescription: this.state.description
        };

        axios.put("http://localhost:8080/api/v1/password/edit", password)
            .then(response => {
                if(response.data){
                    this.setState({"show":true, "method":"put"});
                    setTimeout(() => this.setState({"show":false}), 2000);
                }
                else
                    alert("You need to be an owner to edit or delete shared passwords")
            })
            .catch(error => {

            });
        this.setState(this.initialState);
    };

    passwordChange = event => {
        this.setState({
            [event.target.name]:event.target.value
        });
    };

    passwordList = () => {
        return this.props.history.push("/list");
    };

    render() {
        const {login, password, web_address, description} = this.state;

        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast show = {this.state.show} message = {this.state.method === "put" ? "Password Updated Successfully." : "Password Saved Successfully."} type = {"success"}/>
                </div>
                <Card className={"border border-light bg-light text-black"}>
                    <Card.Header>
                        {this.state.id ? "Update Password" : "Add New Password"}
                    </Card.Header>
                    <Form onReset={this.resetpassword} onSubmit={this.state.id ? this.updatepassword : this.submitpassword} id="passwordFormId">
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridTitle">
                                    <Form.Label>Login</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="login"
                                        value={login} onChange={this.passwordChange}
                                        className={"bg-light text-black"}
                                        placeholder="Enter Login" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridAuthor">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="password" name="password"
                                        value={password} onChange={this.passwordChange}
                                        className={"bg-light text-black"}
                                        placeholder="Enter Password " />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridCoverPhotoURL">
                                    <Form.Label>Web-site URL</Form.Label>
                                    <InputGroup>
                                        <Form.Control required autoComplete="off"
                                            type="text" name="web_address"
                                            value={web_address} onChange={this.passwordChange}
                                            className={"bg-light text-black"}
                                            placeholder="Enter Web-site URL" />
                                    </InputGroup>
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridISBNNumber">
                                    <Form.Label>Description</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="description"
                                        value={description} onChange={this.passwordChange}
                                        className={"bg-light text-black"}
                                        placeholder="Enter description" />
                                </Form.Group>
                            </Form.Row>

                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" variant="success" type="submit">
                                {this.state.id ? "Update" : "Save"}
                            </Button>{' '}
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
};

const mapStateToProps = state => {
    return {
        savedpasswordObject: state.password,
        passwordObject: state.password,
        updatedpasswordObject: state.password
    };
};

const mapDispatchToProps = dispatch => {
    return {
        savePassword: (password) => dispatch(savePassword(password)),
        fetchpassword: (passwordId) => dispatch(fetchpassword(passwordId)),
        updatepassword: (password) => dispatch(updatepassword(password))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Password);