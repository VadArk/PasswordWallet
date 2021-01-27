import React, {Component} from 'react';
import './../../assets/css/Style.css';
import {Card, Table, Image, ButtonGroup, Button, InputGroup, FormControl, Form, Col} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {
    faStepBackward,
    faFastBackward,
    faStepForward,
    faFastForward
} from '@fortawesome/free-solid-svg-icons';

import axios from 'axios';
import {connect} from "react-redux";

import {showPassword} from "../../services";

class ActionsList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            actions: [],
            currentPage: 1,
            actionsPerPage: 10,
            sortDir: "asc",
            visible: false,
            share: false,
            sharedLogin: "",
            mode: localStorage.getItem("mode"),
            isAlertModeVisible: false,
            chosenAction: "all"
        };
    }

    sortData = () => {
        setTimeout(() => {
            this.state.sortDir === "asc" ? this.setState({sortDir: "desc"}) : this.setState({sortDir: "asc"});
            this.findAllActions(this.state.currentPage);
        }, 500);
    };

    componentDidMount() {
        this.findAllActions(this.state.currentPage);
    }

    userChange = event => {
        this.setState({
            chosenAction: event.target.value
        });
    };

    findAllActions(currentPage) {
        currentPage -= 1;
        axios.get("http://localhost:8080/api/v1/function/all?login=" + localStorage.getItem('login'))
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    actions: data.map(action => {
                        //action.present_value = "******"
                        return action
                    }),

                });
            });
    };

    getFilterString(filterValue){

        let filterString=""
        switch (filterValue) {
            case 'all':
                break;
            case 'login':
                filterString="login"
                break;
            case 'create':
                filterString="create password"
                break;
            case 'update':
                filterString="update password"
                break;
            case 'restore':
                filterString="restore password"
                break;
            case 'delete':
                filterString="delete password"
                break;
            default:
        }
        return filterString;

    }

    render() {
        const {actions, currentPage, totalPages, sharedLogin, share, mode, isAlertModeVisible, chosenAction} = this.state;


        let filterString=this.getFilterString(chosenAction)
        return (
            <div>

                <Card className={"border border-light bg-light text-black"}>
                    <Card.Header>
                        <div style={{"float": "left"}}>
                           History for user  {localStorage.getItem("login")}
                        </div>
                        <div style={{"float": "right"}}>
                            <InputGroup size="sm">

                                <InputGroup.Append>

                                </InputGroup.Append>
                            </InputGroup>
                        </div>
                        <Form>
                        <Form.Row>
                            <Form.Group as={Col}>
                                <Form.Label> </Form.Label>
                                <Form.Control as="select"
                                              className={"bg-light text-black"}
                                              onChange={this.userChange}
                                              name={"chosenAction"}
                                              value={chosenAction}                                >
                                    <option value={"all"}>All actions</option>
                                    <option value={"login"}>Login</option>
                                    <option value={"create"}>Create</option>
                                    <option value={"update"}>Update</option>
                                    <option value={"restore"}>Restore</option>
                                    <option value={"delete"}>Delete</option>

                                </Form.Control>
                            </Form.Group>
                        </Form.Row>
                        </Form>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="light">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Time</th>
                                <th>Function Name</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                actions.length === 0 ?
                                    <tr align="center">
                                        <td colSpan="7">No history.</td>
                                    </tr> :





                                actions.filter(a => filterString?a.functionName===filterString:true).map((action) => (
                                        <tr key={action.id}>


                                            <td>{action.id} </td>
                                            <td>{action.time}</td>
                                            <td>{action.functionName}</td>


                                        </tr>
                                    ))
                            }
                            </tbody>
                        </Table>
                    </Card.Body>
                    {actions.length > 0 ?
                        <Card.Footer>
                            <div style={{"float": "left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float": "right"}}>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.firstPage}>
                                            <FontAwesomeIcon icon={faFastBackward}/> First
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1}
                                                onClick={this.prevPage}>
                                            <FontAwesomeIcon icon={faStepBackward}/> Prev
                                        </Button>
                                    </InputGroup.Prepend>
                                    <FormControl className={"page-num bg-dark"} name="currentPage" value={currentPage}
                                                 onChange={this.changePage}/>
                                    <InputGroup.Append>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages}
                                                onClick={this.nextPage}>
                                            <FontAwesomeIcon icon={faStepForward}/> Next
                                        </Button>
                                        <Button type="button" variant="outline-info"
                                                disabled={currentPage === totalPages}
                                                onClick={this.lastPage}>
                                            <FontAwesomeIcon icon={faFastForward}/> Last
                                        </Button>
                                    </InputGroup.Append>
                                </InputGroup>
                            </div>
                        </Card.Footer> : null
                    }
                </Card>
            </div>
        );
    }

}

const mapStateToProps = state => {
    return {
        actionObject: state.action
    };
};

const mapDispatchToProps = dispatch => {
    return {
        showPassword: (passwordId) => dispatch(showPassword(passwordId))
    };
};
export default connect(mapStateToProps,mapDispatchToProps)(ActionsList);