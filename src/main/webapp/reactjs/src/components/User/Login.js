import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Row, Col, Card, Form, InputGroup, FormControl, Button, Alert} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faSignInAlt, faEnvelope, faLock, faUndo, faInfo} from "@fortawesome/free-solid-svg-icons";
import {authenticateUser} from '../../services/index';


class Login extends Component {
    constructor(props) {
        super(props);
        this.state = this.initialState;
    }

    initialState = {
        login: '', password: '', error: '', lastSuccess: '', lastUnsuccessful: '', mode: 'read'
    };

    credentialChange = event => {
        this.setState({
            [event.target.name]: event.target.value
        });
    };

    validateUser = () => {
        this.props.authenticateUser(this.state.login, this.state.password, this.state.mode);
        setTimeout(() => {

            if (this.props.auth.isLoggedIn === "0") {
                return this.props.history.push("/");
            } else {
                this.resetLoginForm();
                this.setState({"lastSuccess": "Last successful login: " + this.props.auth.lastSuccess});
                this.setState({"lastUnsuccessful": "Last unsuccessful login: " + this.props.auth.lastUnsuccessful});
                if (this.props.auth.isLoggedIn === "1") {
                    this.setState({"error": "please wait for 5 sec"});
                } else if (this.props.auth.isLoggedIn === "2") {
                    this.setState({"error": "please wait for 10 sec"});
                } else if (this.props.auth.isLoggedIn === "3") {
                    this.setState({"error": "please wait  for 2 mins"});
                } else if (this.props.auth.isLoggedIn === "4") {
                    this.setState({"error": "ip address blocked forever"});
                } else {
                    this.setState({"error": "Invalid login and password"});
                }
            }
        }, 500);
    };

    resetLoginForm = () => {
        this.setState(() => this.initialState);
    };

    userChange = event => {
        this.setState({
            mode: event.target.value
        });
    };

    render() {

        const {login, password, error,lastSuccess, lastUnsuccessful, mode } = this.state;
        console.log(this.state)
        return (
            <Row className="justify-content-md-center">
                <Col xs={5}>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Card className={"border border-light bg-light text-black"}>
                        <Card.Header>
                            Login
                        </Card.Header>
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col}>
                                    <InputGroup>
                                        <InputGroup.Prepend>
                                            <InputGroup.Text>Login</InputGroup.Text>
                                        </InputGroup.Prepend>
                                        <FormControl required autoComplete="off" type="text" name="login" value={login}
                                                     onChange={this.credentialChange}
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
                                                     value={password} onChange={this.credentialChange}
                                                     className={"bg-light text-black"} placeholder="Enter Password"/>
                                    </InputGroup>
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col}>
                                    <Form.Label>Choose a method to store your password</Form.Label>
                                    <Form.Control as="select"
                                                  className={"bg-light text-black"}
                                                  onChange={this.userChange}
                                                  name={"mode"}
                                                  value={mode}
                                    >
                                        <option value={"read"}>Read Mode</option>
                                        <option value={"write"}>Modify Mode</option>
                                    </Form.Control>
                                </Form.Group>
                            </Form.Row>
                        </Card.Body>
                        <Card.Footer style={{"text-align": "right"}}>
                            <Button size="lg" type="button" variant="success" onClick={this.validateUser}
                                    disabled={this.state.login.length === 0 || this.state.password.length === 0}>
                                Login
                            </Button>{' '}
                        </Card.Footer>
                    </Card>
                    <Card className={"border border-light bg-light text-black"}>
                        <Card.Header>
                            {lastSuccess!=null? lastSuccess : "no attempts found"}
                        </Card.Header>
                        <Card.Header>
                            {lastUnsuccessful!=null? lastUnsuccessful: "no attempts found" }
                        </Card.Header>
                    </Card>
                </Col>
            </Row>
        );
    }
}

const mapStateToProps = state => {
    return {
        auth: state.auth
    }
};

const mapDispatchToProps = dispatch => {
    return {
        authenticateUser: (login, password, mode) => dispatch(authenticateUser(login, password, mode))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);