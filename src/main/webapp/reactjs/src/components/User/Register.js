import React, {Component} from 'react';
import {Row, Col, Card, Form, InputGroup, FormControl, Button} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEnvelope, faLock, faUndo, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import MyToast from "../MyToast";

export default class Register extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
    }

    initialState = {
        login: '', password: '', password2: '', passStoreMethod:'SHA512'
    };

    submitRegisterForm = event => {
        event.preventDefault();


        const user = {
            login: this.state.login,
            password: this.state.password,
            isPasswordSavedAsHash: this.state.passStoreMethod === 'SHA512'
        };
        axios.post("http://localhost:8080/api/v1/user/add", user)
            .then(response => {
                if (response.data != null) {
                    this.setState({"show": true});
                    setTimeout(() => this.setState({"show": false}), 3000);
                } else {
                    this.setState({"show": false});
                }
            });
        this.setState(this.initialState);
    }


    userChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    resetRegisterForm = () => {
        this.setState(() => this.initialState);
    };


    render() {
        const {login, password, password2, passStoreMethod} = this.state;

        return (
            <div>
                <div style={{"display": this.state.show ? "block" : "none"}}>
                    <MyToast children={{show: this.state.show, message: "Registered Successfully."}}/>
                </div>
                <Row className="justify-content-md-center">
                    <Col xs={5}>
                        <Card className={"border border-light bg-light text-black"}>
                            <Card.Header>
                                Registration
                            </Card.Header>
                            <Card.Body>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text>Login</InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="text" name="login"
                                                         value={login} onChange={this.userChange}
                                                         className={"bg-light text-black"} placeholder="Enter Login"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text>Pass</InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="password" name="password"
                                                         value={password} onChange={this.userChange}
                                                         className={"bg-light text-black"} placeholder="Enter Password"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <InputGroup>
                                            <InputGroup.Prepend>
                                                <InputGroup.Text>Pass</InputGroup.Text>
                                            </InputGroup.Prepend>
                                            <FormControl required autoComplete="off" type="password" name="password2"
                                                         value={password2} onChange={this.userChange}
                                                         className={"bg-light text-black"}
                                                         placeholder="Enter Password One More Time"/>
                                        </InputGroup>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>Choose a method</Form.Label>
                                        <Form.Control as="select"
                                                      className={"bg-light text-black"}
                                                      onChange={this.userChange}
                                                      name={"passStoreMethod"}
                                                      value={passStoreMethod}
                                        >
                                            <option value={"SHA512"}>SHA512 with salt and pepper</option>
                                            <option value={"HMAC"}>HMAC</option>
                                        </Form.Control>
                                    </Form.Group>
                                </Form.Row>
                            </Card.Body>
                            <Card.Footer style={{"textAlign": "right"}}>
                                <Button size="lg" type="button" variant="success" onClick={this.submitRegisterForm}
                                        disabled={this.state.login.length === 0 || this.state.password.length === 0 || this.state.password !== this.state.password2}>
                                    Register
                                </Button>{' '}

                            </Card.Footer>
                        </Card>
                    </Col>
                </Row></div>
        );
    }
}